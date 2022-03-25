package ui.gui;

import persistence.JsonReader;
import ui.exceptions.InvalidNameEnteredException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

// Frame for when a table is being added to restaurant
public class NewFrame extends JFrame implements ActionListener {

    private RestaurantMangerGUI previousFrame;
    private JPanel newPanel;
    private JPanel store1Panel;
    private JPanel store2Panel;
    private JPanel store3Panel;
    private JPanel store4Panel;
    private JPanel store5Panel;
    private JPanel quitPanel;
    private JButton store1Button;
    private JButton store2Button;
    private JButton store3Button;
    private JButton store4Button;
    private JButton store5Button;
    private JButton quitButton;

    // EFFECTS: Constructs Frame
    public NewFrame(RestaurantMangerGUI previousFrame) {
        super("Create New Restaurant");

        this.previousFrame = previousFrame;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createButtons();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(newPanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes JButtons
    private void createButtons() {
        store1Button = new JButton("Current Restaurant: "
                + getRestaurantNameFromJson("./data/json/store1.json"));
        store1Button.setActionCommand("store1");
        store1Button.addActionListener(this);
        store2Button = new JButton("Current Restaurant: "
                + getRestaurantNameFromJson("./data/json/store2.json"));
        store2Button.setActionCommand("store2");
        store2Button.addActionListener(this);
        store3Button = new JButton("Current Restaurant: "
                + getRestaurantNameFromJson("./data/json/store3.json"));
        store3Button.setActionCommand("store3");
        store3Button.addActionListener(this);
        store4Button = new JButton("Current Restaurant: "
                + getRestaurantNameFromJson("./data/json/store4.json"));
        store4Button.setActionCommand("store4");
        store4Button.addActionListener(this);
        store5Button = new JButton("Current Restaurant: "
                + getRestaurantNameFromJson("./data/json/store5.json"));
        store5Button.setActionCommand("store5");
        store5Button.addActionListener(this);
        quitButton = new JButton("Back");
        quitButton.setActionCommand("back");
        quitButton.addActionListener(this);
    }

    // EFFECTS: returns restaurant name from file
    public String getRestaurantNameFromJson(String jsonStore) {
        JsonReader jsonReader = new JsonReader(jsonStore);

        try {
            return jsonReader.read().getName();
        } catch (IOException e) {
            System.err.println("Unable to read from file: " + jsonStore);
            return "Could Not Find Restaurant";
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes JPanels
    private void createPanels() {
        newPanel = new JPanel();
        store1Panel = new JPanel();
        store2Panel = new JPanel();
        store3Panel = new JPanel();
        store4Panel = new JPanel();
        store5Panel = new JPanel();
        quitPanel = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates frame by adding JObjects to each other
    private void createFrame() {
        newPanel.setLayout(new GridLayout(7, 1));

        JLabel newLabel = new JLabel("Choose Store To Save New Restaurant To:");
        newLabel.setHorizontalAlignment(JLabel.CENTER);
        newPanel.add(newLabel);

        store1Panel.add(new JLabel("Store 1:"));
        store1Panel.add(store1Button);
        newPanel.add(store1Panel);

        store2Panel.add(new JLabel("Store 2:"));
        store2Panel.add(store2Button);
        newPanel.add(store2Panel);

        store3Panel.add(new JLabel("Store 3:"));
        store3Panel.add(store3Button);
        newPanel.add(store3Panel);

        store4Panel.add(new JLabel("Store 4:"));
        store4Panel.add(store4Button);
        newPanel.add(store4Panel);

        store5Panel.add(new JLabel("Store 5:"));
        store5Panel.add(store5Button);
        newPanel.add(store5Panel);

        quitPanel.add(new JLabel("Go Back:"));
        quitPanel.add(quitButton);
        newPanel.add(quitPanel);

    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("back".equals(e.getActionCommand())) {
            setVisible(false);
            previousFrame.setVisible(true);
        } else {
            newRestaurant(e);
        }
    }

    // EFFECTS: displays prompt to user if they want to use storage slot and processes user input
    private void newRestaurant(ActionEvent e) {
        try {
            setVisible(false);
            if (JOptionPane.showConfirmDialog(this,
                    "Are You Sure You Want To Use This Storage Slot? Any Restaurant Occupying This Slot Will "
                            + "Be Deleted And Replaced By Your New Restaurant.",
                    "New Restaurant Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                    new ImageIcon("./data/images/!.png")) == 1) {
                throw new InvalidNameEnteredException();
            }
            newRestaurantSwitch(e);
        } catch (InvalidNameEnteredException exception) {
            setVisible(true);
        }

    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    private void newRestaurantSwitch(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "store1":
                createNewRestaurant("./data/json/store1.json");
                break;
            case "store2":
                createNewRestaurant("./data/json/store2.json");
                break;
            case "store3":
                createNewRestaurant("./data/json/store3.json");
                break;
            case "store4":
                createNewRestaurant("./data/json/store4.json");
                break;
            case "store5":
                createNewRestaurant("./data/json/store5.json");
                break;
        }
    }

    // EFFECTS: creates a NewFrame with jsonStore
    private void createNewRestaurant(String jsonStore) {
        setVisible(false);
        new NameFrame(jsonStore, this, previousFrame);
    }
}
