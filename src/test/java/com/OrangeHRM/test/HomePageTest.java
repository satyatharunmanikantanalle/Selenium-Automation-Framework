package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
    public void verifyOrangeHRMLogo(String username, String password) {

        System.out.println("Running verifyOrangeHRMLogo on Thread : "
                + Thread.currentThread().getId());

        ExtentManager.logStep("Logging into OrangeHRM");

        loginPage.login(username, password);

        ExtentManager.logStep("Verifying OrangeHRM Logo");

        Assert.assertTrue(homePage.verifyOrangeHRMlogo(),
                "OrangeHRM logo is not visible.");

        ExtentManager.logStep("Logo verified successfully");

        homePage.logout();

        ExtentManager.logStep("Logout successful");
    }

}