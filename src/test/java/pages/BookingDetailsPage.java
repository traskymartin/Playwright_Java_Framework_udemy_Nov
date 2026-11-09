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
        assertThat(page.getByTestId("nav-bookings")).isVisible();
        Locator bookingReference = page.getByText(Pattern.compile("^Q-[A-Z0-9]+$")).first();
        assertThat(bookingReference).isVisible();
        return bookingReference.innerText();
    }

    public Response cancelBooking() {
        return page.waitForResponse(
                response -> response.url().contains("/api/bookings/")
                        && response.request().method().equals("DELETE"),
                () -> {
                    page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Cancel Booking")).click();
                    page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Yes, cancel it")).click();
                });
    }
}
