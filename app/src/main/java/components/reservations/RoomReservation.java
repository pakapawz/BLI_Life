package components.reservations;

import androidx.annotation.NonNull;

public final class RoomReservation extends Reservation {
    private String roomNo;

    public RoomReservation(){}

    public RoomReservation(String user, String email, String date, String roomNo){
        super(user, email, date);
        this.roomNo = roomNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + "room " + roomNo;
    }
}
