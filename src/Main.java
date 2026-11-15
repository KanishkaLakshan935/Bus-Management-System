import model.Driver;

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

        driver.updateTripStatus();

        driver.reportBreakdown();

        driver.reportAccident();

        driver.requestReplacementBus();

    }
}