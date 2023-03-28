/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import schedulingapp_heathersmith.Model.User;

/**
 *
 * @author hlsmi
 */
public class Logger {

    File file = new File("C:/Users/hlsmi/Documents/NetBeansProjects/SchedulingApp_HeatherSmith/logger.txt");

    public void writeLogFile(String message) {
        Charset utf8 = StandardCharsets.UTF_8;
        String text = "[" + LocalDateTime.now().toString() + "]: " + message;
        try {
            Files.write(Paths.get(file.toString()), text.getBytes(utf8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Error");
            a.showAndWait();
            ex.printStackTrace();
        }
    }
}
