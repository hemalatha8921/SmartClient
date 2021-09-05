package stepdefs;
import Utilities.SetDriver;
//import com.sun.net.ssl.internal.ssl.Provider;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.apache.log4j.Logger.getLogger;

public class commonMethods {
    public static Properties OrProp = new Properties();
    public static Properties Prop = new Properties();
    String separator = System.getProperty("file.separator");
    String project_path = System.getProperty("user.dir");
    Logger logger = getLogger(commonMethods.class);
    public static WebDriver webDriver;
    public static WebElement element;
    public static List<WebElement> elements;
    public static FileInputStream ofis;
    public static FileInputStream cfis;

    public commonMethods(){
        try{
            System.out.println("Properties Initaited");
            String OrConfigPath = project_path+separator+"src"+separator+"test"+separator+"resources"+separator+"or.properties";
            ofis = new FileInputStream(OrConfigPath);
            OrProp.load(ofis);
            String PropConfigPath = project_path+separator+"src"+separator+"test"+separator+"resources"+separator+"config.properties";
            cfis = new FileInputStream(PropConfigPath);
            Prop.load(cfis);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @BeforeTest
    public static void initialize(){
        String brwsr = Prop.getProperty("browser");
        switch (brwsr.toUpperCase()){
            case "CHROME":
                webDriver = SetDriver.setChromeDriver();
                break;
            case "FIREFOX":
                webDriver = SetDriver.setFirefoxDriver();
                break;
            case "IE":
                webDriver = SetDriver.setIEDriver();
                break;
        }
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @AfterTest
    static void tearDown() {
        webDriver.quit();
    }

    public static void setDriver(WebDriver driver) { webDriver = driver; }

    public static WebDriver getDriver(){
        return webDriver;
    }


    public static String getText(String PropertyName){
        element = webDriver.findElement(By.xpath(PropertyName));
        return element.getText();
    }

    public static void sendText(String PropertyName, String Value){
        element = webDriver.findElement(By.xpath(PropertyName));
        element.sendKeys(Value);
    }

    public static WebElement getWebElement(String PropertyName){
        element = webDriver.findElement(By.xpath(PropertyName));
        return element;
   }
    public static List<WebElement> getWebElements(String PropertyName) throws Exception{
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        elements = webDriver.findElements(By.xpath(PropertyName));
        return elements;
    }
    public static void clickElement(String PropertyName){
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(PropertyName)));
        element.click();
        waitForPageToLoad();
    }

    public static void waitForPageToLoad() {
        new WebDriverWait(webDriver, 15).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public static void scrollToElement(WebElement element) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
        waitForPageToLoad();
    }
}
