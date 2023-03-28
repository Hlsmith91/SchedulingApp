/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith;

import Utils.DbConnection;
import Utils.Logger;
import Utils.UserNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import schedulingapp_heathersmith.Model.Appointment;
import schedulingapp_heathersmith.Model.Customer;
import schedulingapp_heathersmith.Model.Scheduler;
import schedulingapp_heathersmith.Model.User;
import schedulingapp_heathersmith.View.AddAppointmentController;
import schedulingapp_heathersmith.View.AddCustomerController;
import schedulingapp_heathersmith.View.EditAppointmentController;
import schedulingapp_heathersmith.View.EditCustomerController;
import schedulingapp_heathersmith.View.LogInController;
import schedulingapp_heathersmith.View.MainScreenController;
import javafx.scene.control.MenuItem;
import schedulingapp_heathersmith.View.AppointmentReportController;
import schedulingapp_heathersmith.View.ConsultantSchedReportController;
import schedulingapp_heathersmith.View.TypeFrequencyReportController;

/**
 *
 * @author hlsmi
 */
public class SchedulingApp_HeatherSmith extends Application {

    private Scene logInScene;
    private Scene mainScene;
    private Scene newAppScene;
    private Scene editAppScene;
    private Scene addCustScene;
    private Scene editCustScene;
    private Scene appReportScene;
    private Scene consultantSchedScene;
    private Scene freqReportScene;
    
    private Scheduler scheduler;
    private User user;
    private final Logger appLogger = new Logger();
    private final ResourceBundle rb = ResourceBundle.getBundle("language_files/rb", Locale.getDefault());

    @Override
    public void start(Stage stage) throws IOException {
        DbConnection.init(); // init shared DBconnection
        
        // instatiate scheduler using shared connection
        scheduler = new Scheduler(DbConnection.getConn());

        FXMLLoader loginFXML = new FXMLLoader(getClass().getResource("View/LogIn.fxml"));
        FXMLLoader mainFXML = new FXMLLoader(getClass().getResource("View/MainScreen.fxml"));
        FXMLLoader newAppFXML = new FXMLLoader(getClass().getResource("View/AddAppointment.fxml"));
        FXMLLoader editAppFXML = new FXMLLoader(getClass().getResource("View/EditAppointment.fxml"));
        FXMLLoader addCustFXML = new FXMLLoader(getClass().getResource("View/AddCustomer.fxml"));
        FXMLLoader editCustFXML = new FXMLLoader(getClass().getResource("View/EditCustomer.fxml"));
        FXMLLoader appReportFXML = new FXMLLoader(getClass().getResource("View/AppointmentReport.fxml"));
        FXMLLoader freqReportFXML = new FXMLLoader(getClass().getResource("View/TypeFrequencyReport.fxml"));
        FXMLLoader consultantSchedFXML = new FXMLLoader(getClass().getResource("View/ConsultantSchedReport.fxml"));

        logInScene = new Scene(loginFXML.load());
        mainScene = new Scene(mainFXML.load());
        newAppScene = new Scene(newAppFXML.load());
        editAppScene = new Scene(editAppFXML.load());
        addCustScene = new Scene(addCustFXML.load());
        editCustScene = new Scene(editCustFXML.load());
        appReportScene = new Scene(appReportFXML.load());
        freqReportScene = new Scene(freqReportFXML.load());
        consultantSchedScene = new Scene(consultantSchedFXML.load());

        LogInController logInCtrl = loginFXML.getController();
        MainScreenController mainScreenCtrl = mainFXML.getController();
        AddAppointmentController newAppCtrl = newAppFXML.getController();
        EditAppointmentController editAppCtrl = editAppFXML.getController();
        AddCustomerController addCustCtrl = addCustFXML.getController();
        EditCustomerController editCustCtrl = editCustFXML.getController();
        AppointmentReportController appRptCtrl = appReportFXML.getController();
        TypeFrequencyReportController typeFreqCtrl = freqReportFXML.getController();
        ConsultantSchedReportController consultantSchedCtrl = consultantSchedFXML.getController();

        stage.setTitle("Scheduling Application");
        
        //Prompts alert to confirm closing the application
        stage.setOnCloseRequest(evt -> {
            evt.consume();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to exit the scheduler application?", ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                DbConnection.closeConn();
                Platform.exit();
            }
        }); 
        stage.setScene(logInScene);
        stage.show();

