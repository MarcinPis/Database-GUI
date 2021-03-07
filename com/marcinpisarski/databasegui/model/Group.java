package com.marcinpisarski.databasegui.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Group {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;

    public Group() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
    }

    // Override equals to be able to compare list items
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Group)) {
            return false;
        }

        Group other = (Group) o;
        boolean nameEquals = (this.getName() == null && other.getName() == null)
                || (this.getName() != null && this.getId() == other.getId());

        return nameEquals;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
}