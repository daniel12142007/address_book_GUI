package com.databi;

import com.databi.ui.MainControlPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new MainControlPane(), 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}