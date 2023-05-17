import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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
    public void setup() {
        // Set the path to the chromedriver executable
        System.setProperty("webdriver.chrome.driver", "/Users/goat/ts1/ts1-semestralni-prace/src/test/java/chromedriver");

        // Create ChromeOptions and set the start-fullscreen argument
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");

        // Create the WebDriver with the ChromeOptions
        driver = new ChromeDriver(options);
    }


    @Test
    public void registerTest() {
        username = "312231321";
        password = "allidoiswin123";
        email = "theg321ottt32tt@gmail.com";

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

        driver.quit();

        // Perform the assertEquals
        assertEquals(loginPage, currentUrl);

    }

    @Test
    public void loginToPageTest() {
        driver.get(loginPage);
        driver.findElement(By.id("user")).sendKeys("admin");
        driver.findElement(By.id("pass")).sendKeys("admin");

        driver.findElement(By.id("button")).click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(10));
        sleep.until(ExpectedConditions.urlToBe(homePage));

        // Get the current URL
        String currentUrl = driver.getCurrentUrl();

        driver.quit();

        // Perform the assertEquals
        assertEquals(homePage, currentUrl);
    }

    @Test
    public void loginErrorTest() {
        driver.get(loginPage);
        driver.findElement(By.id("user")).sendKeys("wrongName");
        driver.findElement(By.id("pass")).sendKeys("wrongPassword");

        driver.findElement(By.id("button")).click();

        // Wait for the error message to be visible
        WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorMessage = errorWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.loginError")));

        // Get the error message text
        String errorText = errorMessage.getText();

        // Verify the error message
        assertEquals("Přihlašovací údaje nebyly zadány správně", errorText);

        driver.quit();
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

        driver.quit();

        // Perform the assertEquals
        assertEquals(taskPage, currentUrl);
    }

    @Test
    public void deleteTaskTest() {
        login();

        // Wait for the Poznámky link to be clickable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement taskLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='../pages/list-notes.php']")));

        taskLink.click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(3));
        sleep.until(ExpectedConditions.urlToBe(taskPage));

        // Get the initial number of tasks
        List<WebElement> taskElements = driver.findElements(By.cssSelector("ul.wrapper li.note"));
        int tasksOnPageCount = taskElements.size();

        // Delete a task if there are tasks on the page
        if (tasksOnPageCount > 0) {
            WebElement deleteButton = driver.findElement(By.cssSelector("button[name='deleteButton']"));
            deleteButton.click();

            // Get the updated number of tasks
            taskElements = driver.findElements(By.cssSelector("ul.wrapper li.note"));
            int updatedTaskCount = taskElements.size();

            // Assert that the number of tasks has decreased by 1
            assertEquals(tasksOnPageCount - 1, updatedTaskCount);
        } else {
            // Assert that there are no tasks on the page
            assertEquals(0, tasksOnPageCount);
        }
        driver.quit();
    }

    @Test
    public void doneButtonTest() {
        login();

        // Wait for the Poznámky link to be clickable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement taskLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='../pages/list-notes.php']")));

        taskLink.click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(3));
        sleep.until(ExpectedConditions.urlToBe(taskPage));

        // Get the initial number of tasks
        List<WebElement> taskElements = driver.findElements(By.cssSelector("ul.wrapper li.note"));
        int tasksOnPageCount = taskElements.size();

        // Delete a task if there are tasks on the page
        if (tasksOnPageCount > 0) {
            WebElement doneButton = driver.findElement(By.cssSelector("button[name='doneButton']"));
            doneButton.click();

            // Wait for the note element to have the 'done' class
            WebDriverWait doneWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            doneWait.until(ExpectedConditions.attributeContains(By.cssSelector("ul.wrapper li.note"), "class", "done"));

            // Verify that the 'done' class is added to the note element
            WebElement noteElement = driver.findElement(By.cssSelector("ul.wrapper li.note"));
            String classAttributeValue = noteElement.getAttribute("class");
            assertEquals("note done", classAttributeValue);
            driver.quit();
        } else {
            driver.quit();
        }
    }










}
