package pfe.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		// Forcer JNA à utiliser les DLLs Tesseract installées sur le système Windows
		System.setProperty("jna.library.path", "C:/Program Files/Tesseract-OCR");
		SpringApplication.run(BackendApplication.class, args);
	}

}
