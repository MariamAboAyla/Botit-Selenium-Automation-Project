package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

public class ItemsPage {

    @FindBy(className = "catalog-icon")
    private WebElement catalogButton;

    @FindBy(id = "tab-2")
    private WebElement itemsButton;

    @FindBy(className = "add-new-item")
    private WebElement addNewItemButton;

    @FindBy(className = "exit-button")
    private WebElement saveAndExitButton;

    @FindBy(className = "add-more")
    private WebElement saveAndMoreButton;

    @FindBy(name = "en_name")
    private WebElement itemNameEnField;

    @FindBy(name = "ar_name")
    private WebElement itemNameArField;

    @FindBy(id = "discountPriceItemId")
    private WebElement discountField;

    @FindBy(name = "en_desc")
    private WebElement descriptionEnField;

    @FindBy(xpath = "//*[@id=\"up_image2\"]/div[1]/ul/li[6]/textarea")
    private WebElement descriptionArField;

    @FindBy(xpath = "//*[@id=\"up_image2\"]/div[1]/ul/li[7]/div[1]/div[2]/label[1]")
    private WebElement yesOneSizeButton;

    @FindBy(id = "No")
    private WebElement noOneSizeButton;

    @FindBy(xpath = "//*[@id=\"up_image2\"]/div[4]/input")
    private WebElement priceField;

    @FindBy(id = "Yessss")
    private WebElement yesExtendPrepTimeButton;

    @FindBy(id = "Nossss")
    private WebElement noExtendPrepTimeButton;

    @FindBy(id = "selectDiscountActiveAndNot")
    private WebElement activeDiscount;

    @FindBy(name = "prepTimeMins")
    private WebElement preparationTime;

    @FindBy(className = "up-image2")
    private WebElement imgButton;

    @FindBy(id = "Grid_next")
    private WebElement nextPageButton;

    @FindBy(id = "Grid")
    private WebElement itemsTable;

    @FindBy(id = "mainCategory_select")
    private WebElement selectCategoryFromDownList;

    @FindBy(xpath = "//*[@id=\"file-upload\"]")
    private WebElement imgUploadButton;

    private WebDriver driver;

    private WebDriverWait wait;

    public SoftAssert softAssert;

    public ItemsPage ( WebDriver driver ) throws InterruptedException {

        this.driver = driver;

        softAssert = new SoftAssert ( );
        wait = new WebDriverWait ( driver , 500 );

        //This initElements method will create all WebElements
        PageFactory.initElements ( driver , this );

        // wait
        waitFewSeconds ( );

        // click on catalog button to open catalog page
        clickCatalogButton ( );

        // wait
        waitFewSeconds ( );

        // click on items button => open Items page
        clickItemsButton ( );

        // wait few seconds
        waitFewSeconds ( );

    }


    // add new item
    public void addNewItem( String itemNameEn, String itemNameAr, boolean hasDiscount, String discount, String category
            , String imgPath, String descriptionEn, String descriptionAr, boolean hasVariation, Map<String, String> variations, String price
            ,boolean extendPrepTime,  String extendedPrepTime  ){

        // wait few seconds
        waitFewSeconds ( );

        // click on (add new item)button
        clickAddNewItemButton ();

        // wait few seconds
        waitFewSeconds ( );


        // needed accessors
        /// select from the status drop-down list=> by value / index / visible text
        Select activeDiscountList = new Select ( activeDiscount );

        /// select from the category's drop-down list=>by value / index / visible text
        Select itemCategory = new Select ( selectCategoryFromDownList );


        // Add the item's information
        setItemNameEnField ( itemNameEn ); // item Name English
        setItemNameArField ( itemNameAr ); // item Name Arabic

        // discount
        if(hasDiscount) {
            // choose active and write the discount amount
            activeDiscountList.selectByVisibleText ( "Active" );
            discountField.sendKeys ( discount );

        }else {
            // choose (not active)
            activeDiscountList.selectByVisibleText ( "Not Active" );
        }

        // wait
        waitFewSeconds ();

        // add the item's category
        itemCategory.selectByVisibleText ( category );

        // wait
        waitFewSeconds ();

        // upload image
        itemImgUpload().sendKeys( imgPath );

        // wait
        waitFewSeconds ();

        // add the item's description english & arabic
        setDescriptionEnField ( descriptionEn ); // add english description

        // wait
        waitFewSeconds ();

        // todo => if un-necessary remove
        setDescriptionArField ( descriptionAr ); // add arabic description

        waitFewSeconds ();

        // check if the item is one-size or has variations
        if(hasVariation) {
            // todo => add the variation map && press the button of => no

            clickNoOneSizeButton (); // click no to add the variations

        } else {

            // click yes one size & add its price
            clickYesOneSizeButton ();
            setPriceInput ( price );

        }

        // check if there is preparation time
        if (extendPrepTime) {

            // extend preparation time
            clickYesExtendPrepTimeButton ();

            /// select preparation time
            Select preparationTimeList = new Select ( preparationTime );
            preparationTimeList.selectByValue ( extendedPrepTime );

        }else {

            // click no-extended preparation time
            clickNoExtendPrepTimeButton ();

        }

        // wait
        waitFewSeconds ();

        // save and return to catalog page
        clickSaveAndExitButton ();

        // wait few seconds
        waitFewSeconds ();

    }


