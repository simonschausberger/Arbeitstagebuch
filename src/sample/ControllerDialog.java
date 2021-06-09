package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    public DatePicker dateNachhilfe;
    public TextField timeFromNachhilfe;
    public TextField timeToNachhilfe;
    public TextField clientsNachhilfe;
    public TextField amountNachhilfe;
    public Button addEventNachhilfe;
    public Button addEventCA;
    public DatePicker dateCA;
    public TextField amountCA;
    FXMLLoader fxmlLoader;
    private final MainController mainController;
    private Stage thisStage;
    private String title;
    private long difference = 0;

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
        if (title.equals(mainController.NAME_LQ) || title.equals(mainController.NAME_SH)) {
            addEventNachhilfe.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        CheckFields();
                        Connection connection = mainController.GetDatabaseConnection();
                        Statement statement = connection.createStatement();
                        Random random = new Random();
                        int number = random.nextInt(9999);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        String hash = Integer.toString(dateNachhilfe.getValue().hashCode()) + Integer.toString(amountNachhilfe.hashCode()) + Integer.toString(number);
                        Date starttime = simpleDateFormat.parse(timeFromNachhilfe.getText());
                        Date endtime = simpleDateFormat.parse(timeToNachhilfe.getText());
                        difference = endtime.getTime() - starttime.getTime();
                        String duration = String.format("%02d:%02d", (difference / (60 * 60 * 1000) % 24), (difference / (60 * 1000) % 60));

                        String sql = "INSERT INTO eintrag (id, datum, firma, beginn, ende, dauer, schueler, betrag) VALUES(\'" + hash + "\'" + "," + "\'" + dateNachhilfe.getValue() + "\'" + "," + "\'" + title + "\'" + "," + "\'" + timeFromNachhilfe.getText() + "\'" + "," + "\'" + timeToNachhilfe.getText() + "\'" + "," + "\'" + duration + "\'" + "," + "\'" + clientsNachhilfe.getText() + "\'" + "," + "\'" +amountNachhilfe.getText().replace(",", ".") + "\')";
                        System.out.println(sql);
                        statement.executeUpdate(sql);
                        mainController.ReadDataBase();
                        thisStage.close();
                    } catch (SQLException e) {
                        try {
                            throw new EntryException("Überprüfe bitte deine Eingabe!");
                        } catch (EntryException ex) {
                            ex.printStackTrace();
                        }
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (EntryException e) {
                        e.printStackTrace();
                    }
                }
            });
            timeFromNachhilfe.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                        if (!newPropertyValue)
                            TimeFieldAdaption(timeFromNachhilfe);
                        System.out.println("Textfield out focus");
                    }
            );
            timeToNachhilfe.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                        if (!newPropertyValue)
                            TimeFieldAdaption(timeToNachhilfe);
                        System.out.println("Textfield out focus");
                    }
            );
        } else {
            addEventCA.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        CheckFields();
                        Connection connection = mainController.GetDatabaseConnection();
                        Statement statement = connection.createStatement();
                        Random random = new Random();
                        int number = random.nextInt(9999);
                        String hash = dateCA.getValue().hashCode() + amountCA.hashCode() + Integer.toString(number);
                        String sql = "INSERT INTO eintrag (id, datum, firma, betrag) VALUES(\'" + hash + "\'" + "," + "\'" + dateCA.getValue() + "\'" + "," + "\'" + title + "\'" + "," + "\'" +amountCA.getText().replace(",", ".") + "\')";
                        System.out.println(sql);
                        statement.executeUpdate(sql);
                        mainController.ReadDataBase();
                        thisStage.close();
                    } catch (EntryException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        try {
                            throw new EntryException("Überprüfe bitte deine Eingabe!");
                        } catch (EntryException ex) {
                            ex.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void TimeFieldAdaption(TextField textField){
        String text = textField.getText();
        if (textField.getText().matches("[0-9]{4}")){
            textField.setText(text.substring(0,2) +":" +text.substring(2,4));
        }
    }

   /* public boolean CheckTimeFields(TextField textField){
        boolean rv = true;
        if (textField.getText().matches("[0-9]{2}:[0-9]{2}") || textField.getText().substring(0,2) > ){

        }
    }*/

    public void showStage() {
        thisStage.showAndWait();
    }

    public void CheckFields() throws EntryException {
        if (title.equals(mainController.NAME_CA)) {
            if (dateCA.getValue() == null || amountCA.getText() == null || dateCA.getValue().toString().trim().isEmpty() || amountCA.getText().trim().isEmpty()) {
                throw new EntryException("Überprüfe bitte deine Eingabe!");
            }
        } else {
            TimeFieldAdaption(timeToNachhilfe);
            TimeFieldAdaption(timeFromNachhilfe);
            System.out.println(difference);
            if (dateNachhilfe.getValue() == null || timeFromNachhilfe.getText() == null ||  timeToNachhilfe.getText() == null ||
                    clientsNachhilfe.getText() == null || amountNachhilfe.getText() == null ||dateNachhilfe.getValue().toString().trim().isEmpty() ||
                    timeFromNachhilfe.getText().trim().isEmpty() || timeToNachhilfe.getText().trim().isEmpty() || clientsNachhilfe.getText().trim().isEmpty() ||
                    amountNachhilfe.getText().trim().isEmpty() || difference < 0){
                //TODO differenz, zeiteingabe, excel ausgabe
                throw new EntryException("Überprüfe bitte deine Eingabe!");
            }
        }
    }
}
