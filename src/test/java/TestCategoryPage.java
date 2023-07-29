import Pages.BasePage;
import Pages.CategoryPage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestCategoryPage {

    WebDriver driver;
    BasePage basePage;
    CategoryPage categoryPage;

    String HomePage = "http://transmission-dev.azurewebsites.net/home" ;
    String LoginPage = "http://transmission-dev.azurewebsites.net/login";

    @BeforeClass
    public void setUp() {

        driver = new ChromeDriver (  );
        driver.get( LoginPage );

    }

    @Test (priority = 1, dataProvider = "LoginData.json", dataProviderClass = TestBasePage.class)
    public void TestLogin (String email, String password) {

        basePage = new BasePage ( driver );
        basePage.login ( email, password );

        // assert successful login
        basePage.softAssert.assertTrue ( basePage.getDriver ().getCurrentUrl ().equals ( HomePage ) );

    }

    @Test (priority = 5, dataProvider = "CategoryData.json")
    public void TestAddCategory( String categoryEn, String categoryAr ) {

        // add the new category
        categoryPage = new CategoryPage ( driver);

        categoryPage.addNewCategory ( categoryEn, categoryAr );

        // check the new category is added
        boolean addedSuccessfully = categoryPage.checkCategoryFound( categoryEn );

        // assert the result
        categoryPage.softAssert.assertTrue ( addedSuccessfully );

    }

    // class that provides the category data
    @DataProvider(name = "CategoryData.json")
    public Object[][] getCategoryData() {

        FileReader reader = null;
        try {
            reader = new FileReader ("D:\\Fawry-testing internship\\automation-testing-project\\src\\test\\resources\\CategoryData.json");
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

        Object[][] categoryData = new Object[1][2];

        if(jsonObject == null) {
            System.out.println ("CategoryData Json file is empty!" );
            return null;
        }

        // loginData - Read JSON file
        categoryData[0][0] = jsonObject.get("CategoryName");
        categoryData[0][1] = jsonObject.get("LocalName");

        return categoryData;


    }


    @Test (priority = 8)
    public void TestEditCategory() {
        // add the new category
        categoryPage = new CategoryPage ( driver );

        // the category's new data & old english name
        String oldName = "nwCategory";
        String newEnName = "Backpack";
        String newArName = "حقيبة الظهر";

        // edit the category's data
        categoryPage.editCategory(oldName, newEnName, newArName);

        // check if correctly edited
        boolean foundCategory = categoryPage.checkCategoryFound(newEnName);

        categoryPage.softAssert.assertEquals ( true, foundCategory );

    }

    @Test (priority = 7)
    public void TestDeleteCategory () {

        // add the new category
        categoryPage = new CategoryPage ( driver );

        // Determine the category to be deleted
        String CategoryNameEn  = "newCategory";
        categoryPage.deleteCategory ( CategoryNameEn );

        // check the category is deleted successfully
        boolean deletionSuccessfully = categoryPage.checkCategoryFound ( CategoryNameEn);

        categoryPage.softAssert.assertEquals ( false, deletionSuccessfully );
        System.err.println (CategoryNameEn + " -category is deleted successfully!" );

    }


    @AfterClass
    public void terminate() {
        categoryPage.getDriver().quit ();
        basePage.getDriver ().quit ();
        driver.quit ();
    }

}
