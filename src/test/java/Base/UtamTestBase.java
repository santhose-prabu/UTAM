package Base;

import Utils.TestEnvironment;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import utam.core.driver.Document;
import utam.core.driver.DriverType;
import utam.core.framework.base.RootPageObject;
import utam.core.framework.consumer.UtamLoader;
import utam.core.framework.consumer.UtamLoaderConfig;
import utam.core.framework.consumer.UtamLoaderConfigImpl;
import utam.core.framework.consumer.UtamLoaderImpl;
import utam.core.selenium.factory.WebDriverFactory;

import java.time.Duration;

public class UtamTestBase {
    private WebDriver driver;
    protected UtamLoader loader;

    static String getUserHomeRelativePath(String fileName) {
        return System.getProperty("user.home") + System.getProperty("file.separator") + fileName;
    }

    /**
     * get instance of the Document object
     *
     * @return instance of document object in case it needs to be called from test
     */
    protected final Document getDomDocument() {
        return loader.getDocument();
    }

    /** create chrome driver and setup loader */
    protected final void setupChrome() {
        System.setProperty("webdriver.chrome.driver", getUserHomeRelativePath("chromedriver.exe"));
        setUtam(WebDriverFactory.getWebDriver(DriverType.chrome));
        driver.manage().window().maximize();
    }

    /** create firefox driver and setup loader */
    protected final void setupFirefox() {
        System.setProperty("webdriver.gecko.driver", getUserHomeRelativePath("geckodriver"));
        setUtam(WebDriverFactory.getWebDriver(DriverType.firefox));
    }

    /**
     * helper method to load any Root Page Object
     *
     * @param rootPageObjectType type of the page object to load
     * @param <T> generic bound
     * @return instance of the loaded PO
     */
    protected final <T extends RootPageObject> T from(Class<T> rootPageObjectType) {
        if (loader == null) {
            throw new NullPointerException("UtamLoader is not set, please use setDriver method first!");
        }
        return loader.load(rootPageObjectType);
    }

    /** Quit web driver if it's not null. Method is used in test or suite teardown */
    protected final void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * get instance of the driver
     *
     * @return instance of the web driver
     */
    protected final WebDriver getDriver() {
        return driver;
    }

    /**
     * set driver instance
     *
     * @param driver instance of the driver
     * @return instance of UtamLoader
     */
    final UtamLoader setUtam(WebDriver driver) {
        this.driver = driver;
        UtamLoaderConfig config = new UtamLoaderConfigImpl();
        config.setExplicitTimeout(Duration.ofSeconds(60));
        config.setImplicitTimeout(Duration.ZERO);
        loader = new UtamLoaderImpl(config, driver);
        return loader;
    }

    /**
     * Get environment properties from resources
     *
     * @see TestEnvironment class for format of the file
     * @param envNamePrefix environment name prefix
     * @return object with a test environment information
     */
    protected final TestEnvironment getTestEnvironment(String envNamePrefix) {
        return new TestEnvironment(envNamePrefix);
    }

    /**
     * method that waits for hardcoded time, only for debug
     *
     * @param seconds seconds to sleep for
     */
    protected final void debug(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ie) {
            throw new AssertionError(ie);
        }
    }

    /**
     * helper method to print information to console
     *
     * @param str message to print
     */
    protected final void log(String str) {
        Reporter.log("Utam log: " + str);
    }
}
