package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class ActionDriver {

	  private WebDriver driver;
	  private WebDriverWait wait;
		public static final Logger Logger = BaseClass.Logger;
	  
	  public ActionDriver(WebDriver driver) {
		  this.driver =driver;
		  int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		  this.wait = new WebDriverWait(driver,Duration.ofSeconds(explicitWait));
		  Logger.info("Webdriver instance is created");
	  }
	  //Method to click element
	  public void click(By by) {
		  String elementDescription=getElementDescription(by);
		  try {
			  applyBorder(by,"green");
			driver.findElement(by).click();
			ExtentManager.logStep("clicked an element ---> " + elementDescription);
			Logger.info("clicked an element--->"+elementDescription);
		} catch (Exception e) {
			applyBorder(by,"red");
			System.out.println("unable to click element:"+e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(),"unable to click element", elementDescription);
			Logger.error("unable to click element");
		}
	  }
		  //Method to enter text into an input field
		  public void enterText(By by , String value) {
			  try {
				  applyBorder(by,"green");
				WebElement element = driver.findElement(by);
				element.clear();
				 element.sendKeys(value);
				 Logger.info("Entered text on"+getElementDescription(by)+" "+value);
			} catch (Exception e) {
				applyBorder(by,"red");
				Logger.error("unable to enter the value:"+e.getMessage());
			}
		  }
		  
		  //Method to get text from input field
       public String getText(By by) {
			 try {
				 applyBorder(by,"green");
				waitForElementToBeVisible(by);
				 return driver.findElement(by).getText();
			} catch (Exception e) {
				applyBorder(by,"red");
				Logger.error("unable to get the text:"+e.getMessage());
				return "";
			}
		  }
       
       //Method to compare two text - changed the return type
       public boolean compareText(By by,String expectedText) {
    	   try {
			waitForElementToBeVisible(by);
			   String actualText = driver.findElement(by).getText();
			   if(expectedText.equals(actualText)) {
				   applyBorder(by,"green");
				   Logger.info("Text are matching:"+actualText+"equals"+expectedText);
				   ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text","Text Verified Successfully!"+ actualText+ " equals "+expectedText);
			   return true;
			   }
			   else {
				   applyBorder(by,"red");
				   Logger.error("Text are not matching:"+actualText+" not equals"+expectedText);
				   ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparision failed","Text Comparision failed!"+ actualText+ "Not equals "+expectedText);
				   return false;
			   }
		} catch (Exception e) {
			applyBorder(by,"red");
			Logger.error("unable to compare text:"+e.getMessage());
		}
		return false;
    	   }
       
       //Method to check if an element is dispalyed
      /* public boolean isDisplayed(By by) {
    	   try {
			waitForElementToBeVisible(by);
			   boolean isDisplayed = driver.findElement(by).isDisplayed();
			   if(isDisplayed) {
				   System.out.println("Elemet is visible");
				   return isDisplayed;
			   }
			   else {
				   return isDisplayed;
			   }
		} catch (Exception e) {
			System.out.println("Element is not visible"+e.getMessage());
			return false;
		}
       } */
       //simplified the method and remove redundant conditions
       public boolean isDisplayed(By by) {
    	   try {
    		   applyBorder(by,"green");
			waitForElementToBeVisible(by);
			Logger.info("Element is Displayed: "+getElementDescription(by));
			ExtentManager.logStep("Element is Displayed:"+getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed", "Element is displayed"+getElementDescription(by));
			return driver.findElement(by).isDisplayed();
    	   } catch (Exception e) {
    		   applyBorder(by,"red");
    		   ExtentManager.logFailure(BaseClass.getDriver(),"Element is not Displayed:","Element is not Displayed"+getElementDescription(by));
    		   Logger.error("Element is not visible"+e.getMessage());
   			return false;
    	   }
       }
    
    // Wait for the page to load
   	public void waitForPageLoad(int timeOutInSec) {
   		try {
   			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
   					.executeScript("return document.readyState").equals("complete"));
   			Logger.info("Page loaded successfully.");
   		} catch (Exception e) {
   			Logger.error("Page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
   		}
   	}

   	// Scroll to an element -- Added a semicolon ; at the end of the script string
   	public void scrollToElement(By by) {
   		try {
   			applyBorder(by,"green");
   			JavascriptExecutor js = (JavascriptExecutor) driver;
   			WebElement element = driver.findElement(by);
   			js.executeScript("arguments[0].scrollIntoView(true);", element);
   		} catch (Exception e) {
   			applyBorder(by,"red");
   			Logger.error("Unable to locate element:" + e.getMessage());
   		}
   	}


	 //wait for element to be clickable
		  public void waitForElementToBeClickable(By by) {
			  try {
				wait.until(ExpectedConditions.elementToBeClickable(by));
			} catch (Exception e) {
				Logger.error("element is not clcikable:"+e.getMessage());
			}
		  }
			//wait for element to be visible
			  public void waitForElementToBeVisible(By by) {
				  try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(by));
				} catch (Exception e) {
					Logger.error("element is not visible:"+e.getMessage());
				}  
	  }
			  //Method to get the description of an element using By locator
			  public String getElementDescription(By locator) {
				  //Check for null driver or locator to avoid Nullpointer Exception
				  if(driver == null)
					  return "driver is null";
				  if(locator == null)
					  return "locator is null";
				  
				  //find the element using the locator
				  try {
					WebElement element = driver.findElement(locator);
					  
					  //get Element Attributes
					  String name = element.getDomAttribute("name");
					  String id = element.getDomAttribute("id");
					  String text = element.getText();
					  String className = element.getDomAttribute("class");
					  String placeHolder = element.getDomAttribute("placeholder");
					  
					  //Return the description based on element attributes
					  if(isNotEmpty(name)) {
						  return "Element with name :"+ name;
					  }
					  else if(isNotEmpty(id)) {
						  return "Element with id :"+ id;
					  }
					  else if(isNotEmpty(text)) {
						  return "Element with text :"+ truncate(text,50);
					  }
					  else if(isNotEmpty(placeHolder)) {
						  return "Element with placeholder :"+ placeHolder;
					  }
					  else if  (isNotEmpty(className)) {
						  return "Element with classname :"+ className;
  }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Logger.error("Unable to describe the element"+e.getMessage());
				}
				  return ("Unable to describe the element");
			  }
				  //Utility Method to check a string is not null or empty
				  private boolean isNotEmpty(String value) {
					  return value!=null && !value.isEmpty();
				  }
				  
				  //utility method to truncate long string
				  private String truncate(String value, int maxLength) {
					  if(value==null || value.length()<=maxLength) {
						  return value;
					  }
					  return value.substring(0,maxLength)+"......";				 
							  }  
				  //utility Method to border an element
				  public void applyBorder(By by,String colour) {
					  //Locate the element
					  try {
						WebElement element = driver.findElement(by);
						  //Apply the border
						  String script = "arguments[0].style.border='3px solid "+colour+"'";
						  JavascriptExecutor js =(JavascriptExecutor) driver;
						  js.executeScript(script, element);
						  Logger.info("Applied the border with colour"+colour+"to element"+getElementDescription(by));
					} catch (Exception e) {
						Logger.warn("Filed to applied the border to an element"+getElementDescription(by));
					}
				  }
}