        //submitButtonClicked event for login scene
        Button logInSubmitBtn = logInCtrl.getSubmitButton();
        logInSubmitBtn.setOnAction(e -> {
            try{
                 user = logInCtrl.submitButtonHandle();
            }
            catch(UserNotFoundException ex){
            
                 showAlert(AlertType.INFORMATION, rb.getString("loginError"));
                return;
            }
            
            appLogger.writeLogFile("'" + user.getUsername() + "' is logged in.\n");
            mainScreenCtrl.setUser(user);
            try{
                scheduler.setAppointmentsByUser(user.getUserId());
                scheduler.setCustomers();
                scheduler.setCities();
            }
            catch(Exception ex){
                showAlert(AlertType.ERROR, ex.getMessage());
            }
            
            mainScreenCtrl.setAppointmentTable(scheduler.getAppointments());
            newAppCtrl.setCustomerComboBox(scheduler.getCustomers());
            addCustCtrl.setCityCombo(scheduler.getCities());
            
            if (scheduler.checkAppointmentTimes()) {
                showAlert(AlertType.INFORMATION, "You have an appointment in 15 minutes.");
            }
            stage.setScene(mainScene);            
        });

        //Sort appointmentTable
        //Opens new appointment scene
        Button newApp = mainScreenCtrl.getNewAppButton();
        newApp.setOnAction(e -> {
            newAppCtrl.setCurrentUser(user);
            stage.setScene(newAppScene);
        });

        //Opens edit appointment Scene
        Button editApp = mainScreenCtrl.getEditAppButton();
        editApp.setOnAction(e -> {
            Appointment appointment = mainScreenCtrl.getSelectedApp();
            if (appointment != null) {
                editAppCtrl.setCurrentUser(user);
                editAppCtrl.setAppointmentId(appointment.getAppointmentId());
                editAppCtrl.populateCustomerComboBox(scheduler.getCustomers());
                editAppCtrl.setAppointment(appointment);
                stage.setScene(editAppScene);
            } else {
                showAlert(AlertType.INFORMATION, "Please select a appointment");
            }
        });

        /**
         * Add appointment actions
         */
        //Inserts appointment data into appointment table
        Button saveApp = newAppCtrl.getSaveButton();
        saveApp.setOnAction((ActionEvent e) -> {
            Button btn = newAppCtrl.getSaveButton();
            try {
                Appointment appointment = newAppCtrl.saveButtonClicked(user);
                if(appointment == null) return;
                
                if (scheduler.checkAppointmentConflict(appointment)) {
                    showAlert(AlertType.ERROR, "An appointment is already scheduled for this time.");
                    return;
                }
                
                scheduler.addAppointment(appointment, user);
                newAppCtrl.setCustomerComboBox(scheduler.getCustomers());
                stage.setScene(mainScene);
                newAppCtrl.clearAppInfo();
                
            } catch (IOException | SQLException ex) {
                showAlert(AlertType.ERROR, ex.getMessage());
            }catch(Exception ep){
                showAlert(AlertType.ERROR, ep.getMessage());
            }
        });

        //Cancels add appointment and returns to main screen
        Button cancelNewApp = newAppCtrl.getCancelButton();
        cancelNewApp.setOnAction(e -> {
            if (showAndWaitAlert(AlertType.CONFIRMATION, "Are you sure you want to cancel?").get() == ButtonType.OK) {
                stage.setScene(mainScene);
                newAppCtrl.clearAppInfo();
            }
        });

