package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public TableColumn col_ue_amount;
    public TableColumn col_ca_date;
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
        col_ue_date.setCellValueFactory(new PropertyValueFactory<EntryUebersicht, Date>("date"));
        col_ue_date.setCellFactory(column -> {
            TableCell<EntryUebersicht, Date> cell = new TableCell<EntryUebersicht, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(format.format(item));
                    }
                }
            };
            return cell;
        });
        col_ue_company.setCellValueFactory(new PropertyValueFactory<EntryUebersicht, String>("company"));
        col_ue_amount.setCellValueFactory(new PropertyValueFactory<EntryUebersicht, Double>("amount"));
        col_ue_amount.setCellFactory(column -> {
            TableCell<EntryUebersicht, Double> cell = new TableCell<EntryUebersicht, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if(empty) {
                        setText(null);
                    }
                    else {
                        NumberFormat df = DecimalFormat.getInstance();
                        df.setMinimumFractionDigits(2);
                        df.setRoundingMode(RoundingMode.DOWN);
                        setText(df.format(Double.parseDouble(item.toString()))+"€");
                    }
                }
            };
            return cell;
        });
        col_ca_date.setCellValueFactory(new PropertyValueFactory<EntryCA, Date>("date"));
        col_ca_date.setCellFactory(column -> {
            TableCell<EntryUebersicht, Date> cell = new TableCell<EntryUebersicht, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(format.format(item));
                    }
                }
            };
            return cell;
        });
        col_ca_amount.setCellValueFactory(new PropertyValueFactory<EntryCA, String>("amount"));
        col_ca_amount.setCellFactory(column -> {
            TableCell<EntryUebersicht, Double> cell = new TableCell<EntryUebersicht, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if(empty) {
                        setText(null);
                    }
                    else {
                        NumberFormat df = DecimalFormat.getInstance();
                        df.setMinimumFractionDigits(2);
                        df.setRoundingMode(RoundingMode.DOWN);
                        setText(df.format(Double.parseDouble(item.toString()))+"€");
                    }
                }
            };
            return cell;
        });
        col_lq_date.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, Date>("date"));
        col_lq_date.setCellFactory(column -> {
            TableCell<EntryUebersicht, Date> cell = new TableCell<EntryUebersicht, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(format.format(item));
                    }
                }
            };
            return cell;
        });
        col_lq_begin.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("begin"));
        col_lq_end.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("end"));
        col_lq_duration.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("duration"));
        col_lq_clients.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("clients"));
        col_lq_amount.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("amount"));
        col_lq_amount.setCellFactory(column -> {
            TableCell<EntryUebersicht, Double> cell = new TableCell<EntryUebersicht, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if(empty) {
                        setText(null);
                    }
                    else {
                        NumberFormat df = DecimalFormat.getInstance();
                        df.setMinimumFractionDigits(2);
                        df.setRoundingMode(RoundingMode.DOWN);
                        setText(df.format(Double.parseDouble(item.toString()))+"€");
                    }
                }
            };
            return cell;
        });

        col_sh_date.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, Date>("date"));
        col_sh_date.setCellFactory(column -> {
            TableCell<EntryUebersicht, Date> cell = new TableCell<EntryUebersicht, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(format.format(item));
                    }
                }
            };
            return cell;
        });
        col_sh_begin.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("begin"));
        col_sh_end.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("end"));
        col_sh_duration.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("duration"));
        col_sh_clients.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("clients"));
        col_sh_amount.setCellValueFactory(new PropertyValueFactory<EntryNachhilfe, String>("amount"));
        col_sh_amount.setCellFactory(column -> {
            TableCell<EntryUebersicht, Double> cell = new TableCell<EntryUebersicht, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if(empty) {
                        setText(null);
                    }
                    else {
                        NumberFormat df = DecimalFormat.getInstance();
                        df.setMinimumFractionDigits(2);
                        df.setRoundingMode(RoundingMode.DOWN);
                        setText(df.format(Double.parseDouble(item.toString())) +"€");
                    }
                }
            };
            return cell;
        });
    }

    public Connection GetDatabaseConnection() throws SQLException {
        String url = "jdbc:postgresql://castor.db.elephantsql.com:5432/pkcucibq";
        Properties props = new Properties();
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        props.setProperty("user", "pkcucibq");
        props.setProperty("password", "DNmkKgt8vRKKdRNFdBGKlj2-2XLwmYon");
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
                String string = dateFormat.format(oldDate);
                System.out.println(string);
                Date date = dateFormat.parse(string);
                System.out.println(date);

                if (result.getArray(COMPANY).toString().equals(NAME_CA)) {
                    EntryCA entryCA = new EntryCA(result.getArray(ID).toString(), date, result.getArray(COMPANY).toString(), Double.valueOf(result.getArray(AMOUNT).toString()));
                    listUebersicht.add(entryCA);
                    listCA.add(entryCA);
                } else if (result.getArray(COMPANY).toString().equals(NAME_LQ)) {
                    System.out.println(result.getArray(END).toString());
                    EntryNachhilfe entryNachhilfe = new EntryNachhilfe(result.getArray(ID).toString(), date, result.getArray(COMPANY).toString(), Double.valueOf(result.getArray(AMOUNT).toString()), result.getArray(START).toString(), result.getArray(END).toString(), result.getArray(DURATION).toString(), result.getArray(CLIENTS).toString());
                    listUebersicht.add(entryNachhilfe);
                    listLQ.add(entryNachhilfe);
                } else if (result.getArray(COMPANY).toString().equals(NAME_SH)) {
                    EntryNachhilfe entryNachhilfe = new EntryNachhilfe(result.getArray(ID).toString(), date, result.getArray(COMPANY).toString(), Double.valueOf(result.getArray(AMOUNT).toString()), result.getArray(START).toString(), result.getArray(END).toString(), result.getArray(DURATION).toString(), result.getArray(CLIENTS).toString());
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
        col_ue_date.setSortType(TableColumn.SortType.DESCENDING);
        tvUebersicht.getSortOrder().add(col_ue_date);
        tvUebersicht.sort();
        col_ca_date.setSortType(TableColumn.SortType.DESCENDING);
        tvCA.getSortOrder().add(col_ca_date);
        tvCA.sort();
        col_lq_date.setSortType(TableColumn.SortType.DESCENDING);
        tvLQ.getSortOrder().add(col_lq_date);
        tvLQ.sort();
        col_sh_date.setSortType(TableColumn.SortType.DESCENDING);
        tvSH.getSortOrder().add(col_sh_date);
        tvSH.sort();
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
        DeactivateBtnDelete();
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

        double sum = 0;
        int selectedTab = tabPane.getSelectionModel().getSelectedIndex();

        switch (selectedTab) {
            case 0:
                for (EntryUebersicht entry : listUebersicht) {
                    sum += entry.amount;
                }
                break;

            case 1:
                for (EntryCA entry : listCA) {
                    sum += entry.amount;
                }
                break;

            case 2:
                for (EntryNachhilfe entry : listLQ) {
                    sum += entry.amount;
                }
                break;

            case 3:
                for (EntryNachhilfe entry : listSH) {
                    sum += entry.amount;
                }
                break;
        }
            NumberFormat df = DecimalFormat.getInstance();
            df.setMinimumFractionDigits(2);
            df.setRoundingMode(RoundingMode.DOWN);
            tfSum.setText("Summe: " +df.format(sum) +"€");
    }

    public void ExportCSV(ActionEvent actionEvent) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        OutputStream os = null;
        PrintWriter printWriter = null;
        String delim = ";";
        String newline = "\n";
        String dir = "./Excel/";
        String tab = "";
        Files.createDirectories(Paths.get("./Excel/"));
        switch (tabPane.getSelectionModel().getSelectedIndex()){
            case 0:
                tab = "Uebersicht";
                os = new FileOutputStream(dir + tab + "_" + date + ".csv");
                printWriter = new PrintWriter(new OutputStreamWriter(os, "Cp1250"));

                printWriter.append(col_ue_date.getText() +delim +col_ue_company.getText() +delim +col_ue_amount.getText() +newline);
                for (EntryUebersicht entry : listUebersicht){
                    printWriter.append(entry.getDate() +delim +entry.getCompany() +delim +entry.getAmount() +newline);
                }
                break;

            case 1:
                tab = "CundA";
                os = new FileOutputStream(dir + tab + "_" + date + ".csv");
                printWriter = new PrintWriter(new OutputStreamWriter(os,"Cp1250"));
                printWriter.append(col_ca_date.getText() +delim +col_ca_amount.getText() +newline);
                for (EntryCA entry : listCA){
                    printWriter.append(entry.getDate() +delim +entry.getAmount() +newline);
                }
                break;

            case 2:
                tab = "LernQuadrat";
                os = new FileOutputStream(dir + tab + "_" + date + ".csv");
                printWriter = new PrintWriter(new OutputStreamWriter(os, "Cp1250"));
                printWriter.append(col_lq_date.getText() +delim +col_lq_clients.getText() +delim +col_lq_begin.getText() +delim +col_lq_end.getText() + delim +col_lq_duration.getText() +delim +col_lq_amount.getText() +newline);
                for (EntryNachhilfe entry : listLQ){
                    printWriter.append(entry.getDate() +delim +entry.getClients() +delim +entry.getBegin() +delim +entry.getEnd() +delim +entry.getDuration() + delim +entry.getAmount() +newline);
                }
                break;
                
            case 3:
                tab = "Schuelerhilfe";
                os = new FileOutputStream(dir + tab + "_" + date + ".csv");
                printWriter = new PrintWriter(new OutputStreamWriter(os, "Cp1250"));
                printWriter.append(col_sh_date.getText() +delim +col_sh_clients.getText() +delim +col_sh_begin.getText() +delim +col_sh_end.getText() + delim +col_sh_duration.getText() +delim +col_sh_amount.getText() +newline);
                for (EntryNachhilfe entry : listLQ){
                    printWriter.append(entry.getDate() +delim +entry.getClients() +delim +entry.getBegin() +delim +entry.getEnd() +delim +entry.getDuration() + delim +entry.getAmount() +newline);
                }
                break;
        }

        printWriter.flush();
        printWriter.close();
    }
}

