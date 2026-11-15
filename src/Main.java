import model.Driver;
import model.Driver;
import exceptions.InvalidTripStatusException;

public class Main {

    public static void main(String[] args) {

        Driver driver = new Driver(
                1,
                "Kamal",
                "kamal@gmail.com",
                "12345",
                "0771234567",
                "B123456"
        );

        System.out.println("===== INTERFACE TEST =====");

        driver.viewAssignedBus();

        driver.viewRoute();

        try {

            driver.updateTripStatus("Started");

            driver.updateTripStatus("Completed");

            driver.updateTripStatus("WrongStatus");

        } catch (InvalidTripStatusException e) {

            System.out.println("Error: " + e.getMessage());

        }

        driver.reportBreakdown();

        driver.reportAccident();

        driver.requestReplacementBus();

    }
}