    // edit item with variation => todo


    // edit item's price => one size (no variation)
    public boolean editItemPriceWithoutVar ( String itemName , String price ) {

        // open items page
        clickItemsButton ( );

        // wait few seconds
        waitFewSeconds ( );

        // loop on the pages
        while (true) {

            // loop on the items in this page
            String rowElementName = null;

            // path of the table
            String tableXPath = "//*[@id=\"Grid\"]/tbody/tr";

            // get all the elements with the path => of the table's rows
            List<WebElement> rowsNumber = driver.findElements ( By.xpath ( tableXPath ) );

            int rowCount = rowsNumber.size ( );
            System.err.println ( rowCount );

            for (int i = 1; i <= rowCount; i++) {

                rowElementName = itemsTable.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[1]/div/div[2]/h2" ) ).getText ( );

                if ( rowElementName.toString ( ).equals ( itemName ) ) {

                    // click on its edit button
                    WebElement editButton = driver.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[7]/div/a" ) );
                    editButton.click ( );

                    // wait for few seconds
                    waitFewSeconds ( );

                    // change the price
                    WebElement priceField = driver.findElement ( By.xpath ( "/html/body/div[1]/div/div/div[2]/div[5]/div/form/div[4]/input" ) );
                    priceField.clear ( );
                    priceField.sendKeys ( price );


                    //save & exit button
                    WebElement saveButton = driver.findElement ( By.xpath ( "/html/body/div[1]/div/div/div[2]/div[5]/div/form/div[5]/div[5]/ul/li[2]/input" ) );
                    saveButton.click ( );

                    // if found
                    return true;
                }

            }

            // check if this is the last page => break;
            if ( getNextPageButton ( ).getAttribute ( "class" ).equals ( "paginate_button next disabled" ) ) {
                break;
            }

            // if not last page => click to go to the next page
            clickNextPageButton ( );

            // wait few seconds
            waitFewSeconds ( );

        }

        // return that the item wasn't found
        System.out.println ( "The item " + itemName + ", was not found!" );
        return false;

    }

