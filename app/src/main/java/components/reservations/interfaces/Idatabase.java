package components.reservations.interfaces;

import android.widget.DatePicker;

import components.reservations.Reservation;

public interface Idatabase {
    void writeToDatabase(Reservation reservation);
    void reserve();

    void onDateSet(DatePicker view, int year, int month, int dayOfMonth);
}
