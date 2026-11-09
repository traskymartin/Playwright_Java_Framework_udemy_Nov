package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {
    private Page page;
    private String base_URL;
    private static final String emailPlaceGolder = "you@email.com";
    private static final String passwordPlaceGolder = "Password";

    public DashboardPage loginToEventManagmentApplication() {
        return loginToEventManagmentApplication("trasky1508@gmail.com", "Master#91X");
    }

    public DashboardPage loginToEventManagmentApplication(String email, String password) {
        page.navigate(base_URL);
        page.getByPlaceholder(emailPlaceGolder).fill(email);
        page.getByLabel(passwordPlaceGolder).fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        return new DashboardPage(page);
    }

    public LoginPage(Page page, String baseUrl) {
        this.page = page;
        this.base_URL = baseUrl;
    }
}
