package com.marcinpisarski.databasegui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.marcinpisarski.databasegui.model.Datasource;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/mainWindow.fxml"));
        Parent root = loader.load();

        //Controller controller = loader.getController();
        //controller.handleClickEmployeesTable();

        primaryStage.setTitle("Database GUI");
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("view/style.css").toExternalForm());
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        Datasource.getInstance().open();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}