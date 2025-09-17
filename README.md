# 🚌 Bus Reservation System

A simple Java-based **Bus Reservation System** that allows users to book bus tickets, manage reservations, and view available buses.  
This project demonstrates the use of **Java OOP concepts, DAO pattern, JDBC database connectivity, and service layers** to build a structured application.

---

## 📌 Features
- User registration and login  
- View available buses and schedules  
- Book tickets for a bus  
- Cancel bookings  
- Store and fetch data using database connectivity (JDBC)  
- DAO and Service layer architecture  

---

## 🛠️ Technologies Used
- **Java** (Core + OOP concepts)  
- **JDBC** (Database Connectivity)  
- **MySQL / any RDBMS** (for data storage)  
- **IntelliJ IDEA** (IDE used)  

---

## 📂 Project Structure
src/
├── dao/
│ ├── BookingDAO.java
│ ├── BusDAO.java
│ └── UserDAO.java
├── db/
│ └── DBConnection.java
├── models/
│ ├── Booking.java
│ └── User.java
├── service/
│ └── BusService.java
└── Main.java


- **models/** → Contains entity classes (`User`, `Booking`)  
- **dao/** → Handles database operations (Data Access Object)  
- **service/** → Business logic (`BusService`)  
- **db/** → Database connection utility (`DBConnection`)  
- **Main.java** → Entry point of the application  

---

## 🚀 How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/Bus-Reservation-System.git
