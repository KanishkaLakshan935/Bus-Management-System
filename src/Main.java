import model.Driver;

public class Main {

    public static void main(String[] args) {

        // Create Driver Object
        Driver driver = new Driver(
                1,
                "Kamal",
                "kamal@gmail.com",
                "12345",
                "0771234567",
                "B123456"
        );

        // Display Driver Details
        System.out.println("===== DRIVER DETAILS =====");

        driver.viewProfile();

        System.out.println("License Number: " + driver.getLicenseNumber());

        // Driver Methods
        System.out.println("\n===== DRIVER ACTIVITIES =====");

        driver.login("12345");

        driver.viewAssignedBus();

        driver.viewRoute();

        driver.updateTripStatus();

        driver.reportBreakdown();

        driver.reportAccident();

        driver.requestReplacementBus();

        driver.logout();
    }
}