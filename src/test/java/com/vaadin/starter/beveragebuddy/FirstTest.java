package com.vaadin.starter.beveragebuddy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.vaadin.johannest.loadtestdriver.LoadTestDriver;
import org.vaadin.johannest.loadtestdriver.LoadTestDriverBuilder;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.testbench.IPAddress;
import com.vaadin.testbench.TestBenchTestCase;

public class FirstTest extends TestBenchTestCase {

    @Before
    public void setup() throws Exception {
        WebDriver driver = new LoadTestDriverBuilder().
                withIpAddress(LoadTestDriver.getLocalIpAddress()).
                withNumberOfConcurrentUsers(1).
                withRampUpTimeInSeconds(1).
                withTestName("LoginTest").
                withPath("/Users/jotatu/Desktop/gatling").
                withResourcesPath("/Users/jotatu/Desktop/gatling").
                withStaticResourcesIngnoring().
                withHeadlessEnabled(true).
                build();
        setDriver(driver);
    }

    @Test
    public void firstTest() {
        getDriver().get("http://" + IPAddress.findSiteLocalAddress() + ":8080/");

        final ReviewsListElement reviewsListElement = $(ReviewsListElement.class).waitForFirst();
        final ButtonElement newReviewButton = reviewsListElement.getNewReviewButton();
        newReviewButton.click();
    }

    @After
    public void tearDown() {
        getDriver().close();
    }

}
