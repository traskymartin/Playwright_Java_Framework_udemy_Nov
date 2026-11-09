package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardPage {
    private final Page page;
    public DashboardPage(Page page) {
        this.page = page;
    }
    public void waitForEventsToLoad(){
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
    }

    public MyBookingDetails goToMyBookings() {
        page.getByTestId("nav-bookings").click();
        return new MyBookingDetails(page);
    }
}
