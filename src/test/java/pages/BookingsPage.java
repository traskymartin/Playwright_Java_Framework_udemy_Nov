package pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingsPage {

    Page page;

    public BookingsPage(Page page) {
        this.page = page;
    }

    public void goTo() {
        page.navigate("https://eventhub.rahulshettyacademy.com/bookings");
    }

    public BookingDetailsPage viewBookingDetails(String bookingId) {
        page.locator("a[href=\"/bookings/" + bookingId + "\"]").click();
        return new BookingDetailsPage(page);
    }

    public boolean isBookingVisible(String bookingReference) {
        return page.getByText(bookingReference).isVisible();
    }

    public void waitForBookingsToLoad() {
        assertThat(page.getByRole(com.microsoft.playwright.options.AriaRole.HEADING, 
            new Page.GetByRoleOptions().setName("My Bookings"))).isVisible();
    }

}
