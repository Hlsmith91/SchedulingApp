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
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author hlsmi
 */
public class TypeFrequencyReportController implements Initializable {
    
    @FXML private ScatterChart appTypeFreqRpt;
    @FXML private Button homeBtn;
    private final Connection conn = DbConnection.getConn();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateAppTypeFreqRptChart();
    }   
    
    public void populateAppTypeFreqRptChart()
    {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        try{
        PreparedStatement pst = conn.prepareStatement("SELECT type, COUNT(type) FROM appointment GROUP BY type");
        ResultSet rs = pst.executeQuery();
        
        while(rs.next())
        {
            series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getInt(2)));
        }
        
            appTypeFreqRpt.getData().add(series);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("ERROR");
            a.setContentText("SQL Error");
            a.show();
        }
    }

    public Button getHomeBtn() {
        return homeBtn;
    }
    
}
