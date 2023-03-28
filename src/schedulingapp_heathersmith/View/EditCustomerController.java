/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith.View;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import schedulingapp_heathersmith.Model.City;
import schedulingapp_heathersmith.Model.Customer;

/**
 * FXML Controller class
 *
 * @author hlsmi
 */
public class EditCustomerController implements Initializable {
    @FXML
    private TextField custNameTextBox;
    @FXML
    private TextField addressTextBox;
    @FXML
    private TextField address2TextBox;
    @FXML
    private TextField postalTextBox;
    @FXML
    private TextField contactTextBox;
    @FXML
    private ComboBox<City> cityCombo;
    @FXML
    public TableView<Customer> custTable;
    @FXML
    private TableColumn custIdCol;
    @FXML
    private TableColumn custNameCol;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button selectButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ComboBox activeCombo;
    private String isActive;
    @FXML
    private Label validateLabel;
    ObservableList<Customer> custList = FXCollections.observableArrayList();
    Customer customer;
    City city;
    String cityName;
    boolean active;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        
        populateActiveCombo();
        setListeners();
    }

    public void setCustTable(ObservableList custList) {
        custTable.setItems(custList);
    }

    public TableView getCustTable() {
        return custTable;
    }

    public void setCustomerInfo(Customer selectedCustomer) {
        customer = selectedCustomer;
        custNameTextBox.setText(customer.getCustomerName());
        addressTextBox.setText(customer.getAddress());
        address2TextBox.setText(customer.getAddress2());
        postalTextBox.setText(customer.getPostal());
        contactTextBox.setText(customer.getContact());
        activeCombo.setValue(getActive(customer.getActive()));
        cityCombo.setValue(fromString(customer.getCity()));
    }

    public Customer selectedCustomer() {
        customer = custTable.getSelectionModel().getSelectedItem();
        return customer;
    }

    public void populateActiveCombo() {
        activeCombo.getItems().addAll("Yes", "No");
    }

    public String getActive(boolean rs) {
        if (rs == true) {
            isActive = "Yes";
        } else {
            isActive = "No";
        }

        return isActive;
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

    public void selectButtonClicked() {
        setCustomerInfo(customer);
    }

    public Button getSelectButton() {
        return selectButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void deleteCustomerButtonClicked(ActionEvent e) {
    }

    public Customer saveCustomerButtonClicked() {
        if (!isValidated()) {
            return null;
        }
        int custId = custTable.getSelectionModel().getSelectedItem().getCustomerId();
        int addressId = custTable.getSelectionModel().getSelectedItem().getAddressId();
        String name = custNameTextBox.getText();
        String address = addressTextBox.getText();
        String address2 = address2TextBox.getText();
        String postal = postalTextBox.getText();
        String contact = contactTextBox.getText();
        boolean active = getActive();
        int cityId = cityCombo.getSelectionModel().getSelectedItem().getCityId();

        customer = new Customer(custId, name, address, address2, addressId, postal, cityId, contact, active);

        return customer;
    }

    ;

    public Button getCancelButton() {
        return cancelButton;
    }

    private boolean isValidated() {
        if (custNameTextBox.getText().isEmpty()) {
            custNameTextBox.setStyle("-fx-border-color: red;");
            validateLabel.setTextFill(Color.RED);
            validateLabel.setText("Please enter customer name");
            return false;
        }
        if (addressTextBox.getText().isEmpty()) {
            addressTextBox.setStyle("-fx-border-color: red;");
            validateLabel.setTextFill(Color.RED);
            validateLabel.setText("Please enter address");
            return false;
        }
        if (postalTextBox.getText().isEmpty() || !postalTextBox.getText().matches("[0-9]+") || postalTextBox.getText().trim().length() != 5) {
            postalTextBox.setStyle("-fx-border-color: red;");
            validateLabel.setTextFill(Color.RED);
            validateLabel.setText("Please enter postal code");

            return false;
        }
        if (cityCombo.getValue() == null) {
            cityCombo.setStyle("-fx-border-color: red;");
            validateLabel.setTextFill(Color.RED);
            validateLabel.setText("Please select a city");
            return false;
        }
        if (contactTextBox.getText().isEmpty() || !contactTextBox.getText().matches("\\d{3}-\\d{4}")) {
            contactTextBox.setStyle("-fx-border-color: red;");
            validateLabel.setTextFill(Color.RED);
            validateLabel.setText("Please enter phone number ex: 111-1111");
            return false;
        }
        if (activeCombo.getValue() == null) {
            activeCombo.setStyle("-fx-border-color: red;");
            validateLabel.setTextFill(Color.RED);
            validateLabel.setText("Please select active");
            return false;
        }

        return true;
    }

    public boolean getActive() {
        if (activeCombo.getSelectionModel().getSelectedItem().equals("Yes")) {
            active = true;
        } else {
            active = false;
        }

        return active;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void clearInfo() {
        custNameTextBox.clear();
        custNameTextBox.setStyle(null);
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
        custTable.getItems().clear();
    }

    public City fromString(String string) {

        return cityCombo.getItems().stream().filter(city
                -> city.getCity().equals(string)).findFirst().orElse(null);

    }
    
    public void setListeners() {
        postalTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.length() == 5) {
                postalTextBox.setStyle(null);
                validateLabel.setText("");
            }
        });

        custNameTextBox.textProperty().addListener((observable, oldval, newval) -> {
            if (newval.length() > 0) {
                custNameTextBox.setStyle(null);
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

    }

}
