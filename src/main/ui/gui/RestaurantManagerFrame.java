package ui.gui;

import model.Restaurant;
import model.Table;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

// Frame for restaurant commands
public class RestaurantManagerFrame extends JFrame implements ActionListener {

    static final DecimalFormat DF = new DecimalFormat("0.00");

    static Restaurant restaurant;
    static JComboBox<String> tableComboBox;

    private final String jsonStore;
    private final RestaurantMangerGUI gui;
    private JsonWriter jsonWriter;

    private JPanel restaurantPanel;
    private JPanel tablePanel;
    private JPanel addPanel;
    private JPanel assignPanel;
    private JPanel infoPanel;
    private JPanel savePanel;
    private JPanel earningsPanel;
    private JPanel viewPanel;
    private JPanel quitPanel;
    private JButton addButton;
    private JButton assignButton;
    private JButton infoButton;
    private JButton saveButton;
    private JButton viewButton;
    private JButton earningsButton;
    private JButton selectButton;
    private JButton quitButton;

    // EFFECTS: Constructs Frame
    public RestaurantManagerFrame(Restaurant restaurant, String jsonStore, RestaurantMangerGUI gui) {
        super(restaurant.getName() + " Manager");

        this.jsonStore = jsonStore;
        RestaurantManagerFrame.restaurant = restaurant;
        this.gui = gui;
        this.jsonWriter = new JsonWriter(jsonStore);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createComboBox();
        createButtons1();
        createPanels();
        createFrame1();

        setLocationRelativeTo(null);
        add(restaurantPanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes JComboBox
    private void createComboBox() {
        tableComboBox = new JComboBox<>();
        for (Table table : restaurant.getTables()) {
            tableComboBox.addItem(table.getName());
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes JButtons
    private void createButtons1() {
        addButton = new JButton("Add Table");
        addButton.setActionCommand("add");
        addButton.addActionListener(this);

        assignButton = new JButton("Assign");
        assignButton.setActionCommand("assign");
        assignButton.addActionListener(this);

        infoButton = new JButton("Info");
        infoButton.setActionCommand("info");
        infoButton.addActionListener(this);

        saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);

        createButtons2();
    }

    // MODIFIES: this
    // EFFECTS: initializes JButtons
    private void createButtons2() {
        earningsButton = new JButton("Earnings");
        earningsButton.setActionCommand("earnings");
        earningsButton.addActionListener(this);

        selectButton = new JButton("Select");
        selectButton.setActionCommand("select");
        selectButton.addActionListener(this);

        viewButton = new JButton("View");
        viewButton.setActionCommand("view");
        viewButton.addActionListener(this);

        quitButton = new JButton("Close");
        quitButton.setActionCommand("close");
        quitButton.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: initializes JPanels
    private void createPanels() {
        restaurantPanel = new JPanel();
        tablePanel = new JPanel();
        viewPanel = new JPanel();
        addPanel = new JPanel();
        infoPanel = new JPanel();
        quitPanel = new JPanel();
        assignPanel = new JPanel();
        earningsPanel = new JPanel();
        savePanel = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates frame by adding JObjects to each other
    private void createFrame1() {
        restaurantPanel.setLayout(new GridLayout(8, 1));

        infoPanel.add(new JLabel("Restaurant Manager Information:"));
        infoPanel.add(infoButton);
        restaurantPanel.add(infoPanel);

        tablePanel.add(new JLabel("Select Table To Manage:"));
        tablePanel.add(tableComboBox);
        tablePanel.add(selectButton);
        restaurantPanel.add(tablePanel);

        viewPanel.add(new JLabel("View All Tables:"));
        viewPanel.add(viewButton);
        restaurantPanel.add(viewPanel);

        addPanel.add(new JLabel("Add New Table:"));
        addPanel.add(addButton);
        restaurantPanel.add(addPanel);

        createFrame2();
    }

    // MODIFIES: this
    // EFFECTS: creates frame by adding JObjects to each other
    private void createFrame2() {
        assignPanel.add(new JLabel("Assign Customer Party To A Table:"));
        assignPanel.add(assignButton);
        restaurantPanel.add(assignPanel);

        earningsPanel.add(new JLabel("Check Earnings Of Restaurant:"));
        earningsPanel.add(earningsButton);
        restaurantPanel.add(earningsPanel);

        savePanel.add(new JLabel("Save Restaurant:"));
        savePanel.add(saveButton);
        restaurantPanel.add(savePanel);

        quitPanel.add(new JLabel("Close Manager For This Restaurant:"));
        quitPanel.add(quitButton);
        restaurantPanel.add(quitPanel);
    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "info":
                infoRestaurant();
                break;
            case "select":
                selectRestaurant();
                break;
            case "view":
                viewTables();
            default:
                actionPerformed2(e);
        }
    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    private void actionPerformed2(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "add":
                addRestaurant();
                break;
            case "assign":
                assignRestaurant();
                break;
            case "earnings":
                earningsRestaurant();
                break;
            case "save":
                saveRestaurant();
                break;
            case "close":
                closeRestaurant();
                break;
        }
    }

    // EFFECTS: displays restaurant manager information to user
    private void infoRestaurant() {
        JOptionPane.showMessageDialog(this, infoMessage(),
                "Info", JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon("./data/images/info.png"));
    }

    // EFFECTS: returns string of restaurant manager information
    private String infoMessage() {
        return "\nWelcome to the " + restaurant.getName() + " Restaurant Manager, here is some basic information to"
                + " get started:"
                + "\nA menu of possible actions will be displayed,"
                + "\nYou can trigger those actions by pressing the buttons"

                + "\n\nMost actions require other actions to be completed first"
                + "\nTable actions cannot be done without first adding a table"
                + "\nCannot assign customers to invalid tables (not clean, not set, and/or already occupied)"
                + "\nCertain actions (like cleaning) require the table to have the opposite effect done first"

                + actionsList();
    }

    // EFFECTS: returns string of restaurant manager actions
    private String actionsList() {
        return "\n\nComplete list of prerequisite actions before action can be called:"
                + "\n\tAdd Table: Nothing"
                + "\n\tCheck Earnings: Nothing"
                + "\n\tSave: Nothing"
                + "\n\tSelect: Nothing"
                + "\n\tView: Nothing"
                + "\n\tInfo: Nothing"
                + "\n\tQuit/Back/Close: Nothing"
                + "\n\tRemove Table: Add Table"
                + "\n\tClean: Add Table, Mark Unoccupied"
                + "\n\tSet: Add Table, Mark Unoccupied"
                + "\n\tMark Unoccupied: Assign Customers"
                + "\n\tAssign Customers: Mark Unoccupied"
                + "\n\tOrder Food: Assign Customers"
                + "\n\tDeliver Food: Order Food"
                + "\n\tPay For Food: Assign Customers"
                + "\n\tCheck History (purchase, cleaning, or delivery history): Add Table";
    }

    // MODIFIES: this
    // EFFECTS: processes user information and creates a TableFrame for selected table
    private void selectRestaurant() {
        setVisible(false);
        if (!restaurant.getTables().isEmpty()) {
            Table table = restaurant.findTable((String) tableComboBox.getSelectedItem());
            new TableFrame(table, this);
        } else {
            JOptionPane.showMessageDialog(this, "No Table To Select",
                    "Table Error Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/!.png"));
            setVisible(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates frame for viewing all the tables
    private void viewTables() {
        setVisible(false);
        if (!restaurant.getTables().isEmpty()) {
            new ViewTablesFrame(restaurant.getTables(), this);
        } else {
            JOptionPane.showMessageDialog(this, "No Tables To Display",
                    "Table Error Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/!.png"));
            setVisible(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates AddFrame
    private void addRestaurant() {
        setVisible(false);
        new AddFrame(this);
    }

    // MODIFIES: this
    // EFFECTS: creates assign frame
    private void assignRestaurant() {
        setVisible(false);
        if (!restaurant.getTables().isEmpty() && oneIsValid()) {
            new AssignFrame(this);
        } else {
            JOptionPane.showMessageDialog(this, "No Table To Assign Customers To",
                    "Table Error Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/!.png"));
            setVisible(true);
        }
    }

    // EFFECTS: returns true if one table is valid
    private boolean oneIsValid() {
        for (Table table : restaurant.getTables()) {
            if (restaurant.validStatuses(table)) {
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: displays restaurant earnings to customer
    private void earningsRestaurant() {
        setVisible(false);
        JOptionPane.showMessageDialog(this, earningsMessage(),
                "Earnings", JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon("./data/images/money.png"));
        setVisible(true);
    }

    // EFFECTS: returns string of restaurant earnings
    private String earningsMessage() {
        return restaurant.getName() + " has made $" + DF.format(restaurant.getEarnings()) + "!"
                + "\nThere has also been $" + DF.format(restaurant.getTips()) + " in tips!"
                + "\nA total of " + restaurant.getTotalCustomers() + " customers have gone to " + restaurant.getName()
                + "!";
    }

    // MODIFIES: this
    // EFFECTS: saves restaurant to file
    private void saveRestaurant() {
        try {
            jsonWriter.open();
            jsonWriter.write(restaurant);
            jsonWriter.close();
            setVisible(false);
            JOptionPane.showMessageDialog(this, restaurant.getName()
                            + " has been successfully saved",
                    "Load Restaurant Message", JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon("./data/images/woo!.png"));
            setVisible(true);
        } catch (FileNotFoundException e) {
            setVisible(false);
            JOptionPane.showMessageDialog(this, "Unable To Save Restaurant To File",
                    "Failed Save Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/!.png"));
            setVisible(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets visibility of frame to false and sets visibility of gui frame to true
    private void closeRestaurant() {
        setVisible(false);
        gui.setVisible(true);
    }

}
