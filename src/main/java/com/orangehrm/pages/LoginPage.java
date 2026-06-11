package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {
	private ActionDriver actionDriver;
    private By userNameField = By.name("username");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginButton = By.xpath("//*[@id=\"app\"]/div[1]/div/div[1]/div/div[2]/div[2]/form/div[3]/button");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
    
    /*public LoginPage(WebDriver driver) {
    	this.actionDriver = new ActionDriver(driver);
    } */
    public LoginPage(WebDriver driver) {
    	this.actionDriver = BaseClass.getActionDriver();
    }
    
    //Method to perform login
    public void login(String userName,String password) {
    	actionDriver.enterText(userNameField, userName);
    	actionDriver.enterText(passwordField, password);
    	actionDriver.click(loginButton);
    }
    
    //Method to check if error message is displayed
    public boolean isErrorMessageDisplayed() {
    	return actionDriver.isDisplayed(errorMessage);
    }
  //Method to get the text from error message
    public String getErrorMessageText() {
    	return actionDriver.getText(errorMessage);
    }
    //verify error message is correct or not
      public boolean verifyErrorMessage(String expectedError){
      return actionDriver.compareText(errorMessage,expectedError);
    }
    }

