package tests;

import com.microsoft.playwright.Locator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtil;

import java.io.IOException;
import java.util.HashMap;

public class FrameworkBuildDataDrivenTest extends TestBase {

    @DataProvider(name = "eventBookingData")
    public Object[][] eventBookingData() throws IOException {
        return DataProviderUtil.getJsonDataToMap("/src/test/resources/eventBookingData.json");
    }


    @Test(dataProvider = "eventBookingData", groups = "smoke", description = " Create Event - Book that event and verify if its booked ")
    public void demotest(HashMap<String, String> data) throws InterruptedException {

        LoginPage loginPage = new LoginPage(page, base_URL);
        DashboardPage dashboardPage = loginPage.loginToEventManagmentApplication();
        dashboardPage.waitForEventsToLoad();
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        String titlePrefix = data.get("titlePrefix");
        adminEventsPage.createEvent(titlePrefix, data.get("description"), data.get("category"),
                data.get("city"), data.get("venue"), data.get("dateTime"), data.get("price"), data.get("totalSeats"));

        // Find the new event and book it
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.gotoEventsPage();
        eventsPage.waitForEventsToLoad();
        eventsPage.findEventsCard(titlePrefix);
        int seatNumberBeforeBooking = eventsPage.getSeatsCount();
        Locator specificEvent = eventsPage.getSpecificEvent();
        BookingFormPage bookingFormPage = eventsPage.proceedToBookingCardEvent();

        bookingFormPage.numberOfSeatBooking(5);
        bookingFormPage.BookingTicketForCustomer(data.get("fullName"), data.get("email"), data.get("phone"));
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