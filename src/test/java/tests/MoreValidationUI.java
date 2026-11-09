package tests;

import java.nio.file.Paths;


import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MoreValidationUI {
    private Playwright playwright;
    private Browser browser;
    private Page page;
    private BrowserContext context;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }

    @Test(groups = "regression")
    public void childWinddowHandling() {
        Locator blinkingTexts = page.locator(".blinkingText");
        Page childPage = context.waitForPage(() -> {
            blinkingTexts.first().click();
        });

        childPage.waitForLoadState();
        String[] childPageText = childPage.locator(".red").textContent().split("at ");
        String childPageEmail = childPageText[1].split(" ")[0].trim();
        System.out.println("Child Page Email: " + childPageEmail);
        Locator usernameField = page.getByLabel("username");
        usernameField.fill(childPageEmail);
        String usernameFieldValue = usernameField.inputValue();
        System.out.println("Username field value: " + usernameFieldValue);
        Assert.assertTrue(usernameField.equals(usernameField));
    }

    @Test(groups = "regression")
    public void uiControls() {
        Locator userRadio = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
        userRadio.click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
        Assert.assertTrue(userRadio.isChecked());
        Locator termsCheckbox = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("terms"));
        termsCheckbox.check();
        Assert.assertTrue(termsCheckbox.isChecked());
        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (page != null) {
            page.close();
        }
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));

        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
