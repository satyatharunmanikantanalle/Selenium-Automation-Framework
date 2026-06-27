package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;  code commented
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	// Getter method for soft assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");

		// Start the Extent Report
		// ExtentManager.getReporter(); --This has been implemented in TestListener
	}

	@BeforeMethod
	@Parameters("browser")
	public synchronized void setup(String browser) throws IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
		launchBrowser(browser);
		configureBrowser();
		staticWait(2);
		// Sample logger message
		logger.info("WebDriver Initialized and Browser Maximized");
		logger.trace("This is a Trace message");
		logger.error("This is a error message");
		logger.debug("This is a debug message");
		logger.fatal("This is a fatal message");
		logger.warn("This is a warm message");

		// Initialize ActionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initlialized for thread: " + Thread.currentThread().getId());

	}

	/*
	 * Initialize the WebDriver based on browser defined in config.properties file
	 */
	private synchronized void launchBrowser(String browser) {

	    // String browser = prop.getProperty("browser");

	    boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
	    String gridURL = prop.getProperty("gridURL");

	    if (seleniumGrid) {

	        try {

	            if (browser.equalsIgnoreCase("chrome")) {

	                ChromeOptions options = new ChromeOptions();

	                // Headless mode (Enable only for Jenkins/Grid)
	                // options.addArguments("--headless=new");
	                // options.addArguments("--disable-gpu");
	                // options.addArguments("--window-size=1920,1080");

	                // Visible Chrome
	                options.addArguments("--start-maximized");
	                options.addArguments("--disable-notifications");

	                driver.set(new RemoteWebDriver(new URL(gridURL), options));

	            } else if (browser.equalsIgnoreCase("firefox")) {

	                FirefoxOptions options = new FirefoxOptions();

	                // options.addArguments("-headless");

	                driver.set(new RemoteWebDriver(new URL(gridURL), options));

	            } else if (browser.equalsIgnoreCase("edge")) {

	                EdgeOptions options = new EdgeOptions();

	                // options.addArguments("--headless=new");
	                // options.addArguments("--disable-gpu");
	                // options.addArguments("--window-size=1920,1080");

	                options.addArguments("--start-maximized");

	                driver.set(new RemoteWebDriver(new URL(gridURL), options));

	            } else {

	                throw new IllegalArgumentException("Browser Not Supported : " + browser);

	            }

	            logger.info("RemoteWebDriver Instance Created.");

	        } catch (MalformedURLException e) {

	            throw new RuntimeException("Invalid Grid URL", e);

	        }

	    } else {

	        if (browser.equalsIgnoreCase("chrome")) {

	            ChromeOptions options = new ChromeOptions();

	            // ===========================
	            // Headless Options (Keep for Jenkins)
	            // ===========================

	            // options.addArguments("--headless=new");
	            // options.addArguments("--disable-gpu");
	            // options.addArguments("--window-size=1920,1080");

	            // ===========================
	            // Visible Chrome
	            // ===========================

	            options.addArguments("--start-maximized");
	            options.addArguments("--disable-notifications");

	            // Optional for CI
	            // options.addArguments("--no-sandbox");
	            // options.addArguments("--disable-dev-shm-usage");

	            driver.set(new ChromeDriver(options));

	            ExtentManager.registerDriver(getDriver());

	            logger.info("ChromeDriver Instance is created.");

	        }

	        else if (browser.equalsIgnoreCase("firefox")) {

	            FirefoxOptions options = new FirefoxOptions();

	            // options.addArguments("--headless");
	            // options.addArguments("--disable-gpu");
	            // options.addArguments("--width=1920");
	            // options.addArguments("--height=1080");

	            options.addArguments("--start-maximized");

	            driver.set(new FirefoxDriver(options));

	            ExtentManager.registerDriver(getDriver());

	            logger.info("FirefoxDriver Instance is created.");

	        }

	        else if (browser.equalsIgnoreCase("edge")) {

	            EdgeOptions options = new EdgeOptions();

	            // options.addArguments("--headless=new");
	            // options.addArguments("--disable-gpu");
	            // options.addArguments("--window-size=1920,1080");

	            options.addArguments("--start-maximized");

	            driver.set(new EdgeDriver(options));

	            ExtentManager.registerDriver(getDriver());

	            logger.info("EdgeDriver Instance is created.");

	        }

	        else {

	            throw new IllegalArgumentException("Browser Not Supported : " + browser);

	        }

	    }

	}
	/*
	 * Configure browser settings such as implicit wait, maximize the browser and
	 * navigate to the URL
	 */

	private void configureBrowser() {
		// Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		boolean seleniumGrid = Boolean.parseBoolean(System.getProperty("seleniumGrid", prop.getProperty("seleniumGrid")));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// maximize the browser
		getDriver().manage().window().maximize();

		// Navigate to URL
		/*try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to Navigate to the URL:" + e.getMessage());
		} */
		
		if (seleniumGrid) {
			getDriver().get(prop.getProperty("url_grid"));
		} else {
			getDriver().get(prop.getProperty("url_local"));
		}
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("unable to quit the driver:" + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed.");
		driver.remove();
		actionDriver.remove();
		// driver = null;
		// actionDriver = null;
		// ExtentManager.endTest(); --This has been implemented in TestListener
	}

	/*
	 * 
	 * 
	 * //Driver getter method public WebDriver getDriver() { return driver; }
	 */

	// Getter Method for WebDriver
	public static WebDriver getDriver() {

		if (driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();

	}

	// Getter Method for ActionDriver
	public static ActionDriver getActionDriver() {

		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get();

	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}

	// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}