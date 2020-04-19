package components.reservations;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        String[] tokensDate = date.split("/");
        String[] tokensCurrDate = creationDate.split("/");

        int[] currDate = new int[3];
        currDate[0] = Integer.parseInt(tokensCurrDate[0]);
        currDate[1] = Integer.parseInt(tokensCurrDate[1]);
        currDate[2] = Integer.parseInt(tokensCurrDate[2]);

        int[] resDate = new int[3];
        resDate[0] = Integer.parseInt(tokensDate[0]);
        resDate[1] = Integer.parseInt(tokensDate[1]);
        resDate[2] = Integer.parseInt(tokensDate[2]);

        if (resDate[2] > currDate[2]) return true;
        else if (resDate[2] == currDate[2]) {
            if (resDate[1] > currDate[1]) return true;
            else if (resDate[1] == currDate[1]) {
                if (resDate[0] > currDate[0]) return true;
                else return false;
            }
            else return false;
        }
        else return false;
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
