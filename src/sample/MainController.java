package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import sun.awt.SunHints;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


public class MainController {
    public TabPane tabPane;
    public Button btnEntry;
    public TableView tvUebersicht;
    public TableView tvCA;

    public void initialize(){
        Deactivate_btnEntry();

        ReadDataBase();

    }

    public void ReadDataBase() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "losenstein8");

        try {
            Connection conn = DriverManager.getConnection(url, props);
            PreparedStatement pst = conn.prepareStatement("SELECT * from eintrag");
            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                Date oldDate = new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getArray(1).toString());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String date = dateFormat.format(oldDate);

                System.out.println(date);
                System.out.println(resultSet.getArray(2));
                System.out.println(resultSet.getArray(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
