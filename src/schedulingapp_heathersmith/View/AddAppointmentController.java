/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith.View;

import java.io.IOException;
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
import schedulingapp_heathersmith.Model.Scheduler;
import schedulingapp_heathersmith.Model.User;

/**
 * FXML Controller class
 *
 * @author hlsmi
 */
public class AddAppointmentController implements Initializable {

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private DatePicker meetingDate;
    @FXML
    private Label validationLabel;
    @FXML
    private ComboBox<LocalTime> startTimeCombo;
    @FXML
    private ComboBox<LocalTime> endTimeCombo;
    @FXML
    private ComboBox<Customer> customerCombo = new ComboBox<>();
    @FXML
    private TextField titleTextBox;
    @FXML
    private TextArea descriptionTextBox;
    @FXML
    private TextField locationTextBox;
    @FXML
    private TextField typeTextBox;
    @FXML
    private TextField urlTextBox;
    @FXML
    private TextField contactTextBox;
    Appointment appointment = new Appointment();
    Scheduler scheduler;
    User currentUser;
    Customer customer;
    int custId;
    ObservableList<Integer> customerId = FXCollections.observableArrayList();
    ObservableList<LocalTime> startTime = FXCollections.observableArrayList();
    ObservableList<LocalTime> endTime = FXCollections.observableArrayList();

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

    public Appointment saveButtonClicked(User user) throws IOException {
        if (!isValid()) return null;
        
        custId = customerCombo.getSelectionModel().getSelectedItem().getCustomerId();
        int userId = user.getUserId();
        String title = titleTextBox.getText();
        String description = descriptionTextBox.getText();
        String type = typeTextBox.getText();
        String location = locationTextBox.getText();
        String contact = contactTextBox.getText();
        String custName = customerCombo.getSelectionModel().getSelectedItem().getCustomerName();
        String url = urlTextBox.getText();
        LocalDate date = meetingDate.getValue();
        LocalTime startTime = startTimeCombo.getSelectionModel().getSelectedItem();
        LocalTime endTime = endTimeCombo.getSelectionModel().getSelectedItem();
        LocalDateTime startDT = LocalDateTime.of(date, startTime);
        LocalDateTime endDT = LocalDateTime.of(date, endTime);

        appointment = new Appointment(userId, custId, title, description, type, url, location, custName, contact, startDT, endDT);

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
        if (startTimeCombo.getValue() == null) {
            startTimeCombo.setStyle("-fx-border-color: red;");
            validationLabel.setText("Choose a start time");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (endTimeCombo.getValue() == null) {
            endTimeCombo.setStyle("-fx-border-color: red;");
            validationLabel.setText("Choose a end time");
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
        if (endTimeCombo.getSelectionModel().getSelectedItem().isBefore(startTimeCombo.getSelectionModel().getSelectedItem())) {
            endTimeCombo.setStyle("-fx-border-color: red;");
            validationLabel.setText("End time goes past business hours");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (startTimeCombo.getSelectionModel().getSelectedItem().isAfter(endTimeCombo.getSelectionModel().getSelectedItem())) {
            startTimeCombo.setStyle("-fx-border-color: red;");
            validationLabel.setText("Start time cannot be after the end time");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        if (endTimeCombo.getSelectionModel().getSelectedItem().equals((startTimeCombo.getSelectionModel().getSelectedItem()))) {
            endTimeCombo.setStyle("-fx-border-color: red;");
            validationLabel.setText("Start time and end time can not be the same.");
            validationLabel.setTextFill(Color.RED);
            return false;
        }
        return true;
    }

    public void populateTimeCombos() {
        startTime.add(LocalTime.of(7, 0));
        startTime.add(LocalTime.of(7, 30));
        startTime.add(LocalTime.of(8, 0));
        startTime.add(LocalTime.of(8, 30));
        startTime.add(LocalTime.of(9, 0));
        startTime.add(LocalTime.of(9, 30));
        startTime.add(LocalTime.of(10, 0));
        startTime.add(LocalTime.of(10, 30));
        startTime.add(LocalTime.of(11, 0));
        startTime.add(LocalTime.of(11, 30));
        startTime.add(LocalTime.of(12, 0));
        startTime.add(LocalTime.of(12, 30));
        startTime.add(LocalTime.of(13, 0));
        startTime.add(LocalTime.of(13, 30));
        startTime.add(LocalTime.of(14, 0));
        startTime.add(LocalTime.of(14, 30));
        startTime.add(LocalTime.of(15, 0));
        startTime.add(LocalTime.of(15, 30));
        startTime.add(LocalTime.of(16, 00));
        startTime.add(LocalTime.of(16, 30));
        startTime.add(LocalTime.of(17, 0));
        startTime.add(LocalTime.of(17, 30));

        startTimeCombo.setItems(startTime);

        endTime.add(LocalTime.of(7, 0));
        endTime.add(LocalTime.of(7, 30));
        endTime.add(LocalTime.of(8, 0));
        endTime.add(LocalTime.of(8, 30));
        endTime.add(LocalTime.of(9, 0));
        endTime.add(LocalTime.of(9, 30));
        endTime.add(LocalTime.of(10, 0));
        endTime.add(LocalTime.of(10, 30));
        endTime.add(LocalTime.of(11, 0));
        endTime.add(LocalTime.of(11, 30));
        endTime.add(LocalTime.of(12, 0));
        endTime.add(LocalTime.of(12, 30));
        endTime.add(LocalTime.of(13, 0));
        endTime.add(LocalTime.of(13, 30));
        endTime.add(LocalTime.of(14, 0));
        endTime.add(LocalTime.of(14, 30));
        endTime.add(LocalTime.of(15, 0));
        endTime.add(LocalTime.of(15, 30));
        endTime.add(LocalTime.of(16, 00));
        endTime.add(LocalTime.of(16, 30));
        endTime.add(LocalTime.of(17, 0));
        endTime.add(LocalTime.of(17, 30));
        endTime.add(LocalTime.of(18, 0));

        endTimeCombo.setItems(endTime);

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

    public Appointment getAppointment() {
        return appointment;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public void clearAppInfo() {
        titleTextBox.clear();
        titleTextBox.setStyle(null);
        descriptionTextBox.clear();
        descriptionTextBox.setStyle(null);
        locationTextBox.clear();
        locationTextBox.setStyle(null);
        customerCombo.setValue(null);
        customerCombo.setStyle(null);
        typeTextBox.clear();
        typeTextBox.setStyle(null);
        urlTextBox.clear();
        urlTextBox.setStyle(null);
        contactTextBox.clear();
        contactTextBox.setStyle(null);
        meetingDate.setValue(null);
        meetingDate.setStyle(null);
        startTimeCombo.setValue(null);
        startTimeCombo.setStyle(null);
        endTimeCombo.setValue(null);
        endTimeCombo.setStyle(null);
        validationLabel.setText("");

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

        customerCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldval, newval) -> {
            if (newval != null) {
                customerCombo.setStyle(null);
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

        startTimeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldval, newval) -> {
            if (newval != null) {
                startTimeCombo.setStyle(null);
                validationLabel.setText("");
            }
        });

        endTimeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldval, newval) -> {
            if (newval != null) {
                endTimeCombo.setStyle(null);
                validationLabel.setText("");
            }
        });
    }

}
