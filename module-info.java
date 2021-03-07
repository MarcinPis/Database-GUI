module GuiForDatabase {

    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens com.marcinpisarski.databasegui;
    opens com.marcinpisarski.databasegui.model;
}