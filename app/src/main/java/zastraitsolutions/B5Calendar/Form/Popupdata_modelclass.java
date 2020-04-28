package zastraitsolutions.B5Calendar.Form;

public class Popupdata_modelclass {
    String id;
    String event_name;
    String event_description;
    String event_short;
    String user_id;
    int checked;

    public Popupdata_modelclass(String id, String event_name, String event_description, String event_short, String user_id, int checked) {
        this.id = id;
        this.event_name = event_name;
        this.event_description = event_description;
        this.event_short = event_short;
        this.user_id = user_id;
        this.checked = checked;
    }

    public Popupdata_modelclass() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_short() {
        return event_short;
    }

    public void setEvent_short(String event_short) {
        this.event_short = event_short;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
