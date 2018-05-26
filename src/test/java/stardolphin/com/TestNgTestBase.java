package stardolphin.com;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Capabilities;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import ru.stqa.selenium.factory.WebDriverPool;

/**
 * Base class for TestNG-based test classes
 */
public class TestNgTestBase {

    private static URL gridHubUrl = null;
    protected static String baseUrl;
    private static Capabilities capabilities;

    protected WebDriver driver;

    @BeforeSuite
    public void initTestSuite() {

        SuiteConfiguration config;
        try {
            config = new SuiteConfiguration();

            baseUrl = config.getProperty("site.url");
            if (config.hasProperty("grid.url") && !"".equals(config.getProperty("grid.url"))) {
                gridHubUrl = new URL(config.getProperty("grid.url"));
            }
            capabilities = config.getCapabilities();
        } catch (IOException e) {
            throw new SkipException("Skipping test suite because web driver is not available");
        }

    }

    @BeforeMethod
    public void initWebDriver() {
        try {
            driver = WebDriverPool.DEFAULT.getDriver(gridHubUrl, capabilities);
        } catch (Exception e) {
            throw new SkipException("Skipping test becaue Webdriver could not be obtained");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        WebDriverPool.DEFAULT.dismissAll();
    }
}
