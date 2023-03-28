/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp_heathersmith.Model;

/**
 *
 * @author hlsmi
 */
public class Customer {

    private int customerId;
    private String customerName;
    private int addressId;
    private String address;
    private String address2;
    private String postal;
    private int cityId;
    private String city;
    private String contact;
    private boolean active;
    
    public Customer(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }
    
    public Customer(int customerId, String customerName, String address)
    {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
    }
    
        public Customer(int customerId, String customerName, String address, String address2, int addressId, String postal, int cityId, String contact, boolean active) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.addressId = addressId;
        this.postal = postal;
        this.cityId = cityId;
        this.contact = contact;
        this.active = active;
    }

    public Customer(int customerId, String customerName, int addressId, boolean active)
    {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
    }
    
    public Customer(String customerName, String address, String address2, String postal, int cityId, String contact, boolean active) {
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.postal = postal;
        this.cityId = cityId;
        this.contact = contact;
        this.active = active;
    }
    
    public Customer(int customerId, String customerName, String address, String address2, int addressId, String postal, String city, String contact, boolean active) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.addressId = addressId;
        this.postal = postal;
        this.city = city;
        this.contact = contact;
        this.active = active;
    }

    public Customer() {
        
    }
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
    
    public boolean getActive()
    {
        return active;
    }
    
    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
