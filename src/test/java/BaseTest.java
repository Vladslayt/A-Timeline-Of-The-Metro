import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    protected Playwright playwright1;
    protected Browser browser1;
    protected Playwright playwright2;
    protected Browser browser2;
    protected Playwright playwright3;
    protected Browser browser3;
    protected Playwright playwright4;
    protected Browser browser4;
    protected Playwright playwright5;
    protected Browser browser5;

    @BeforeAll
    public void launchBrowsers() {
        playwright1 = Playwright.create();
        browser1 = playwright1.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

        playwright2 = Playwright.create();
        browser2 = playwright2.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

        playwright3 = Playwright.create();
        browser3 = playwright3.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

        playwright4 = Playwright.create();
        browser4 = playwright4.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

        /*playwright5 = Playwright.create();
        browser5 = playwright5.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));*/
    }

    @AfterAll
    public void closeBrowsers() {
        browser1.close();
        playwright1.close();
        browser2.close();
        playwright2.close();
        browser3.close();
        playwright3.close();
        browser4.close();
        playwright4.close();
        browser5.close();
        playwright5.close();
    }
}
