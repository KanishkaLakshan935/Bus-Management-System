package interfaces;
import exceptions.InvalidTripStatusException;

public interface DriverActions {

    void viewAssignedBus();

    void viewRoute();

    void updateTripStatus(String status)
            throws InvalidTripStatusException;

    void reportBreakdown();

    void reportAccident();

    void requestReplacementBus();

}