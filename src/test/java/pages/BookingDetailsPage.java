package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingDetailsPage {
    private final Page page;

    public BookingDetailsPage(Page page) {
        this.page = page;
    }

    public String verifyDetailsPageAndGetBookingReference() {
        assertThat(page).hasURL(Pattern.compile(".*/bookings/[^/?#]+/?(?:\\?.*)?$"));
        Locator bookingReference = page.getByText(Pattern.compile("^Q-[A-Z0-9]+$|"
            + "^[A-Za-z0-9-]+$", Pattern.CASE_INSENSITIVE)).first();
        if (bookingReference.count() > 0) {
            assertThat(bookingReference).isVisible();
            return bookingReference.innerText();
        }
        String url = page.url();
        String[] segments = url.split("/");
        return segments[segments.length - 1].split("\\?")[0];
    }

    public void verifyBookingDetailsPageIsDisplayed() {
        assertThat(page).hasURL(Pattern.compile(".*/bookings/[^/?#]+/?(?:\\?.*)?$"));
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName(Pattern.compile(".*Booking.*")))).isVisible();
    }

    public void verifyBookingCancellationSuccess() {
        assertThat(page).hasURL(Pattern.compile(".*/bookings/?(?:\\?.*)?$"));
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("My Bookings"))).isVisible();
    }

    public Response cancelBooking() {
        Response cancellationResponse = page.waitForResponse(
                response -> response.url().contains("/api/bookings/")
                        && response.request().method().equals("DELETE"),
                () -> {
                    page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Cancel Booking")).click();
                    page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Yes, cancel it")).click();
                });
        page.waitForURL(Pattern.compile(".*/bookings/?(?:\\?.*)?$"));
        return cancellationResponse;
    }
}