package zastraitsolutions.b5calendar.Calendar;

public class EventModel {
    String eventName;
    String calendarDate;
    String eventColor;
    public String getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(String calendarDate) {
        this.calendarDate = calendarDate;
    }




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


    public String getEventName() {


        return eventName;
    }


    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

}
