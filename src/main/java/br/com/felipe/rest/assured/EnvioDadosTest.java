package br.com.felipe.rest.assured;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class EnvioDadosTest {

	@Test
	public void deveEnviarValorViaQuery() {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/v2/users?format=json")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
		;
	}

	@Test
	public void deveEnviarValorViaQueryViaParam() {
		given()
			.log().all()
			.param("format", "xml")
			.param("outra", "coisa")
		.when()
			.get("http://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
			.contentType(Matchers.containsString("utf-8"))
		;
	}
	
	@Test
	public void deveEnviarValorViaHeader() {
		given()
			.log().all()
			.accept(ContentType.XML) // qdo eu quero dizer o que eu quero que venha da resposta, nao usamos contentType, usamos o accept
		.when()
			.get("http://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
		;
		
	}
}
