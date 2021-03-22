package com.marcinpisarski.databasegui;

import com.marcinpisarski.databasegui.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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
        loadGroupsListViewContent();
        loadDepartmentsComboBoxContent();
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

    public void updateEmployeeInitialize(EmployeeTableRow employeeTableRow) {
        firstNameField.setText(employeeTableRow.getFirstName());
        lastNameField.setText(employeeTableRow.getLastName());

        Department department = new Department();
        department.setId(employeeTableRow.getDepartmentId());
        department.setName(employeeTableRow.getDepartmentName());
        departmentSelector.setValue(department);

        List<Group> employeeGroups = employeeTableRow.getGroups();
        for (Group group : employeeGroups) {
            mailingGroupsList.getSelectionModel().select(group);
        }
    }

    public void loadGroupsListViewContent() {
        ObservableList<Group> groupsList = FXCollections.observableArrayList(Datasource.getInstance().queryGroups());
        mailingGroupsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mailingGroupsList.itemsProperty().setValue(groupsList);

        mailingGroupsList.setCellFactory(groupListView -> new ListCell<>() {
            @Override
            protected void updateItem(Group item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
    }

    public void loadDepartmentsComboBoxContent() {
        ObservableList<Department> departments = FXCollections.observableArrayList(Datasource.getInstance().queryDepartments());
        departmentSelector.itemsProperty().setValue(departments);
        departmentSelector.getSelectionModel().select(0);

        departmentSelector.setConverter(new StringConverter<>() {
            @Override
            public String toString(Department dept) {
                if (dept == null) {
                    return null;
                }
                return dept.getName();
            }

            @Override
            public Department fromString(String name) {
                return departmentSelector.getItems().stream()
                                                    .filter(d -> d.getName().equals(name))
                                                    .findFirst()
                                                    .orElse(null);
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

    public void updateRecord(EmployeeTableRow employeeTableRow) {
        Comparator<Group> groupsSorter = (g1, g2) -> g1.getId() - g2.getId();
        int employeeId = employeeTableRow.getEmployeeId();

        String newFirstName = firstNameField.getText().trim();
        String newLastName = lastNameField.getText().trim();
        int newDepartmentId = departmentSelector.getValue().getId();
        List<Group> newGroups = mailingGroupsList.getSelectionModel().getSelectedItems().stream()
                                                                                        .sorted(groupsSorter)
                                                                                        .collect(Collectors.toList());

        String oldFirstName = employeeTableRow.getFirstName();
        String oldLastName = employeeTableRow.getLastName();
        int oldDepartmentId = employeeTableRow.getDepartmentId();
        List<Group> oldGroups = employeeTableRow.getGroups().stream()
                                                            .sorted(groupsSorter)
                                                            .collect(Collectors.toList());

        boolean groupsEqual = newGroups.equals(oldGroups);

        if ( (oldFirstName.equals(newFirstName) || oldLastName.equals(newLastName) || (oldDepartmentId == newDepartmentId)) && !groupsEqual) {
            Datasource.getInstance().updateEmployee(newFirstName, newLastName, employeeId, newDepartmentId, newGroups);
            return;
        }

        if (!groupsEqual) {
            Datasource.getInstance().updateEmployee(employeeId, newGroups);
            return;
        }
        if ( !(oldFirstName.equals(newFirstName) && oldLastName.equals(newLastName) && (oldDepartmentId == newDepartmentId)) ) {
            Datasource.getInstance().updateEmployee(newFirstName, newLastName, employeeId, newDepartmentId);
            return;
        }
    }
}