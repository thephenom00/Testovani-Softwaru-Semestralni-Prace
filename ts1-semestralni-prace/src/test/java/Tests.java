import io.opentelemetry.api.metrics.LongGaugeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

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

    /**
     * Login method
     */
    public void login() {
        driver.get(loginPage);
        driver.findElement(By.id("user")).sendKeys("test1");
        driver.findElement(By.id("pass")).sendKeys("test1password");

        driver.findElement(By.id("button")).click();
    }

    /**
     * Count all tasks method
     * @return returns number of tasks
     */
    public int countAllTasks() {
        login();
        int numberOfAllTasks = 0;

        driver.findElement(By.cssSelector("a[href='../pages/list-notes.php']")).click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(3));
        sleep.until(ExpectedConditions.urlToBe(taskPage));

        // We are on the first page
        List<WebElement> taskElements = driver.findElements(By.cssSelector("ul.wrapper li.note"));

        // If there is no tasks on page
        if (taskElements.size() > 0) {
            WebElement paginationForm = driver.findElement(By.id("paginationForm"));

            Select pageSelect = new Select(paginationForm.findElement(By.id("page_num")));
            List<WebElement> pageOptions = pageSelect.getOptions();
            int numberOfPages = pageOptions.size();

            if (numberOfPages > 1) {
                pageSelect.selectByValue(Integer.toString(numberOfPages));

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.urlToBe(taskPage + "?importanceFilter=All&page_num=" + numberOfPages));

                List<WebElement> tasksOnTheLastPage = driver.findElements(By.cssSelector("ul.wrapper li.note"));
                numberOfAllTasks += 8 * (numberOfPages - 1) + tasksOnTheLastPage.size();
            } else {
                numberOfAllTasks += taskElements.size();
            }
        }
        return numberOfAllTasks;


    }

    /**
     * Register Test //1
     */
    @Test
    public void registerTest() {
        username = "331323121";
        password = "allidoiswin123";
        email = "theg321otttt@gmail.com";

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

    /**
     * Login error Test //2
     */
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

    /**
     * Adding multiple tasks Test //3
     */
    @Test
    public void addMultipleTasksTest() {
        int numberOfAllTasksOnPage = countAllTasks();
        int numberOfTasksWantToAdd = 20;
        int expectedNumberOfTasks = numberOfAllTasksOnPage + numberOfTasksWantToAdd;

        // Counted all tasks, need to go back
        WebElement home = driver.findElement(By.cssSelector("a[href='../pages/index.php']"));
        home.click();


        // Adds all the tasks
        for (int i = 1; i <= numberOfTasksWantToAdd; i++) {
            // Enter task details
            WebElement titleInput = driver.findElement(By.id("title"));
            titleInput.sendKeys("Task Title " + i);

            WebElement dateInput = driver.findElement(By.id("date"));
            dateInput.sendKeys("12-12-2024");

            WebElement importanceCheckbox = driver.findElement(By.id("noteImportance"));
            importanceCheckbox.click();

            WebElement descriptionTextarea = driver.findElement(By.id("text"));
            descriptionTextarea.sendKeys("Task Description " + i);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("addNote")));

            // Submit the form
            submitButton.click();

            WebDriverWait sleeep = new WebDriverWait(driver, Duration.ofSeconds(5));
            sleeep.until(ExpectedConditions.urlToBe(taskPage));

            WebElement homee = driver.findElement(By.cssSelector("a[href='../pages/index.php']"));
            homee.click();
        }

        int numberOfAllTasksUpdated = countAllTasks();

        assertEquals(expectedNumberOfTasks, numberOfAllTasksUpdated);
        driver.quit();
    }

    /**
     * Removing all tasks Test //4
     */
    @Test
    public void removeAllTasksTest() {
        login();

        driver.findElement(By.cssSelector("a[href='../pages/list-notes.php']")).click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(3));
        sleep.until(ExpectedConditions.urlToBe(taskPage));

        List<WebElement> taskElements = driver.findElements(By.cssSelector("ul.wrapper li.note"));
        int tasksOnPageCount = taskElements.size();

        if (tasksOnPageCount == 0) {
            assertEquals(0, tasksOnPageCount);
        } else {
            boolean tasksExist = true;

            while (tasksExist) {
                WebElement deleteButton = driver.findElement(By.cssSelector("button[name='deleteButton']"));
                deleteButton.click();

                WebDriverWait deletionWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                deletionWait.until(ExpectedConditions.stalenessOf(taskElements.get(0)));

                taskElements = driver.findElements(By.cssSelector("ul.wrapper li.note"));
                tasksOnPageCount = taskElements.size();

                if (tasksOnPageCount == 0) {
                    tasksExist = false;
                }
            }

            assertEquals(0, tasksOnPageCount);
        }

        driver.quit();
    }


    /**
     * Done button Test //5
     */
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

    /**
     * Login parametrized Test // 6
     * @param username
     * @param password
     * @param expected
     */
    @ParameterizedTest(name = "Using username {0} and password {1} should give result {2}") //6
    @CsvSource({"false, false, false","rearre, rear, false","test1, test1password, true", "test2, test2password, true", "test3, test3password, true", "test4, test4password, true"})
    public void loginParametrizedTest(String username, String password, boolean expected) {
        driver.get(loginPage);
        driver.findElement(By.id("user")).sendKeys(username);
        driver.findElement(By.id("pass")).sendKeys(password);

        driver.findElement(By.id("button")).click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(5));

        if (expected) {
            sleep.until(ExpectedConditions.urlToBe(homePage));
        }


        // Get the current URL
        String current = driver.getCurrentUrl();

        // Perform the assertTrue if expected is true
        if (expected) {
            assertEquals(current, homePage);
        } else {
            assertEquals(current, loginPage);
        }
        driver.quit();
    }

    /**
     * Filter Test //7
     */
    @Test
    public void filterTest() {
        login();

        /**
         * Adds tasks without importance
         */
        WebElement titleInput = driver.findElement(By.id("title"));
        titleInput.sendKeys("Important Task");

        WebElement dateInput = driver.findElement(By.id("date"));
        dateInput.sendKeys("06-06-2026");

        WebElement importanceCheckbox = driver.findElement(By.id("noteImportance"));
        importanceCheckbox.click();

        WebElement descriptionTextarea = driver.findElement(By.id("text"));
        descriptionTextarea.sendKeys("Important Task Description");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("addNote")));

        // Submit the form
        submitButton.click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(5));
        sleep.until(ExpectedConditions.urlToBe(taskPage));

        WebElement home = driver.findElement(By.cssSelector("a[href='../pages/index.php']"));
        home.click();

        /**
         * Adds tasks without importance
         */
        WebElement title = driver.findElement(By.id("title"));
        title.sendKeys("Unimportant Task");

        WebElement date = driver.findElement(By.id("date"));
        date.sendKeys("12-12-2024");

        WebElement description = driver.findElement(By.id("text"));
        description.sendKeys("Unimportant Task Description");

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button = wait2.until(ExpectedConditions.elementToBeClickable(By.name("addNote")));

        // Submit the form
        button.click();

        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait3.until(ExpectedConditions.urlToBe(taskPage));

        /**
         * Select important tasks in filter
         */
        WebElement filter = driver.findElement(By.id("importanceFilter"));
        Select filterSelect = new Select(filter);
        filterSelect.selectByVisibleText("Důležité");
        List<WebElement> importantTasks = driver.findElements(By.cssSelector("ul.wrapper li.note"));
        int numberOfImportantTasks = importantTasks.size();
        assertTrue(numberOfImportantTasks > 0);

        /**
         * Select unimportant tasks in filter
         */
        WebElement filter2 = driver.findElement(By.id("importanceFilter"));
        Select filterSelect2 = new Select(filter2);
        filterSelect2.selectByVisibleText("Nedůležité");
        List<WebElement> unimportantTasks = driver.findElements(By.cssSelector("ul.wrapper li.note"));
        int numberOfUnimportantTasks = unimportantTasks.size();
        assertTrue(numberOfUnimportantTasks > 0);
        driver.quit();
    }

    /**
     * Dark / Light mode Test //8
     */
    @Test
    public void changeModeTest() {
        login();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement settings = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='../pages/settings.php']")));
        settings.click();

        WebElement applyButton = driver.findElement(By.cssSelector("button.modeButton"));
        String firstButtonColor = applyButton.getCssValue("background-color");

        WebElement modeSelect = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("select[name='theme-mode']")));
        Select select = new Select(modeSelect);

        WebElement selectedOptionElement = select.getFirstSelectedOption();
        String selectedOption = selectedOptionElement.getText();

        List<WebElement> options = select.getOptions();
        for (WebElement option : options) {
            if (!option.getText().equals(selectedOption)) {
                option.click();
                break;
            }
        }

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button = wait2.until(ExpectedConditions.elementToBeClickable(By.className("modeButton")));
        button.click();

        WebElement applyButton2 = driver.findElement(By.cssSelector("button.modeButton"));
        String secondButtonColor = applyButton2.getCssValue("background-color");

        assertNotEquals(firstButtonColor, secondButtonColor);

        driver.quit();
    }

    /**
     * Task suggestion Test //10
     */
    @Test
    public void taskNameSuggestionTest() {
        login();

        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "j", "l", "k", "m", "n", "o", "p", "r", "t", "u", "v", "z"};
        WebElement titleInput = driver.findElement(By.id("title"));

        Random random = new Random();
        int index = random.nextInt(letters.length);

        titleInput.sendKeys(letters[index]);


        // Wait for the suggestion to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement suggestion = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("suggestion")));
        assertTrue(suggestion.isDisplayed());
        driver.quit();
    }

    /**
     * Wrong date test //11
     */
    @Test
    public void wrongDateTest() {
        login();
        WebElement titleInput = driver.findElement(By.id("title"));
        titleInput.sendKeys("Task");

        // Put the date in the past
        WebElement dateInput = driver.findElement(By.id("date"));
        dateInput.sendKeys("12-12-2022");

        WebElement importanceCheckbox = driver.findElement(By.id("noteImportance"));
        importanceCheckbox.click();

        WebElement descriptionTextarea = driver.findElement(By.id("text"));
        descriptionTextarea.sendKeys("Task Description");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("addNote")));

        // Submit the form
        submitButton.click();

        // Checks if the error pops up
        WebElement errorMessage = driver.findElement(By.className("timeTravel"));
        assertTrue(errorMessage.isDisplayed());
        driver.quit();
    }

    /**
     * Parametrized name Test //12
     */
    @ParameterizedTest(name = "Using username {0} should say: Vítej {0}")
    @CsvSource({"test1, test1password", "test2, test2password", "test3, test3password", "test4, test4password"})
    public void helloParametrizedTest(String username, String password) {
        driver.get(loginPage);
        driver.findElement(By.id("user")).sendKeys(username);
        driver.findElement(By.id("pass")).sendKeys(password);

        driver.findElement(By.id("button")).click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(5));
        sleep.until(ExpectedConditions.urlToBe(homePage));

        WebElement hello = driver.findElement(By.cssSelector("nav label.welcomee"));
        String helloText = hello.getText();

        assertEquals("Vítej " + username, helloText);

        driver.quit();
    }

    /**
     * Parametrized task add test //13
     */
    @ParameterizedTest(name = "Creating note with title {0}, date {1}, {2} importance box and description {3}")
    @CsvSource({"Uklidit, 12-12-2024, false, yoyo","Tancovat, 11-11-2025, true, yaya", "Neco, 12-12-2025, true, neco"})
    public void addTaskParametrizedTest(String name, String date, boolean check, String text) {
        login();

        WebElement home = driver.findElement(By.cssSelector("a[href='../pages/index.php']"));
        home.click();

        WebElement titleInput = driver.findElement(By.id("title"));
        titleInput.sendKeys(name);

        WebElement dateInput = driver.findElement(By.id("date"));
        dateInput.sendKeys(date);

        if(check) {
            WebElement importanceCheckbox = driver.findElement(By.id("noteImportance"));
            importanceCheckbox.click();
        }

        WebElement descriptionTextarea = driver.findElement(By.id("text"));
        descriptionTextarea.sendKeys(text);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("addNote")));
        submitButton.click();
        driver.quit();

    }
    /**
     * Log Out Test //14
     */
    @Test
    public void logOutTest() {
        login();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(5));
        sleep.until(ExpectedConditions.urlToBe(homePage));

        WebElement logOut = driver.findElement(By.xpath("/html/body/nav/ul/li[4]/a"));
        logOut.click();

        WebDriverWait sleep1 = new WebDriverWait(driver, Duration.ofSeconds(5));
        sleep1.until(ExpectedConditions.urlToBe(loginPage));

        String current = driver.getCurrentUrl();
        assertEquals(current, loginPage);

        driver.quit();
    }



}
