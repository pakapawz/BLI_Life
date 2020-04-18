package components.reservations.interfaces;

import components.reservations.Reservation;

public interface Ivalidations {
    boolean validateReservation(Reservation reservation);
    boolean checkAvailability(Reservation reservation);
    void showErrorDialog();
}
