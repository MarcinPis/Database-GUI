package com.marcinpisarski.databasegui.model;

public class Department {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Override equals to be able to compare list items
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Department)) {
            return false;
        }

        Department other = (Department) o;
        boolean nameEquals = (this.getName() == null && other.getName() == null)
                || (this.getName() != null && this.getId() == other.getId());

        return nameEquals;
    }
}