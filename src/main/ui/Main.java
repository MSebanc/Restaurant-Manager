package ui;

import ui.manager.RestaurantManager;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            new RestaurantManager();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