    // check the item with its price
    public boolean checkItemPriceNoVar ( String itemName , String price ) {

        // wait
        waitFewSeconds ( );

        // open items page
        clickItemsButton ( );

        // wait
        waitFewSeconds ( );

        // loop on all pages searching for the item
        while (true) {

            // loop on the table of items
            // loop on the items in this page
            String rowElementName = null;

            // path of the table
            String tableXPath = "//*[@id=\"Grid\"]/tbody/tr";

            // get all the elements with the path => of the table's rows
            List<WebElement> rowsNumber = driver.findElements ( By.xpath ( tableXPath ) );

            int rowCount = rowsNumber.size ( );
            System.err.println ( rowCount );

            for (int i = 1; i <= rowCount; i++) {

                rowElementName = itemsTable.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[1]/div/div[2]/h2" ) ).getText ( );

                if ( rowElementName.toString ( ).equals ( itemName ) ) {

                    // the price field of this item
                    WebElement curPriceField = itemsTable.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[5]/div/h2" ) );

                    // get the price and check
                    if ( curPriceField.getText ( ).equals ( price ) ) {

                        // item found but different price
                        System.err.println ( "The item : " + itemName + ", is found but different price!" );
                        return false;

                    } else {

                        // found with the name and price
                        return true;

                    }

                }

            }

            // check if this is the last page => break;
            if ( getNextPageButton ( ).getAttribute ( "class" ).equals ( "paginate_button next disabled" ) ) {
                break;
            }

            // if not last page => click to go to the next page
            clickNextPageButton ( );

            // wait few seconds
            waitFewSeconds ( );

        }

        // return that the item wasn't found
        System.out.println ( "The item: " + itemName + ", was not found!" );
        return false;

    }


    // check the item's price with variations
    public boolean checkItemPriceWithVar ( String itemName , String variation , String price ) {

        // wait
        waitFewSeconds ( );

        // open items page
        clickItemsButton ( );

        // wait
        waitFewSeconds ( );

        // loop on all pages searching for the item
        String rowElementName2 = null;

        // loop on the table of items
        // loop on the items in this page
        String rowElementName = null;


        // loop on all pages searching for the item
        while (true) {


            // path of the table
            String tableXPath = "//*[@id=\"Grid\"]/tbody/tr";

            // get all the elements with the path => of the table's rows
            List<WebElement> rowsNumber = driver.findElements ( By.xpath ( tableXPath ) );

            int rowCount = rowsNumber.size ( );
            System.err.println ( rowCount );

            for (int j = 1; j <= rowCount; j++) {

                rowElementName = itemsTable.findElement ( By.xpath ( tableXPath + "[" + j + "]/td[1]/div/div[2]/h2" ) ).getText ( );

                if ( rowElementName.toString ( ).equals ( itemName ) ) {

                    // click on edit button to check its data
                    WebElement editButton = driver.findElement ( By.xpath ( tableXPath + "[" + j + "]/td[7]/div/a" ) );
                    editButton.click ( );

                    // wait for few seconds
                    waitFewSeconds ( );

                    // path of the table
                    String tableXPath2 = "//*[@id=\"up_image2\"]/div[3]/div[3]/div";

                    // get all the elements with the path => of the table's rows
                    List<WebElement> rowsNumber2 = driver.findElements ( By.xpath ( tableXPath2 ) );

                    int rowCount2 = rowsNumber2.size ( );
                    System.err.println ( rowCount2 );

                    for (int i = 1; i <= rowCount2; i++) {

                        WebElement curItem = driver.findElement ( By.xpath ( tableXPath2 + "[" + i + "]/ul/li[1]/h2" ) );
                        rowElementName = curItem.getText ( );

                        // if that variation is found => check its price
                        if ( rowElementName.toString ( ).equals ( variation ) ) {

                            // the price field of this item
                            WebElement curPriceField = driver.findElement ( By.xpath ( tableXPath2 + "[" + i + "]/ul/li[2]/div/input" ) );

                            // get the price and check
                            if ( ! curPriceField.getText ( ).equals ( price ) ) {

                                // item found but different price
                                System.err.println ( "The item : " + itemName + ", is found but different price!" );
                                return false;

                            } else {

                                // found with the name and price
                                return true;

                            }

                        }

                    }

                }

            }

            // check if this is the last page => break;
            if ( getNextPageButton ( ).getAttribute ( "class" ).equals ( "paginate_button next disabled" ) ) {
                break;
            }

            // if not last page => click to go to the next page
            clickNextPageButton ( );

            // wait few seconds
            waitFewSeconds ( );


        }

        // return that the item wasn't found
        System.out.println ( "The item " + itemName + ", was not found!" );
        return false;


    }

