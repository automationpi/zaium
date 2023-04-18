import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class WebTest extends com.example.tests.BaseTest {
    @Test()
    public void HTMLTableMax() throws Exception {
        DriverManager objManager = new DriverManager();
        WebDriver driver = objManager.getDriver("chrome");

        boolean testresult = true;
        try {
            driver.manage().window().maximize();
            driver.get(" https://juice-shop.herokuapp.com");
            driver.get(" https://google.com");
            driver.close();
        } catch (Exception e) {

        }finally {
            {
                objManager.finalizeReport();
            }
        }
    }
}
