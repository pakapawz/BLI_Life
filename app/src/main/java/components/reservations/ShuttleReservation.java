package components.reservations;

public class ShuttleReservation extends Reservation {
    private boolean toBLI = false;
    private boolean fromBLI = false;

    public ShuttleReservation(){}

    public ShuttleReservation(String user, String email, String date,
                              boolean toBLI, boolean fromBLI){
        super(user, email, date);
        this.toBLI = toBLI;
        this.fromBLI = fromBLI;
    }

    public boolean isFromBLI() {
        return fromBLI;
    }

    public void setFromBLI(boolean fromBLI) {
        this.fromBLI = fromBLI;
    }

    public boolean isToBLI() {
        return toBLI;
    }

    public void setToBLI(boolean toBLI) {
        this.toBLI = toBLI;
    }
}
