package pfe.backend.verificationIdentite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pfe.backend.verificationIdentite.dto.OcrResultDTO;
import pfe.backend.verificationIdentite.model.OcrResult;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.repository.OcrResultRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responsabilité UNIQUE : lire une photo CIN et extraire les champs.
 * Aucune vérification croisée ici — c'est le rôle de VerificationIdentiteService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OcrService {

    private final OcrResultRepository ocrResultRepository;

    @Value("${tesseract.datapath:/usr/share/tesseract-ocr/4.00/tessdata}")
    private String tessDataPath;

    @Value("${tesseract.language:fra}")
    private String tessLanguage;

    // ══════════════════════════════════════════════════════════
    //  POINT D'ENTRÉE
    //  Appelé par VerificationIdentiteService après sauvegarde.
    //  Retourne les champs extraits SANS vérification.
    // ══════════════════════════════════════════════════════════

    public OcrResultDTO extraireEtPersister(
            MultipartFile photoCin,
            VerificationIdentite verificationIdentite
    ) throws IOException {

        File tempFile = toTempFile(photoCin);

        try {
            File preprocessed = preprocessImage(tempFile);
            String rawText    = runTesseract(preprocessed);

            log.info("=== OCR RAW TEXT ===\n{}", rawText);

            Map<String, String> parsed = parseIdCard(rawText);

            // Persister les champs extraits (sans cinValide, sans comparaison)
            OcrResult entity = OcrResult.builder()
                    .numeroCin(parsed.get("numero"))
                    .nom(parsed.get("nom"))
                    .prenom(parsed.get("prenom"))
                    .dateNaissance(parsed.get("date_naissance"))
                    .sexe(parsed.get("sexe"))
                    .lieuNaissance(parsed.get("lieu_naissance"))
                    .profession(parsed.get("profession"))
                    .dateEtablissement(parsed.get("date_etablissement"))
                    .dateExpiration(parsed.get("date_expiration"))
                    .rawText(rawText)
                    .verificationIdentite(verificationIdentite)
                    .build();

            ocrResultRepository.save(entity);

            return toDTO(entity);

        } finally {
            tempFile.delete();
        }
    }

    // ══════════════════════════════════════════════════════════
    //  TESSERACT
    // ══════════════════════════════════════════════════════════

    private String runTesseract(File imageFile) throws IOException {
        Tesseract tess = new Tesseract();
        tess.setDatapath(tessDataPath);
        tess.setLanguage(tessLanguage);
        tess.setPageSegMode(4);
        tess.setOcrEngineMode(1);
        tess.setVariable("tessedit_char_whitelist",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
                + "0123456789-/:. ");
        try {
            return tess.doOCR(imageFile);
        } catch (TesseractException e) {
            throw new IOException("OCR échoué : " + e.getMessage(), e);
        }
    }

    // ══════════════════════════════════════════════════════════
    //  PRÉTRAITEMENT IMAGE
    // ══════════════════════════════════════════════════════════

    private File preprocessImage(File src) throws IOException {

    BufferedImage original = ImageIO.read(src);

    BufferedImage gray = new BufferedImage(
            original.getWidth(),
            original.getHeight(),
            BufferedImage.TYPE_BYTE_GRAY);

    Graphics g = gray.getGraphics();
    g.drawImage(original, 0, 0, null);
    g.dispose();

    BufferedImage binary = new BufferedImage(
            gray.getWidth(),
            gray.getHeight(),
            BufferedImage.TYPE_BYTE_BINARY);

    Graphics2D g2 = binary.createGraphics();
    g2.drawImage(gray, 0, 0, null);
    g2.dispose();

    File out = File.createTempFile("ocr_pre_", ".png");
    ImageIO.write(binary, "png", out);

    return out;
}
    // ══════════════════════════════════════════════════════════
    //  PARSING REGEX — miroir exact de la logique Flutter
    //  Retourne le texte brut tel que Tesseract le voit,
    //  plus les champs parsés si les patterns matchent.
    // ══════════════════════════════════════════════════════════

    public Map<String, String> parseIdCard(String rawText) {
        Map<String, String> data = new HashMap<>();
        data.put("numero",             null);
        data.put("nom",                null);
        data.put("prenom",             null);
        data.put("date_naissance",     null);
        data.put("sexe",               null);
        data.put("lieu_naissance",     null);
        data.put("profession",         null);
        data.put("date_etablissement", null);
        data.put("date_expiration",    null);

        for (String rawLine : rawText.split("\\r?\\n")) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;

            // ── Numéro CIN (8 chiffres ou XXXX-XXX-XXXX) ─────
            if (data.get("numero") == null) {
                Matcher m = Pattern.compile("\\b\\d{8}\\b").matcher(line);
                if (!m.find()) m = Pattern.compile("\\d{4}-\\d{3}-\\d{4}").matcher(line);
                if (m.find()) { data.put("numero", m.group()); continue; }
            }

            // ── Nom ───────────────────────────────────────────
            if (label(line, "Nom")) {
                data.put("nom", value(line)); continue;
            }

            // ── Prénom ────────────────────────────────────────
            if (label(line, "Pr[eé]nom")) {
                data.put("prenom", value(line)); continue;
            }

            // ── Date naissance (+ Sexe sur la même ligne) ─────
            if (label(line, "N[eé]?\\s*le")) {
                Matcher dm = Pattern.compile("\\d{2}[-/]\\d{2}[-/]\\d{4}").matcher(line);
                if (dm.find()) data.put("date_naissance", dm.group());

                Matcher sm = Pattern.compile("(?i)Sexe\\s*:\\s*([MF])").matcher(line);
                if (sm.find()) data.put("sexe", sm.group(1).toUpperCase());
                continue;
            }

            // ── Sexe seul ─────────────────────────────────────
            if (data.get("sexe") == null && label(line, "Sexe")) {
                Matcher m = Pattern.compile("(?i)([MF])\\b").matcher(line);
                if (m.find()) data.put("sexe", m.group(1).toUpperCase());
                continue;
            }

            // ── Profession ────────────────────────────────────
            if (label(line, "Profession")) {
                data.put("profession", value(line)); continue;
            }

            // ── Date établissement ────────────────────────────
            if (data.get("date_etablissement") == null
                    && Pattern.compile("(?i)(fait|d[ée]livr[ée]|emis)").matcher(line).find()) {
                Matcher m = Pattern.compile("\\d{2}[-/]\\d{2}[-/]\\d{4}").matcher(line);
                if (m.find()) { data.put("date_etablissement", m.group()); continue; }
            }

            // ── Date expiration ───────────────────────────────
            if (label(line, "Expire?\\s*le")) {
                Matcher m = Pattern.compile("\\d{2}[-/]\\d{2}[-/]\\d{4}").matcher(line);
                if (m.find()) data.put("date_expiration", m.group());
            }
        }

        return data;
    }

    // ── Helpers ───────────────────────────────────────────────

    private boolean label(String line, String pattern) {
        return Pattern.compile("(?i)^" + pattern + "\\s*:?").matcher(line).find();
    }

    private String value(String line) {
        int i = line.indexOf(':');
        if (i == -1) return null;
        String v = line.substring(i + 1).trim();
        return v.isEmpty() ? null : v;
    }

    private File toTempFile(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        String ext  = (name != null && name.contains("."))
                ? name.substring(name.lastIndexOf('.')) : ".jpg";
        File tmp = File.createTempFile("cin_", ext);
        file.transferTo(tmp);
        return tmp;
    }

    private OcrResultDTO toDTO(OcrResult e) {
        OcrResultDTO dto = new OcrResultDTO();
        dto.setNumeroCin(e.getNumeroCin());
        dto.setNom(e.getNom());
        dto.setPrenom(e.getPrenom());
        dto.setDateNaissance(e.getDateNaissance());
        dto.setSexe(e.getSexe());
        dto.setLieuNaissance(e.getLieuNaissance());
        dto.setProfession(e.getProfession());
        dto.setDateEtablissement(e.getDateEtablissement());
        dto.setDateExpiration(e.getDateExpiration());
        dto.setRawText(e.getRawText()); // ← texte brut complet, sans modification
        // Pas de cinValide ici — ce sera rempli par VerificationIdentiteService
        return dto;
    }
}