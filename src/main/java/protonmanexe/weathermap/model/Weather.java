package protonmanexe.weathermap.model;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Weather {

    private final String appKey;
    private String thisLocation;
    private String main;
    private String description;
    private double temp;
    private int humidity;
    private String icon;

    // constructor
    public Weather () {
        String key = System.getenv("W_API_KEY");
        if ((null != key) && (key.trim().length() > 0))
            appKey = key;
        else
            appKey = "";
    }

    // methods
    public String getAppKey () {
        return appKey;
    }

    public void setThisLocation(String thisLocation) {
        this.thisLocation = thisLocation;
    }

    public String getThisLocation() {
        return thisLocation;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static Weather create (JsonObject o) {
        final Weather w = new Weather();
        w.setMain(o.getString("main"));
        w.setDescription(o.getString("description"));
        w.setIcon(o.getString("icon"));
        return w;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("cityName", thisLocation)
            .add("main", main)
            .add("description", description)
            .add("icon", icon)
            .add("temperature", temp)
            .build();
    }


    
}
