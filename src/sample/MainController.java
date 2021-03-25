package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;

public class MainController {
    public TabPane tabPane;
    public Button btnEntry;
    public TableView tvUebersicht;
    public TableView tvCA;

    public void initialize(){
        Deactivate_btnEntry();
    }
    public void Activate_btnEntry(){
        btnEntry.setDisable(false);
    }
    public void Deactivate_btnEntry(){
        btnEntry.setDisable(true);
    }

    public void NewEntry(ActionEvent actionEvent) {

        switch (tabPane.getSelectionModel().getSelectedIndex()){
            case 1:
                Controller_CA controller_ca = new Controller_CA(this, new FXMLLoader(getClass().getResource("entry_ca.fxml")), "C&A");
                controller_ca.showStage();
        }
    }


}
