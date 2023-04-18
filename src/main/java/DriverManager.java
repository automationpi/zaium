
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.log4testng.Logger;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class DriverManager {

    private static final String ZAP_PROXY = "localhost:8080";
    private static final String ZAP_API_KEY = "4f59d5ad-03f1-40be-86bf-a00303273166";
    protected ClientApi zapApi;

    public WebDriver getDriver(String browser) throws Exception {
        WebDriver driver = null;
        zapApi = new ClientApi("localhost", 8080, ZAP_API_KEY);
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--aggressive-cache-discard");
            options.addArguments("--disable-cache");
            options.addArguments("--disable-application-cache");
            options.addArguments("--disable-offline-load-stale-cache");
            options.addArguments("--disk-cache-size=0");
            options.addArguments("--ignore-ssl-errors=yes");
            options.addArguments("--ignore-certificate-errors");
            //options.addArguments("headless");
            options.addArguments("window-size=1920x1080");
            options.addArguments("--disable-gpu");
            DesiredCapabilities capabilitiesChrome = new DesiredCapabilities();
            capabilitiesChrome.setCapability("browserName", "chrome");
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(ZAP_PROXY).setSslProxy(ZAP_PROXY);
            options.setCapability(CapabilityType.PROXY, proxy);
            WebDriverManager.chromedriver().setup();
            capabilitiesChrome.setCapability(ChromeOptions.CAPABILITY, options);
            driver = new ChromeDriver(capabilitiesChrome);
        } catch (Exception e) {
            throw new Exception("Unable to launch Browser.");
        }
        EventFiringWebDriver drv = new EventFiringWebDriver(driver);
        drv.register(new ZAPReportListener(zapApi));
        return drv;
    }

    public void finalizeReport() throws ClientApiException {
        byte[] response = null;
        try {
            response = zapApi.core.htmlreport();
            //save response as an html file
            // Save the report to a file
            Path outputPath = Paths.get("zap-report.html");
            try {
                Files.write(outputPath, response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("ZAP HTML Report has been saved to: " + outputPath.toAbsolutePath());
        } catch (ClientApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println("ZAP XML Report:\n" + response.toString());
    }
}
