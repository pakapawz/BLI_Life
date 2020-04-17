package components.reservations;

public final class RoomReservation extends Reservation {
    private String reservationDate;
    private String roomName;

    public RoomReservation(){}

    public RoomReservation(String reservationDate, String roomName){
        super();
        this.reservationDate = reservationDate;
        this.roomName = roomName;
    }

    public void setReservationDate(String reservationDate){
        this.reservationDate = reservationDate;
    }

    public void setRoomName(String roomName){
        this.roomName = roomName;
    }

    public String getReservationDate(){
        return reservationDate;
    }

    public String getRoomName(){
        return roomName;
    }
}
