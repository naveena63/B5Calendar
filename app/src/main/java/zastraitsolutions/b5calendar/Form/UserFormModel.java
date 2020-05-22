package zastraitsolutions.b5calendar.Form;

public class UserFormModel {
    private String eventName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String eventDesc;
    private String userid;
    private String checkboxValue;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }



    public String getCheckboxValue() {
        return checkboxValue;
    }

    public void setCheckboxValue(String checkboxValue) {
        this.checkboxValue = checkboxValue;
    }


    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    private String eventDate;

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
