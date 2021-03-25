package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller_CA {
    FXMLLoader fxmlLoader;
    private final MainController mainController;
    private Stage thisStage;

    public Controller_CA(MainController mainController, FXMLLoader fxmlLoader, String title) {
        this.mainController = mainController;
        thisStage = new Stage();

        try {
            this.fxmlLoader = fxmlLoader;

            // Set this class as the controller
            this.fxmlLoader.setController(this);

            thisStage.setScene(new Scene(this.fxmlLoader.load(), 330, 150));
            thisStage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage(){
        thisStage.showAndWait();
    }

}
