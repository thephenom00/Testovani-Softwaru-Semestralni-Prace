import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    private final String loginPage = "https://wa.toad.cz/~leductha/zwa/view/pages/login.php";
    private final String registerPage = "https://wa.toad.cz/~leductha/zwa/view/pages/register.php";
    private final String homePage = "https://wa.toad.cz/~leductha/zwa/view/pages/index.php";
    private final String taskPage = "https://wa.toad.cz/~leductha/zwa/view/pages/list-notes.php";

    private String username;
    private String password;
    private String email;

    WebDriver driver;

    @BeforeEach
    public void driver() {
        driver = new ChromeDriver();

    }

    @Test
    public void registerTest() {
        username = "312321";
        password = "allidoiswin123";
        email = "thegottt32tt@gmail.com";

        driver.get(registerPage);
        driver.findElement(By.id("user")).sendKeys(username);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("pass")).sendKeys(password);
        driver.findElement(By.id("confirmpass")).sendKeys(password);

        driver.findElement(By.id("buttonRegister")).click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(10));
        sleep.until(ExpectedConditions.urlToBe(loginPage));

        // Get the current URL
        String currentUrl = driver.getCurrentUrl();

        // Perform the assertEquals
        assertEquals(loginPage, currentUrl);

    }

    @Test
    public void loginToPageTest() {
        driver.get(loginPage);
        driver.findElement(By.id("user")).sendKeys("admin");
        driver.findElement(By.id("pass")).sendKeys("admin");

        driver.findElement(By.id("button")).click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(3));
        sleep.until(ExpectedConditions.urlToBe(homePage));

        // Get the current URL
        String currentUrl = driver.getCurrentUrl();

        // Perform the assertEquals
        assertEquals(homePage, currentUrl);
    }

    public void login() {
        driver.get(loginPage);
        driver.findElement(By.id("user")).sendKeys("admin");
        driver.findElement(By.id("pass")).sendKeys("admin");

        driver.findElement(By.id("button")).click();
    }

    @Test
    public void addTaskTest() {
        login();

        // Enter task details
        WebElement titleInput = driver.findElement(By.id("title"));
        titleInput.sendKeys("Task Title");

        WebElement dateInput = driver.findElement(By.id("date"));
        dateInput.sendKeys("12-12-2024");

        WebElement importanceCheckbox = driver.findElement(By.id("noteImportance"));
        importanceCheckbox.click();

        WebElement descriptionTextarea = driver.findElement(By.id("text"));
        descriptionTextarea.sendKeys("Task Description");

        // Submit the form
        driver.findElement(By.name("addNote")).click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(3));
        sleep.until(ExpectedConditions.urlToBe(taskPage));

        // Get the current URL
        String currentUrl = driver.getCurrentUrl();

        // Perform the assertEquals
        assertEquals(taskPage, currentUrl);


    }




}
