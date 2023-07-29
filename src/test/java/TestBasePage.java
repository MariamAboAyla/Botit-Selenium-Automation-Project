import Pages.BasePage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class TestBasePage {

    WebDriver driver;
    BasePage basePage;

    String HomePage = "http://transmission-dev.azurewebsites.net/home" ;
    String LoginPage = "http://transmission-dev.azurewebsites.net/login";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver (  );
        driver.get( LoginPage );
    }

    @Test (priority =  0, dataProvider = "LoginData.json")
    public void testLogin(String email, String password) {
        basePage = new BasePage ( driver );

        basePage.login ( email, password);

        basePage.softAssert.assertTrue ( Objects.equals ( basePage.getDriver ( ).getCurrentUrl ( ) , HomePage ) );

    }

    // class that provides the login data
    @DataProvider(name = "LoginData.json")
    public Object[][] getLoginData() {

        FileReader reader = null;
        try {
            reader = new FileReader ("D:\\Fawry-testing internship\\automation-testing-project\\src\\test\\resources\\LoginData.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace ( );
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace ( );
        }

        Object[][] loginData = new Object[1][2];

        if(jsonObject == null) {
            System.out.println ("LoginData Json file is empty!" );
            return null;
        }

        // loginData - Read JSON file
        loginData[0][0] = jsonObject.get("email");
        loginData[0][1] = jsonObject.get("password");

        return loginData;


    }

    @AfterClass
    public void terminate() {
        driver.quit ();
    }


}
