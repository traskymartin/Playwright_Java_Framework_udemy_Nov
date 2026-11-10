package tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.nio.file.Paths;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;

public class UIValidationContinue {

    private Playwright playwright;
    private Browser browser;
    private Page page;
    private BrowserContext context;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions());
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @Test
    public void popUpValidation() {
        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();
        page.onDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Alert")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        // page.getByRole(AriaRole.LINK, new
        // Page.GetByRoleOptions().setName("Top")).click();
        // page.waitForTimeout(3000);
        FrameLocator frameLocator = page.frameLocator("#courses-iframe");
        frameLocator.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning Paths")).click();

    }

    @Test
    public void screenshotTest() {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreen.png")));
        Locator displayEditBox = page.getByPlaceholder("Hide/Show Example");
        page.locator("#hide-textbox").click();
        displayEditBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("displayEditBox.png")));
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenpost.png")));
    }

    @AfterMethod
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