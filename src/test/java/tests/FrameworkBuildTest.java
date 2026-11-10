package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameWorkBuildTest extends TestBase {

    @Test(description = " Create Event - Book that event and verify if its booked ")
    public void demotest() throws InterruptedException {
        ;
        String eventTitle = "Playwright Framework Test";
        LoginPage loginPage = new LoginPage(page, base_URL);
        DashboardPage dashboardPage = loginPage.loginToEventManagmentApplication();
        dashboardPage.waitForEventsToLoad();
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(eventTitle, "trasky", "Concert",
                "Test City", "Test Venue", "2026-11-21T11:56", "250", "200");

        // Find the new event and book it
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.gotoEventsPage();
        eventsPage.waitForEventsToLoad();
        eventsPage.findEventsCard(eventTitle);
        int seatNumberBeforeBooking = eventsPage.getSeatsCount();
        Locator specificEvent = eventsPage.getSpecificEvent();
        BookingFormPage bookingFormPage = eventsPage.proceedToBookingCardEvent();

        bookingFormPage.numberOfSeatBooking(5);
        bookingFormPage.BookingTicketForCustomer("Trasky", "trasky@gmail.com", "1234567890");
        String bookingRef = bookingFormPage.verifyBookingDetails();
        MyBookingDetails myBookingDetails = new MyBookingDetails(page);

        // seat count deducted after booking
//        eventsPage.gotoEventsPage();
//        int seatCountAfter = eventsPage.getSeatsCount();
//        System.out.println("Seat Count: " + seatNumberBeforeBooking);
//        Assert.assertTrue(seatNumberBeforeBooking > seatCountAfter, "nothing is same");
//        System.out.println("Seat Count Before Booking: " + seatNumberBeforeBooking);
//        System.out.println("Seat Count After Booking: " + seatCountAfter);

    }


}
