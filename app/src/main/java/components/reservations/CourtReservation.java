package components.reservations;

import androidx.annotation.NonNull;

public final class CourtReservation extends Reservation {

    public CourtReservation(){}

    public CourtReservation(String user, String email, String date){
        super(user, email, date);
    }
}
