package com.OrangeHRM.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;

public class DummyTest2 extends BaseClass {

    @Test
    public void dummyTest() {

        String title = driver.getTitle();

        assert title.equals("OrangeHRM")
                : "Test Failed - Title not matched";

        System.out.println("Test Passed - Title is matching");
    }
}