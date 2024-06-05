package org.softserve.academy.logining;

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
@DisplayName("Login Modal Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginModalTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String BASE_URL = "http://speak-ukrainian.eastus2.cloudapp.azure.com/dev/";
    private static final String USER_ICON_CSS_SELECTOR = "svg[data-icon='user']";
    private static final String DROPDOWN_MENU_CSS_SELECTOR = ".ant-dropdown-menu";
    private static final String LOGIN_HEADER_CSS_SELECTOR = ".login-header";
    private static final String EMAIL_INPUT_CSS_SELECTOR = "#basic_email";
    private static final String PASSWORD_INPUT_CSS_SELECTOR = "#basic_password";
    private static final String LOGIN_BUTTON_CSS_SELECTOR = ".login-button";

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
    @Order(1)
    @DisplayName("1. Test clicking on user icon")
    public void testClickUserIcon() {
        WebElement userIcon = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(USER_ICON_CSS_SELECTOR)));
        scrollToElement(userIcon);
        clickElementWithJS(userIcon);

        WebElement dropdownMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(DROPDOWN_MENU_CSS_SELECTOR)));
        assertNotNull(dropdownMenu);
    }

    @Test
    @Order(2)
    @DisplayName("2. Test clicking on 'Login' menu item")
    public void testClickLoginMenuItem() {
        openModalWindow();
    }

    @Test
    @Order(3)
    @DisplayName("3. Test login modal header text")
    public void testLoginModalHeader() {
        openModalWindow();

        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LOGIN_HEADER_CSS_SELECTOR)));
        assertNotNull(header);
        assertEquals("Вхід", header.getText());
    }

    @Test
    @Order(4)
    @DisplayName("4. Test login form fields presence and placeholders")
    public void testLoginFormFieldsPresence() {
        openModalWindow();

        assertEquals("Введіть ваш емейл", driver.findElement(By.cssSelector(EMAIL_INPUT_CSS_SELECTOR)).getAttribute("placeholder"));
        assertEquals("Введіть ваш пароль", driver.findElement(By.cssSelector(PASSWORD_INPUT_CSS_SELECTOR)).getAttribute("placeholder"));
    }

    @Test
    @Order(5)
    @DisplayName("5. Test filling login form fields")
    public void testLoginFormFields() {
        openModalWindow();

        WebElement emailInput = fillAndAssertField(EMAIL_INPUT_CSS_SELECTOR, "test@gmail.com");
        WebElement passwordInput = fillAndAssertField(PASSWORD_INPUT_CSS_SELECTOR, "TestPass123!");

        assertEquals("test@gmail.com", emailInput.getAttribute("value"));
        assertEquals("TestPass123!", passwordInput.getAttribute("value"));
    }

    @Test
    @Order(6)
    @DisplayName("6. Test login button activation after filling form")
    public void testLoginButton() {
        openModalWindow();

        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(LOGIN_BUTTON_CSS_SELECTOR)));
        assertTrue(loginButton.isEnabled(), "Login button should be enabled after filling all fields");
    }

    @Test
    @Order(7)
    @DisplayName("7. Test successful login")
    public void testSuccessfulLogin() {
        openModalWindow();

        fillAndAssertField(EMAIL_INPUT_CSS_SELECTOR, "test@gmail.com");
        fillAndAssertField(PASSWORD_INPUT_CSS_SELECTOR, "TestPass123!");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(LOGIN_BUTTON_CSS_SELECTOR)));
        clickElementWithJS(loginButton);

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ant-message-success")));
        assertTrue(successMessage.isDisplayed(), "Success message should be displayed");
    }

    private WebElement fillAndAssertField(String fieldCssSelector, String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(fieldCssSelector)));
        assertNotNull(field, "Field with CSS Selector '" + fieldCssSelector + "' should be present");
        field.sendKeys(value);
        return field;
    }

    @Test
    @Order(8)
    @DisplayName("8. Test login with empty fields")
    public void testLoginWithEmptyFields() {
        openModalWindow();

        //TODO
    }

    @Test
    @Order(9)
    @DisplayName("9. Test login with incorrect email format")
    public void testLoginWithIncorrectEmailFormat() {
        openModalWindow();

        //TODO
    }

    @Test
    @Order(10)
    @DisplayName("10. Test login with incorrect password")
    public void testLoginWithIncorrectPassword() {
        openModalWindow();
        //TODO

    }

    @Test
    @Order(11)
    @DisplayName("11. Test login with short password")
    public void testLoginWithShortPassword() {
        openModalWindow();
        //TODO
    }

    private void openModalWindow() {
        WebElement userIcon = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(USER_ICON_CSS_SELECTOR)));
        scrollToElement(userIcon);
        clickElementWithJS(userIcon);

        WebElement dropdownMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(DROPDOWN_MENU_CSS_SELECTOR)));
        assertNotNull(dropdownMenu);

        WebElement menuItem = findElementContainingText();
        scrollToElement(menuItem);
        clickElementWithJS(menuItem);
    }

    private WebElement findElementContainingText() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "return Array.from(document.querySelectorAll(\"" + "li[role='menuitem'] div" + "\")).find(element => element.textContent.includes(\"" + "Увійти" + "\"));";
        return (WebElement) js.executeScript(script);
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