package Pages;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

public class BasePage {

    @FindBy(name="email")
    private WebElement emailInput;

    @FindBy(name="password")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@class='button-input newd']")
    private WebElement loginButton;

    private WebDriverWait wait;

    public SoftAssert softAssert;

    private WebDriver driver;

    public String homePage;

    public String loginPage;


    public BasePage (WebDriver driver) {
        this.driver = driver;

        WebDriverManager.chromedriver().setup();
        // Maximize browser
        driver.manage().window().maximize();

        softAssert = new SoftAssert ();
        wait = new WebDriverWait ( driver, 150 );

        // determining the login and homepage
        homePage = "http://transmission-dev.azurewebsites.net/home" ;
        loginPage = "http://transmission-dev.azurewebsites.net/login";

        driver.get ( loginPage );

        //This initElements method will create all WebElements
        PageFactory.initElements(driver, this);

    }


    // login to the website
    public void login (String email, String password) {

        setEmailInput(email);
        setPasswordInput(password);
        clickLogin();

    }


    // setter of the email input
    public void setEmailInput ( String email ) {
        emailInput.sendKeys ( email );
    }

    // getter of the email input
    public String getEmailInput ( ) {
        return emailInput.getText ();
    }

    // setter of the password input
    public void setPasswordInput ( String password ) {
        passwordInput.sendKeys ( password );
    }

    // getter of the password input
    public String getPasswordInput ( ) {
        return passwordInput.getText ();
    }

    // click on the login button
    public void clickLogin() {
        loginButton.click ();
    }

    // return the driver
    public WebDriver getDriver ( ) {
        return driver;
    }


}
