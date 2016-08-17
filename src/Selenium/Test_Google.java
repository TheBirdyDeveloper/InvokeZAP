package Selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Test_Google {

    // attribut
    public FirefoxDriver driver;

    // Reglage du driver Selenium
    public void SettingUp() {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy("localhost:8500");
        proxy.setFtpProxy("localhost:8500");
        proxy.setSslProxy("localhost:8500");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);

        driver = new FirefoxDriver(capabilities);
        this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    }

    void waitForLoad(WebDriver driver) {
        new WebDriverWait(driver, 30)
                .until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd)
                        .executeScript("return document.readyState").equals("complete"));
    }

    public void RechercheGoogle() {
        driver.get("https://www.troyhunt.com");
        waitForLoad(this.driver);
        driver.get("https://www.troyhunt.com/speaking/");
        // driver.findElementByLinkText("Media");

        // driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

}
