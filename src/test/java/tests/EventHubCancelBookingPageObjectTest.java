package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import pages.BookingDetailsPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.MyBookingDetails;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventHubCancelBookingPageObjectTest extends TestBase {

    @Test(description = "Cancel the first available EventHub booking")
    public void cancelFirstAvailableBooking() {
        String username = requiredEnvironmentVariable("EVENTHUB_USERNAME");
        String password = requiredEnvironmentVariable("EVENTHUB_PASSWORD");

        LoginPage loginPage = new LoginPage(page, base_URL);
        DashboardPage dashboardPage = loginPage.loginToEventManagmentApplication(username, password);
        dashboardPage.waitForEventsToLoad();

        MyBookingDetails myBookingDetails = dashboardPage.goToMyBookings();
        assertThat(page.getByRole(AriaRole.HEADING,
                new com.microsoft.playwright.Page.GetByRoleOptions().setName("My Bookings"))).isVisible();
        BookingDetailsPage bookingDetailsPage = myBookingDetails.openFirstAvailableBooking();
        assertThat(page).hasURL(Pattern.compile(".*/bookings/[^/?#]+/?(?:\\?.*)?$"));
        String bookingReference = bookingDetailsPage.verifyDetailsPageAndGetBookingReference();

        Response cancellationResponse = bookingDetailsPage.cancelBooking();
        Assert.assertTrue(cancellationResponse.ok(),
                "Cancellation request failed with status " + cancellationResponse.status());
        Assert.assertTrue(cancellationResponse.text().contains("Booking cancelled"),
                "Cancellation response did not confirm the booking was cancelled");
        page.waitForURL(Pattern.compile(".*/bookings/?(?:\\?.*)?$"));
        myBookingDetails.verifyBookingWasRemoved(bookingReference);
    }

    private String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            value = System.getProperty(name);
        }
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Set the " + name
                    + " environment variable or JVM system property before running this test");
        }
        return value;
    }
}