    // check the item is added successfully
    public boolean checkItemPresence ( String itemName ) {

        // wait
        waitFewSeconds ( );

        // open items page
        clickItemsButton ( );

        // wait
        waitFewSeconds ( );

        // loop on all pages searching for the item
        while (true) {

            // loop on the table of items
            // loop on the items in this page
            String rowElementName = null;

            // path of the table
            String tableXPath = "//*[@id=\"Grid\"]/tbody/tr";

            // get all the elements with the path => of the table's rows
            List<WebElement> rowsNumber = driver.findElements ( By.xpath ( tableXPath ) );

            int rowCount = rowsNumber.size ( );
            System.err.println ( rowCount );

            for (int i = 1; i <= rowCount; i++) {

                rowElementName = itemsTable.findElement ( By.xpath ( tableXPath + "[" + i + "]/td[1]/div/div[2]/h2" ) ).getText ( );

                if ( rowElementName.toString ( ).equals ( itemName ) ) {

                    // if found
                    return true;
                }

            }

            // check if this is the last page => break;
            if ( getNextPageButton ( ).getAttribute ( "class" ).equals ( "paginate_button next disabled" ) ) {
                break;
            }

            // if not last page => click to go to the next page
            clickNextPageButton ( );

            // wait few seconds
            waitFewSeconds ( );

        }

        // return that the item wasn't found
        System.out.println ( "The item " + itemName + ", was not found!" );
        return false;

    }


    // get WebElements
    public WebElement getNextPageButton ( ) {
        return nextPageButton;
    }

    public WebElement getPriceInput ( ) {
        return priceField;
    }


    ////  the fields to be written in

    public WebElement itemImgUpload() {
        return imgUploadButton;
    }

    public void setPriceInput ( String price ) {
        priceField.sendKeys ( price );
    }

    public void setItemNameEnField ( String itemNameEn ) {
        itemNameEnField.sendKeys ( itemNameEn );
    }

    public void setItemNameArField ( String itemNameAr ) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript ( "arguments[0].setAttribute('value', '" + itemNameAr + "')" , itemNameArField );
    }

    public void setDiscountField ( String discount ) {
        discountField.sendKeys ( discount );
    }

    public void setDescriptionEnField ( String descEn ) {
        descriptionEnField.sendKeys ( descEn );
    }

    public void setDescriptionArField ( String descAr ) {

//        JavascriptExecutor jse = (JavascriptExecutor) driver;
//        jse.executeScript ( "arguments[0].value = '"+ descAr +"'" , itemNameArField );
        Actions action = new Actions ( getDriver () );
        action.sendKeys ( descriptionArField,descAr ).build ().perform ();

    }

    public void setPreTime ( String preTime ) {
        preparationTime.sendKeys ( preTime );
    }

    /// the buttons to be clicked
    public void clickItemsButton ( ) {
        itemsButton.click ( );
    }

    public void clickNextPageButton ( ) {
        nextPageButton.click ( );
    }

    public void clickAddNewItemButton ( ) {
        addNewItemButton.click ( );
    }

    public void clickSaveAndExitButton ( ) {
        saveAndExitButton.click ( );
    }

    public void clickSaveAndAddMoreButton ( ) {
        saveAndMoreButton.click ( );
    }

    public void clickNoOneSizeButton ( ) {
        noOneSizeButton.click ( );
    }

    public void clickYesOneSizeButton ( ) {
        yesOneSizeButton.click ( );
    }

    public void clickYesExtendPrepTimeButton ( ) {
        yesExtendPrepTimeButton.click ( );
    }

    public void clickCatalogButton ( ) {
        catalogButton.click ( );
    }

    public void clickNoExtendPrepTimeButton ( ) {
        noExtendPrepTimeButton.click ( );
    }

    public void sendImage ( String imgPath ) {
        imgButton.sendKeys ( imgPath );
    }

    // wait a few seconds
    // function wait
    public void waitFewSeconds ( ) {
        try {
            Thread.sleep ( 9000 );
        } catch (InterruptedException e) {
            e.printStackTrace ( );
        }
    }

    /// return the driver
    public WebDriver getDriver ( ) {
        return driver;
    }


}
