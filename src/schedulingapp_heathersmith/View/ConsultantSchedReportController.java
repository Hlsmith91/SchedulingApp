/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith.View;

import Utils.DbConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalDateTimeStringConverter;
import schedulingapp_heathersmith.Model.Appointment;
import schedulingapp_heathersmith.Model.User;

/**
 * FXML Controller class
 *
 * @author hlsmi
 */
public class ConsultantSchedReportController implements Initializable {
    @FXML
    private Button homeBtn;
    @FXML private TableView consultantSchedTable;
    @FXML private TableColumn startCol;
    @FXML private TableColumn endCol;
    @FXML private TableColumn titleCol;
    @FXML private TableColumn custNameCol;
    @FXML private TableColumn contactCol;
    
    private final Connection conn = DbConnection.getConn();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm a");
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        startCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateTimeStringConverter(dtf, dtf)));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        endCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateTimeStringConverter(dtf, dtf)));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));        
    }

    public void populateConsultantSchedRpt(User user) {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT start, end, title, cu.customerName, contact FROM appointment INNER JOIN customer AS cu ON appointment.customerId = cu.customerId"
                +" WHERE userId = ? ORDER BY MONTH(start)");
            pst.setInt(1, user.getUserId());
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()) {
                Appointment appointment = new Appointment();
                ZonedDateTime localDT = rs.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
                ZonedDateTime localEDT = rs.getTimestamp("end").toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
                
                appointment.setStart(localDT.toLocalDateTime());
                appointment.setEnd(localEDT.toLocalDateTime());
                appointment.setTitle(rs.getString("title"));
                appointment.setCustomerName(rs.getString("cu.customerName"));
                appointment.setContact(rs.getString("contact"));
                
                consultantSchedTable.getItems().add(appointment);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("ERROR");
            a.setContentText("SQL Error");
            a.show();
        }
    }
    
    public Button getHomeBtn()
    {
        return homeBtn;
    }
    
}
