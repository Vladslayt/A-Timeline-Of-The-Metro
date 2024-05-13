import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    protected Page page;

    @BeforeAll
    public void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    public void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    public void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    public void closeContext() {
        context.close();
    }
}
