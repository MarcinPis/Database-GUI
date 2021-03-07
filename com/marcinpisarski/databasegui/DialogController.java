package com.marcinpisarski.databasegui;

import com.marcinpisarski.databasegui.model.Department;
import com.marcinpisarski.databasegui.model.Group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import com.marcinpisarski.databasegui.model.Datasource;
import com.marcinpisarski.databasegui.model.Employee;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DialogController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ListView<Group> mailingGroupsList;

    @FXML
    private ComboBox<Department> departmentSelector;

    public void initialize() {
        loadListViewContent();
        loadComboBoxContent();
    }

    public boolean isInputValid() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void dialogInitialize(Employee employee, Department department) {
        firstNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
        departmentSelector.setValue(department);

        ObservableList<Group> groupsList = FXCollections.observableArrayList(Datasource.getInstance().queryGroups());
        mailingGroupsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mailingGroupsList.itemsProperty().setValue(groupsList);

        List<Group> employeeGroups = new ArrayList<>();
        employeeGroups = Datasource.getInstance().queryEmployeesMailing(employee.getId());

        for (Iterator<Group> iterator = employeeGroups.iterator(); iterator.hasNext();) {
            Group group = (Group) iterator.next();
            mailingGroupsList.getSelectionModel().select(group);
        }
    }

    public void loadListViewContent() {
        ObservableList<Group> groupsList = FXCollections.observableArrayList(Datasource.getInstance().queryGroups());
        mailingGroupsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mailingGroupsList.itemsProperty().setValue(groupsList);

        convertListViewDisplay();
    }

    private void convertListViewDisplay() {
        mailingGroupsList.setCellFactory(groupListView -> new ListCell<Group>() {
            @Override
            protected void updateItem(Group item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
    }

    public void loadComboBoxContent() {
        ObservableList<Department> groupsList = FXCollections.observableArrayList(Datasource.getInstance().queryDepartments());
        departmentSelector.itemsProperty().setValue(groupsList);
        departmentSelector.getSelectionModel().select(0);

        convertComboDisplayList();
    }

    private void convertComboDisplayList() {
        departmentSelector.setConverter(new StringConverter<Department>() {
            @Override
            public String toString(Department dept) {
                if (dept == null) {
                    return null;
                }
                return dept.getName();
            }

            @Override
            public Department fromString(String string) {
                return departmentSelector.getItems().stream().filter(gr -> gr.getName().equals(string)).findFirst().orElse(null);
            }
        });
    }

    public void insertRecord() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        int departmentId = departmentSelector.getValue().getId();

        List<Group> selectedGroups = mailingGroupsList.getSelectionModel().getSelectedItems();

        Datasource.getInstance().insertEmployee(firstName, lastName, departmentId, selectedGroups);
    }

    public void updateRecord(Employee employee) {
        String newFirstName = firstNameField.getText().trim();
        String newLastName = lastNameField.getText().trim();
        String oldFirstName = employee.getFirstName();
        String oldLastName = employee.getLastName();

        if ( (oldFirstName.equals(newFirstName)) && (oldLastName.equals(newLastName)) ) {
            System.out.println("First name and last name did not change");
        } else {
            System.out.println("Name has changed");
        }

        int employeeId = employee.getId();

        Department selectedDepartment = departmentSelector.getValue();
        int departmentId = selectedDepartment.getId();

        List<Group> selectedGroups = mailingGroupsList.getSelectionModel().getSelectedItems();

        Datasource.getInstance().updateEmployee(newFirstName, newLastName, employeeId, departmentId, selectedGroups);
    }
}