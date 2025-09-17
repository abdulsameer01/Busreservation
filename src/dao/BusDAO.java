package dao;

import models.Bus;
import java.sql.*;
import java.util.*;

public class BusDAO {
    private Connection conn;

    public BusDAO(Connection conn) {
        this.conn = conn;
    }

    // Fetch all buses from database
    public List<Bus> getAllBuses() throws SQLException {
        String sql = "SELECT * FROM buses"; // Make sure buses table has from_place, to_place, fare columns
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Bus> buses = new ArrayList<>();

        while (rs.next()) {
            Bus bus = new Bus();
            bus.setId(rs.getInt("bus_id"));
            bus.setName(rs.getString("bus_name"));
            bus.setRouteFrom(rs.getString("from_place"));
            bus.setRouteTo(rs.getString("to_place"));
            bus.setCapacity(rs.getInt("total_seats")); // adjust if your column name is different
            bus.setFare(rs.getDouble("fare")); // fetch fare
            buses.add(bus);
        }
        return buses;
    }

    // Print all buses in desired format
    public void viewBuses() throws SQLException {
        List<Bus> buses = getAllBuses();
        System.out.println("\nAvailable Buses:");
        for (Bus bus : buses) {
            System.out.println(bus.getId() + " - " + bus.getName() +
                    " (" + bus.getRouteFrom() + " → " + bus.getRouteTo() + ")" +
                    ", Fare: $" + bus.getFare());
        }
        System.out.println();
    }

    // Initialize seats for a bus (if not already initialized)
    public void initializeSeats(int busId, int capacity) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM seats WHERE bus_id=?";
        PreparedStatement psCheck = conn.prepareStatement(checkSql);
        psCheck.setInt(1, busId);
        ResultSet rs = psCheck.executeQuery();
        rs.next();
        if (rs.getInt(1) == 0) {
            String insertSeat = "INSERT INTO seats(bus_id, seat_no) VALUES(?,?)";
            PreparedStatement ps = conn.prepareStatement(insertSeat);
            for (int i = 1; i <= capacity; i++) {
                ps.setInt(1, busId);
                ps.setInt(2, i);
                ps.executeUpdate();
            }
        }
    }

    // Update a bus route (admin function)
    public boolean updateBusRoute(int busId, String fromPlace, String toPlace) throws SQLException {
        String sql = "UPDATE buses SET from_place = ?, to_place = ? WHERE bus_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, fromPlace);
        ps.setString(2, toPlace);
        ps.setInt(3, busId);

        int rowsUpdated = ps.executeUpdate();
        return rowsUpdated > 0;
    }

    // Update fare for a bus (admin function)
    public boolean updateBusFare(int busId, double fare) throws SQLException {
        String sql = "UPDATE buses SET fare = ? WHERE bus_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, fare);
        ps.setInt(2, busId);

        int rowsUpdated = ps.executeUpdate();
        return rowsUpdated > 0;
    }

    // Get bus by ID (optional helper method)
    public Bus getBusById(int busId) throws SQLException {
        String sql = "SELECT * FROM buses WHERE bus_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, busId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Bus bus = new Bus();
            bus.setId(rs.getInt("bus_id"));
            bus.setName(rs.getString("bus_name"));
            bus.setRouteFrom(rs.getString("from_place"));
            bus.setRouteTo(rs.getString("to_place"));
            bus.setCapacity(rs.getInt("total_seats"));
            bus.setFare(rs.getDouble("fare")); // fetch fare
            return bus;
        }
        return null; // bus not found
    }
}
