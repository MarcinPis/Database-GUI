package com.marcinpisarski.databasegui;

import com.marcinpisarski.databasegui.model.Department;
import com.marcinpisarski.databasegui.model.Group;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import com.marcinpisarski.databasegui.model.Datasource;
import com.marcinpisarski.databasegui.model.Employee;

import java.util.Optional;

public class Controller {

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableView<Group> groupsTable;
    @FXML
    private BorderPane mainBorderPane;


    public void initialize() {
        listEmployees();
        handleClickEmployeesTable();
        employeeTable.getSelectionModel().selectFirst();
    }

    public void listEmployees() {
        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        employeeTable.getItems().setAll(Datasource.getInstance().queryEmployees());
        employeeTable.requestFocus();
    }

    @FXML
    public void handleClickEmployeesTable() {
        employeeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Employee>() {
            @Override
            public void changed(ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) {
                if (newValue != null) {
                    Employee employee = employeeTable.getSelectionModel().getSelectedItem();

                    Task<ObservableList<Group>> task = new Task<ObservableList<Group>>() {
                        @Override
                        protected ObservableList<Group> call() throws Exception {
                            return FXCollections.observableArrayList(
                                    Datasource.getInstance().queryEmployeesMailing(employee.getId()));
                        }
                    };
                    groupsTable.itemsProperty().bind(task.valueProperty());

                    new Thread(task).start();
                }
            }
        });
    }

    @FXML
    public void newEmployeeDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("New Employee");
        dialog.setHeaderText("Enter New Employee details");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("View/employeeDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        //dialog.getDialogPane().getStylesheets().add(getClass().getResource("view/style.css").toExternalForm());
        //dialog.getDialogPane().getStyleClass().add("style");
        DialogController controller = fxmlLoader.getController();

        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    if (!controller.isInputValid()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Fields: First Name and Last Name cannot be empty.");
                        alert.showAndWait();
                        event.consume();
                    }
                }
        );

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.insertRecord();

            listEmployees();
            employeeTable.getSelectionModel().selectLast();
        }
    }

    @FXML
    public void editEmployeeDialog() {

        final Employee employee = employeeTable.getSelectionModel().getSelectedItem();
        if (employee == null) {
            // Nothing was selected
            return;
        }

        Department department = new Department();
        department.setId(employee.getDepartmentId());
        department.setName(employee.getDepartmentName());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit Employee");
        dialog.setHeaderText("Edit Employee details");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("View/employeeDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        DialogController controller = fxmlLoader.getController();
        controller.dialogInitialize(employee, department);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    if (!controller.isInputValid()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Fields: First Name and Last Name cannot be empty.");
                        alert.showAndWait();
                        event.consume();
                    }
                }
        );

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.updateRecord(employee);

            listEmployees();
            employeeTable.getSelectionModel().select(employee);
        }
    }

    @FXML
    public void deleteEmployee() {

        final Employee employee = employeeTable.getSelectionModel().getSelectedItem();
        if (employee == null) {
            // Nothing was selected
            return;
        }

        Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
        alert.setTitle("Delete Employee");
        alert.setHeaderText("Do you really want to delete employee: " + employee.getFirstName() + " " + employee.getLastName() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            // Delete
            Datasource.getInstance().deleteEmployee(employee);

            listEmployees();
            employeeTable.getSelectionModel().selectFirst();
        }
    }
}