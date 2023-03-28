 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith.View;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Utils.DbConnection;
import Utils.UserNotFoundException;
import java.sql.Connection;
import java.util.Locale;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import schedulingapp_heathersmith.Model.User;

/**
 * FXML Controller class
 *
 * @author hlsmi
 */
public class LogInController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label usernameLabel;
    @FXML 
    private Label passwordLabel;
    @FXML
    private Label userLoginLabel;
    
   // private final ResourceBundle rb = ResourceBundle.getBundle("language_files/rb", Locale.getDefault());
    private final Connection conn;
    
    public LogInController()
    {
        conn = DbConnection.getConn();
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       ResourceBundle rt = ResourceBundle.getBundle("language_files/rb", Locale.getDefault());
       
       usernameLabel.setText(rt.getString("username"));
       passwordLabel.setText(rt.getString("password"));
       userLoginLabel.setText(rt.getString("userLogin"));
       submitButton.setText(rt.getString("submit"));
       cancelButton.setText(rt.getString("cancel"));
    }
    
    public User submitButtonHandle() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        User validU = validateLogIn(username, password);

        return validU;
    }

    public User validateLogIn(String username, String password) {
        User user = null;
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT userName, password, userId FROM user WHERE userName = ? AND BINARY password = ?");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setUserId(rs.getInt("userId"));       
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if(user == null)
             throw new UserNotFoundException(username);
        
        return user;
    }

    public void cancelButtonClicked() {
        System.exit(0);
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getSubmitButton() {

        return submitButton;
    }
    
}
