/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith.View;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import schedulingapp_heathersmith.Model.Appointment;
import schedulingapp_heathersmith.Model.Customer;
import schedulingapp_heathersmith.Model.User;

/**
 * FXML Controller class
 *
 * @author hlsmi
 */
public class EditAppointmentController implements Initializable {

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private DatePicker meetingDate;
    @FXML
    private ComboBox<LocalTime> startTime;
    @FXML
    private ComboBox<LocalTime> endTime;
    @FXML
    private TextField titleTextBox;
    @FXML
    private TextArea descriptionTextBox;
    @FXML
    private TextField locationTextBox;
    @FXML
    private TextField contactTextBox;
    @FXML
    private TextField typeTextBox;
    @FXML
    private TextField urlTextBox;
    @FXML
    private Label validationLabel;
    @FXML
    private ComboBox<Customer> customerCombo;
    private int appointmentId = 0;
    User currentUser;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        populateTimeCombos();
        setDatePicker();
        setListeners();
    }

    public void setAppointment(Appointment selectedApp) {

        //Separates the end and start time stamps
        LocalDate mtDate = selectedApp.getStart().toLocalDate();
        LocalTime stTime = selectedApp.getStart().toLocalTime();
        LocalTime endTimes = selectedApp.getEnd().toLocalTime();
        customerCombo.setValue(fromString(selectedApp.getCustomerName()));
        titleTextBox.setText(selectedApp.getTitle());
        descriptionTextBox.setText(selectedApp.getDescription());
        locationTextBox.setText(selectedApp.getLocation());
        typeTextBox.setText(selectedApp.getType());
        urlTextBox.setText(selectedApp.getUrl());
        contactTextBox.setText(selectedApp.getContact());
        meetingDate.setValue(mtDate);
        startTime.setValue(stTime);
        endTime.setValue(endTimes);

    }

    public Appointment saveButtonClicked() {

        Appointment appointment = null;

        if (!isValid()) {
            return appointment;
        }
        int custId = customerCombo.getSelectionModel().getSelectedItem().getCustomerId();
        String title = titleTextBox.getText();
        String description = descriptionTextBox.getText();
        String type = typeTextBox.getText();
        String location = locationTextBox.getText();
        String contact = contactTextBox.getText();
        String custName = customerCombo.getSelectionModel().getSelectedItem().getCustomerName();
        String url = urlTextBox.getText();
        LocalDate date = meetingDate.getValue();
        LocalTime startTimes = startTime.getSelectionModel().getSelectedItem();
        LocalTime endTimes = endTime.getSelectionModel().getSelectedItem();
        LocalDateTime startDT = LocalDateTime.of(date, startTimes);
        LocalDateTime endDT = LocalDateTime.of(date, endTimes);

        appointment = new Appointment(currentUser.getUserId(), custId, title, description, type, url, location, custName, contact, startDT, endDT);
        appointment.setAppointmentId(appointmentId);

        return appointment;
    }

    public void cancelButtonClicked() {
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public boolean isValid() {
        if (titleTextBox.getText().trim().isEmpty()) {
            titleTextBox.setStyle("-fx-border-color: red;");
            validationLabel.setText("Please enter a title");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (customerCombo.getValue() == null) {
            customerCombo.setStyle("-fx-border-color: red;");
            validationLabel.setText("Please select a customer");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (typeTextBox.getText().trim().isEmpty()) {
            typeTextBox.setStyle("-fx-border-color: red;");
            validationLabel.setText("Please enter type of meeting");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (meetingDate.getValue() == null) {
            meetingDate.setStyle("-fx-border-color: red;");
            validationLabel.setText("Choose a meeting date");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (endTime.getValue() == null) {
            endTime.setStyle("-fx-border-color: red;");
            validationLabel.setText("Choose a end time");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (startTime.getValue() == null) {
            startTime.setStyle("-fx-border-color: red;");
            validationLabel.setText("Choose a start time");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (locationTextBox.getText().trim().isEmpty()) {
            locationTextBox.setStyle("-fx-border-color: red;");
            validationLabel.setText("Please enter a location");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (contactTextBox.getText().trim().isEmpty() || !contactTextBox.getText().matches("\\d{3}-\\d{4}")) {
            contactTextBox.setStyle("-fx-border-color: red;");
            validationLabel.setText("Please enter a contact number ex: 111-1111");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (descriptionTextBox.getText().trim().isEmpty()) {
            descriptionTextBox.setStyle("-fx-border-color: red;");
            validationLabel.setText("Please enter a description");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (urlTextBox.getText().trim().isEmpty()) {
            urlTextBox.setStyle("-fx-border-color: red;");
            validationLabel.setText("Please enter a url");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (endTime.getSelectionModel().getSelectedItem().isBefore(startTime.getSelectionModel().getSelectedItem())) {
            endTime.setStyle("-fx-border-color: red;");
            validationLabel.setText("End time goes past business hours");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (startTime.getSelectionModel().getSelectedItem().isAfter(endTime.getSelectionModel().getSelectedItem())) {
            startTime.setStyle("-fx-border-color: red;");
            validationLabel.setText("Start time cannot be after the end time");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        return true;
    }

    public void populateCustomerComboBox(ObservableList customer) {

        customerCombo.setItems(customer);
        customerCombo.setConverter(new StringConverter<Customer>() {

            @Override
            public String toString(Customer object) {
                return object.getCustomerName();
            }

            @Override
            public Customer fromString(String string) {
                return customerCombo.getItems().stream().filter(cust
                        -> cust.getCustomerName().equals(string)).findFirst().orElse(null);
            }
        });

    }

    public void populateTimeCombos() {
        ObservableList<LocalTime> startTimeList = FXCollections.observableArrayList();
        startTimeList.add(LocalTime.of(7, 0));
        startTimeList.add(LocalTime.of(7, 30));
        startTimeList.add(LocalTime.of(8, 0));
        startTimeList.add(LocalTime.of(8, 30));
        startTimeList.add(LocalTime.of(9, 0));
        startTimeList.add(LocalTime.of(9, 30));
        startTimeList.add(LocalTime.of(10, 0));
        startTimeList.add(LocalTime.of(10, 30));
        startTimeList.add(LocalTime.of(11, 0));
        startTimeList.add(LocalTime.of(11, 30));
        startTimeList.add(LocalTime.of(12, 0));
        startTimeList.add(LocalTime.of(12, 30));
        startTimeList.add(LocalTime.of(13, 0));
        startTimeList.add(LocalTime.of(13, 30));
        startTimeList.add(LocalTime.of(14, 0));
        startTimeList.add(LocalTime.of(14, 30));
        startTimeList.add(LocalTime.of(15, 0));
        startTimeList.add(LocalTime.of(15, 30));
        startTimeList.add(LocalTime.of(16, 00));
        startTimeList.add(LocalTime.of(16, 30));
        startTimeList.add(LocalTime.of(17, 0));
        startTimeList.add(LocalTime.of(17, 30));

        startTime.setItems(startTimeList);

        ObservableList<LocalTime> endTimeList = FXCollections.observableArrayList();
        endTimeList.add(LocalTime.of(7, 0));
        endTimeList.add(LocalTime.of(7, 30));
        endTimeList.add(LocalTime.of(8, 0));
        endTimeList.add(LocalTime.of(8, 30));
        endTimeList.add(LocalTime.of(9, 0));
        endTimeList.add(LocalTime.of(9, 30));
        endTimeList.add(LocalTime.of(10, 0));
        endTimeList.add(LocalTime.of(10, 30));
        endTimeList.add(LocalTime.of(11, 0));
        endTimeList.add(LocalTime.of(11, 30));
        endTimeList.add(LocalTime.of(12, 0));
        endTimeList.add(LocalTime.of(12, 30));
        endTimeList.add(LocalTime.of(13, 0));
        endTimeList.add(LocalTime.of(13, 30));
        endTimeList.add(LocalTime.of(14, 0));
        endTimeList.add(LocalTime.of(14, 30));
        endTimeList.add(LocalTime.of(15, 0));
        endTimeList.add(LocalTime.of(15, 30));
        endTimeList.add(LocalTime.of(16, 00));
        endTimeList.add(LocalTime.of(16, 30));
        endTimeList.add(LocalTime.of(17, 0));
        endTimeList.add(LocalTime.of(17, 30));
        endTimeList.add(LocalTime.of(18, 0));

        endTime.setItems(endTimeList);

    }

    public Customer fromString(String string) {
        return customerCombo.getItems().stream().filter(cust
                -> cust.getCustomerName().equals(string)).findFirst().orElse(null);
    }

    public void clearAppInfo() {
        titleTextBox.clear();
        descriptionTextBox.clear();
        locationTextBox.clear();
        customerCombo.setValue(null);
        typeTextBox.clear();
        urlTextBox.clear();
        contactTextBox.clear();
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void setAppointmentId(int appId) {
        appointmentId = appId;
    }

    public void setCustomerComboBox(ObservableList<Customer> customers) {
        customerCombo.setItems(customers);
        customerCombo.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer object) {
                return object.getCustomerName();
            }

            @Override
            public Customer fromString(String string) {
                return customerCombo.getItems().stream().filter(cust
                        -> cust.getCustomerName().equals(string)).findFirst().orElse(null);
            }
        });
    }
    
    public void setDatePicker() {
        meetingDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });
    }

    public void setListeners() {
        titleTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.trim().length() > 0) {
                titleTextBox.setStyle(null);
                validationLabel.setText("");
            }
        });

        descriptionTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.trim().length() > 0) {
                descriptionTextBox.setStyle(null);
                validationLabel.setText("");
            }
        });

        locationTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.trim().length() > 0) {
                locationTextBox.setStyle(null);
                validationLabel.setText("");
            }
        });

        typeTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.trim().length() > 0) {
                typeTextBox.setStyle(null);
                validationLabel.setText("");
            }
        });

        urlTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.trim().length() > 0) {
                urlTextBox.setStyle(null);
                validationLabel.setText("");
            }
        });

        contactTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.trim().matches("\\d{3}-\\d{4}")) {
                contactTextBox.setStyle(null);
                validationLabel.setText("");
            }
        });

        meetingDate.valueProperty().addListener((observable, oldval, newval) -> {
            if (newval != null) {
                meetingDate.setStyle(null);
                validationLabel.setText("");
            }
        });

        startTime.getSelectionModel().selectedItemProperty().addListener((observable, oldval, newval) -> {
            if (newval != null) {
                startTime.setStyle(null);
                validationLabel.setText("");
            }
        });

        endTime.getSelectionModel().selectedItemProperty().addListener((observable, oldval, newval) -> {
            if (newval != null) {
                endTime.setStyle(null);
                validationLabel.setText("");
            }
        });
    }

}
