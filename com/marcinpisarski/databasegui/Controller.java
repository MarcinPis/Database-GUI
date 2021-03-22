package com.marcinpisarski.databasegui;

import com.marcinpisarski.databasegui.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

public class Controller {

    @FXML
    private TableView<EmployeeTableRow> employeeTable;
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
        employeeTable.setItems(Datasource.getInstance().queryEmployees());
        employeeTable.requestFocus();
    }

    @FXML
    public void handleClickEmployeesTable() {
        employeeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EmployeeTableRow>() {
            @Override
            public void changed(ObservableValue<? extends EmployeeTableRow> observable, EmployeeTableRow oldValue, EmployeeTableRow newValue) {
                if (newValue != null) {
                    EmployeeTableRow employee = employeeTable.getSelectionModel().getSelectedItem();
                    groupsTable.setItems(employee.getGroups());
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
        final EmployeeTableRow employeeTableRow = employeeTable.getSelectionModel().getSelectedItem();
        if (employeeTableRow == null) {
            return;
        }

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
        controller.updateEmployeeInitialize(employeeTableRow);

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
            controller.updateRecord(employeeTableRow);

            listEmployees();
            employeeTable.getSelectionModel().select(employeeTableRow);
        }
    }

    @FXML
    public void deleteEmployee() {
        final EmployeeTableRow employeeTableRow = employeeTable.getSelectionModel().getSelectedItem();
        if (employeeTableRow == null) {
            return;
        }

        Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
        alert.setTitle("Delete Employee");
        alert.setHeaderText("Do you really want to delete employee: " + employeeTableRow.getFirstName() + " " + employeeTableRow.getLastName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {

            Datasource.getInstance().deleteEmployee(employeeTableRow.getEmployeeId());
            listEmployees();
            employeeTable.getSelectionModel().selectFirst();
        }
    }
}