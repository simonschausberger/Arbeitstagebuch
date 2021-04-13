package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class MainController {
    private static final int ID = 1;
    private static final int DATE = 2;
    private static final int COMPANY = 3;
    private static final int START = 4;
    private static final int END = 5;
    private static final int DURATION = 6;
    private static final int INTERRUPTION = 7;
    private static final int CLIENTS = 8;
    private static final int AMOUNT = 9;

    public TabPane tabPane;
    public Button btnEntry;
    public Button btnDelete;
    public TableView tvUebersicht;
    public TableView tvCA;
    public TableColumn col_ue_date;
    public TableColumn col_ue_company;
    public TableColumn col_ue_duration;
    public TableColumn col_ue_amount;
    public TableColumn col_ca_date;
    public TableColumn col_ca_begin;
    public TableColumn col_ca_end;
    public TableColumn col_ca_duration;
    public TableColumn col_ca_interruption;
    public TableColumn col_ca_amount;
    public TableColumn col_lq_date;
    public TableColumn col_lq_clients;
    public TableColumn col_lq_begin;
    public TableColumn col_lq_end;
    public TableColumn col_lq_duration;
    public TableColumn col_lq_amount;
    public TableColumn col_sh_date;
    public TableColumn col_sh_clients;
    public TableColumn col_sh_begin;
    public TableColumn col_sh_end;
    public TableColumn col_sh_duration;
    public TableColumn col_sh_amount;

    List<EntryUebersicht> listUebersicht = new ArrayList<>();
    List<EntryCA> listCA = new ArrayList<>();

    public void initialize() {
        col_ue_date.setCellValueFactory(new PropertyValueFactory<EntryUebersicht, String>("date"));
        col_ue_company.setCellValueFactory(new PropertyValueFactory<EntryUebersicht, String>("company"));
        col_ue_duration.setCellValueFactory(new PropertyValueFactory<EntryUebersicht, String>("duration"));
        col_ue_amount.setCellValueFactory(new PropertyValueFactory<EntryUebersicht, Float>("amount"));

        col_ca_date.setCellValueFactory(new PropertyValueFactory<EntryCA, String>("date"));
        col_ca_begin.setCellValueFactory(new PropertyValueFactory<EntryCA, String>("begin"));
        col_ca_end.setCellValueFactory(new PropertyValueFactory<EntryCA, String>("end"));
        col_ca_duration.setCellValueFactory(new PropertyValueFactory<EntryCA, String>("duration"));
        col_ca_interruption.setCellValueFactory(new PropertyValueFactory<EntryCA, String>("interruption"));
        col_ca_amount.setCellValueFactory(new PropertyValueFactory<EntryCA, String>("amount"));

        DeactivateBtnDelete();
        DeactivateBtnEntry();
        ReadDataBase();
    }

    public Connection GetDatabaseConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "losenstein8");
        Connection conn = DriverManager.getConnection(url, props);
        return conn;
    }

    public void ReadDataBase() {
        listUebersicht.clear();
        listCA.clear();

        try {
            Connection connection = GetDatabaseConnection();
            PreparedStatement pst = connection.prepareStatement("SELECT * from eintrag");
            ResultSet result = pst.executeQuery();

            while (result.next()) {
                Date oldDate = new SimpleDateFormat("yyyy-MM-dd").parse(result.getArray(DATE).toString());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String date = dateFormat.format(oldDate);

                if (result.getArray(COMPANY).toString().equals("C&A")) {
                    EntryCA entryCA = new EntryCA(result.getArray(ID).toString(), date, result.getArray(COMPANY).toString(), result.getArray(START).toString(), result.getArray(END).toString(), result.getArray(DURATION).toString(), Float.parseFloat(result.getArray(AMOUNT).toString()), result.getArray(INTERRUPTION).toString());
                    listUebersicht.add(entryCA);
                    listCA.add(entryCA);
                }
            }
            ObservableList<EntryUebersicht> observableListUebersicht = FXCollections.observableList(listUebersicht);
            ObservableList<EntryCA> observableListCA = FXCollections.observableList(listCA);
            tvUebersicht.setItems(observableListUebersicht);
            tvCA.setItems(observableListCA);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void ActivateBtnEntry() {
        btnEntry.setDisable(false);
    }

    public void DeactivateBtnEntry() {
        btnEntry.setDisable(true);
    }

    public void ActivateBtnDelete() {
        btnDelete.setDisable(false);
    }

    public void DeactivateBtnDelete() {
        btnDelete.setDisable(true);
    }


    public void NewEntry(ActionEvent actionEvent) {

        switch (tabPane.getSelectionModel().getSelectedIndex()) {
            case 1:
                ControllerDialog controllerDialog = new ControllerDialog(this, new FXMLLoader(getClass().getResource("entry.fxml")), "C&A");
                controllerDialog.showStage();
        }
    }


    public void DeleteEntry(ActionEvent actionEvent) {
        String id = "";


        if (tabPane.getSelectionModel().getSelectedIndex() == 1) {
            id = listCA.get(tvCA.getSelectionModel().getFocusedIndex()).getId();
        } else if (tabPane.getSelectionModel().getSelectedIndex() == 2) {

        }

        try {
            Connection connection = GetDatabaseConnection();
            Statement statement = connection.createStatement();

            statement.executeUpdate("DELETE FROM eintrag WHERE id = \'" + id + "\'");
            System.out.println(listCA.get(tvCA.getSelectionModel().getFocusedIndex()).getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ReadDataBase();

    }


    public void MouseClickedCA(MouseEvent mouseEvent) {
        if (!(tvCA.getSelectionModel().getSelectedCells().isEmpty())) {
            ActivateBtnDelete();
        }
    }

    public void SecondaryTabSelected(Event event) {
        DeactivateBtnDelete();
        ActivateBtnEntry();
    }

    public void MainTabSelected(Event event) {
        DeactivateBtnDelete();
        DeactivateBtnEntry();
    }
}
