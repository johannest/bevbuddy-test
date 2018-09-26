package com.vaadin.starter.beveragebuddy;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("reviews-list")
public class ReviewsListElement extends TestBenchElement {

    protected ButtonElement getNewReviewButton() {
        return $(ButtonElement.class).id("newReview");
    }

}
