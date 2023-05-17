import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

    private final String loginPage = "https://wa.toad.cz/~leductha/zwa/view/pages/login.php";
    private final String registerPage = "https://wa.toad.cz/~leductha/zwa/view/pages/register.php";

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
        username = "theoattttt";
        password = "allidoiswin123";
        email = "thegottttt@gmail.com";

        driver.get(registerPage);
        driver.findElement(By.id("user")).sendKeys(username);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("pass")).sendKeys(password);
        driver.findElement(By.id("confirmpass")).sendKeys(password);

        driver.findElement(By.id("buttonRegister")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(loginPage));

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
    }

    


}
