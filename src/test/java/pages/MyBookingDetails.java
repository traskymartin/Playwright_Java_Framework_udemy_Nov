package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MyBookingDetails {
    private final Page page;
    private static final String bookingCardsTestId = "booking-card";

    public MyBookingDetails(Page page) {
        this.page = page;
    }

    public BookingDetailsPage openFirstAvailableBooking() {
        Locator firstBooking = page.getByTestId(bookingCardsTestId).first();
        assertThat(firstBooking).isVisible();
        firstBooking.getByRole(AriaRole.LINK,
                new Locator.GetByRoleOptions().setName("View Details")).click();
        return new BookingDetailsPage(page);
    }

    public void verifyBookingWasRemoved(String bookingReference) {
        Locator matchingBooking = page.getByTestId(bookingCardsTestId)
                .filter(new Locator.FilterOptions().setHasText(bookingReference));
        assertThat(matchingBooking).hasCount(0);
    }

}
