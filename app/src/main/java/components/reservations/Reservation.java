package components.reservations;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Reservation {
    private String creationDate;
    private String reservingUsername;
    private String reservingEmail;

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Reservation(){
        this.creationDate = getCurrentDate();
        this.reservingEmail = "";
        this.reservingUsername = "";
    }

    public String getCreationDate(){
        return creationDate;
    }

    public String getReservingUsername(){
        return reservingUsername;
    }

    public String getReservingEmail(){
        return reservingEmail;
    }

    public void setReservingUsername(String reservingUsername){
        this.reservingUsername = reservingUsername;
    }

    public void setReservingEmail(String reservingEmail){
        this.reservingEmail = reservingEmail;
    }
}
