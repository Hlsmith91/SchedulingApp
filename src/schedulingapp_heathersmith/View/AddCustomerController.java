/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith.View;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import schedulingapp_heathersmith.Model.City;
import schedulingapp_heathersmith.Model.Customer;
import schedulingapp_heathersmith.Model.User;

/**
 * FXML Controller class
 *
 * @author hlsmi
 */
public class AddCustomerController implements Initializable {

    @FXML
    private Label validateLabel;
    @FXML
    private ComboBox<City> cityCombo;
    @FXML
    private TextField customerNameTextBox;
    @FXML
    private TextField addressTextBox;
    @FXML
    private TextField address2TextBox;
    @FXML
    private TextField postalTextBox;
    @FXML
    private TextField contactTextBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox activeCombo;
    User currentUser;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        populateActiveCombo();
        setListeners();
    }

    public Customer saveButtonClicked() {
        if(!isValidated()) return null;
        
        String name = customerNameTextBox.getText();
        String address = addressTextBox.getText();
        String address2 = address2TextBox.getText();
        String postal = postalTextBox.getText();
        int cityId = cityCombo.getSelectionModel().getSelectedItem().getCityId();
        String contact = contactTextBox.getText();
        boolean active = getActive();

        return new Customer(name, address, address2, postal, cityId, contact, active);
    }

    public void populateActiveCombo() {
        activeCombo.getItems().addAll("Yes", "No");
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public boolean getActive() {        
        return activeCombo.getSelectionModel().getSelectedItem().equals("Yes");
    }

    public void setCityCombo(ObservableList<City> cities) {
        cityCombo.setItems(cities);
        cityCombo.setConverter(new StringConverter<City>() {
            @Override
            public String toString(City object) {
                return object.getCity();
            }

            @Override
            public City fromString(String string) {
                return cityCombo.getItems().stream().filter(city
                        -> city.getCity().equals(string)).findFirst().orElse(null);
            }
        });
    }

    private boolean isValidated() {
        if (customerNameTextBox.getText().isEmpty()) {
            customerNameTextBox.setStyle("-fx-border-color: red;");
            validateLabel.setText("Please enter customer name");
            validateLabel.setTextFill(Color.RED);
            return false;
        }
        if (addressTextBox.getText().isEmpty()) {
            addressTextBox.setStyle("-fx-border-color: red;");
            validateLabel.setText("Please enter address");
            validateLabel.setTextFill(Color.RED);
            return false;
        }
        if (postalTextBox.getText().isEmpty() || !postalTextBox.getText().matches("[0-9]+") || postalTextBox.getText().trim().length() != 5) {
            postalTextBox.setStyle("-fx-border-color: red;");
            validateLabel.setText("Please enter postal code");
            validateLabel.setTextFill(Color.RED);
            return false;
        }
        if (cityCombo.getValue() == null) {
            cityCombo.setStyle("-fx-border-color: red;");
            validateLabel.setText("Please select a city");
            validateLabel.setTextFill(Color.RED);
            return false;
        }
        if (contactTextBox.getText().isEmpty() || !contactTextBox.getText().matches("\\d{3}-\\d{4}")) {
            contactTextBox.setStyle("-fx-border-color: red;");
            validateLabel.setText("Please enter phone number ex: 111-1111");
            validateLabel.setTextFill(Color.RED);
            return false;
        }
        if (activeCombo.getValue() == null) {
            activeCombo.setStyle("-fx-border-color: red;");
            validateLabel.setText("Please select active");
            validateLabel.setTextFill(Color.RED);
            return false;
        }

        return true;
    }

    public void clearCustInfo() {
        customerNameTextBox.clear();
        customerNameTextBox.setStyle(null);
        addressTextBox.clear();
        addressTextBox.setStyle(null);
        address2TextBox.clear();
        address2TextBox.setStyle(null);
        postalTextBox.clear();
        postalTextBox.setStyle(null);
        contactTextBox.clear();
        contactTextBox.setStyle(null);
        cityCombo.setValue(null);
        cityCombo.setStyle(null);
        activeCombo.setValue(null);
        activeCombo.setStyle(null);
        validateLabel.setText("");
    }

    public void setListeners() {
        postalTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.length() == 5) {
                postalTextBox.setStyle(null);
                validateLabel.setText("");
            }
        });

        customerNameTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.length() > 0) {
                customerNameTextBox.setStyle(null);
                validateLabel.setText("");
            }
        });

        addressTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.length() > 0) {
                addressTextBox.setStyle(null);
                validateLabel.setText("");
            }
        });

        contactTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.matches("\\d{3}-\\d{4}")) {
                contactTextBox.setStyle(null);
                validateLabel.setText("");
            }
        });

        cityCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldval, newval) -> {
            if (newval != null) {
                cityCombo.setStyle(null);
                validateLabel.setText("");
            }
        });

        activeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldval, newval) -> {
            if (newval != null) {
                activeCombo.setStyle(null);
                validateLabel.setText("");
            }
        });
    }
}
