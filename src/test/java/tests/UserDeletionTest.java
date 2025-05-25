package tests;
import Listener.TestListener;
import Reports.ExtentReportManager;
import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.UserService;

import static io.restassured.RestAssured.given;


@Listeners(TestListener.class)
public class UserDeletionTest extends BaseTest {
    @Test(description = "Delete a user")
    public void deleteUserById_shouldReturn204() {
        ExtentReportManager.test.log(Status.INFO, "Starting test: Get user ID");
        int userId = UserService.getUserId();
        given()
                .header("x-api-key","reqres-free-v1")
                .pathParam("id", userId)
                .when()
                .delete(config.getProperty("users_url")+"{id}")
                .then()
                .statusCode(204); // No Content
        ExtentReportManager.test.log(Status.INFO, "Successfully deleted the User");

    }



}
