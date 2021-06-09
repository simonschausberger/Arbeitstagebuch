package sample;

import javafx.scene.control.Alert;

import javax.swing.*;

public class EntryException extends Exception {
    public EntryException(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Oooops Leli, da hast was falsch gmacht!");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
