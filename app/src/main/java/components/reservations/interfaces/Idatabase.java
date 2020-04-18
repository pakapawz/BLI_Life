package components.reservations.interfaces;

import components.reservations.Reservation;

public interface Idatabase {
    void writeToDatabase(Reservation reservation);
    void reserve();
}
