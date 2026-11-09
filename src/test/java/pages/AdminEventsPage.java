package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {
    private final Page page;
    private final String eventTitleInputLocator = "#event-title-input";
    private final String describeEvent = "textarea[placeholder='Describe the event…']";
    private final String addEventBtnLoctor = "#add-event-btn";
    private final String categoryLable = "Category";
    private final String cityLabel = "City";
    private final String venueLabel = "Venue";
    private final String eventDateAndTimeLabel = "Event Date & Time";
    private final String priceLabel = "Price ($)";
    private final String totalSeatsLabel = "Total Seats";

    public AdminEventsPage(Page page) {
        this.page = page;
    }

    public void goTo() {
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    }

    public void createEvent(String title, String description, String category, String city, String venue,
                            String dateAndTime, String price, String totalSeats) {
        page.locator(eventTitleInputLocator).fill(title, new Locator.FillOptions().setTimeout(5000));
        page.locator(describeEvent).fill(description);
        page.getByLabel(categoryLable).selectOption(category);
        page.getByLabel(cityLabel).fill(city);
        page.getByLabel(venueLabel).fill(venue);
        page.getByLabel(eventDateAndTimeLabel).fill(dateAndTime);
        page.getByLabel(priceLabel).fill(price);
        page.getByLabel(totalSeatsLabel).fill(totalSeats);
        page.locator(addEventBtnLoctor).click();
        assertThat(page.getByText("Event created!")).isVisible();
    }
}
