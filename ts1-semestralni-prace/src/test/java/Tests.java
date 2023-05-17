import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

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
        username = "thegoat";
        password = "allidoiswin123";
        email = "thegoat@gmail.com";

        driver.get(registerPage);
        driver.findElement(By.id("user")).sendKeys(username);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("pass")).sendKeys(password);
        driver.findElement(By.id("confirmpass")).sendKeys(password);

        driver.findElement(By.id("buttonRegister")).click();


    }

    @Test
    public void loginToPageTest() {
        driver.get(loginPage);
        driver.findElement(By.id("user")).sendKeys("admin");
        driver.findElement(By.id("pass")).sendKeys("admin");

        driver.findElement(By.id("button")).click();
    }


}
