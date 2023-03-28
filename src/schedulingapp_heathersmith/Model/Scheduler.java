/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * The Scheduler class stores and manages user appointments.
 *
 * @author hlsmi
 */
public class Scheduler {

    private final Connection dbConnection;
    private final LocalDateTime currentDate;
    private final ObservableList<Appointment> appointments;
    private final ObservableList<Customer> customers;
    private final ObservableList<City> cities;
    private final ObservableList<Customer> customerInfo;

    public Scheduler(Connection dbConnectionection) {
        dbConnection = dbConnectionection;
        currentDate = LocalDateTime.now();
        appointments = FXCollections.observableArrayList();
        customers = FXCollections.observableArrayList();
        cities = FXCollections.observableArrayList();
        customerInfo = FXCollections.observableArrayList();
    }

    /**
     * Add appointment to the scheduled appointments.
     *
     * @param newApp the appointment to add
     * @param user the user the appointment is for.
     */
    public void addAppointment(Appointment newApp, User user) throws Exception {

        checkAppointmentConflict(newApp);

        try {
            PreparedStatement pst = dbConnection.prepareStatement("INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            pst.setInt(1, newApp.getCustomerId()); //customerId
            pst.setInt(2, user.getUserId()); //userId
            pst.setString(3, newApp.getTitle()); //title
            pst.setString(4, newApp.getDescription()); //description
            pst.setString(5, newApp.getLocation()); //location
            pst.setString(6, newApp.getContact()); //contact
            pst.setString(7, newApp.getType()); //type
            pst.setString(8, newApp.getUrl()); //url
            pst.setTimestamp(9, Timestamp.valueOf(newApp.getStart().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())); //start
            pst.setTimestamp(10, Timestamp.valueOf(newApp.getEnd().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())); //end
            pst.setTimestamp(11, Timestamp.valueOf(currentDate)); //createDate
            pst.setString(12, user.getUsername()); //createdBy
            pst.setString(13, user.getUsername()); //lastUpdatedBy

            pst.execute();

            appointments.add(newApp);
        } catch (SQLException e) {
            System.out.println(e);
            throw new Exception("There was an error when adding appointment.");
        }
    }

    public void modifyAppointment(Appointment selectedApp, User user) throws Exception {

        checkAppointmentConflict(selectedApp);
        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement pst = dbConnection.prepareStatement("UPDATE appointment SET customerId = ?, title = ?, description = ?, location = ?, contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdateBy = ? "
                    + " WHERE appointmentId = ?");
            pst.setInt(1, selectedApp.getCustomerId());
            pst.setString(2, selectedApp.getTitle());
            pst.setString(3, selectedApp.getDescription());
            pst.setString(4, selectedApp.getLocation());
            pst.setString(5, selectedApp.getContact());
            pst.setString(6, selectedApp.getType());
            pst.setString(7, selectedApp.getUrl());
            pst.setTimestamp(8, Timestamp.valueOf(selectedApp.getStart().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
            pst.setTimestamp(9, Timestamp.valueOf(selectedApp.getEnd().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
            pst.setString(10, user.getUsername());
            pst.setInt(11, selectedApp.getAppointmentId());

            pst.executeUpdate();
            dbConnection.commit();

            //find appointment in appointments and replace appointment
            for (int i = 0; i < appointments.size(); i++) {
                if (selectedApp.equals(appointments.get(i))) {
                    appointments.set(i, selectedApp);
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex);
            throw new Exception("There was an error when modifying the appointment.");
        }
    }

    public void deleteAppointment(Appointment selectedApp) throws Exception {

        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement pst = dbConnection.prepareStatement("DELETE FROM appointment WHERE appointmentId = ?");
            pst.setInt(1, selectedApp.getAppointmentId());
            pst.executeUpdate();
            dbConnection.commit();

            appointments.remove(selectedApp);

        } catch (SQLException ex) {
            dbConnection.rollback();
            System.out.println(ex);
            throw new Exception("There was an error when deleting the appointment.");
        }
    }

    public void addCustomer(Customer customer, User user) throws Exception {

        try {

            int addressId = 0;

            dbConnection.setAutoCommit(false);
            PreparedStatement pst2 = dbConnection.prepareStatement("INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst2.setString(1, customer.getAddress());
            pst2.setString(2, customer.getAddress2());
            pst2.setInt(3, customer.getCityId());
            pst2.setString(4, customer.getPostal());
            pst2.setString(5, customer.getContact());
            pst2.setTimestamp(6, Timestamp.valueOf(currentDate));
            pst2.setString(7, user.getUsername());
            pst2.setString(8, user.getUsername());
            pst2.executeUpdate();
            ResultSet rs = pst2.getGeneratedKeys();
            if (rs.next()) {
                addressId = rs.getInt(1);
            }

            PreparedStatement pst = dbConnection.prepareStatement("INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy) "
                    + "VALUES(?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, customer.getCustomerName());
            pst.setInt(2, addressId);
            pst.setBoolean(3, customer.getActive());
            pst.setTimestamp(4, Timestamp.valueOf(currentDate));
            pst.setString(5, user.getUsername());
            pst.setString(6, user.getUsername());
            pst.executeUpdate();
            ResultSet rs2 = pst.getGeneratedKeys();
            if (rs2.next()) {
                customer.setCustomerId(rs2.getInt(1));
            }
            dbConnection.commit();

            customers.add(customer);
        } catch (SQLException ex) {
            System.out.println(ex);
            dbConnection.rollback();
            throw new Exception("There was an error when adding customer.");
        }
    }

    public void closeConnection() {
        if (dbConnection == null) {
            return;
        }
        try {
            dbConnection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Appointment> getAppointmentsByCustomer(int customerId) {
        List<Appointment> appointments = new ArrayList<>();

        try {
            PreparedStatement pst = dbConnection.prepareStatement("SELECT * FROM appointment WHERE customerId = ?");
            pst.setInt(1, customerId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointmentId"));
                appointment.setCustomerId(rs.getInt("customerId"));
                appointments.add(appointment);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return appointments;
    }

    public void deleteCustomer(Customer customer) throws Exception {

        // grab the latest data for customer and appointments
        List<Customer> customers = getCustomerByAddress(customer.getAddressId());
        List<Appointment> customerAppointment = getAppointmentsByCustomer(customer.getCustomerId());

        try {
            dbConnection.setAutoCommit(false);

            if (customerAppointment != null && customerAppointment.size() > 0) {
                PreparedStatement pst2 = dbConnection.prepareStatement("DELETE FROM appointment WHERE customerId = ?");
                pst2.setInt(1, customer.getCustomerId());
                pst2.executeUpdate();
            }
            PreparedStatement pst3 = dbConnection.prepareStatement("DELETE FROM customer WHERE customerId = ?");
            pst3.setInt(1, customer.getCustomerId());
            pst3.executeUpdate();

            if (customers != null && !customers.isEmpty() && customers.size() == 1) {
                PreparedStatement pst = dbConnection.prepareStatement("DELETE FROM address WHERE addressId = ?");
                pst.setInt(1, customer.getAddressId());
                pst.executeUpdate();
            }

            dbConnection.commit();

            // I'm using lambda expressions to remove all of the customer's appointments from the scheduler appointments.
            // Using a lamda expression is more efficient than writing a loop to iterate through all of the appointments and removing them one at at a time.
            appointments.removeIf(t -> t.getCustomerId() == customer.getCustomerId());
            setCustomers();
        } catch (SQLException ex) {
            System.out.println(ex);
            dbConnection.rollback();
            throw new Exception("There was an error when deleting the customer.");
        }
    }

    public void updateCustomer(Customer customer, User user) throws Exception {
        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement pst = dbConnection.prepareStatement("UPDATE address SET address = ?, address2 = ?, postalCode = ?, phone = ?, cityId = ?, lastUpdateBy = ? WHERE addressId = ?");
            pst.setString(1, customer.getAddress());
            pst.setString(2, customer.getAddress2());
            pst.setString(3, customer.getPostal());
            pst.setString(4, customer.getContact());
            pst.setInt(5, customer.getCityId());
            pst.setString(6, user.getUsername());
            pst.setInt(7, customer.getAddressId());
            pst.executeUpdate();
            dbConnection.commit();

            PreparedStatement pst2 = dbConnection.prepareStatement("UPDATE customer SET customerName = ?, addressId = ?, active = ?, lastUpdateBy = ? WHERE customerId = ?");
            pst2.setString(1, customer.getCustomerName());
            pst2.setInt(2, customer.getAddressId());
            pst2.setBoolean(3, customer.getActive());
            pst2.setString(4, user.getUsername());
            pst2.setInt(5, customer.getCustomerId());
            pst2.executeUpdate();
            dbConnection.commit();

        } catch (SQLException ex) {
            System.out.println(ex);
            dbConnection.rollback();
            throw new Exception("There was an error when modifying the customer.");
        } finally {

            closeConnection();
        }
    }

    public void setAppointmentsByUser(Integer userId) throws Exception {

        try {
            appointments.clear();

            PreparedStatement pst = dbConnection.prepareStatement("SELECT a.appointmentId, a.customerId, a.title, a.type, a.description, a.location, a.url, "
                    + "a.`start`,a.`end`, a.userId, c.customerName, a.contact, a.createdBy "
                    + " FROM appointment AS a"
                    + " LEFT JOIN customer  AS c ON a.customerId = c.customerId"
                    + " WHERE userId = " + userId
                    + " ORDER BY `start`");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("a.appointmentId");
                Timestamp start = rs.getTimestamp("a.start");
                LocalDateTime sldt = start.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

                Timestamp end = rs.getTimestamp("a.end");
                LocalDateTime eldt = end.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

                String title = rs.getString("a.title");
                String type = rs.getString("a.type");
                String description = rs.getString("a.description");
                String customer = rs.getString("c.customerName");
                int userIds = rs.getInt("a.userId");
                String user = rs.getString("a.createdBy");
                String location = rs.getString("a.location");
                String url = rs.getString("a.url");
                String contact = rs.getString("a.contact");

                appointments.add(new Appointment(appointmentId, title, description, location, customer, contact, type, sldt, eldt, userIds, user, url));

            }
           
        } catch (SQLException e) {
            System.out.println(e);
            throw new Exception("There was an error when loading data.");

        }
    }

    public void setCustomers() throws Exception {
        try {
            customers.clear();
            PreparedStatement ps = dbConnection.prepareStatement("SELECT customerName, customerId FROM customer;");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("customerName");
                int custId = rs.getInt("customerId");
                customers.add(new Customer(custId, name));
            }
        } catch (SQLException exception) {
            System.out.println(exception);
            throw new Exception("There was an error when loading customer data.");
        }
    }

    public void setCities() throws Exception {
        try {
            cities.clear();
            PreparedStatement ps = dbConnection.prepareStatement("SELECT cityId, city FROM city ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int cityId = rs.getInt("cityId");
                String cityName = rs.getString("city");
                cities.add(new City(cityId, cityName));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            throw new Exception("There was an error when loading city data.");
        }
    }

    public void setCustomerData() throws Exception {
        try {
            PreparedStatement pst = dbConnection.prepareStatement("SELECT cu.customerId, cu.customerName, cu.active, cu.addressId, a.address, a.address2, a.postalCode, a.phone, ci.city "
                    + " from customer AS cu "
                    + " Left JOIN address As a ON cu.addressId = a.addressId "
                    + " Left JOIN city As ci ON a.cityId = ci.cityId ");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int custId = rs.getInt("customerId");
                int addressId = rs.getInt("addressId");
                String custName = rs.getString("customerName");
                String address = rs.getString("address");
                boolean active = rs.getBoolean("active");
                String address2 = rs.getString("address2");
                String postal = rs.getString("postalCode");
                String contact = rs.getString("phone");
                String cityName = rs.getString("city");
                Customer customer = new Customer(custId, custName, address, address2, addressId, postal, cityName, contact, active);
                customerInfo.add(customer);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            throw new Exception("There was an error when loading customer data.");
        }
    }

    /**
     * Finds and returns all customers that match the addressId.
     *
     * @param addressId the appointment to add
     * @return list of customers that match the addressId
     */
    public List<Customer> getCustomerByAddress(int addressId) throws Exception {
        List<Customer> custAddress = new ArrayList<>();

        try {
            PreparedStatement pst = dbConnection.prepareStatement("SELECT * FROM customer WHERE addressId = ?");
            pst.setInt(1, addressId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customerId"));
                customer.setCustomerName(rs.getString("customerName"));
                custAddress.add(customer);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            throw new Exception("There was an error while loading customer by address.");
        }

        return custAddress;
    }

    public ObservableList getAppointments() {
        return appointments;
    }

    public ObservableList<Customer> getBuildCustData() {
        return customerInfo;
    }

    public ObservableList<City> getCities() {
        return cities;
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public boolean checkAppointmentConflict(Appointment appointment) {

       FilteredList<Appointment> otherAppointments = appointments.filtered(x -> x.getAppointmentId() != appointment.getAppointmentId());

        //I used a lambda expression to compare the start and end times for a new appointment to the start and end times in the scheduler appointments to prevent
        //time overlapping.
        //Using a lambda expression makes it more efficient because it avoids nested branching and makes the logic more readable;
        return otherAppointments.stream()
                .anyMatch(x -> x.getStart().isEqual(appointment.getStart())
                || ((appointment.getStart().isAfter(x.getStart()) || appointment.getStart().isEqual(x.getEnd())) && appointment.getStart().isBefore(x.getEnd()))
                || (appointment.getStart().isBefore(x.getStart()) && appointment.getEnd().isAfter(x.getStart()))
                || (appointment.getStart().isAfter(x.getStart()) && appointment.getEnd().isBefore(x.getEnd()))
                );
    }

    public boolean checkAppointmentTimes() {
        LocalDateTime currDate = LocalDateTime.now();

        return appointments.stream().anyMatch(t -> ChronoUnit.MINUTES.between(currDate, t.getStart()) >= 0
                && ChronoUnit.MINUTES.between(currDate, t.getStart()) <= 15);
    }
    
    public long checkDup()
    {
        long count = appointments.stream().distinct().count();
        
        return count;
    }
}
