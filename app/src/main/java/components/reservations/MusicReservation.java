package components.reservations;

import androidx.annotation.NonNull;

public final class MusicReservation extends Reservation {

    private boolean keyboard = false;
    private boolean drumBox  = false;
    private boolean bass     = false;
    private boolean guitar   = false;

    public MusicReservation(){}

    public MusicReservation(String user, String email, String date,
                            boolean keyboard, boolean drumBox, boolean bass, boolean guitar){
        super(user, email, date);
        this.keyboard = keyboard;
        this.drumBox = drumBox;
        this.bass = bass;
        this.guitar = guitar;
    }

    public boolean isKeyboard() {
        return keyboard;
    }

    public void setKeyboard(boolean keyboard) {
        this.keyboard = keyboard;
    }

    public boolean isDrumBox() {
        return drumBox;
    }

    public void setDrumBox(boolean drumBox) {
        this.drumBox = drumBox;
    }

    public boolean isBass() {
        return bass;
    }

    public void setBass(boolean bass) {
        this.bass = bass;
    }

    public boolean isGuitar() {
        return guitar;
    }

    public void setGuitar(boolean guitar) {
        this.guitar = guitar;
    }
}
