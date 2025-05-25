package tests;
import Listener.TestListener;
import Reports.ExtentReportManager;
import base.BaseTest;
import com.aventstack.extentreports.Status;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.UserService;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@Listeners(TestListener.class)
public class UserUpdateTest extends BaseTest {


    @DataProvider(name = "jobUpdates")
    public Object[][] provideJobUpdates() {
        return new Object[][] {
                {"Team Lead"},
                {"Engineering Manager"},
                {"CTO"}
        };
    }




    @Test(dataProvider = "jobUpdates",description = "Update User Test")
    public void updateUserJobWithDifferentRoles(String newJob) {

        ExtentReportManager.test.log(Status.INFO, "Starting test: Get user ID");

        int userId = UserService.getUserId();
        String requestBody = String.format("{\"job\": \"%s\"}", newJob);

        given()
                .pathParam("id", userId)
                .contentType(ContentType.JSON)
                .header("x-api-key","reqres-free-v1")
                .body(requestBody)
                .when()
                .put(config.getProperty("users_url") +"{id}")
                .then()
                .statusCode(200)
                .body("job", equalTo(newJob))
                .body("updatedAt", containsString(Instant.now().toString().substring(0, 10)));

        ExtentReportManager.test.log(Status.INFO, "Successfully Updated the user");
    }



}
