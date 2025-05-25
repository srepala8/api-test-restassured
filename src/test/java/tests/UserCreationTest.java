package tests;
import Listener.TestListener;
import Reports.ExtentReportManager;
import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


import base.BaseTest;
import pojos.User;
@Listeners(TestListener.class)
public class UserCreationTest extends BaseTest {

      /*
           4.	POST /api/users
               o	Send a request with name and job
               o	Validate status 201 and that response contains the sent data


     */

    @DataProvider(name = "userData")
    public Object[][] provideUserData() {
        return new Object[][] {
                {"Alice Johnson", "DevOps Engineer"},
                {"Bob Smith", "Frontend Developer"},
                {"Charlie Brown", "Backend Developer"}
        };
    }

    @Test(dataProvider = "userData",description = "Create a User")
    public void createUserTest(String name, String job) {
        ExtentReportManager.test.log(Status.INFO, "Starting test: Create a User object");

        User newUser = new User();
        newUser.setName(name);
        newUser.setJob(job);

        // Send request and validate
        given()
                .contentType(ContentType.JSON)
                .header("x-api-key","reqres-free-v1")
                .body(newUser)
                .when()
                .post(config.getProperty("base.url")+config.getProperty("users_endpoint"))
                .then()
                .statusCode(201)
                .body("name", equalTo(name))
                .body("job", equalTo(job))
                .body("id", notNullValue())
                .body("createdAt", containsString(Instant.now().toString().substring(0, 10)));

        ExtentReportManager.test.log(Status.INFO, "Successfully Created the User");
    }



}
