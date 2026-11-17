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
                "CON001"
        );

        if (conductor.login("demo123")) {
            System.out.println("Login successful");
            conductor.viewProfile();

            User user = conductor;
            user.displayRole();

            conductor.logout();
        } else {
            System.out.println("Incorrect password");
        }
    }
}