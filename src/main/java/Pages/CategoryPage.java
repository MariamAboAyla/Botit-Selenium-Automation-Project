package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class CategoryPage {

    // the needed webElements
    @FindBy(className = "catalog-icon")
    private WebElement catalogButton;

    @FindBy(xpath = "//*[@id=\"tab2\"]/div[2]/div[1]/div[1]/a")
    private WebElement addNewCategoryButton;

    @FindBy(id = "add_name_en")
    private WebElement categoryNameFieldEN;

    @FindBy(id = "add_name_ar")
    private WebElement categoryNameFieldAR;

    @FindBy(xpath = "//*[@id=\"popup2-branch\"]/div/div[2]/input")
    private WebElement doneAddingButton;

    @FindBy(id = "itemContainerother")
    private WebElement categoryTable;

    @FindBy(id = "itemContainerother_next")
    private WebElement nextButton;

    private WebDriver driver;

    private WebDriverWait wait;

    public SoftAssert softAssert;


    public CategoryPage ( WebDriver driver ) {

        this.driver = driver;

        softAssert = new SoftAssert ( );
        wait = new WebDriverWait ( driver , 500 );

        //This initElements method will create all WebElements
        PageFactory.initElements ( driver , this );

        // wait
        waitFewSeconds ();

        // click on catalog button to open catalog page
        clickCatalogButton ( );

        // wait
        waitFewSeconds ();

    }

    // open "add new category" page
    public void addNewCategory ( String categoryEn , String categoryAr ) {


        // wait
        waitFewSeconds ( );

        // click the button
        clickAddNewCategoryButton ( );

        // write in the 2 text-fields
        writeInCategoryNameFieldEN ( categoryEn );
        writeInCategoryNameFieldAr ( categoryAr );

        // click on done
        clickDoneAddingCategory ( );

    }

    // Edit Category's data
    public boolean editCategory ( String oldCategoryName , String newEnName , String newArName ) {

        /// wait
        waitFewSeconds ( );

        // open catalog page
        clickCatalogButton ( );

        /// wait
        waitFewSeconds ( );

        // loop on categories table
        String rowElementName = null;

        // path of the table
        String tableXPath = "//*[@id='itemContainerother']/tbody/tr";

        while (true) {

            // get all the elements with the path => of the table's rows
            List<WebElement> rowsNumber = driver.findElements ( By.xpath ( tableXPath ) );

            int rowCount = rowsNumber.size ( );
            System.err.println ( rowCount );

            for (int i = 1; i <= rowCount; i++) {

                rowElementName = categoryTable.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[1]/div/div[2]/a/h2" ) ).getText ( );

                if ( rowElementName.toString ( ).equals ( oldCategoryName ) ) {

                    // press edit button
                    WebElement editButton = driver.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[3]/div[1]/div[1]/div[1]/a" ) );
                    editButton.click ( );

                    // wait
                    waitFewSeconds ( );

                    // edit with new data
                    // english name
                    WebElement categoryField = driver.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[3]/div[2]/div/form/div/div/ul/li[1]/input" ) );
                    categoryField.clear ( );
                    categoryField.sendKeys ( newEnName );

                    // arabic name
                    categoryField = driver.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[3]/div[2]/div/form/div/div/ul/li[2]/input" ) );
                    JavascriptExecutor jse = (JavascriptExecutor) driver;
                    jse.executeScript ( "arguments[0].setAttribute('value', '" + newArName + "')" , categoryField );

                    // done editing
                    WebElement doneButton = driver.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[3]/div[2]/div/form/div/input" ) );
                    doneButton.click ( );

                    // if found
                    return true;
                }

            }

            if ( getNextButton ( ).getAttribute ( "class" ) .equals ( "paginate_button next disabled" )) {
                return false;
            }

            // go to the next page
            clickNextButton ( );

            // wait few seconds
            waitFewSeconds ( );


        }

        // in case not found
//        return false;


    }

    // Delete a category
    public boolean deleteCategory ( String categoryEn ) {

        /// wait
        waitFewSeconds ( );

        // click on catalog button to open catalog page
        clickCatalogButton ( );

        /// wait
        waitFewSeconds ( );

        // loop on categories table
        String rowElementName = null;

        // path of the table
        String tableXPath = "//*[@id='itemContainerother']/tbody/tr";

        while (true) {

            // get all the elements with the path => of the table's rows
            List<WebElement> rowsNumber = driver.findElements ( By.xpath ( tableXPath ) );
            int rowCount = rowsNumber.size ( );

            System.err.println ( rowCount );

            for (int i = 1; i <= rowCount; i++) {

                rowElementName = categoryTable.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[1]/div/div[2]/a/h2" ) ).getText ( );


                if ( rowElementName.toString ( ).equals ( categoryEn ) ) {

                    // delete it and return
                    WebElement deleteButton = categoryTable.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[3]/div[1]/div[1]/div[2]/button" ) );
                    deleteButton.click ( );

                    // wait
                    waitFewSeconds ( );

                    WebElement confirmDelete = driver.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[3]/div[1]/div[2]/div/div[2]/div/div/input[1]" ) );
                    confirmDelete.click ( );

                    // wait
                    waitFewSeconds ( );

                    return true;

                }

            }

            if ( getNextButton ( ).getAttribute ( "class" ) .equals ( "paginate_button next disabled" )) {
                return false;
            }

            // go to the next page
            clickNextButton ( );

            // wait few seconds
            waitFewSeconds ( );

        }

//        return false;

    }


    // check the category is found in the table
    public boolean checkCategoryFound ( String categoryEn ) {

        // wait for few seconds
        waitFewSeconds ( );

        // open catalog page
        clickCatalogButton ( );


        /// wait
        waitFewSeconds ( );


        // loop on categories table
        String rowElementName = null;

        // path of the table
        String tableXPath = "//*[@id='itemContainerother']/tbody/tr";

        while ( true ) {


            // get all the elements with the path => of the table's rows
            List<WebElement> rowsNumber = driver.findElements ( By.xpath ( tableXPath ) );

            int rowCount = rowsNumber.size ( );
            System.err.println ( rowCount );

            for (int i = 1; i <= rowCount; i++) {

                rowElementName = categoryTable.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[1]/div/div[2]/a/h2" ) ).getText ( );

                if ( rowElementName.toString ( ).equals ( categoryEn ) ) {
                    // if found
                    return true;
                }

            }

            if ( getNextButton ( ).getAttribute ( "class" ) .equals ( "paginate_button next disabled" )) {
                return false;
            }

            // go to the next page
            clickNextButton ( );

            // wait few seconds
            waitFewSeconds ( );

        }

        // in case not found
//        return false;

    }


    //// Web-Elements accessors
    // click on catalog button to open catalog page
    public void clickCatalogButton ( ) {
        catalogButton.click ( );
    }

    // click on next button
    public void clickNextButton ( ) {
        nextButton.click ( );
    }

    // click on "add new category" button to open catalog page
    public void clickAddNewCategoryButton ( ) {
        addNewCategoryButton.click ( );
    }

    // write the english-name of category
    public void writeInCategoryNameFieldEN ( String categoryEn ) {
        categoryNameFieldEN.sendKeys ( categoryEn );
    }

    // write the arabic-name of category
    public void writeInCategoryNameFieldAr ( String categoryAr ) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript ( "arguments[0].setAttribute('value', '" + categoryAr + "')" , categoryNameFieldAR );
    }


    // click on doneAddingCategory button
    public void clickDoneAddingCategory ( ) {
        doneAddingButton.click ( );
    }

    // returns the driver
    public WebDriver getDriver ( ) {
        return driver;
    }

    // return next button
    public WebElement getNextButton ( ) {
        return nextButton;
    }

    // function wait
    public void waitFewSeconds ( ) {
        try {
            Thread.sleep ( 7000 );
        } catch (InterruptedException e) {
            e.printStackTrace ( );
        }
    }


}
