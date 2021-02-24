package br.com.felipe.rest.assured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class FileTest {
	
	@Test
	public void deveObrigarEnviarArquivo() {
		
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) // deveria ser 400 erro de implementacao da API
			.body("error", is("Arquivo n�o enviado"))
		;
	}

	@Test
	public void deveFazerUploadArquivo() {
		
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("users.pdf"))
		;
	}
	
	@Test
	public void naoDeveFazerUploadArquivoGrande() {
		
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/geckodriver-v0.29.0-win64.zip"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(3000L)) // se for maior que lessThan(3000L) 3 segundos o teste quebra
			.statusCode(413)
		;
	}
	
	@Test
	public void deveFazerDownloadDoArquivo() throws IOException {
		
		byte[] image = given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
//			.log().all()
			.statusCode(200)
			.extract().asByteArray()
		;
		
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		
//		System.out.println(imagem.length());
		Assert.assertThat(imagem.length(), lessThan(100000L));
		
	}
}