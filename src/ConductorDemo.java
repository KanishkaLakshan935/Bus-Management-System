import model.Conductor;
import model.User;

public class ConductorDemo {

    public static void main(String[] args) {

        Conductor conductor = new Conductor(
                845,
                "Gimhan",
                "gimhan@example.com",
                "demo123",
                "0771234567",
                "CON001",
                2
        );

        if (!conductor.login("demo123")) {
            System.out.println("Incorrect password");
            return;
        }

        System.out.println("Login successful");

        User user = conductor;
        user.displayRole();

        try {
            conductor.recordBoarding("P001");
            conductor.recordBoarding("P002");

            System.out.println(
                    "Passengers inside: " + conductor.getPassengerCount()
            );

            System.out.println(
                    "Available seats: " + conductor.getAvailableSeats()
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            conductor.recordBoarding("P003");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            conductor.recordExiting("P001");

            System.out.println(
                    "Passengers after exit: " + conductor.getPassengerCount()
            );

            System.out.println(
                    "Available seats after exit: " + conductor.getAvailableSeats()
            );

            conductor.recordBoarding("P003");

            System.out.println(
                    "Passengers now: " + conductor.getOnboardPassengerIds()
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        conductor.logout();
    }
}