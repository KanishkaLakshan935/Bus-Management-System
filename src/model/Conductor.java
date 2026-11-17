package model;

public class Conductor extends User {

    private String employeeId;

    public Conductor(int userId, String name, String email,
                     String password, String phone, String employeeId) {

        super(userId, name, email, password, phone);
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public void displayRole() {
        System.out.println("User Role: Conductor");
    }

    @Override
    public void viewProfile() {
        super.viewProfile();
        System.out.println("Employee ID: " + employeeId);
    }
}