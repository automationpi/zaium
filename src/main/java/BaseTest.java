package com.example.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
    protected WebDriver driver;
    protected Properties properties;
    private static final String ZAP_PROXY = "localhost:8080";
    private static final String ZAP_API_KEY = "4f59d5ad-03f1-40be-86bf-a00303273166";
    protected ClientApi zapApi;

    @BeforeSuite
    public void initZap() {
        zapApi = new ClientApi("localhost", 8080, ZAP_API_KEY);
    }

    @BeforeMethod
    public void setUp() throws IOException {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(ZAP_PROXY).setSslProxy(ZAP_PROXY);
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.PROXY, proxy);
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
    }


    public void tearDownAndGenerateReport() throws ClientApiException {
        driver.quit();
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
