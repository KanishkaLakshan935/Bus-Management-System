import filehandling.ConductorFileHandler;
import model.Conductor;
import model.LostItemReport;
import model.Ticket;
import service.ConductorService;
import java.io.IOException;
import java.util.Scanner;

public class ConductorApp {

    private static String read(Scanner input, String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    private static void showMenu() {
        System.out.println("\n--- CONDUCTOR MENU ---");
        System.out.println("1. Record boarding");
        System.out.println("2. Record exiting");
        System.out.println("3. Issue ticket");
        System.out.println("4. View passengers and seats");
        System.out.println("5. View ticket sales");
        System.out.println("6. Calculate commission");
        System.out.println("7. View passengers without tickets");
        System.out.println("8. Report lost item");
        System.out.println("9. View lost item reports");
        System.out.println("10. Update lost item status");
        System.out.println("11. Save records");
        System.out.println("0. Save and exit");
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Conductor profile = new Conductor(
                845,
                "Gimhan",
                "gimhan@example.com",
                "demo123",
                "0771234567",
                "CON001",
                40
        );

        System.out.println("Demo login - conductor: Gimhan");

        if (!profile.login(read(input, "Password: "))) {
            System.out.println("Incorrect password");
            return;
        }

        ConductorFileHandler files = new ConductorFileHandler(
                "data/conductor_845.properties"
        );

        ConductorService service;

        try {
            service = files.load(profile);

            System.out.println(
                    "Ready. Passengers inside: " + service.getPassengerCount()
            );

        } catch (IOException e) {
            System.out.println("Cannot load records: " + e.getMessage());
            return;
        }

        while (true) {
            showMenu();
            System.out.print("Choice: ");

            if (!input.hasNextLine()) {
                try {
                    files.save(service);
                } catch (IOException e) {
                    System.out.println("Save failed: " + e.getMessage());
                }
                return;
            }

            try {
                int choice = Integer.parseInt(input.nextLine().trim());

                switch (choice) {
                    case 1:
                        service.recordBoarding(
                                read(input, "Passenger ID: ")
                        );
                        System.out.println("Boarding recorded.");
                        break;

                    case 2:
                        service.recordExiting(
                                read(input, "Passenger ID: ")
                        );
                        System.out.println("Exit recorded.");
                        break;

                    case 3:
                        String passenger = read(input, "Passenger ID: ");

                        int fare = Integer.parseInt(
                                read(input, "Fare (whole rupees): ")
                        );

                        Ticket ticket = service.issueTicket(passenger, fare);

                        System.out.println(
                                "Ticket issued. ID: " + ticket.getTicketId()
                        );
                        break;

                    case 4:
                        System.out.println(
                                "Passengers: " + service.getPassengerCount()
                        );
                        System.out.println(
                                "Available seats: " + service.getAvailableSeats()
                        );
                        break;

                    case 5:
                        service.viewTicketSales();
                        break;

                    case 6:
                        double rate = Double.parseDouble(
                                read(input, "Commission percentage: ")
                        );

                        System.out.printf(
                                "Commission: Rs. %.2f%n",
                                service.calculateCommission(rate)
                        );
                        break;

                    case 7:
                        System.out.println(
                                "Without tickets: "
                                        + service.getPassengersWithoutTickets()
                        );
                        break;

                    case 8:
                        String owner = read(input, "Passenger ID: ");
                        String description = read(input, "Item description: ");

                        LostItemReport report = service.reportLostItem(
                                owner, description
                        );

                        System.out.println(
                                "Report created. ID: " + report.getReportId()
                        );
                        break;

                    case 9:
                        service.viewLostItemReports();
                        break;

                    case 10:
                        int id = Integer.parseInt(
                                read(input, "Report ID: ")
                        );

                        String status = read(
                                input, "New status (FOUND / RETURNED): "
                        );

                        service.updateLostItemStatus(id, status);
                        System.out.println("Status updated.");
                        break;

                    case 11:
                        files.save(service);
                        System.out.println("Records saved.");
                        break;

                    case 0:
                        files.save(service);
                        System.out.println("Records saved.");
                        profile.logout();
                        return;

                    default:
                        System.out.println("Choose a number from 0 to 11.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());

            } catch (IOException e) {
                System.out.println(
                        "Save failed. Records are still in memory: "
                                + e.getMessage()
                );
            }
        }
    }
}