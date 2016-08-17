package Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.Test;

import Selenium.Test_Google;
import br.com.softplan.security.zap.zaproxy.clientapi.core.ClientApiException;
import configZAP.Zap;

public class NewTest_ZAP {

    Zap test = new Zap();
    Test_Google google = new Test_Google();

    @Test(priority = 0)
    public void demarrageZap() throws IOException {
        test.startZAP();
    }

    @Test(priority = 2)
    public void lancementSelenium() throws InterruptedException {
        // Thread.sleep(5000);
        google.SettingUp();
        google.RechercheGoogle();
        // Thread.sleep(3000);
        google.driver.close();
    }

    @Test(priority = 3)
    public void generationReport()
            throws FileNotFoundException, ClientApiException, InterruptedException {
        test.GeneratingReport();

    }
}
