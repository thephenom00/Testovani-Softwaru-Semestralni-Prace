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

    final String loginPage = "https://wa.toad.cz/~leductha/zwa/view/pages/login.php";
    final String registerPage = "https://wa.toad.cz/~leductha/zwa/view/pages/register.php";
    final String homePage = "https://wa.toad.cz/~leductha/zwa/view/pages/index.php";
    final String taskPage = "https://wa.toad.cz/~leductha/zwa/view/pages/list-notes.php";

    String username;
    String password;
    String email;
    WebDriver driver;

    @BeforeEach
    public void setup() {
        // Set the path to the chromedriver
        System.setProperty("webdriver.chrome.driver", "/Users/goat/ts1/ts1-semestralni-prace/src/test/java/chromedriver");

        // Set the chrome to fullscreen
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");

        // Create the WebDriver
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

        // All tasks on a page
        List<WebElement> tasksOnPage = driver.findElements(By.cssSelector("ul.wrapper li.note"));

        // If there are tasks on page
        if (tasksOnPage.size() > 0) {
            WebElement paginationForm = driver.findElement(By.id("paginationForm"));

            Select pageSelect = new Select(paginationForm.findElement(By.id("page_num")));
            List<WebElement> pageOptions = pageSelect.getOptions();
            int numberOfPages = pageOptions.size();

            if (numberOfPages > 1) {
                // Gets to the last page
                pageSelect.selectByValue(Integer.toString(numberOfPages));

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.urlToBe(taskPage + "?importanceFilter=All&page_num=" + numberOfPages));

                List<WebElement> tasksOnTheLastPage = driver.findElements(By.cssSelector("ul.wrapper li.note"));
                numberOfAllTasks += 8 * (numberOfPages - 1) + tasksOnTheLastPage.size();
            } else {
                numberOfAllTasks += tasksOnPage.size();
            }
        }

        WebElement home = driver.findElement(By.cssSelector("a[href='../pages/index.php']"));
        home.click();

        return numberOfAllTasks;
    }

    /**
     * Testing the registration form
     */
    @Test
    public void registerTest() {
        username = "BeautifulUsername9999";
        password = "password12345";
        email = "beautifulemail35442@email.com";

        driver.get(registerPage);
        driver.findElement(By.id("user")).sendKeys(username);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("pass")).sendKeys(password);
        driver.findElement(By.id("confirmpass")).sendKeys(password);
        driver.findElement(By.id("buttonRegister")).click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(10));
        sleep.until(ExpectedConditions.urlToBe(loginPage));

        assertEquals(loginPage, driver.getCurrentUrl());

        driver.quit();
    }

    /**
     * Testing if the error message appears
     */
    @Test
    public void loginErrorTest() {
        driver.get(loginPage);
        driver.findElement(By.id("user")).sendKeys("wrongName");
        driver.findElement(By.id("pass")).sendKeys("wrongPassword");
        driver.findElement(By.id("button")).click();

        // Wait for the error message
        WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorMessage = errorWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.loginError")));

        String errorText = errorMessage.getText();

        assertEquals("Přihlašovací údaje nebyly zadány správně", errorText);

        driver.quit();
    }

    /**
     * Adding multiple tasks Test
     */
    @Test
    public void addMultipleTasksTest() {
        int numberOfAllTasksOnPage = countAllTasks();
        int numberOfTasksWantToAdd = 300;
        int expectedNumberOfTasks = numberOfAllTasksOnPage + numberOfTasksWantToAdd;

        // Adds all the tasks
        for (int i = 1; i <= numberOfTasksWantToAdd; i++) {
            // Enter task title and description
            WebElement titleInput = driver.findElement(By.id("title"));
            titleInput.sendKeys("Task #" + i);

            WebElement dateInput = driver.findElement(By.id("date"));
            dateInput.sendKeys("12-12-2024");

            WebElement descriptionTextarea = driver.findElement(By.id("text"));
            descriptionTextarea.sendKeys("Task Description #" + i);

            WebElement submitButton = driver.findElement(By.name("addNote"));

            // In case of lag
            while (!driver.getCurrentUrl().equals(taskPage)) {
                submitButton.click();
            }

            WebElement home = driver.findElement(By.cssSelector("a[href='../pages/index.php']"));
            home.click();
        }

        int numberOfAllTasksUpdated = countAllTasks();

        assertEquals(expectedNumberOfTasks, numberOfAllTasksUpdated);
        driver.quit();
    }

    /**
     * Removing all tasks Test
     */
    @Test
    public void removeAllTasksTest() {
        login();

        driver.findElement(By.cssSelector("a[href='../pages/list-notes.php']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(taskPage));

        List<WebElement> taskElements = driver.findElements(By.cssSelector("ul.wrapper li.note"));

        // Deletes while there is any task
        while (!taskElements.isEmpty()) {
            WebElement deleteButton = driver.findElement(By.cssSelector("button[name='deleteButton']"));
            deleteButton.click();

            wait.until(ExpectedConditions.invisibilityOf(deleteButton));

            taskElements = driver.findElements(By.cssSelector("ul.wrapper li.note"));
        }

        int tasksOnPageCount = taskElements.size();
        assertEquals(0, tasksOnPageCount);

        driver.quit();
    }



    /**
     * Done button Test
     */
    @Test
    public void doneButtonTest() {
        login();

        // Gets to the task page
        WebElement taskLink = driver.findElement(By.cssSelector("a[href='../pages/list-notes.php']"));
        taskLink.click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(3));
        sleep.until(ExpectedConditions.urlToBe(taskPage));

        List<WebElement> taskElements = driver.findElements(By.cssSelector("ul.wrapper li.note"));
        int tasksOnPageCount = taskElements.size();

        // Mark task as done if there is any
        if (tasksOnPageCount > 0) {
            WebElement doneButton = driver.findElement(By.cssSelector("button[name='doneButton']"));
            doneButton.click();

            WebDriverWait doneWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            doneWait.until(ExpectedConditions.attributeContains(By.cssSelector("ul.wrapper li.note"), "class", "done"));

            // Verify that the done is added to the note element
            WebElement note = driver.findElement(By.cssSelector("ul.wrapper li.note"));
            String noteClass = note.getAttribute("class");
            assertEquals("note done", noteClass);
        }

        driver.quit();
    }

    /**
     * Filter Test
     */
    @Test
    public void filterTest() {
        login();

        // Adds an important task
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

        // Adds task without an importance
        WebElement title = driver.findElement(By.id("title"));
        title.sendKeys("Unimportant Task");

        WebElement date = driver.findElement(By.id("date"));
        date.sendKeys("12-12-2024");

        WebElement description = driver.findElement(By.id("text"));
        description.sendKeys("Unimportant Task Description");

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button = wait2.until(ExpectedConditions.elementToBeClickable(By.name("addNote")));

        button.click();

        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait3.until(ExpectedConditions.urlToBe(taskPage));

        // Selects the important tasks in filter
        WebElement importantFilter = driver.findElement(By.id("importanceFilter"));
        Select importantFilterSelect = new Select(importantFilter);
        importantFilterSelect.selectByVisibleText("Důležité");
        List<WebElement> importantTasks = driver.findElements(By.cssSelector("ul.wrapper li.note"));
        int numberOfImportantTasks = importantTasks.size();
        assertTrue(numberOfImportantTasks > 0);

        // Selects unimportant tasks in filter
        WebElement unimportantFilter = driver.findElement(By.id("importanceFilter"));
        Select unimportantFilterSelect = new Select(unimportantFilter);
        unimportantFilterSelect.selectByVisibleText("Nedůležité");
        List<WebElement> unimportantTasks = driver.findElements(By.cssSelector("ul.wrapper li.note"));
        int numberOfUnimportantTasks = unimportantTasks.size();
        assertTrue(numberOfUnimportantTasks > 0);

        driver.quit();
    }

    /**
     * Dark / Light mode Test
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
        if (options.get(0).getText().equals(selectedOption)) {
            options.get(1).click();
        } else {
            options.get(0).click();
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
     * Task suggestion Test
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
     * Login parametrized Test
     * @param username
     * @param password
     * @param expected
     */
    @ParameterizedTest(name = "Using username {0} and password {1} should give result {2}")
    @CsvSource({"false, false, false", "batman555, password, false","test1, test1password, true", "test2, test2password, true", "test3, test3password, true", "test4, test4password, true"})
    public void loginParametrizedTest(String username, String password, boolean expected) {
        driver.get(loginPage);
        driver.findElement(By.id("user")).sendKeys(username);
        driver.findElement(By.id("pass")).sendKeys(password);

        driver.findElement(By.id("button")).click();

        WebDriverWait sleep = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Perform the assertTrue if expected is true
        if (expected) {
            sleep.until(ExpectedConditions.urlToBe(homePage));
            assertEquals(driver.getCurrentUrl(), homePage);
        } else {
            assertEquals(driver.getCurrentUrl(), loginPage);
        }

        driver.quit();
    }

    /**
     * Parametrized test
     *
     * adding task with wrong and correct date
     */
    @ParameterizedTest(name = "Using title {0}, description {1} and date {2} should give result {3}")
    @CsvSource({"correct, correct, 12-12-2025, true", "wrong date, blabla, 12-12-2012, false", "wrong date 2, zzz, 11-11-2011, false", "wrong date 3, hello, 06-06-2006, false", "Go to the gym, gym gym, 04-03-2024, true", "sleep, sleep, 12-04-2024,true",})
    public void dateParametrizedTest(String title, String description, String date, boolean bool) {
        login();
        WebElement titleInput = driver.findElement(By.id("title"));

        titleInput.sendKeys(title);

        WebElement dateInput = driver.findElement(By.id("date"));
        dateInput.sendKeys(date);

        WebElement importanceCheckbox = driver.findElement(By.id("noteImportance"));
        importanceCheckbox.click();

        WebElement descriptionTextarea = driver.findElement(By.id("text"));
        descriptionTextarea.sendKeys(description);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("addNote")));

        submitButton.click();

        if (bool) {
            assertTrue(driver.getCurrentUrl().equals(taskPage));
        } else {
            // Checks if the error pops up
            WebElement errorMessage = driver.findElement(By.className("timeTravel"));
            assertTrue(errorMessage.isDisplayed());
        }

        driver.quit();

    }

    /**
     * Hello parametrized name test
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
     * Log Out Test
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

        String currentPage = driver.getCurrentUrl();
        assertEquals(currentPage, loginPage);

        driver.quit();
    }



}
