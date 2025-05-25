package utils;

import base.BaseTest;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class UserService extends BaseTest {

    public static int getUserIdByEmail(String email) {
        Response response = given()
                .spec(requestSpec)
                .queryParam("page", 1)
                .when()
                .get(config.getProperty("users_endpoint"))
                .then()
                .statusCode(200)
                .extract().response();

        return response.jsonPath()
                .getInt("data.find { it.email == '" + email + "' }.id");
    }

    public static int getUserId() {
        Response response = given()
                .spec(requestSpec)
                .queryParam("page", 1)
                .when()
                .get(config.getProperty("users_endpoint"))
                .then()
                .statusCode(200)
                .extract().response();

        return response.jsonPath().getInt("data[0].id");

    }


}
