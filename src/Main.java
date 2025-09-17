import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dao.*;
import models.*;
import service.BusService;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = db.DBConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(conn);
            BusDAO busDAO = new BusDAO(conn);
            BookingDAO bookingDAO = new BookingDAO(conn);
            BusService busService = new BusService(busDAO, bookingDAO);

            // Initialize seats for buses
            List<Bus> buses = busService.listBuses();
            for (Bus b : buses) {
                busService.initializeSeats(b.getId(), b.getCapacity());
            }

            Scanner sc = new Scanner(System.in);
            System.out.println("1. Register\n2. Login");
            int choice = sc.nextInt(); sc.nextLine();

            User currentUser = null;

            if (choice == 1) {
                System.out.print("Name: ");
                String name = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                System.out.print("Password: ");
                String pass = sc.nextLine();
                userDAO.registerUser(name, email, pass);
                System.out.println("Registered successfully!");
            }

            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();
            currentUser = userDAO.login(email, pass);

            if (currentUser == null) {
                System.out.println("Invalid login!");
                sc.close();
                return;
            }

            System.out.println("Welcome " + currentUser.getName());

            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println("\n1. View Buses");
                System.out.println("2. Book Seat");
                System.out.println("3. My Bookings");
                System.out.println("4. Cancel Booking");
                System.out.println("5. Exit");
                System.out.println("6. Booking History"); // new feature
                System.out.print("Choose an option: ");
                choice = sc.nextInt(); sc.nextLine();

                switch (choice) {
                    case 1:
                        buses = busService.listBuses();
                        for (Bus b : buses) {
                            System.out.println(b.getId() + " - " + b.getName() +
                                    " (" + b.getRouteFrom() + " -> " + b.getRouteTo() + ")");
                        }
                        break;
                    case 2:
                        System.out.print("Bus ID: ");
                        int busId = sc.nextInt();
                        sc.nextLine();

                        // Show available seats
                        bookingDAO.showAvailableSeats(busId);

                        System.out.print("Seat No: ");
                        int seatNo = sc.nextInt();

                        // Fetch fare automatically from bus
                        Bus bookedBus = busService.getBusById(busId);
                        if (bookedBus == null) {
                            System.out.println("Invalid Bus ID!");
                            break;
                        }
                        double fare = bookedBus.getFare();
                        System.out.println("Ticket Fare: $" + fare);
                        System.out.println("Payment of $" + fare + " received successfully!");

                        // Reserve seat
                        boolean booked = busService.reserveSeat(currentUser.getId(), busId, seatNo);
                        if (booked) {
                            System.out.println("Booking successful!");
                            System.out.println("Bus: " + bookedBus.getName() +
                                    " (" + bookedBus.getRouteFrom() + " -> " + bookedBus.getRouteTo() +
                                    "), Seat No: " + seatNo);
                        } else {
                            System.out.println("Seat not available!");
                        }
                        break;


                    case 3:
                        bookingDAO.viewUserBookings(currentUser.getId());
                        break;

                    case 4:
                        System.out.print("Enter Reservation ID to cancel: ");
                        int resId = sc.nextInt();
                        if (bookingDAO.cancelBooking(resId)) {
                            System.out.println("Booking cancelled successfully!");
                        } else {
                            System.out.println("Reservation ID not found.");
                        }
                        break;

                    case 5:
                        loggedIn = false;
                        System.out.println("Logged out.");
                        break;

                    case 6:
                        System.out.print("Enter filter (active/cancelled) or press Enter for all: ");
                        String filter = sc.nextLine().trim();
                        if(filter.isEmpty()) filter = null;
                        bookingDAO.viewBookingHistory(currentUser.getId(), filter);
                        break;

                    default:
                        System.out.println("Invalid option.");
                }
            }

            System.out.println("Thank you for using the Bus Reservation System!");
            sc.close();

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}
