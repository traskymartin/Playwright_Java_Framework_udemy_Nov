package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicTest {
    private Page page;
    private BrowserContext context;

    @BeforeMethod
    public void setUp() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions());
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

    @Test(description = " Create Event - Book that event and verify if its booked ")
    public void demotest() {
        // assertThat(page).hasTitle("EventHub — Discover & Book Events");
        // page.getByText("Email").fill("");

        // validate the page login successfully
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        // create event
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");

        // we can adjust autowait in locator level using the setDefaultTimeout method
        page.locator("#event-title-input").fill("Qa summit", new Locator.FillOptions().setTimeout(5000));
        page.locator("textarea[placeholder='Describe the event…']").fill("trasky");

        // When we are need a addition second of waiting for the page to load, we can
        // use this apply all the assertion
        // PlaywrightAssertions.setDefaultAssertionTimeout(7000);
        page.getByLabel("Category").selectOption("Concert");
        page.getByLabel("City").fill("Test City");
        page.getByLabel("Venue").fill("Test Venue");
        page.getByLabel("Event Date & Time").fill("2026-11-21T11:56");
        page.getByLabel("Price ($)").fill("250");
        page.getByLabel("Total Seats").fill("200");
        page.locator("#add-event-btn").click();
        assertThat(page.getByText("Event created!")).isVisible();

        // if we want to autowait for the element use this method we have to increase
        // and decrease the time using the setDefaultTimeout method
        page.setDefaultTimeout(8000);

        // Find the new event and book it
        page.locator("#nav-events").click();
        Locator eventCards = page.getByTestId("event-card");
        Locator specificEvent = eventCards.filter(new Locator.FilterOptions().setHasText("Qa summit"));
        assertThat(specificEvent).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));

        String seatcount = getSeatCount(specificEvent);
        specificEvent.getByTestId("book-now-btn").click();

        // }

        // @Test(description = " Create Event - Book that event and verify if its booked
        // ")
        // public void bookEventTest() {
        // page.locator("#nav-events").click();
        // Locator eventCards = page.getByTestId("event-card");
        // Locator specificEvent = eventCards.filter(new
        // Locator.FilterOptions().setHasText("Qa summit"));
        // assertThat(specificEvent).isVisible(new
        // LocatorAssertions.IsVisibleOptions().setTimeout(10000));

        // String seatCount = getSeatCount(specificEvent);
        // System.out.println("Seat Count: " + seatCount);
        // specificEvent.getByTestId("book-now-btn").click();

        // page.locator("#ticket-count").evaluate("e1 => e1.textContent = '5' ");
        // assertThat(page.getByText("5", new
        // Page.GetByTextOptions().setExact(true))).isVisible();
        Locator ticketCountInput = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+"));
        for (int i = 0; i < 4; i++) {
            ticketCountInput.click();
        }

        page.getByPlaceholder("Your full name").fill("Trasky");
        page.getByTestId("customer-email").fill("trasky@gmail.com");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Phone")).fill("1234567890");
        String totalPrice = page.locator("xpath=//span[text()='Total']/following-sibling::span").innerText();
        System.out.println("Total Price: " + totalPrice);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        String bookingRef = page.locator("span.booking-ref").innerText(); // Q-KNKR27
        System.out.println("Booking Reference: " + bookingRef);
        String customerName = page.locator("xpath=//span[text()='Customer']/following-sibling::span").innerText(); // we
        System.out.println("Customer Name: " + customerName);
        String ticketCount = page.locator("xpath=//span[text()='Tickets']/following-sibling::span").innerText();
        System.out.println("Tickets: " + ticketCount);
        String totalAmount = page.locator("xpath=//span[text()='Total']/following-sibling::span").innerText();
        System.out.println("Total Amount: " + totalAmount);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View My Bookings")).click();
//        Locator bookingCards = page.getByTestId("booking-card");

        // verfication of booking reference - in my booking page
//        Locator specificBooking = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));

        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("booking.png")).setFullPage(true));

        // seat count deducted after booking
        page.locator("#nav-events").click();
        page.waitForTimeout(1000);
        Locator eventCardsAfter = page.getByTestId("event-card");
        Locator specificEventAfter = eventCardsAfter.filter(new Locator.FilterOptions().setHasText("Qa summit"));
        assertThat(specificEvent).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));

        String seatCountafter = getSeatCount(specificEventAfter);
        System.out.println("Seat Count: " + seatcount);
        seatcount = seatcount.replaceAll("[^0-9]", "");

        int seatCountBefore = Integer.parseInt(seatcount);
        int seatCountAfter = Integer.parseInt(seatCountafter.replaceAll("[^0-9]", ""));
        Assert.assertTrue(seatCountBefore > seatCountAfter, "nothing is same");

        System.out.println("Seat Count Before Booking: " + seatCountBefore);
        System.out.println("Seat Count After Booking: " + seatCountAfter);

    }

    private static String getSeatCount(Locator specificEvent) {
        return specificEvent.getByText("seats").innerText();
    }

    @AfterMethod
    public void tearDown() {
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
    }
}