package model;
import interfaces.DriverActions;

public class Driver extends User implements DriverActions {

    private String licenseNumber;

    // Constructor
    public Driver(int userId, String name, String email,
                  String password, String phone,
                  String licenseNumber) {

        super(userId, name, email, password, phone);

        this.licenseNumber = licenseNumber;
    }

    // Getter
    public String getLicenseNumber() {
        return licenseNumber;
    }

    // Setter
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    // View Assigned Bus
    public void viewAssignedBus() {
        System.out.println("Viewing assigned bus...");
    }

    // View Route
    public void viewRoute() {
        System.out.println("Viewing assigned route...");
    }

    // Update Trip Status
    public void updateTripStatus() {
        System.out.println("Trip status updated.");
    }

    // Report Breakdown
    public void reportBreakdown() {
        System.out.println("Bus breakdown reported.");
    }

    // Report Accident
    public void reportAccident() {
        System.out.println("Accident reported.");
    }

    // Request Replacement Bus
    public void requestReplacementBus() {
        System.out.println("Replacement bus requested.");
    }
}