package tests;
import Listener.TestListener;
import Reports.ExtentReportManager;
import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
import pojos.UsersResponse;
import utils.UserService;
import static org.testng.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(TestListener.class)
public class UsersTest extends BaseTest {


    /*
        1.	GET /api/users
          o	Validate status code is 200
          o	Validate that the response contains a list of users
          o	Count the amount of users returned on the current page


     */
    @Test
    public void validate_StausCode() {

        ExtentReportManager.test.log(Status.INFO, "Starting test: Get list of Users");
        given()
                .spec(requestSpec)
                .when()
                .get(config.getProperty("users_endpoint"))
                .then()
                .statusCode(200)
               .body("size()", greaterThan(0));

        ExtentReportManager.test.log(Status.INFO, "Successfully check the status code");
    }

    @Test
    public void validate_ListOfUsers() {

        ExtentReportManager.test.log(Status.INFO, "Starting test: Get list of Users");
        Response response = given()
                .spec(requestSpec)
                .when()
                .get(config.getProperty("users_endpoint"))
                .then()
                .statusCode(200)
                .extract()
                .response();
        // Validate all users have required fields
        response.jsonPath().getList("data").forEach(user -> {

            assertThat(((Integer) ((java.util.Map)user).get("id"))).isPositive();
            assertThat(((String) ((java.util.Map)user).get("email"))).isNotBlank();
            assertThat(((String) ((java.util.Map)user).get("first_name"))).isNotBlank();
            assertThat(((String) ((java.util.Map)user).get("last_name"))).isNotBlank();
            assertThat(((String) ((java.util.Map)user).get("avatar"))).contains("https://");

            ExtentReportManager.test.log(Status.INFO, "Successfully validate the list of Users");
        });
    }
    @Test
    public void count_AmountOfUsers() {

        ExtentReportManager.test.log(Status.INFO, "Starting test: Get list of Users");

        UsersResponse response = given()
                .spec(requestSpec)
                .when()
                .get(config.getProperty("users_endpoint"))
                .then()
                .statusCode(200)
                .extract()
                .as(UsersResponse.class);

        int userCount = response.getUserCountOnPage();
        System.out.println("Users on page " + response.getPage() + ": " + userCount);

        assertThat(userCount).isEqualTo(response.getPer_page());

        ExtentReportManager.test.log(Status.INFO, "Successfully validate the list of Users Count");
    }

    /*
       2.	GET /api/users
          o Add query params that will return full list of users
          o	Extract the id of the user where email = ‘charles.morris@reqres.in’


*/
    @Test
    public void validate_ListOfUsers_getBy_queryParameter() {

        ExtentReportManager.test.log(Status.INFO, "Starting test: Get Users for a page");
        Response response = given()
                .spec(requestSpec)
                .queryParam("page", 2)
                .when()
                .get(config.getProperty("users_endpoint"))
                .then()
                .statusCode(200)
                .extract()
                .response();
        // Validate all users have required fields
        response.jsonPath().getList("data").forEach(user -> {
            assertThat(((Integer) ((java.util.Map)user).get("id"))).isPositive();
            assertThat(((String) ((java.util.Map)user).get("email"))).isNotBlank();
            assertThat(((String) ((java.util.Map)user).get("first_name"))).isNotBlank();
            assertThat(((String) ((java.util.Map)user).get("last_name"))).isNotBlank();
            assertThat(((String) ((java.util.Map)user).get("avatar"))).contains("https://");

            ExtentReportManager.test.log(Status.INFO, "Successfully get the list of Users by Page");
        });
    }
    @Test
    public void extractUserIdByEmail() {

        ExtentReportManager.test.log(Status.INFO, "Starting test: Get User by ID");
        String targetEmail = "charles.morris@reqres.in";
        int userId = UserService.getUserIdByEmail(targetEmail);

        System.out.println("Extracted User ID: " + userId);
        assertEquals(userId, 5, "Verify ID matches expected value");

        ExtentReportManager.test.log(Status.INFO, "Successfully extract User by ID and check Email Validation");
    }

    /*
     3.	GET /api/users/{id}
       o	Use the id retrieved in step 2 to GET single user
       o	Validate that the response contains correct user details

    */

    @Test
    public void getUserById_Validations() {

        ExtentReportManager.test.log(Status.INFO, "Starting test: Get User BY Id");

        String email=config.getProperty("email");
        String firstname=config.getProperty("firstname");
        String lastname=config.getProperty("lastname");
        String avatar=config.getProperty("avatar");
        int userId = UserService.getUserId();

        System.out.println("Extracted User ID: " + userId);
        assertEquals(userId, 1, "Verify ID matches expected value");
        given()
                .header("x-api-key","reqres-free-v1")
                .pathParam("id", userId)
                .when()
                .get(config.getProperty("users_url")+"{id}")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(userId))
                .body("data.email", equalTo(email))
                .body("data.first_name", equalTo(firstname))
                .body("data.last_name", equalTo(lastname))
                .body("data.avatar", equalTo(avatar));

        ExtentReportManager.test.log(Status.INFO, "Successfully validate the user by ID");

    }









}
