package BLL;

import BE.Event;
import DAL.EventDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//The class that will pass db methods for controller.
public class EventLogic {
    EventDAO eventDAO=new EventDAO();

    public void createEvent(Event event){
        eventDAO.createEvent(event);
    }
    public List<Event> getAllEvents() throws SQLException {
        return eventDAO.getAllEvents();
    }
    public void updateEvent(Event event) {
        eventDAO.updateEvent(event);
    }
    public void deleteEvent(Event selectedEvent) {
        eventDAO.deleteEvent(selectedEvent);
    }

    public ArrayList<Integer> getEventID(int eventID){
       return eventDAO.getEventID(eventID);
    }

    public Event getEvent(int eventID){
        return eventDAO.getEvent(eventID);
    }

    public List<Event> searchEvents(String searchText) {
        return eventDAO.searchEvents(searchText);
    }
}
