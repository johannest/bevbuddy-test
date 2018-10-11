package com.vaadin.starter.beveragebuddy.ui.views.categorieslist;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.starter.beveragebuddy.backend.Category;

public class CategoryDetail extends HorizontalLayout {
    public CategoryDetail() {
        add(new Label("Foo"));
    }

    public void setCategory(Category c) {
        removeAll();
        TextField textField = new TextField();
        textField.setValue(c.getName());
        add(textField);
    }
}
