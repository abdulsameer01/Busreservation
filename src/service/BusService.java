package service;
import dao.BusDAO;
import dao.BookingDAO;
import models.Bus;
import java.sql.SQLException;
import java.util.List;

public class BusService {
    private BusDAO busDAO;
    private BookingDAO bookingDAO;

    public BusService(BusDAO busDAO, BookingDAO bookingDAO){
        this.busDAO = busDAO;
        this.bookingDAO = bookingDAO;
    }

    public List<Bus> listBuses() throws SQLException{
        return busDAO.getAllBuses();
    }

    public void initializeSeats(int busId, int capacity) throws SQLException{
        busDAO.initializeSeats(busId,capacity);
    }

    public boolean reserveSeat(int userId,int busId,int seatNo) throws SQLException{
        return bookingDAO.bookSeat(userId,busId,seatNo);
    }
    public Bus getBusById(int busId) throws SQLException {
        return busDAO.getBusById(busId);
    }

}
