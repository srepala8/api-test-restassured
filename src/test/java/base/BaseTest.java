package base;
import Reports.ExtentReportManager;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.ITestResult;
import org.testng.annotations.BeforeSuite;
import java.io.InputStream;
import org.testng.annotations.AfterMethod;


import static com.aventstack.extentreports.Status.INFO;
import java.util.Properties;

public class BaseTest {
    protected static RequestSpecification requestSpec;
    protected static Properties config;

    @BeforeSuite
    public void setup() throws Exception{

        loadConfig();
        RestAssured.baseURI = config.getProperty("base.url");
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(RestAssured.baseURI)
                .addHeader("x-api-key", config.getProperty("api.key"))
                .build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private void loadConfig() throws Exception {
        config = new Properties();
        InputStream inputStream = BaseTest.class
                .getClassLoader()
                .getResourceAsStream("config.properties");

        if (inputStream == null) {
            throw new RuntimeException("config.properties not found!");
        }
        config.load(inputStream);
    }
    @AfterMethod
    public void logTestInfo(ITestResult result) {
        if (ExtentReportManager.test != null) {
            ExtentReportManager.test.log(INFO, "Test Method: " + result.getMethod().getMethodName());
            ExtentReportManager.test.log(INFO, "Test Description: " + result.getMethod().getDescription());
        }
    }

}
