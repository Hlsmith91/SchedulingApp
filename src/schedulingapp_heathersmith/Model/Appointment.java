/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith.Model;

import java.time.LocalDateTime;

/**
 *
 * @author hlsmi
 */
public class Appointment {

    private int appointmentId;
    private int customerId;
    private String title;
    private String description;
    private String contact;
    private LocalDateTime start;
    private LocalDateTime end;
    private int userId;
    private String type;
    private String location;
    private String url;
    private String createdBy;
    private String customerName;

    public Appointment() {
    }
    
    public Appointment(LocalDateTime start, LocalDateTime end, String customerName, String contact)
    {
        this.start = start;
        this.end = end;
        this.customerName = customerName;
        this.contact = contact;
    }

    public Appointment(int appointmentId, int customerId, String title, String description, String location, String contact, String type, LocalDateTime start, LocalDateTime end, String url) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.url = url;
    }

    public Appointment(int appointmentId, int customerId, String title, String description, String location, String customerName, String contact, String type, LocalDateTime start, LocalDateTime end, int userId, String createBy) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.customerName = customerName;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.userId = userId;
        this.createdBy = createdBy;
    }

    public Appointment(int appointmentId, String title, String description, String location, String customerName, String type, String url, int userId, LocalDateTime start, LocalDateTime end) {
        this.appointmentId = appointmentId;
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.customerName = customerName;
        this.userId = userId;
        this.type = type;
    }

    public Appointment(int customerId, String title, String description, String location, String contact, String type, LocalDateTime start, LocalDateTime end, int userId, String createBy, String url) {
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.userId = userId;
        this.createdBy = createdBy;
        this.url = url;
    }

    public Appointment(int userId, int customerId, String title, String description, String type, String url, String location, String customerName, String contact, LocalDateTime start, LocalDateTime end) {
        this.userId = userId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.url = url;
        this.location = location;
        this.customerName = customerName;
        this.contact = contact;
        this.start = start;
        this.end = end;

    }

    public Appointment(int appointmentId, String title, String description, String location, String customerName, String contact, String type, LocalDateTime start, LocalDateTime end, int userId, String createdBy, String url) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.customerName = customerName;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.userId = userId;
        this.createdBy = createdBy;
        this.url = url;
    }

    public Appointment(LocalDateTime end, LocalDateTime start, String customerName, String contact, String description, String location, String title, String type, String url) {
        this.end = end;
        this.start = start;
        this.customerName = customerName;
        this.contact = contact;
        this.description = description;
        this.location = location;
        this.title = title;
        this.type = type;
        this.url = url;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy() {
        this.createdBy = createdBy;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
