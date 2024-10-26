package org.softserve.academy.clubs;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Clubs Page Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClubsPageTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String BASE_URL = "http://speak-ukrainian.eastus2.cloudapp.azure.com/dev/";
    private static final String CLUBS_MENU_ITEM_CSS = "span.ant-menu-title-content a[href='/dev/clubs']";
    private static final String SECOND_PAGE_LINK_CSS = "li.ant-pagination-item-2 a";
    private static final String LOGO_SELECTOR_CSS = ".left-side-menu .logo";

    @BeforeAll
    public void setUpAll() {
        try {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.get(BASE_URL);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Setup failed: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUpEach() {
        driver.get(BASE_URL);
    }

    @AfterEach
    public void tearDownEach() {
        driver.manage().deleteAllCookies();
    }

    @Test
    @DisplayName("Test finding and clicking on 'Clubs' menu item")
    public void testClickClubsMenuItem() {
        WebElement clubsMenuItem = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(CLUBS_MENU_ITEM_CSS)));
        scrollToElement(clubsMenuItem);
        clickElementWithJS(clubsMenuItem);

        String expectedUrl = BASE_URL + "clubs";
        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        assertEquals(expectedUrl, driver.getCurrentUrl(), "URL should be the clubs page URL");
    }

   @Test
    @DisplayName("Test navigating to second page")
    public void testNavigateToSecondPage() {
        testClickClubsMenuItem();

        WebElement secondPageLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(SECOND_PAGE_LINK_CSS)));
        scrollToElement(secondPageLink);
        secondPageLink.click();
        //TODO
    }

    @Test
    @DisplayName("Test clicking on club card and navigating to details page")
    public void testClickClubCard() {
        testClickClubsMenuItem();

        //TODO
    }

    @Test
    @DisplayName("Test finding and clicking 'Leave a Comment' button")
    public void testClickLeaveCommentButton() {
        //TODO
    }

    @Test
    @DisplayName("Test writing a comment and submitting")
    public void testWriteCommentAndSubmit() {
        testClickLeaveCommentButton();

        //TODO
    }

    @Test
    @DisplayName("Test finding the logo in the left-side menu")
    public void testFindLogoInLeftSideMenu() {
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LOGO_SELECTOR_CSS)));
        assertTrue(logo.isDisplayed(), "Logo should be visible in the left-side menu");
    }

    private void clickElementWithJS(WebElement element) {
        if (element != null && element.isDisplayed() && element.isEnabled()) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));", element);
        } else {
            throw new IllegalArgumentException("Element is not clickable: " + element);
        }
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    @AfterAll
    public void tearDownAll() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
