package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.Tracing;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.nio.file.Paths;

import com.microsoft.playwright.options.AriaRole;

public class MockWebTest {
    private Playwright playwright;
    private Browser browser;
    private Page page;
    private BrowserContext context;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions());
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.setDefaultTimeout(5000);
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        page.getByPlaceholder("you@email.com").fill("trasky1508@gmail.com");
        page.getByLabel("Password").fill("Master#91X");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
    }

    @Test(groups = "regression", description = " Create Event - Book that event and verify if its booked ")
    public void demotest() {
        // assertThat(page).hasTitle("EventHub — Discover & Book Events");
        // page.getByText("Email").fill("");

        // validate the page login successfully
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        page.route("**/api/events**", route -> route
                .fulfill(new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_6.json"))));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        Locator eventCard = page.getByTestId("event-card");
        assertThat(eventCard.first()).isVisible();
        Assert.assertEquals(eventCard.count(), 6);
        assertThat(page.locator(".mx-1").first()).isVisible();

        page.route("**/api/events**", route -> route
                .fulfill(new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_4.json"))));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        Locator eventCard_4 = page.getByTestId("event-card");
        page.waitForTimeout(7000);
        assertThat(eventCard_4.first()).isVisible();
        Assert.assertEquals(eventCard_4.count(), 4);
        assertThat(page.locator(".mx-1").first()).isHidden();
    }

    @Test 
    public void routeresumeTest() {
        // route.resume()
        page.getByTestId("nav-bookings").click();
        page.route("**/api/bookings**", route -> route.resume(
                new Route.ResumeOptions().setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/72116")));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();
        assertThat(page.getByText("View My Bookings")).isVisible();

    }
}