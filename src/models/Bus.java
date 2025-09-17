package models;

public class Bus {
    private int id;
    private String name;
    private String routeFrom;
    private String routeTo;
    private int capacity;
    private double fare;  // new field

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRouteFrom() { return routeFrom; }
    public void setRouteFrom(String routeFrom) { this.routeFrom = routeFrom; }

    public String getRouteTo() { return routeTo; }
    public void setRouteTo(String routeTo) { this.routeTo = routeTo; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public double getFare() { return fare; }          // getter
    public void setFare(double fare) { this.fare = fare; } // setter
}
