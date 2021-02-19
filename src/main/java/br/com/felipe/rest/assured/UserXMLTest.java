package br.com.felipe.rest.assured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.hamcrest.collection.HasItemInArray;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class UserXMLTest {
	
	public static RequestSpecification reqSpec;
	public static ResponseSpecification respSpec;
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
//		RestAssured.port = 80; // não eh necessário http = 80 e https = 443
//		RestAssured.basePath = "/v2";
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		reqSpec = reqBuilder.build();
		
		ResponseSpecBuilder respBuilder = new ResponseSpecBuilder();
		respBuilder.expectStatusCode(200);
		respSpec = respBuilder.build();
		
		RestAssured.requestSpecification = reqSpec;
		RestAssured.responseSpecification = respSpec;
	}

	@Test
	public void deveTrabalharComXML() {
		given()
//			.spec(reqSpec)
		.when()
			.get("/usersXML/3")
		.then()
//			.statusCode(200)
//			.spec(respSpec)
			
			.rootPath("user") //"user.filhos.name" passa a considerar "filhos.name" ou seja, diminui o caminho na string 
			.body("name", is("Ana Julia"))
			.body("@id", is("3"))
			
			.rootPath("user.filhos") // "filhos.name.size()" passa a considerar "name.size()"
			.body("name.size()", is(2))
			
			.detachRootPath("filhos") // tira "filhos" da string e tem que declarar "filhos." novamente nas linhas abaixo
			.body("filhos.name[0]", is("Zezinho"))
			.body("filhos.name[1]", is("Luizinho"))
			
			.appendRootPath("filhos") // volta a inserir "filho" na string abaixo, entao podemos retirar o "filhos."
			.body("name", hasItem("Luizinho"))
			.body("name", hasItems("Luizinho", "Zezinho"))
		;
		
	}

	@Test
	public void deveFazerPesquisasAvancadasComXML() {
		given()
//			.spec(reqSpec)
		.when()
			.get("/usersXML")
		.then()
//			.statusCode(200)
//			.spec(respSpec)
			.body("users.user.size()", is(3))
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
			.body("users.user.@id", hasItems("1", "2", "3"))
			.body("users.user.@id", hasItem("2"))
			.body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
			.body("users.user.salary.find{it != null}", is("1234.5678"))
			.body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d)) // considera o salario como double
			.body("users.user.age.collect{it.toInteger() * 2}", hasItems(60, 50, 40))
			.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))
		;
		
	}
	
	@Test		// Unindo XMLPath com Java
	public void deveFazerPesquisasAvancadasComXML2() {
		ArrayList<NodeImpl> nomes = given()
//			.spec(reqSpec)
		.when()
			.get("/usersXML")
		.then()
//			.statusCode(200)
//			.spec(respSpec)
			.extract().path("users.user.name.findAll{it.toString().contains('n')}")
		;
		Assert.assertEquals(2, nomes.size());
		Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		Assert.assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString()));
	}

	@Test		// XML com xPath, que eh diferente do XMLPath
	public void deveFazerPesquisasAvancadasComXPath() {
				// https://www.red-gate.com/simple-talk/dotnet/net-framework/xpath-css-dom-and-selenium-the-rosetta-stone/
				// os metodos do xPath se encontram no link acima 
				given()
//					.spec(reqSpec)
				.when()
					.get("/usersXML")
				.then()
//					.statusCode(200)
//					.spec(respSpec)
					.body(hasXPath("count(/users/user)", is("3")))
					.body(hasXPath("/users/user[@id = '1']"))
					.body(hasXPath("//user[@id = '2']"))
					.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
					.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))
					.body(hasXPath("/users/user/name", is("João da Silva")))
					.body(hasXPath("//name", is("João da Silva")))
					.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
					.body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
					.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
					.body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
					.body(hasXPath("//user[age > 20] [age < 30]/name", is("Maria Joaquina"))) // eh o mesmo resultado da linha acima
				;
		
	}
}
