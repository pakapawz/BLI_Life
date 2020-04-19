package components.reservations;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Reservation {
    protected String creationDate = "NULL";
    protected String user = "NULL";
    protected String email = "NULL";
    protected String date = "NULL";

    public Reservation(){}

    public Reservation(String user, String email, String date){
        creationDate = getCurrentDate();
        this.user = user;
        this.email = email;
        this.date = date;
    }

    private String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date();
        return dateFormat.format(now);
    }

    public boolean dateChecker(){
        if (date.compareTo(creationDate) <= 0) return false;
        else return true;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
