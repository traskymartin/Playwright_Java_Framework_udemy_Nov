package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class BasicsTest {

//Invoke browser ->Invoke tab/page->type url
    //HeadMode- Headless
    Playwright  playwright;
    Browser browser;
    Page page;
    @BeforeMethod
    public void setUp()
    {
        playwright = Playwright.create();
       browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage(); //browser.newContext();
        page.setDefaultTimeout(8000);
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000); //Global Assertion time out 7 seconds

    }

    @Test(description = "Create Event -Book that event and verify if its booked")
    public void DemoTest()
    {

        System.out.println(page.title());
       assertThat(page).hasTitle("EventHub — Discover & Book Events");
       //page.getByLabel("Email").fill("rahulshetty1@yahoo.com");
        page.getByPlaceholder("you@email.com").fill("rahulshetty1@yahoo.com");
       page.getByLabel("Password").fill("Magiclife1!");
       //locator
       page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Sign In")).click();

       assertThat(page.getByRole(AriaRole.LINK,
               new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

       //Step 1 - Create Event from Admin page

       page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
        //10 seconds - Actions
       page.locator("#event-title-input").fill("Rahul Shetty QA Summit",new Locator.FillOptions().setTimeout(10000));
       page.locator("#admin-event-form textarea").fill("Rahul Shetty QA Meetups");
       page.getByLabel("Category").selectOption("Concert");
       page.getByLabel("City").fill("Test City");
       page.getByLabel("Venue").fill("Test Venue");
       page.getByLabel("Event Date & Time").fill("2026-12-18T07:25");
       page.getByLabel("Price ($)").fill("100");
       page.getByLabel("Total Seats").fill("50");
       page.locator("#add-event-btn").click(new Locator.ClickOptions().setTimeout(12000));

       //Event created! 2-3
       assertThat(page.getByText("Event created!")).isVisible(); //5 seconds -Assertions







       //Step 2 - Find newly created event in the events page
        page.locator("#nav-events").click();
        Locator eventCards = page.getByTestId("event-card"); //{loc1,loc2,loc3,loc4}
        System.out.println(eventCards.count());
        //Visibility of the card which we have added
       Locator targetCard =  eventCards.filter(new Locator.FilterOptions().setHasText("Rahul Shetty QA Summit"));

       assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));

















       String seatsText =  targetCard.getByText("seats").innerText();
       System.out.println(seatsText);
        int seatsNumBeforeBooking =Integer.parseInt(seatsText.split(" ")[0]);
       targetCard.getByTestId("book-now-btn").click();

       // Book the ticket for event


        page.getByLabel("Full Name").fill("Test Student");
        page.locator("#customer-email").fill("test.student@example.com");
        page.getByPlaceholder("+91 98765 43210").fill("9876543210");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        // #confirm-booking
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        // Verify in Booking History

        Locator bookingCards = page.locator("#booking-card"); //{local1,loc2,loc3}
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetBookingCard).isVisible();
      // seat count reduction check
        page.locator("#nav-events").click();
        page.waitForTimeout(1000);

        Locator eventCardsAfterBooking = page.getByTestId("event-card"); //{loc1,loc2,loc3,loc4}
        //Visibility of the card which we have added
        Locator targetCardAfterBooking =  eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("Rahul Shetty QA Summit"));
        String seatsTextAfterBooking =  targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTextAfterBooking);
        //Afterbookings < BeforeBookings
        //  46 seats available
        int seatsNumAfterBooking =Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

        Assert.assertTrue(seatsNumBeforeBooking > seatsNumAfterBooking);

























































       //locators
    }


    @AfterMethod
    public void tearDown()
    {


    }





}
