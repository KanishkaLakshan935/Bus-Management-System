import model.Conductor;
import model.Ticket;
import model.User;
import service.ConductorService;

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

        ConductorService service = new ConductorService(conductor);

        try {
            service.recordBoarding("P001");
            service.recordBoarding("P002");

            Ticket ticket = service.issueTicket("P001", 150);

            System.out.println(
                    "Issued ticket: " + ticket.getTicketId()
            );

            System.out.println(
                    "Passengers inside: " + service.getPassengerCount()
            );

            System.out.println(
                    "Without tickets: "
                            + service.getPassengersWithoutTickets()
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            service.issueTicket("P001", 150);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            service.issueTicket("P002", 200);

            service.recordExiting("P001");

            System.out.println(
                    "Passengers after exit: " + service.getPassengerCount()
            );

            System.out.println(
                    "Available seats: " + service.getAvailableSeats()
            );

            service.recordBoarding("P001");

            System.out.println(
                    "Without tickets after reboarding: "
                            + service.getPassengersWithoutTickets()
            );

            service.issueTicket("P001", 100);

            service.viewTicketSales();

            System.out.println(
                    "Without tickets now: "
                            + service.getPassengersWithoutTickets()
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        conductor.logout();
    }
}