        /**
         * Edit appointment actions
         */
        //Updates appointment table with new appointment and returns to mainScene
        Button updateApp = editAppCtrl.getSaveButton();
        updateApp.setOnAction(e -> {
            Appointment appointment = editAppCtrl.saveButtonClicked();
            if (appointment == null) return; 
            
            if (scheduler.checkAppointmentConflict(appointment)) {
                showAlert(AlertType.INFORMATION, "An appointment is already scheduled for this time.");
                return;
            }

            if (showAndWaitAlert(AlertType.CONFIRMATION, "Are you sure you want to save?").get() == ButtonType.OK) {
                try{
                scheduler.modifyAppointment(appointment, user);
                scheduler.setAppointmentsByUser(user.getUserId());
                }
                catch(Exception m){
                    showAlert(AlertType.ERROR, m.getMessage());
                }
                stage.setScene(mainScene);
            }
        });

        //Cancels function returns user to mainScene
        Button cancelUpdate = editAppCtrl.getCancelButton();
        cancelUpdate.setOnAction(e -> {
            if (showAndWaitAlert(AlertType.CONFIRMATION, "Are you sure you want to cancel?").get() == ButtonType.OK) {
                stage.setScene(mainScene);
            }
        });

        //Deletes appointment from the tableview and removes appointment from db
        Button deleteApp = mainScreenCtrl.getDeleteButton();
        deleteApp.setOnAction(e -> {
            Appointment selectedApp = (Appointment) mainScreenCtrl.getAppointmentTable().getSelectionModel().getSelectedItem();
                if (selectedApp == null) {
                     showAlert(AlertType.INFORMATION, "Appointment must be selected");
                    return;
                }

            if (showAndWaitAlert(AlertType.CONFIRMATION, "Are you sure you want to delete the appointment?").get() == ButtonType.OK) {
                try {           
                    scheduler.deleteAppointment(selectedApp);
                    mainScreenCtrl.getAppointmentTable().refresh();                  
                } catch (SQLException ex) {
                    showAlert(AlertType.ERROR, ex.getMessage());
                }catch(Exception d){
                    showAlert(AlertType.ERROR, d.getMessage());
                }
            }
        });

        //Add customer scene
        Button addCust = mainScreenCtrl.getAddCustomerButton();
        addCust.setOnAction(e -> {
            addCustCtrl.setCurrentUser(user);
            addCustCtrl.setCityCombo(scheduler.getCities());
            stage.setResizable(false);
            stage.setScene(addCustScene);
        });

        Button saveNewCust = addCustCtrl.getSaveButton();
        saveNewCust.setOnAction(e -> {
            Customer customer = addCustCtrl.saveButtonClicked();
            if (customer == null) return;
            
            try {                
                scheduler.addCustomer(customer, user);
                newAppCtrl.setCustomerComboBox(scheduler.getCustomers());
                stage.setScene(mainScene);
                addCustCtrl.clearCustInfo();
            } catch (SQLException ex) {
                showAlert(AlertType.ERROR, ex.getMessage());
            }catch(Exception c)
            {
                showAlert(AlertType.ERROR, c.getMessage());
            }
        });

        Button cancelNewCust = addCustCtrl.getCancelButton();
        cancelNewCust.setOnAction(e -> {
            if (showAndWaitAlert(AlertType.CONFIRMATION, "Are you sure you want to cancel?").get() == ButtonType.OK) {
                addCustCtrl.clearCustInfo();
                stage.setScene(mainScene);
            }
        });

        Button editCust = mainScreenCtrl.getEditCustButton();
        editCust.setOnAction(e -> {
            try{
            scheduler.setCustomerData();
            }
            catch(Exception p){
                showAlert(AlertType.ERROR, p.getMessage());
            }
                
            editCustCtrl.setCustTable(scheduler.getBuildCustData());
            editCustCtrl.getCustTable();
            editCustCtrl.setCityCombo(scheduler.getCities());
            stage.setResizable(false);
            stage.setScene(editCustScene);
        });

