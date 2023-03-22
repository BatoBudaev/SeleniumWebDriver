package demoqa.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.time.Duration;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DemoQaTest {
    private WebDriver driver;

    @BeforeAll
    public void setup() {
        String browser = System.getProperty("browser");

        if (browser.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browser.equals("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } else {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://demoqa.com/automation-practice-form/");
        driver.manage().window().maximize();
    }

    @Test
    public void checkTitleTest() {
        Assertions.assertEquals("The Internet", driver.getTitle());
    }

    @Test
    public void checkFormTest() throws InterruptedException {
        String firstName = "Ivan";
        String lastName = "Petrov";
        String userEmail = "IvanPetrov@mail.ru";
        String userNumber = "8800555556";
        String month = "November";
        String year = "1991";
        String address = "Novosibirsk";
        String state = "Haryana";
        String city = "Karnal";

        driver.findElement(By.cssSelector("#firstName")).sendKeys(firstName);
        driver.findElement(By.cssSelector("#lastName")).sendKeys(lastName);
        driver.findElement(By.cssSelector("#userEmail")).sendKeys("IvanPetrov@mail.ru");
        driver.findElement(By.xpath("//label[@for='gender-radio-1']")).click();
        driver.findElement(By.cssSelector("#userNumber")).sendKeys(userNumber);
        Thread.sleep(1000);

        WebElement dateElement = driver.findElement(By.cssSelector("#dateOfBirthInput"));
        dateElement.click();
        WebElement monthElement = driver.findElement(By.cssSelector(".react-datepicker__month-select"));
        Select monthSelect = new Select(monthElement);
        monthSelect.selectByVisibleText(month);
        WebElement yearElement = driver.findElement(By.cssSelector(".react-datepicker__year-select"));
        Select yearSelect = new Select(yearElement);
        yearSelect.selectByVisibleText(year);
        driver.findElement(By.xpath("//div[@aria-label='Choose Wednesday, November 13th, 1991']")).click();
        String dateText = driver.findElement(By.cssSelector("#dateOfBirthInput")).getAttribute("value");
        String newDateText = dateText.substring(0, 3) + month + "," + year;
        Thread.sleep(1000);

        WebElement subjectsElement = driver.findElement(By.cssSelector("#subjectsInput"));
        subjectsElement.sendKeys("a");
        WebElement subject = driver.findElement(By.cssSelector("#react-select-2-option-0"));
        String subjectText = subject.getText();
        subject.click();
        driver.findElement(By.xpath("//label[@for='hobbies-checkbox-2']")).click();
        Thread.sleep(1000);

        File file = new File("Tom.jpg");
        WebElement chooseFileButton = driver.findElement(By.cssSelector("#uploadPicture"));
        chooseFileButton.sendKeys(file.getAbsolutePath());
        Thread.sleep(1000);

        driver.findElement(By.cssSelector("#currentAddress")).sendKeys(address);
        driver.findElement(By.cssSelector("#react-select-3-input")).sendKeys(state);
        driver.findElement(By.cssSelector("#react-select-3-input")).sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector("#react-select-4-input")).sendKeys(city);
        driver.findElement(By.cssSelector("#react-select-4-input")).sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector("#submit")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("table-responsive")));
        List<WebElement> tableValues = driver.findElements(By.cssSelector("tbody tr td:last-child"));

        for (WebElement e : tableValues) {
            System.out.println(e.getText());
        }

        Assert.assertEquals(tableValues.get(0).getText(), firstName + " " + lastName);
        Assert.assertEquals(tableValues.get(1).getText(), userEmail);
        Assert.assertEquals(tableValues.get(2).getText(), driver.findElement(By.cssSelector("#gender-radio-1")).getAttribute("value"));
        Assert.assertEquals(tableValues.get(3).getText(), userNumber);
        Assert.assertEquals(tableValues.get(4).getText(), newDateText);
        Assert.assertEquals(tableValues.get(5).getText(), subjectText);
        Assert.assertEquals(tableValues.get(6).getText(), driver.findElement(By.xpath("//label[@for='hobbies-checkbox-2']")).getText());
        Assert.assertEquals(tableValues.get(7).getText(), file.getName());
        Assert.assertEquals(tableValues.get(8).getText(), address);
        Assert.assertEquals(tableValues.get(9).getText(), state + " " + city);

        Thread.sleep(1000);
    }

    @AfterAll
    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
}
