import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

public class ZAPReportListener extends AbstractWebDriverEventListener {

    private ClientApi zapApi;

    public ZAPReportListener(ClientApi zapApi) {
        this.zapApi = zapApi;
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        try {
            saveHtmlReportForPage(url);
        } catch (ClientApiException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveHtmlReportForPage(String url) throws ClientApiException, IOException {
        byte[] response = zapApi.core.htmlreport();
        String sanitizedUrl = url.replaceAll("[^a-zA-Z0-9_-]", "");
        Path outputPath = Paths.get("zap-report-" + sanitizedUrl + ".html");
        Files.write(outputPath, response);
        System.out.println("ZAP HTML Report for page " + url + " has been saved to: " + outputPath.toAbsolutePath());
    }
}
