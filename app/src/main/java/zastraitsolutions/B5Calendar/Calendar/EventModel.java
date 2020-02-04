package zastraitsolutions.B5Calendar.Calendar;

public class EventModel {
    String eventName;
    String eventDesc;

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    String eventColor;
    String eventDate;

    public String getEventColor() {
        return eventColor;
    }

    public void setEventColor(String eventColor) {
        this.eventColor = eventColor;
    }

    public EventModel(String event_name,String eventColor) {
        this.eventName=event_name;
        this.eventColor=eventColor;
    }

//    public EventModel(String eventnam) {
//        this.eventName=eventnam;
//
//    }


    public String getEventName() {


        return eventName;
    }


    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }
}
