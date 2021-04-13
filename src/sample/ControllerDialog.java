package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class ControllerDialog {
    public DatePicker date;
    public TextField timeFrom;
    public TextField timeTo;
    public TextField interruption;
    public TextField amount;
    public Button addEvent;
    FXMLLoader fxmlLoader;
    private final MainController mainController;
    private Stage thisStage;
    private String title;

    public ControllerDialog(MainController mainController, FXMLLoader fxmlLoader, String title) {
        this.mainController = mainController;
        this.title = title;
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

    public void initialize() {
        addEvent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Connection connection = mainController.GetDatabaseConnection();
                    Statement statement = connection.createStatement();
                    Random random = new Random();
                    int number = random.nextInt(9999);
                    String hash = Integer.toString(date.getValue().hashCode()) + Integer.toString(amount.hashCode()) + Integer.toString(number);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    Date starttime = simpleDateFormat.parse(timeFrom.getText());
                    Date endtime = simpleDateFormat.parse(timeTo.getText());
                    long difference = endtime.getTime() - starttime.getTime();
                    String duration = String.format("%02d:%02d", (difference / (60 * 60 * 1000) % 24), (difference / (60 * 1000) % 60));


                    String values = "\'" + hash + "\'" + "," + "\'" + date.getValue() + "\'" + "," + "\'" + title + "\'" + "," + "\'" + timeFrom.getText() + "\'" + "," + "\'" + timeTo.getText() + "\'" + "," + "\'" + duration + "\'" + "," + "\'" + interruption.getText() + "\'" + "," + "\'" + amount.getText() + "\'";
                    statement.executeUpdate("INSERT INTO eintrag (id, datum, firma, beginn, ende, dauer, pause, betrag) VALUES(" + values + ")");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mainController.ReadDataBase();
                thisStage.close();
            }
        });
    }


    public void showStage() {
        thisStage.showAndWait();
    }

}
