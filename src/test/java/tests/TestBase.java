package tests;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class TestBase {
    public Page page;
    public BrowserContext context;
    public Browser browser;
    private Playwright playwright;
    public String base_URL;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            prop.load(fis);
        }
        String browserName = System.getProperty("browser", prop.getProperty("browser", "chromium"));
        playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(false);
        if (browserName.equalsIgnoreCase("chrome") || browserName.equalsIgnoreCase("chromium")) {
            browser = playwright.chromium().launch(launchOptions);
        } else if (browserName.equalsIgnoreCase("firefox")) {
            browser = playwright.firefox().launch(launchOptions);
        } else if (browserName.equalsIgnoreCase("webkit")) {
            browser = playwright.webkit().launch(launchOptions);
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browserName
                    + ". Use chrome, chromium, firefox, or webkit.");
        }
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();

        page.setDefaultTimeout(5000);
        base_URL = prop.getProperty("qa.baseurl");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (context != null) {
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
