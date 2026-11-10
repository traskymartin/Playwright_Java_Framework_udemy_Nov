package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class BookingFormPage {
    private final Page page;
    private static final String fullNamePlaceHolder = "Your full name";
    private static final String customerEmailTestID = "customer-email";
    private static final String totalPriceLocator = "xpath=//span[text()='Total']/following-sibling::span";
    private static final String phoneloc = "Phone";
    private static final String confirmBookingLoc = "Confirm Booking";
    private static final String bookingReflect = "span.booking-ref";
    private static final String customerNameAfterBookingLoc = "xpath=//span[text()='Customer']/following-sibling::span";
    private static final String tickCountConfirmationLoc = "xpath=//span[text()='Tickets']/following-sibling::span";
    private static final String totalAmountOfTicketsLoc = "xpath=//span[text()='Total']/following-sibling::span";
    private static final String viewMyBookingsBTNLoc = "View My Bookings";

    public BookingFormPage(Page page) {
        this.page = page;
    }

    public void numberOfSeatBooking(int numberOfSeats) {
        Locator ticketCountInput = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+"));
        for (int i = 0; i < numberOfSeats; i++) {
            ticketCountInput.click();
        }
    }

    public void BookingTicketForCustomer(String name,String mail,String phone) {
        page.getByPlaceholder(fullNamePlaceHolder).fill(name);
        page.getByTestId(customerEmailTestID).fill(mail);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(phoneloc)).fill(phone);
        String totalPrice = page.locator(totalPriceLocator).innerText();
        System.out.println("Total Price: " + totalPrice);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(confirmBookingLoc)).click();
    }

    public String verifyBookingDetails() {
        String bookingRef = page.locator(bookingReflect).innerText(); // Q-KNKR27
        System.out.println("Booking Reference: " + bookingRef);
        String customerName = page.locator(customerNameAfterBookingLoc).innerText(); // we
        System.out.println("Customer Name: " + customerName);
        String ticketCount = page.locator(tickCountConfirmationLoc).innerText();
        System.out.println("Tickets: " + ticketCount);
        String totalAmount = page.locator(totalAmountOfTicketsLoc).innerText();
        System.out.println("Total Amount: " + totalAmount);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(viewMyBookingsBTNLoc)).click();
        return bookingRef;
    }
}
