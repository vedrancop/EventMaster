package BE;

import java.awt.*;

public class Event {
    private String name;
    private String location;
    private String time;
    private String description;

    int id;

    public Event() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public Event(String time, String description, String location, String name) {
        this.time=time;
        this.description=description;
        this.location=location;
        this.name=name;
    }

    public Event(int id,String time, String description, String location, String name) {
        this.id = id;
        this.time=time;
        this.description=description;
        this.location=location;
        this.name=name;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
    public void updateEvent(String time, String location, String description, String name) {
        this.setTime(time);
        this.setLocation(location);
        this.setDescription(description);
        this.setName(name);
    }

    public int getId() {

        return id;
    }
}
