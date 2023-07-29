import Pages.BasePage;
import Pages.ItemsPage;
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
import java.util.HashMap;
import java.util.Map;

public class TestItemsPage {

    WebDriver driver;
    BasePage basePage;
    ItemsPage itemsPage;

    String HomePage = "http://transmission-dev.azurewebsites.net/home";
    String LoginPage = "http://transmission-dev.azurewebsites.net/login";

    @BeforeClass
    public void setUp ( ) {

        driver = new ChromeDriver ( );
        driver.get ( LoginPage );

    }

    @Test(priority = 1, dataProvider = "LoginData.json", dataProviderClass = TestBasePage.class)
    public void TestLogin ( String email , String password ) {

        basePage = new BasePage ( driver );
        basePage.login ( email , password );

        // assert successful login
        basePage.softAssert.assertTrue ( basePage.getDriver ( ).getCurrentUrl ( ).equals ( HomePage ) );

    }

    @Test(priority = 6)
    public void TestAddNewItem ( ) throws InterruptedException {

        // The item's data
        String itemNameEn = "Strong Leather Handmade Cross Handbag";
        String itemNameAr = "شنطة جلد قوية كروس يدوية الصنع";

        // discount
        boolean hasDiscount = false;
        String discount = "15";

        // item data
        String category = "Small bags";
        String imgPath = "C:\\Users\\Zbook\\Pictures\\bag.jpg";
        String descriptionEn = "Material: Soft PU leather, Polyester lining; Heavy duty metal zippers, smooth and durable.\n can put your daily belongs.\nMultifunctional: can be used as handbag or a wristlet bag, easy to carry for everyday use.\nFeatures: a vintage and simple design with classic envelope clutch shape,makes you more fashion,elegant and chic.";
        String descriptionAr = "المواد: جلد PU ناعم ، بطانة بوليستر. السوستة المعدنية الثقيلة ، على نحو سلس ودائم. يمكن أن تضع انتماءاتك اليومية. متعددة الوظائف: يمكن استخدامها كحقيبة يد أو حقيبة معصم ، يسهل حملها للاستخدام اليومي.  \nالميزات: تصميم خمر وبسيط مع شكل مخلب المغلف الكلاسيكي ، يجعلك أكثر عصرية وأنيقة وأنيقة.";
        boolean extendPrepTime = false;
        String extendedPrepTime = "0";
        String price = "360";

        // variations
        boolean hasVariation = false;
        Map<String, String> variations = new HashMap<> ( );


        itemsPage = new ItemsPage ( driver );

        itemsPage.addNewItem ( itemNameEn , itemNameAr , hasDiscount , discount , category , imgPath , descriptionEn ,
                descriptionAr , hasVariation , variations , price , extendPrepTime , extendedPrepTime );

        if ( ! hasVariation ) {
            // in case of one-size
            boolean checkItem = itemsPage.checkItemPriceNoVar ( itemNameEn , price );
            itemsPage.softAssert.assertEquals ( checkItem , true );

        } else {

            // in case of presence of variations
            boolean checkItem;
            for (Map.Entry<String, String> variation : variations.entrySet ( )) {

                // as the key is the variation name, and the value is its price
                checkItem = itemsPage.checkItemPriceWithVar ( itemNameEn , variation.getKey ( ) , variation.getValue ( ) );
                itemsPage.softAssert.assertEquals ( checkItem , true );

            }

        }


    }


    @Test(priority = 7)
    public void TestEditItemPriceNoVariation() throws InterruptedException {

        String itemName = "White cross bag";
        String price = "650";

        itemsPage = new ItemsPage ( driver );
        boolean found = itemsPage.editItemPriceWithoutVar ( itemName, price );

        if(!found) {
            itemsPage.softAssert.assertEquals ( found, false, "The item ( "+itemName+" ) is not found!" );
            return;
        }

        boolean editedSuccessfully = itemsPage.checkItemPriceNoVar (itemName, price);

        // assert if edited successfully
        itemsPage.softAssert.assertEquals ( editedSuccessfully, true );

    }

