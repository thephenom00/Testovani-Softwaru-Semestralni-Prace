//public class ahoj {
//    @Test
//    public void deleteTaskTest() {
//        //WORKS IF THE ONLY ONE TASK ON THE PAGE
//        // Navigate to the website
//        driver.get("http://wa.toad.cz/~aivazart/web/hlavni/hlavni.php");
//
//        // Find and enter the username
//        driver.findElement(By.name("username")).sendKeys("aivazart");
//
//        // Find and enter the password
//        driver.findElement(By.name("password")).sendKeys("12345678");
//
//        // Click on the login button
//
//        driver.findElement(By.name("submit")).click();
//        // Wait for the page to load
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
//        wait.until(ExpectedConditions.urlToBe("http://wa.toad.cz/~aivazart/web/hlavni/hlavni.php"));
////        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
//        // Find and click on the delete button for the first task
////        WebElement element = driver.findElement(By.xpath("//div[@id='tasks']"));
//        //div[@id='tasks']/div[1]/div[2]/a[2]
//        driver.findElement(By.xpath("//div[@id='tasks']/div/div[2]/a[2]")).click();
//
//
//        // Wait for the page to reload
//        wait = new WebDriverWait(driver, Duration.ofSeconds(4));
//        wait.until(ExpectedConditions.urlToBe("http://wa.toad.cz/~aivazart/web/hlavni/hlavni.php"));
//
//
//        // Verify that the task has been deleted
//        boolean taskExists = driver.findElements(By.xpath("//div[@id='tasks']/div/div[2]/a[2]")).size() > 0;
//
//        assertEquals(false, taskExists, "Task deletion test failed!");
//
//        // Close the browser
//        driver.quit();
//        //*[@id="tasks"]/div[1]/div[2]/a[2]
//        //*[@id="tasks"]/div[2]/div[2]/a[2]
//    }
//
//}
