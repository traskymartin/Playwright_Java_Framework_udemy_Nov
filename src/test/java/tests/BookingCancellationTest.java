package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingCancellationTest extends TestBase {

    @Test(groups = {"framework"}, 
          description = "View booking details and cancel booking - Verify record disappears")
    public void testBookingCancellation() {
        // Step 1: Login to application
        LoginPage loginPage = new LoginPage(page, base_URL);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();

        // Step 2: Navigate to My Bookings
        BookingsPage bookingsPage = new BookingsPage(page);
        bookingsPage.goTo();
        bookingsPage.waitForBookingsToLoad();

        // Step 3: Get first booking ID and open its details page
        Locator firstBookingLink = page.locator("a[href*=\"/bookings/\"]").first();
        String bookingUrl = firstBookingLink.getAttribute("href");
        String firstBookingId = bookingUrl.split("/")[bookingUrl.split("/").length - 1];
        BookingDetailsPage bookingDetailsPage = bookingsPage.viewBookingDetails(firstBookingId);

        // Step 4: Verify booking details page is displayed and capture booking reference for removal checks
        bookingDetailsPage.verifyBookingDetailsPageIsDisplayed();
        String bookingReference = bookingDetailsPage.verifyDetailsPageAndGetBookingReference();

        // Step 5: Cancel the booking
        Response cancellationResponse = bookingDetailsPage.cancelBooking();
        Assert.assertTrue(cancellationResponse.ok(),
                "Cancellation request failed with status " + cancellationResponse.status());
        Assert.assertTrue(cancellationResponse.text().contains("Booking cancelled"),
                "Cancellation response did not confirm the booking was cancelled");

        // Step 6: Verify we are back on bookings page and the booking has been removed from the list
        bookingDetailsPage.verifyBookingCancellationSuccess();
        MyBookingDetails myBookingDetails = new MyBookingDetails(page);
        myBookingDetails.verifyBookingWasRemoved(bookingReference);

        // Optional: ensure the deleted booking link doesn't remain visible
        assertThat(page.locator("a[href=\"/bookings/" + firstBookingId + "\"]")).not().isVisible();
    }

    @AfterMethod
    public void tearDown() {
        // Cleanup if needed
    }

}