    /*
    @Test(priority = 7)
    public void TestEditItemPriceWithVariation() throws InterruptedException {

        String itemName = "Backpack Yellow";
        String price = "660";
        String variation = "Yellow x Grey";

        itemsPage = new ItemsPage ( driver );
        boolean found = itemsPage.editItemPriceWithVar ( itemName, variation,  price );

        if(!found) {
            itemsPage.softAssert.assertEquals ( found, false, "The item ( "+itemName+" ) is not found!" );
            return;
        }

        boolean editedSuccessfully = itemsPage.checkItemPriceWithVar ( itemName , variation ,  price );

        // assert if edited successfully
        itemsPage.softAssert.assertEquals ( editedSuccessfully, true );

    }
    */

    @Test(priority = 5, dataProvider = "addItem")
    public void TestAddItem ( String itemEn , String itemAr , String discount , String status , String category , String descEn ,
                              String descAr , String hasVariation , String price , String prepTime , String imgPath ) throws InterruptedException {

        // determine preparation time boolean
        boolean hasPrepTime;
        if ( prepTime.equals ( "0" ) ) {
            hasPrepTime = false;
        } else {
            hasPrepTime = true;
        }

        // determine discount active-ness
        boolean discountActiveness;
        if ( status.equals ( "true" ) ) discountActiveness = true;
        else discountActiveness = false;

        // check if one-size or has variations
        boolean ifVariation;
        if(hasVariation.equals ( "true" )) ifVariation = true;
        else ifVariation = false;

        // add the new item after getting the data from the data provider
        itemsPage = new ItemsPage ( driver );
        itemsPage.addNewItem ( itemEn , itemAr , discountActiveness , discount , category , imgPath , descEn , descAr , ifVariation , new HashMap<> ( ) ,
                price , hasPrepTime , prepTime );


        // check whether the item is added successfully
        if ( ifVariation ) {
            // in case of one-size
            boolean checkItem = itemsPage.checkItemPriceNoVar ( itemEn , price );
            itemsPage.softAssert.assertEquals ( checkItem , true );

        } else {

            /// if there is variations would be added to this map
            Map<String, String> variations = new HashMap<> ( );

            // in case of presence of variations
            boolean checkItem;
            for (Map.Entry<String, String> variation : variations.entrySet ( )) {

                // as the key is the variation name, and the value is its price
                checkItem = itemsPage.checkItemPriceWithVar ( itemEn , variation.getKey ( ) , variation.getValue ( ) );
                itemsPage.softAssert.assertEquals ( checkItem , true );

            }

        }

    }


    @DataProvider(name = "addItem")
    public Object[][] getItemData() {

        FileReader reader = null;
        try {
            reader = new FileReader ("D:\\Fawry-testing internship\\automation-testing-project\\src\\test\\resources\\ItemData.json");
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

        if(jsonObject == null) {
            System.out.println ("ItemData Json file is empty!" );
            return null;
        }

        // could make it as a loop
        Object[][] itemData = new Object[1][11];
        itemData[0][0] = jsonObject.get ( "itemNameEn" );
        itemData[0][1] = jsonObject.get ( "itemNameAr" );
        itemData[0][2] = jsonObject.get ( "discount" );
        itemData[0][3] = jsonObject.get ( "status" );
        itemData[0][4] = jsonObject.get ( "category" );
        itemData[0][5] = jsonObject.get ( "descriptionEn" );
        itemData[0][6] = jsonObject.get ( "descriptionAr" );
        itemData[0][7] = jsonObject.get ( "hasVariation" );
        itemData[0][8] = jsonObject.get ( "price" );
        itemData[0][9] = jsonObject.get ( "prepTime" );
        itemData[0][10] = jsonObject.get ( "image" );

        return itemData;

    }

    @AfterClass
    public void terminate ( ) {
        itemsPage.getDriver ( ).quit ( );
        basePage.getDriver ( ).quit ( );
        driver.quit ( );
    }


}