        Button selectCust = editCustCtrl.getSelectButton();
        selectCust.setOnAction(e -> {
            Customer customer = editCustCtrl.selectedCustomer();
            editCustCtrl.setCustomerInfo(customer);
        });

        Button saveCust = editCustCtrl.getSaveButton();
        saveCust.setOnAction(e -> {
            Customer customer = editCustCtrl.saveCustomerButtonClicked();
            if (customer == null) return;
            try {
                scheduler.updateCustomer(customer, user);
                newAppCtrl.setCustomerComboBox(scheduler.getCustomers());
                stage.setScene(mainScene);
                editCustCtrl.clearInfo();
            } catch (SQLException ex) {
                showAlert(AlertType.ERROR, ex.getMessage());
            }catch(Exception k)
            {
                showAlert(AlertType.ERROR, k.getMessage());
            }
        });

        Button deleteCust = editCustCtrl.getDeleteButton();
        deleteCust.setOnAction(e -> {
            Button btn = editCustCtrl.getDeleteButton();
            editCustCtrl.deleteCustomerButtonClicked(e);
            Customer customer = (Customer) editCustCtrl.getCustTable().getSelectionModel().getSelectedItem();
            if (customer == null) return;
           
            if (showAndWaitAlert(AlertType.CONFIRMATION, "Are you sure you want to delete the selected customer?").get() != ButtonType.OK)
                return;

            List<Appointment> appointments = scheduler.getAppointmentsByCustomer(customer.getCustomerId());
            if (appointments != null && appointments.size() > 0) 
                if (showAndWaitAlert(AlertType.CONFIRMATION, "Customer has appointments. Are you sure you want to delete?").get() != ButtonType.OK) 
                    return;

            try {
                scheduler.deleteCustomer(customer);
                newAppCtrl.setCustomerComboBox(scheduler.getCustomers());
                editAppCtrl.setCustomerComboBox(scheduler.getCustomers());
                editCustCtrl.custTable.getItems().remove(customer);
                mainScreenCtrl.setAppointmentTable(scheduler.getAppointments());
            } catch (SQLException ex) {
                showAlert(AlertType.ERROR, ex.getMessage());
            } catch(Exception s){
                showAlert(AlertType.ERROR, s.getMessage());
            }          
        });

        Button cancelBtn = editCustCtrl.getCancelButton();
        cancelBtn.setOnAction(e -> {
            editCustCtrl.clearInfo();
            stage.setScene(mainScene);
        });

        MenuItem appReportBtn = mainScreenCtrl.getAppRptItem();
        appReportBtn.setOnAction(e -> {
            appRptCtrl.populateChart();
            stage.setScene(appReportScene);
        });

        Button homeBtn = appRptCtrl.getHomeBtn();
        homeBtn.setOnAction(e -> {
            appRptCtrl.getAppointmentChart().getData().clear();
            stage.setScene(mainScene);
        });

        MenuItem consultantSched = mainScreenCtrl.getConsultRptItem();
        consultantSched.setOnAction(e -> {
            consultantSchedCtrl.populateConsultantSchedRpt(user);
            stage.setScene(consultantSchedScene);
        });

        Button homeBtn3 = consultantSchedCtrl.getHomeBtn();
        homeBtn3.setOnAction(e -> {
            stage.setScene(mainScene);
        });

        MenuItem freqReportBtn = mainScreenCtrl.getAppTypeFreqRptItem();
        freqReportBtn.setOnAction(e -> {
            stage.setScene(freqReportScene);
        });

        Button homeBtn2 = typeFreqCtrl.getHomeBtn();
        homeBtn2.setOnAction(e -> {
            stage.setScene(mainScene);
        });
    }

    private static void showAlert(AlertType alertType, String content) {
        Alert a = new Alert(alertType);
        a.setContentText(content);
        a.show();
    }
    
    private static Optional<ButtonType> showAndWaitAlert(AlertType alertType, String content) {
        Alert a = new Alert(alertType);
        a.setContentText(content);
        return a.showAndWait();
    }

    public static void main(String[] args){
        launch(args);
    }
}
