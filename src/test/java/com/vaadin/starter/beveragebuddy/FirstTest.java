package com.vaadin.starter.beveragebuddy;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
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
                withPath("C:\\dev\\gatling10").
                withResourcesPath("C:\\dev\\gatling10\\resources").
                withStaticResourcesIngnoring().
                withHeadlessEnabled(false).
                build();
        setDriver(driver);
    }

    @Test
    public void firstTest() {
        getDriver().get("http://" + IPAddress.findSiteLocalAddress() + ":8090/");

        final ReviewsListElement reviewsListElement = $(ReviewsListElement.class).waitForFirst();
        final ButtonElement newReviewButton = reviewsListElement.getNewReviewButton();
        newReviewButton.click();

        $(TextFieldElement.class).first().sendKeys("Mehu");
        $(ComboBoxElement.class).first().selectByText("Other");
        $(ButtonElement.class).id("save-button").click();
    }

    @After
    public void tearDown() {
        getDriver().close();
    }

}
