package br.com.felipe.rest.assured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbsTest {

	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Felipe\", \"age\": 36}")
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Felipe"))
			.body("age", is(36))
		;
	}

	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": 36}")
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
		;
	}
	
	@Test
	public void deveSalvarUsuarioViaXML() {
		given()
			.log().all()
			.contentType(ContentType.XML) // ao inves de usar o application/xml , podemos usar este ENUM
			.body("<user><name>Felipe Amaral</name><age>36</age></user>")
		.when()
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Felipe Amaral"))
			.body("user.age", is("36")) //no xml a idade deve ir como string
		;
	}
	
	@Test
	public void deveAlterarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuario Alterado\", \"age\": 101}")
		.when()
			.put("http://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(101))
			.body("salary", is(1234.5678))
		;
	}
	
	@Test
	public void deveCustomizarURL() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuario Alterado\", \"age\": 101}")
		.when()
			.put("http://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(101))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void deveCustomizarURL2() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuario Alterado\", \"age\": 101}")
			.pathParam("entidade", "users")
			.pathParam("userId", "1")
		.when()
			.put("http://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario Alterado"))
			.body("age", is(101))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all()
			.body("{\"name\": \"Usuario Alterado\", \"age\": 101}")
		.when()
			.delete("http://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		given()
			.log().all()
			.body("{\"name\": \"Usuario Alterado\", \"age\": 101}")
		.when()
			.delete("http://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		;
	}
	
	@Test
	public void deveSalvarUsuarioUsandoMAP() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Felipe via MAP");
		params.put("age", 22);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Felipe via MAP"))
			.body("age", is(22))
		;
	}
	
	@Test
	public void deveSalvarUsuarioUsandoObjeto() {
		User user = new User("Usuario via objeto", 25);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via objeto"))
			.body("age", is(25))
		;
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void deveDesserializarObjetoSalvarUsuario() {
		User user = new User("Usuario desserializado", 45);
		
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;
		
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario desserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge() , is(45));
	}
	
	@Test
	public void deveSalvarUsuarioViaXMLUsandoObjeto() {
		User user = new User("Usuario XML", 40);
		
		given()
			.log().all()
			.contentType(ContentType.XML) // ao inves de usar o application/xml , podemos usar este ENUM
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Usuario XML"))
			.body("user.age", is("40")) //no xml a idade deve ir como string
		;
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void deveDesserializarXMLAoSalvarUsuario() {
		User user = new User("Usuario XML", 40);
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML) // ao inves de usar o application/xml , podemos usar este ENUM
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;
		
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertThat(usuarioInserido.getName(), is("Usuario XML"));
		Assert.assertThat(usuarioInserido.getAge(), is(40));
		Assert.assertThat(usuarioInserido.getSalary(), nullValue());
		
	}
}