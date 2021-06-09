package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
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
    private static final int CLIENTS = 7;
    private static final int AMOUNT = 8;
    public static final String NAME_CA = "C&A";
    public static final String NAME_LQ = "LernQuadrat";
    public static final String NAME_SH = "Schülerhilfe";

    public TabPane tabPane;
    public Button btnEntry;
    public Button btnDelete;
    public TableView tvUebersicht;
    public TableView tvCA;
    public TableView tvLQ;
    public TableView tvSH;
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
    public TextField tfSum;


    List<EntryUebersicht> listUebersicht = new ArrayList<>();
    List<EntryCA> listCA = new ArrayList<>();
    List<EntryNachhilfe> listLQ = new ArrayList<>();
    List<EntryNachhilfe> listSH = new ArrayList<>();

    public void initialize() {
        SetColumns();
        DeactivateBtnDelete();
        DeactivateBtnEntry();
        ReadDataBase();
        tfSum.setEditable(false);
        SetSum();
    }

    public void SetColumns() {
        col_ue_date.setCellValueFactory(new PropertyValueFactory<EntryUebersicht, String>("date"));
        col_ue_company.setCellValueFactory(new PropertyValueFactory<EntryUebersicht, String>("company"));
        col_ue_amount.setCellValueFactory(new PropertyValueFactory<EntryUebersicht, Float>("amount"));

        col_ca_date.setCellValueFactory(new PropertyValueFactory<EntryCA, String>("date"));
        col_ca_amount.setCellValueFactory(new PropertyValueFactory<EntryCA, String>("amount"));

        col_lq_date.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("date"));
        col_lq_begin.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("begin"));
        col_lq_end.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("end"));
        col_lq_duration.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("duration"));
        col_lq_clients.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("clients"));
        col_lq_amount.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("amount"));

        col_sh_date.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("date"));
        col_sh_begin.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("begin"));
        col_sh_end.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("end"));
        col_sh_duration.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("duration"));
        col_sh_clients.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("clients"));
        col_sh_amount.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("amount"));
    }

    public Connection GetDatabaseConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "losenstein8");
        Connection conn = DriverManager.getConnection(url, props);
        return conn;
    }

    public void ClearLists() {
        listUebersicht.clear();
        listCA.clear();
        listLQ.clear();
        listSH.clear();
    }

    public void ReadDataBase() {
        ClearLists();
        try {
            Connection connection = GetDatabaseConnection();
            PreparedStatement pst = connection.prepareStatement("SELECT * from eintrag");
            ResultSet result = pst.executeQuery();

            while (result.next()) {
                Date oldDate = new SimpleDateFormat("yyyy-MM-dd").parse(result.getArray(DATE).toString());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String date = dateFormat.format(oldDate);

                if (result.getArray(COMPANY).toString().equals(NAME_CA)) {
                    EntryCA entryCA = new EntryCA(result.getArray(ID).toString(), date, result.getArray(COMPANY).toString(), Float.valueOf(result.getArray(AMOUNT).toString()));
                    listUebersicht.add(entryCA);
                    listCA.add(entryCA);
                } else if (result.getArray(COMPANY).toString().equals(NAME_LQ)) {
                    System.out.println(result.getArray(END).toString());
                    EntryNachhilfe entryNachhilfe = new EntryNachhilfe(result.getArray(ID).toString(), date, result.getArray(COMPANY).toString(), Float.valueOf(result.getArray(AMOUNT).toString()), result.getArray(START).toString(), result.getArray(END).toString(), result.getArray(DURATION).toString(), result.getArray(CLIENTS).toString());
                    listUebersicht.add(entryNachhilfe);
                    listLQ.add(entryNachhilfe);
                } else if (result.getArray(COMPANY).toString().equals(NAME_SH)) {
                    EntryNachhilfe entryNachhilfe = new EntryNachhilfe(result.getArray(ID).toString(), date, result.getArray(COMPANY).toString(), Float.valueOf(result.getArray(AMOUNT).toString()), result.getArray(START).toString(), result.getArray(END).toString(), result.getArray(DURATION).toString(), result.getArray(CLIENTS).toString());
                    listUebersicht.add(entryNachhilfe);
                    listSH.add(entryNachhilfe);
                }
            }
            ObservableList<EntryUebersicht> observableListUebersicht = FXCollections.observableList(listUebersicht);
            ObservableList<EntryCA> observableListCA = FXCollections.observableList(listCA);
            ObservableList<EntryNachhilfe> observableListLQ = FXCollections.observableList(listLQ);
            ObservableList<EntryNachhilfe> observableListSH = FXCollections.observableList(listSH);
            tvUebersicht.setItems(observableListUebersicht);
            tvCA.setItems(observableListCA);
            tvLQ.setItems(observableListLQ);
            tvSH.setItems(observableListSH);
            SetSum();
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
        ControllerDialog controllerDialog = null;
        switch (tabPane.getSelectionModel().getSelectedIndex()) {
            case 1:
                controllerDialog = new ControllerDialog(this, new FXMLLoader(getClass().getResource("entry_ca.fxml")), NAME_CA);
                break;
            case 2:
                controllerDialog = new ControllerDialog(this, new FXMLLoader(getClass().getResource("entry_nachhilfe.fxml")), NAME_LQ);
                break;
            case 3:
                controllerDialog = new ControllerDialog(this, new FXMLLoader(getClass().getResource("entry_nachhilfe.fxml")), NAME_SH);
                break;
        }
        controllerDialog.showStage();
    }


    public void DeleteEntry(ActionEvent actionEvent) {
        String id = "";

        if (tabPane.getSelectionModel().getSelectedIndex() == 1) {
            id = listCA.get(tvCA.getSelectionModel().getFocusedIndex()).getId();
        } else if (tabPane.getSelectionModel().getSelectedIndex() == 2) {
            id = listLQ.get(tvLQ.getSelectionModel().getFocusedIndex()).getId();
        } else {
            id = listSH.get(tvSH.getSelectionModel().getFocusedIndex()).getId();
        }

        try {
            Connection connection = GetDatabaseConnection();
            Statement statement = connection.createStatement();

            statement.executeUpdate("DELETE FROM eintrag WHERE id = \'" + id + "\'");

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

    public void MouseClickedLQ(MouseEvent mouseEvent) {
        if (!(tvLQ.getSelectionModel().getSelectedCells().isEmpty())) {
            ActivateBtnDelete();
        }
    }

    public void MouseClickedSH(MouseEvent mouseEvent) {
        if (!(tvSH.getSelectionModel().getSelectedCells().isEmpty())) {
            ActivateBtnDelete();
        }
    }

    public void MainTabSelected(Event event) {
        DeactivateBtnDelete();
        DeactivateBtnEntry();
        SetSum();
    }

    public void CATabSelected(Event event) {
        DeactivateBtnDelete();
        ActivateBtnEntry();
        SetSum();
    }

    public void LQTabSelected(Event event) {
        DeactivateBtnDelete();
        ActivateBtnEntry();
        SetSum();
    }

    public void SHTabSelected(Event event) {
        DeactivateBtnDelete();
        ActivateBtnEntry();
        SetSum();
    }

    public void SetSum() {
        float sum = 0;
        int selectedTab = tabPane.getSelectionModel().getSelectedIndex();

        switch (selectedTab) {
            case 0:
                for (EntryUebersicht entry : listUebersicht) {
                    sum += Float.valueOf(entry.amount);
                }
                break;

            case 1:
                for (EntryCA entry : listCA) {
                    sum += Float.valueOf(entry.amount);
                }
                break;

            case 2:
                for (EntryNachhilfe entry : listLQ) {
                    sum += Float.valueOf(entry.amount);
                }
                break;

            case 3:
                for (EntryNachhilfe entry : listSH) {
                    sum += Float.valueOf(entry.amount);
                }
                break;
        }
            tfSum.setText("Summe: " +((((int)(sum*100))/100.00)) +"€");
    }
}

