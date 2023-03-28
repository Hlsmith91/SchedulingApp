/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith.View;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalDateTimeStringConverter;
import schedulingapp_heathersmith.Model.Appointment;
import schedulingapp_heathersmith.Model.User;

/**
 * FXML Controller class
 *
 * @author hlsmi
 */
public class MainScreenController implements Initializable {

    @FXML
    private Button newAppointment;
    @FXML
    private Button editAppointment;
    @FXML
    private Button deleteAppointment;
    @FXML
    private Button newCustomer;
    @FXML
    private Button editCustomer;
    @FXML
    private RadioButton allAppointments;
    @FXML
    private RadioButton weekAppointments;
    @FXML
    private RadioButton monthAppointments;
    @FXML
    private TableView appointmentTable;
    @FXML
    private TableColumn startCol;
    @FXML
    private TableColumn endCol;
    @FXML
    private TableColumn titleCol;
    @FXML
    private TableColumn custCol;
    @FXML
    private TableColumn typeCol;
    @FXML
    private TableColumn descriptionCol;
    @FXML
    private TableColumn locationCol;
    @FXML
    private ToggleGroup optionToggleGroup;
    @FXML
    private MenuItem appRptItem;
    @FXML
    private MenuItem consultantRptItem;
    @FXML
    private MenuItem appTypeFreqRptItem;

    User currentUser;
    Appointment appointment;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        optionToggleGroup = new ToggleGroup();
        this.allAppointments.setToggleGroup(optionToggleGroup);
        this.weekAppointments.setToggleGroup(optionToggleGroup);
        this.monthAppointments.setToggleGroup(optionToggleGroup);

        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        startCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateTimeStringConverter(dtf, dtf)));
        endCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        endCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateTimeStringConverter(dtf, dtf)));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        custCol.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
    }

    public void newAppButtonClicked() {

    }

    public void editAppButtonClicked() {

    }

    public void newCustButtonClicked() {

    }

    public void editCustButtonClicked() {

    }

    public void setAppointmentTable(ObservableList appList) {

        FilteredList<Appointment> filteredList = new FilteredList<>(appList);

        optionToggleGroup.selectedToggleProperty().addListener((observable, oldVal,
                newVal) -> {
            filteredList.setPredicate(app -> {

                int currDate = LocalDate.now().getMonthValue();
                int currYear = LocalDate.now().getYear();
                int appYear = app.getStart().getYear();
                int appMonth = app.getStart().getMonthValue();
                LocalDate startWeekNumb = LocalDate.now().with(DayOfWeek.MONDAY);
                LocalDate endWeekNumb = LocalDate.now().with(DayOfWeek.FRIDAY);
                LocalDate appWeek = app.getStart().toLocalDate();

                if (monthAppointments.isSelected()) {
                    if (currDate == appMonth) {
                        return true;
                    }
                    return false;
                } else if (weekAppointments.isSelected()) {
                    if (currYear == appYear && currDate == appMonth
                            && (appWeek.isEqual(startWeekNumb) || appWeek.isEqual(endWeekNumb))
                            || (appWeek.isAfter(startWeekNumb) && appWeek.isBefore(endWeekNumb))) {
                        return true;
                    }
                    return false;
                }
                return true;
            });
        });

        Property<FilteredList<Appointment>> propList = new SimpleObjectProperty<>(filteredList);
        appointmentTable.itemsProperty().bind(propList);

    }

    public TableView getAppointmentTable() {
        return appointmentTable;
    }

    public Button getNewAppButton() {
        return newAppointment;
    }

    public Button getEditAppButton() {
        return editAppointment;
    }

    public void setUser(User user) {
        currentUser = user;
    }

    public Button getAddCustomerButton() {
        return newCustomer;
    }

    public Button getEditCustButton() {
        return editCustomer;
    }

    public Appointment getSelectedApp() {
        Appointment selectedApp = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();

        return selectedApp;
    }

    public void deleteButtonPushed() {

    }

    public Button getDeleteButton() {
        return deleteAppointment;
    }

    public MenuItem getAppRptItem() {
        return appRptItem;
    }

    public MenuItem getConsultRptItem() {
        return consultantRptItem;
    }

    public MenuItem getAppTypeFreqRptItem() {
        return appTypeFreqRptItem;
    }

    public RadioButton getMonthAppointments() {
        return monthAppointments;
    }
    
}
