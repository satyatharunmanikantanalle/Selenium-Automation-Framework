package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

    protected static Properties prop;
   // protected static WebDriver driver;
    //private static ActionDriver actionDriver;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    
	public static final Logger Logger = LoggerManager.getLogger(BaseClass.class);
    
   @BeforeSuite
    public void loadconfig() throws IOException {
        // Load configuration file
        prop = new Properties();
        FileInputStream fis =
        new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
        Logger.info("config.properties file loaded");
    }
    @BeforeMethod
    public synchronized void setup() throws IOException {
    	System.out.println("setting up Webdriver for:"+this.getClass().getSimpleName());
    	launchbrowser();
    	configureBrowser();
    	staticWait(2);
    	Logger.info("WebDriver Initialized and Browser maximized");
    	Logger.trace("this is aTrace message");
    	Logger.error("This is a error message");
    	Logger.debug("this is a debug Message");
    	Logger.fatal("This is a fatal message");
    	Logger.warn("this is a warning Message");
        //Initialize the actionDriver only once
        /*if(actionDriver == null) {
        	actionDriver = new ActionDriver(driver);
        	System.out.println("action driver instance is created"+Thread.currentThread().getId());
         }*/
    	//Intialize ActionDriver for the current Thread
        actionDriver.set(new ActionDriver(getDriver()));
        Logger.info("ActionDriver initialized for thread:"+Thread.currentThread());
    }
    //Intialize the WebDriver based on browser defined in config.properties
    private synchronized void launchbrowser() {
    	// Get browser name
        String browser = prop.getProperty("browser");
        // Initialize browser
        if (browser.equalsIgnoreCase("chrome")) {
            //driver = new ChromeDriver();
        	driver.set(new ChromeDriver());  //new changes as per thread
            Logger.info("ChromeDriver Instance is Created");
        }
        else if (browser.equalsIgnoreCase("firefox")) {
            //driver = new FirefoxDriver();
        	driver.set(new FirefoxDriver());
            Logger.info("FireFoxDriver Instance is Created");
        }
        else if (browser.equalsIgnoreCase("edge")) {
            //driver = new EdgeDriver();
        	driver.set(new EdgeDriver());
            Logger.info("EdgeDriver Instance is Created");
        }
        else {
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }
    }
    private void configureBrowser() {
    	// Implicit wait
        int implicitWait =
         Integer.parseInt(prop.getProperty("implicitwait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        // Maximize browser
        getDriver().manage().window().maximize();
        // Open URL
        try {
        	getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("failed to Navigate to url"+e.getMessage());
		}
    }
    @AfterMethod
    public synchronized void teardown() {
        if (getDriver() != null) {
            try {
            	getDriver().quit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("failed to quit browser"+e.getMessage());
			}
        }
        Logger.info("webDriver instance is closed");
        driver.remove();
        actionDriver.remove();
       // driver= null;
        //actionDriver = null;
    }
    
    //getter method for prop
    public static Properties getProp() {
    	return prop;
    }
    /*
    //Driver getter method
    public WebDriver getDriver() {
    	return driver;
    } */
    
    //Getter method for webdriver
    public static WebDriver getDriver() {
    	if(driver.get()==null) {
    		System.out.println("WebDriver is not initialized");
    		throw new IllegalStateException("WebDriver is not initialized");
    	}
		return driver.get();
    	
    }
  //Getter method for ActionDriver
    public static ActionDriver  getActionDriver() {
    	if(actionDriver.get() == null) {
    		System.out.println("ActionDriver is not initialized");
    		throw new IllegalStateException("ActionDriver is not initialized");
    		
    	}
		return actionDriver.get();
    	
    }
    
    //Driver setter method
    public void setDriver(ThreadLocal<WebDriver> driver) {
    	this.driver=driver;
    }
    //static wait for pause
    public void staticWait(int seconds) {
    	LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}