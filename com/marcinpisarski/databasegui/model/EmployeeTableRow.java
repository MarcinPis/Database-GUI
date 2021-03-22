package com.marcinpisarski.databasegui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class EmployeeTableRow {

    private Employee employee;
    private Department department;
    private ObservableList<Group> groups;

    public EmployeeTableRow() {
        employee = new Employee();
        department = new Department();
        groups = FXCollections.observableArrayList();
    }

    public ObservableList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ObservableList<Group> groups) {
        this.groups = groups;
    }

    public int getEmployeeId() {
        return employee.getId();
    }

    public void setEmployeeId(int employeeId) {
        employee.setId(employeeId);
    }

    public String getFirstName() {
        return employee.getFirstName();
    }

    public void setFirstName(String firstName) {
        employee.setFirstName(firstName);
    }

    public String getLastName() {
        return employee.getLastName();
    }

    public void setLastName(String lastName) {
        employee.setLastName(lastName);
    }

    public int getDepartmentId() {
        return department.getId();
    }

    public void setDepartmentId(int departmentId) {
        department.setId(departmentId);
    }

    public String getDepartmentName() {
        return department.getName();
    }

    public void setDepartmentName(String name) {
        department.setName(name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof EmployeeTableRow)) {
            return false;
        }

        EmployeeTableRow other = (EmployeeTableRow) o;
        boolean idEquals = (this.getEmployeeId() == 0 && other.getEmployeeId() == 0)
                || (this.getEmployeeId() != 0 && this.getEmployeeId() == other.getEmployeeId());

        return idEquals;
    }
}
