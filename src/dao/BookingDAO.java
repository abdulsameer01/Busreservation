package dao;

import java.sql.*;

public class BookingDAO {
    private Connection conn;

    public BookingDAO(Connection conn) {
        this.conn = conn;
    }

    // Book a seat
    public boolean bookSeat(int userId, int busId, int seatNo) throws SQLException {
        // Step 1: Check if seat exists & available
        String checkSql = "SELECT booked, seat_id FROM seats WHERE bus_id=? AND seat_no=?";
        PreparedStatement psCheck = conn.prepareStatement(checkSql);
        psCheck.setInt(1, busId);
        psCheck.setInt(2, seatNo);
        ResultSet rs = psCheck.executeQuery();

        int seatId = -1;
        if (rs.next()) {
            if (rs.getBoolean("booked")) {
                return false; // Seat already booked
            }
            seatId = rs.getInt("seat_id");
        } else {
            return false; // Seat does not exist
        }

        // Step 2: Mark seat as booked
        String updateSql = "UPDATE seats SET booked=? WHERE seat_id=?";
        PreparedStatement psUpdate = conn.prepareStatement(updateSql);
        psUpdate.setBoolean(1, true);
        psUpdate.setInt(2, seatId);
        psUpdate.executeUpdate();

        // Step 3: Insert reservation record with status 'active'
        String insertReservation = "INSERT INTO reservations(user_id, bus_id, seat_id, seat_number, status) VALUES(?,?,?,?,?)";
        PreparedStatement psReservation = conn.prepareStatement(insertReservation);
        psReservation.setInt(1, userId);
        psReservation.setInt(2, busId);
        psReservation.setInt(3, seatId);
        psReservation.setInt(4, seatNo);
        psReservation.setString(5, "active"); // new column
        psReservation.executeUpdate();

        return true;
    }

    // View all bookings for a specific user
    public void viewUserBookings(int userId) throws SQLException {
        String sql = "SELECT r.reservation_id, b.bus_name, b.from_place, b.to_place, r.seat_number, r.booking_time, r.status " +
                "FROM reservations r " +
                "JOIN buses b ON r.bus_id = b.bus_id " +
                "WHERE r.user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        System.out.println("\nYour Bookings:");
        boolean hasBooking = false;
        while (rs.next()) {
            hasBooking = true;
            System.out.println("Reservation ID: " + rs.getInt("reservation_id") +
                    ", Bus: " + rs.getString("bus_name") +
                    " (" + rs.getString("from_place") + " -> " + rs.getString("to_place") + ")" +
                    ", Seat: " + rs.getInt("seat_number") +
                    ", Time: " + rs.getTimestamp("booking_time") +
                    ", Status: " + rs.getString("status"));
        }
        if (!hasBooking) {
            System.out.println("No bookings found.");
        }
        System.out.println();
    }

    // Cancel a reservation by reservation_id (marks as cancelled instead of deleting)
    public boolean cancelBooking(int reservationId) throws SQLException {
        // Step 1: Get seat_id for this reservation
        String selectSql = "SELECT seat_id FROM reservations WHERE reservation_id=?";
        PreparedStatement psSelect = conn.prepareStatement(selectSql);
        psSelect.setInt(1, reservationId);
        ResultSet rs = psSelect.executeQuery();

        if (!rs.next()) {
            return false; // Reservation does not exist
        }
        int seatId = rs.getInt("seat_id");

        // Step 2: Mark the seat as available again
        String updateSeat = "UPDATE seats SET booked=? WHERE seat_id=?";
        PreparedStatement psUpdate = conn.prepareStatement(updateSeat);
        psUpdate.setBoolean(1, false);
        psUpdate.setInt(2, seatId);
        psUpdate.executeUpdate();

        // Step 3: Update reservation status to 'cancelled'
        String updateReservation = "UPDATE reservations SET status=? WHERE reservation_id=?";
        PreparedStatement psReservation = conn.prepareStatement(updateReservation);
        psReservation.setString(1, "cancelled");
        psReservation.setInt(2, reservationId);
        psReservation.executeUpdate();

        return true;
    }

    // Show available seats for a bus
    public void showAvailableSeats(int busId) throws SQLException {
        String sql = "SELECT seat_no, booked FROM seats WHERE bus_id=? ORDER BY seat_no";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, busId);
        ResultSet rs = ps.executeQuery();

        System.out.println("Available seats for bus ID " + busId + ":");
        while (rs.next()) {
            int seatNo = rs.getInt("seat_no");
            boolean booked = rs.getBoolean("booked");
            System.out.print((booked ? "[X]" : "[ ]") + seatNo + "  ");
        }
        System.out.println("\n[X] = Booked, [ ] = Available\n");
    }

    // View booking history with optional filter by status ('active'/'cancelled')
    public void viewBookingHistory(int userId, String statusFilter) throws SQLException {
        String sql = "SELECT r.reservation_id, b.bus_name, b.from_place, b.to_place, r.seat_number, r.booking_time, r.status " +
                "FROM reservations r " +
                "JOIN buses b ON r.bus_id = b.bus_id " +
                "WHERE r.user_id = ?";
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql += " AND r.status = ?";
        }
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        if (statusFilter != null && !statusFilter.isEmpty()) {
            ps.setString(2, statusFilter);
        }
        ResultSet rs = ps.executeQuery();

        System.out.println("\nBooking History:");
        boolean hasBooking = false;
        while (rs.next()) {
            hasBooking = true;
            System.out.println("Reservation ID: " + rs.getInt("reservation_id") +
                    ", Bus: " + rs.getString("bus_name") +
                    " (" + rs.getString("from_place") + " -> " + rs.getString("to_place") + ")" +
                    ", Seat: " + rs.getInt("seat_number") +
                    ", Time: " + rs.getTimestamp("booking_time") +
                    ", Status: " + rs.getString("status"));
        }
        if (!hasBooking) {
            System.out.println("No bookings found.");
        }
        System.out.println();
    }
}
