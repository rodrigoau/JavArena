package com.rodrigo.tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Epic("Módulo API REST")
@Feature("Gestión de Usuarios")
public class ApisTests extends BaseApiTest{

    @Test
    @Story("Obtener detalles de un usuario por ID de forma exitosa")
    @Description("Verifica que una petición GET al endpoint /users/1 retorne un código 200 y la información correcta del usuario.")
    public void testApiGetUser() {
        given().spec(requestSpec)
                .queryParam("api", "users")
                .queryParam("page", "1")
                .queryParam("perPage", "8")
                .when().get("/automate-the-internet")
                .then().statusCode(200);
    }
}
