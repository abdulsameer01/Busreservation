// Booking.java
package models;

public class Booking {
    private int id;
    private int userId;
    private int busId;
    private int seatNo;
    private String status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getBusId() { return busId; }
    public void setBusId(int busId) { this.busId = busId; }
    public int getSeatNo() { return seatNo; }
    public void setSeatNo(int seatNo) { this.seatNo = seatNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
