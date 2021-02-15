package br.com.felipe.rest.assured;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class HelloWordTest {
	
	@Test
	public void testeOlaMundo() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);
		Assert.assertTrue("Deveria retornar statuscode 200", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
	
	@Test
	public void conhecendoOutrasFormasComRestAssured() {
	/*	Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
	*/	
		given()
		.when().get("http://restapi.wcaquino.me/ola")
		.then().statusCode(200);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void exemplosComHamcrest() {
		Assert.assertThat("Maria", Matchers.is("Maria"));
		Assert.assertThat(123, Matchers.is(123));
		Assert.assertThat(123, Matchers.isA(Integer.class));
		Assert.assertThat(128d, Matchers.isA(Double.class));
		Assert.assertThat(123d, Matchers.greaterThan(120d));
		Assert.assertThat(123d, Matchers.lessThan(130d));

		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1, 3, 5, 7, 9));
		assertThat(impares, containsInAnyOrder(5, 7, 9, 1, 3));
		assertThat(impares, hasItem(9));
		assertThat(impares, hasItems(9, 7));

		assertThat("Maria", is(not("Joao"))); // o is eh opcional
		assertThat("Maria", not("Joao"));
		assertThat("Maria", anyOf(is("Maria"), is("Joaquina")));
//		assertThat("Luiz", anyOf(is("Maria"), is("Joaquina")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
		
	}


}
