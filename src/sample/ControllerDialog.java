package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
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
    private long difference;
    private Scene scene;
    DecimalFormat format = new DecimalFormat("#0.00");

    public ControllerDialog(MainController mainController, FXMLLoader fxmlLoader, String title) {
        this.mainController = mainController;
        this.title = title;
        thisStage = new Stage();

        try {
            this.fxmlLoader = fxmlLoader;

            // Set this class as the controller
            this.fxmlLoader.setController(this);
            scene = new Scene(this.fxmlLoader.load(), 330, 150);
            thisStage.setScene(scene);
            thisStage.setTitle(title);
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (title.equals(mainController.NAME_LQ) || title.equals(mainController.NAME_SH)) {
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        AddNewEntryNachhilfe();
                    }
                }
            });

            addEventNachhilfe.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    AddNewEntryNachhilfe();
                }
            });
            timeFromNachhilfe.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                        if (!newPropertyValue)
                            TimeFieldAdaption(timeFromNachhilfe);
                    }
            );
            timeToNachhilfe.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                        if (!newPropertyValue)
                            TimeFieldAdaption(timeToNachhilfe);
                    }
            );
        } else {
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent ke) {
                    if (ke.getCode().equals(KeyCode.ENTER)) {
                        AddNewEntryCA();
                    }
                }
            });
            addEventCA.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                   AddNewEntryCA();
                }
            });
        }
    }

    public void AddNewEntryNachhilfe(){
        try {
            CheckFields();
            Random random = new Random();
            int number = random.nextInt(9999);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String hash = Integer.toString(dateNachhilfe.getValue().hashCode()) + Integer.toString(amountNachhilfe.hashCode()) + Integer.toString(number);
            Date starttime = simpleDateFormat.parse(timeFromNachhilfe.getText());
            Date endtime = simpleDateFormat.parse(timeToNachhilfe.getText());
            setDifference(endtime.getTime() - starttime.getTime());
            CheckDifference();
            Connection connection = mainController.GetDatabaseConnection();
            Statement statement = connection.createStatement();
            String duration = String.format("%02d:%02d", (getDifference() / (60 * 60 * 1000) % 24), (getDifference() / (60 * 1000) % 60));

            String sql = "INSERT INTO eintrag (id, datum, firma, beginn, ende, dauer, schueler, betrag) VALUES(\'" + hash + "\'" + "," + "\'" + dateNachhilfe.getValue() + "\'" + "," + "\'" + title + "\'" + "," + "\'" + timeFromNachhilfe.getText() + "\'" + "," + "\'" + timeToNachhilfe.getText() + "\'" + "," + "\'" + duration + "\'" + "," + "\'" + clientsNachhilfe.getText() + "\'" + "," + "\'" +amountNachhilfe.getText().replace(",", ".") + "\')";
            System.out.println(sql);
            statement.executeUpdate(sql);
            mainController.ReadDataBase();
            thisStage.close();
        } catch (SQLException e) {
            try {
                throw new EntryException("Überprüfe bitte deine Eingabe! SQL-Fehler");
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

    public void AddNewEntryCA(){
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
                throw new EntryException("Überprüfe bitte deine Eingabe! - SQL Fehler!");
            } catch (EntryException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void CheckFields() throws EntryException {
        if (title.equals(mainController.NAME_CA)) {
            if (dateCA.getValue() == null || amountCA.getText() == null || dateCA.getValue().toString().trim().isEmpty() || amountCA.getText().trim().isEmpty()) {
                throw new EntryException("Überprüfe bitte deine Eingabe!");
            }
        } else {
            TimeFieldAdaption(timeToNachhilfe);
            TimeFieldAdaption(timeFromNachhilfe);
            if (dateNachhilfe.getValue() == null || timeFromNachhilfe.getText() == null ||  timeToNachhilfe.getText() == null ||
                    clientsNachhilfe.getText() == null || amountNachhilfe.getText() == null ||dateNachhilfe.getValue().toString().trim().isEmpty() ||
                    timeFromNachhilfe.getText().trim().isEmpty() || timeToNachhilfe.getText().trim().isEmpty() || clientsNachhilfe.getText().trim().isEmpty() ||
                    amountNachhilfe.getText().trim().isEmpty() || !(CheckTimeFields(timeFromNachhilfe)) || !(CheckTimeFields(timeToNachhilfe))) {
                //TODO excel ausgabe
                throw new EntryException("Überprüfe bitte deine Eingabe!");
            }
        }
    }
    public boolean CheckTimeFields(TextField textField){
        boolean rv = true;
        if (!(textField.getText().matches("[0-9]{2}:[0-9]{2}")) || Integer.valueOf(textField.getText().substring(0,2)) > 24 ||
                Integer.valueOf(textField.getText().substring(3,5)) > 59){
            rv = false;
        }
        return rv;
    }

    public void TimeFieldAdaption(TextField textField){
        String text = textField.getText();
        if (textField.getText().matches("[0-9]{4}")){
            textField.setText(text.substring(0,2) +":" +text.substring(2,4));
        }
    }

    public void CheckDifference() throws EntryException {
        if (getDifference() <= 0){
            throw new EntryException("Überprüfe bitte deine Eingabe - Zeiten passen nicht!");
        }
    }

    public long getDifference() {
        return difference;
    }

    public void setDifference(long difference) {
        this.difference = difference;
    }

    public void showStage() {
        thisStage.showAndWait();
    }


}
