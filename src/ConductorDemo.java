import model.Conductor;
import model.Ticket;
import model.User;
import service.ConductorService;
import model.LostItemReport;

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

        try {
            System.out.printf(
                    "Commission at 10%%: Rs. %.2f%n",
                    service.calculateCommission(10)
            );

            LostItemReport report = service.reportLostItem(
                    "P001",
                    "Black wallet"
            );

            System.out.println(
                    "Created lost item report: " + report.getReportId()
            );

            service.viewLostItemReports();

            service.updateLostItemStatus(report.getReportId(), "FOUND");
            service.viewLostItemReports();

            service.updateLostItemStatus(report.getReportId(), "RETURNED");
            service.viewLostItemReports();

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            service.calculateCommission(-5);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            service.updateLostItemStatus(999, "FOUND");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        conductor.logout();
    }
}