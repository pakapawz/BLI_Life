package components.reservations;

public final class CourtReservation extends Reservation {
    private String reservationDate;

    public CourtReservation(){}

    public CourtReservation(String reservationDate){
        super();
        this.reservationDate = reservationDate;
    }

    public void setReservationDate(String reservationDate){
        this.reservationDate = reservationDate;
    }

    public String getReservationDate(){
        return reservationDate;
    }
}
