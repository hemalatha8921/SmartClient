package Utilities;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


public class SetDriver {

    public static WebDriver driver;
    public static String driver_Path = System.getProperty("user.dir")+"/src/test/Drivers/";

    public static WebDriver setFirefoxDriver(){
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setJavascriptEnabled(true);
        dc.setCapability("network.protocol-handler.external.javascript", "true");
        dc.setAcceptInsecureCerts(true);
        dc.acceptInsecureCerts();
        FirefoxOptions op = new FirefoxOptions();
        WebDriverManager.firefoxdriver().setup();
        op.merge(dc);
        try{
            driver = new FirefoxDriver(op);
        }catch (Exception e){
            e.printStackTrace();
        }
        return driver;
    }
    public static WebDriver setChromeDriver(){
        ChromeOptions options = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        options.addArguments("disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        try{
            driver = new ChromeDriver(options);
        } catch (Exception e){
            e.printStackTrace();
        }
        return driver;
    }
    public static WebDriver setIEDriver(){
        DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
        WebDriverManager.iedriver().setup();
        capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        try{
            driver = new InternetExplorerDriver(capability);
        }catch(Exception e){
            e.printStackTrace();
        }
        return driver;
    }
}
