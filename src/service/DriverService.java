package service;

import model.Driver;
import java.util.ArrayList;
import java.util.List;

public class DriverService {

    // Store Drivers using Array List
    private List<Driver> drivers = new ArrayList<>();

    // Adding Drivers
    public void addDriver(Driver driver) {

        drivers.add(driver);

        System.out.println("Driver added successfully");
    }

    // Displaying All Drivers
    public void displayDrivers() {

        System.out.println("\n----- ALL DRIVERS -----");

        for (Driver driver : drivers) {

            System.out.println("Driver ID: " + driver.getUserId());

            System.out.println("Driver Name: " + driver.getName());

            System.out.println("License Number: " + driver.getLicenseNumber());

            System.out.println("----------------------");
        }
    }

    // Total Numbers of Drivers
    public int getTotalDrivers() {

        return drivers.size();
    }
    public List<Driver> getDrivers() {

        return drivers;
    }
}