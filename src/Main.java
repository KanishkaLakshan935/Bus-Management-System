import model.Driver;
import model.User;
import service.DriverService;
import filehandling.DriverFileHandler;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Create DriverService Object
        DriverService driverService = new DriverService();

        // Create Driver Objects
        Driver driver1 = new Driver(
                1, "Kamal", "kamal@gmail.com",
                "12345", "0771234567", "B123456"
        );

        Driver driver2 = new Driver(
                2, "Nimal", "nimal@gmail.com",
                "67890", "0711234567", "B654321"
        );

        // Add Drivers to List
        driverService.addDriver(driver1);
        driverService.addDriver(driver2);

        // Display Drivers
        driverService.displayDrivers();

        // Display Total Drivers
        System.out.println("Total Drivers: "
                + driverService.getTotalDrivers());

        // Create File Handler Object
        DriverFileHandler fileHandler = new DriverFileHandler();

        // Save Drivers to Text File
        fileHandler.saveDrivers(driverService.getDrivers());

        // Read Drivers from Text File
        fileHandler.readDrivers();

        // Polymorphism
        User user = new Driver(
                3,
                "Sunil",
                "sunil@gmail.com",
                "11111",
                "0712345678",
                "B789456"
        );

        System.out.println("\n----- POLYMORPHISM TEST -----");

        user.displayRole();

        // Load Drivers from Text File
        List<Driver> loadedDrivers = fileHandler.loadDrivers();

// Display Loaded Drivers
        System.out.println("\n----- LOADED DRIVERS -----");

        for (Driver driver : loadedDrivers) {

            System.out.println("Driver ID: " + driver.getUserId());

            System.out.println("Driver Name: " + driver.getName());

            System.out.println("Driver Email: " + driver.getEmail());

            System.out.println("Driver Phone: " + driver.getPhone());

            System.out.println("License Number: " + driver.getLicenseNumber());

            System.out.println("----------------------");
        }

    }
}