package br.com.felipe.rest.assured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
import org.hamcrest.collection.HasItemInArray;
import org.junit.Test;

public class UserXMLTest {

	@Test
	public void deveTrabalharComXML() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML/3")
		.then()
			.statusCode(200)
			
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
		.when()
			.get("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
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
}
