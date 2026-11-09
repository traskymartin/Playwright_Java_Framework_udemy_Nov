package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {
    private final   Page page;
    private Locator eventCards;
    private Locator specificEvent;

    public Locator getSpecificEvent() {
        return specificEvent;
    }

    public EventsPage(Page page) {
        this.page = page;
    }

    public void gotoEventsPage() {
        page.locator("#nav-events").click();
    }

    public void waitForEventsToLoad() {
        eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
    }

    public void findEventsCard(String titleCard) {
        specificEvent = eventCards.filter(new Locator.FilterOptions().setHasText(titleCard));
        assertThat(specificEvent).isVisible();
    }

    public int getSeatsCount() {
//        assertThat(specificEvent).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        String seamount = specificEvent.getByText("seats").innerText();
        System.out.println("Seat Count: " + seamount);
        return Integer.parseInt(seamount.split(" ")[0]);
    }
    public BookingFormPage proceedToBookingCardEvent(){
        specificEvent.getByTestId("book-now-btn").click();
        return new BookingFormPage(page);
    }

}