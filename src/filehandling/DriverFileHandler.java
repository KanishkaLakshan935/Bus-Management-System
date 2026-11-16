package filehandling;

import model.Driver;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class DriverFileHandler {

    private final String fileName = "data/drivers.txt";

    // Saving Drivers to Text File
    public void saveDrivers(List<Driver> drivers) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(fileName))) {

            for (Driver driver : drivers) {

                writer.write(
                        driver.getUserId() + "," +
                                driver.getName() + "," +
                                driver.getEmail() + "," +
                                driver.getPassword() + "," +
                                driver.getPhone() + "," +
                                driver.getLicenseNumber()
                );

                writer.newLine();
            }

            System.out.println("Drivers saved successfully");

        } catch (IOException e) {

            System.out.println("Error saving drivers: " + e.getMessage());
        }
    }

    // Read Drivers information from Text File
    public void readDrivers() {

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {

            String line;

            System.out.println("\n----- SAVED DRIVERS -----");

            while ((line = reader.readLine()) != null) {

                System.out.println(line);
            }

        } catch (IOException e) {

            System.out.println("Error reading drivers: " + e.getMessage());
        }
    }
}