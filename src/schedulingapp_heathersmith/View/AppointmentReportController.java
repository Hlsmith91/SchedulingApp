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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author hlsmi
 */
public class AppointmentReportController implements Initializable {

    @FXML
    private BarChart appointmentChart;
    @FXML private Button homeBtn;
    private final Connection conn = DbConnection.getConn();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void populateChart() {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT MONTHNAME(CONVERT_TZ(start, '+0:00', '-5:00')), COUNT(*) FROM appointment  GROUP BY MONTH(start);");
            ResultSet rs = pst.executeQuery();
            
            while(rs.next())
            {
                series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getInt(2)));
            }
            
            appointmentChart.getData().add(series);
            
        } catch (SQLException ex) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("ERROR");
            a.setContentText("SQL error");
            a.show();
            ex.printStackTrace();
        }
    }
    
    public BarChart getAppointmentChart()
    {
        return appointmentChart;
    }
    
    public Button getHomeBtn()
    {
        return homeBtn;
    }

}
