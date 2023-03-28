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
public class City {
    
    private int cityId;
    private String City;
    
    public City(int cityId, String City) {
        this.cityId = cityId;
        this.City = City;
    }
    
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }
}
