package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

     public class HomePage {
		private ActionDriver actionDriver;
		// Define locators using By class
		private By adminTab = By.xpath("//*[@id=\"app\"]/div[1]/div[1]/aside/nav/div[2]/ul/li[1]/a/span"); //span[text()='Admin']
		private By userIDButton = By.className("oxd-userdropdown-name");
		private By logoutButton = By.xpath("//a[text()='Logout']");
		private By orangeHRMlogo = By.xpath("/html/body/div/div[1]/div[1]/aside/nav/div[1]/a/div[2]/img"); ////div[@class='oxd-brand-banner']//img
		
		/*//Initialize the ActionDriver object by passing WebDriver instance
		public HomePage(WebDriver driver) {
		this.actionDriver= new ActionDriver(driver);
		}*/
		
		public HomePage(WebDriver driver) {
	    	this.actionDriver = BaseClass.getActionDriver();
	    }
		//Method to verify if Admin tab is visible
		public boolean isAdminTabVisible() {
			return actionDriver.isDisplayed(adminTab);
		}
		//Method to verify if orangehrmlogoo is visible
				public boolean verifyOrangeHRMlogo() {
					return actionDriver.isDisplayed(orangeHRMlogo);
				}
         //method to perform logout operation
				public void logout() {
					actionDriver.click(userIDButton);
					actionDriver.click(logoutButton);
				}
}
