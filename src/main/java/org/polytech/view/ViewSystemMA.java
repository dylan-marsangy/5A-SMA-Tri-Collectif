package org.polytech.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.polytech.SMAConstants;
import org.polytech.agent.Agent;
import org.polytech.environnement.Environnement;
import org.polytech.environnement.RandomEnvironnement;
import org.polytech.system.SystemMA;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class ViewSystemMA extends Application {

    private final String FILE_NAME = "/view_system.fxml";

    @Override
    public void init() throws Exception {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(FILE_NAME));

        primaryStage.setTitle("SMA Tri collectif");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}