# ZAP Proxy with Selenium WebDriver for Security Testing

This project demonstrates how to use the OWASP ZAP Proxy with Selenium WebDriver for automated security testing of web applications. In this example, we test the intentionally vulnerable web application [OWASP Juice Shop](https://juice-shop.herokuapp.com).

## Prerequisites

- Docker
- Java 8 or later
- Maven

## Getting Started

### Step 1: Install and Run ZAP Proxy

First, pull the official ZAP Proxy Docker image:

```bash
docker pull owasp/zap2docker-stable
```

Run the ZAP Proxy Docker container:
```
docker run -u zap -p 8080:8080 -i owasp/zap2docker-stable zap.sh -daemon -host 0.0.0.0 -port 8080 -config api.key=<your_zap_api_key>
```

Replace <your_zap_api_key> with a suitable API key for your ZAP instance.

### Step 2: Configure the Test Project
Clone this repository and navigate to the project root directory.

Update the DriverManager.java file with your ZAP API key:
private static final String ZAP_API_KEY = "<your_zap_api_key>";

### Step 3: Run the Test
Use your IDE to run test or
```
mvn test
```

Selenium WebDriver will navigate the web application, while the ZAP proxy will passively scan the traffic for vulnerabilities.

### Step 4: Review the Reports
![image](https://user-images.githubusercontent.com/82222256/232899965-5698b350-87c7-4d19-99c8-a524501a5bdc.png)
Sample report
The ZAP HTML reports will be generated for each page visited in the navigateWebApp() test. Each report will be saved in the format zap-report-<sanitizedUrl>.html in the project's root directory.
  
## Note
**The example provided in this project is for educational purposes only. It demonstrates how to use the ZAP Proxy with Selenium WebDriver for security testing. Always ensure you have proper authorization before testing any web application.**
