package components.reservations;

public final class MusicReservation extends Reservation {
    private boolean isKeyboardReserved;
    private boolean isDrumBoxReserved;
    private boolean isBassReserved;
    private boolean isGuitarReserved;
    private String reservationDate;

    public MusicReservation(){}

    public MusicReservation(String reservationDate,
                            boolean isKeyboardReserved, boolean isDrumBoxReserved,
                            boolean isBassReserved, boolean isGuitarReserved){
        super();
        this.reservationDate = reservationDate;
        this.isBassReserved = isBassReserved;
        this.isGuitarReserved = isGuitarReserved;
        this.isDrumBoxReserved = isDrumBoxReserved;
        this.isKeyboardReserved = isKeyboardReserved;
    }
}
