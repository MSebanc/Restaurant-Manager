package ui.gui;

import model.Table;
import ui.exceptions.InvalidNumberException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.gui.RestaurantManagerFrame.restaurant;

// Frame for assigning parties to tables
public class AssignFrame extends JFrame implements ActionListener {

    private RestaurantManagerFrame previousFrame;
    private JPanel assignPanel;
    private JPanel numPanel;
    private JPanel quitPanel;
    private JTextField numTextField;
    private JButton quitButton;
    private JButton enterButton;

    // EFFECTS: Constructs Frame
    public AssignFrame(RestaurantManagerFrame previousFrame) {
        super("Assign Customers To Table");

        this.previousFrame = previousFrame;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createTextFieldsAndButtons();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(assignPanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes JTextFields and JButtons
    private void createTextFieldsAndButtons() {
        numTextField = new JTextField(15);

        quitButton = new JButton("Back");
        quitButton.setActionCommand("back");
        quitButton.addActionListener(this);

        enterButton = new JButton("Enter");
        enterButton.setActionCommand("enter");
        enterButton.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: initializes JPanels
    private void createPanels() {
        assignPanel = new JPanel();
        numPanel = new JPanel();
        quitPanel = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates frame by adding JObjects to each other
    private void createFrame() {
        assignPanel.setLayout(new GridLayout(3, 1));

        JLabel label = new JLabel("Enter Customer Party Size:");
        label.setHorizontalAlignment(JLabel.CENTER);
        assignPanel.add(label);

        numPanel.add(new JLabel("Party Size:"));
        numPanel.add(numTextField);
        numPanel.add(enterButton);
        assignPanel.add(numPanel);

        quitPanel.add(new JLabel("Go Back:"));
        quitPanel.add(quitButton);
        assignPanel.add(quitPanel);
    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        switch (e.getActionCommand()) {
            case "enter":
                assignCustomersToTable();
                break;
            case "back":
                previousFrame.setVisible(true);
                break;
        }
    }

    // MODIFIES: this, restaurant
    // EFFECTS: assigns customer to a table using user input
    private void assignCustomersToTable() {
        try {
            int partySize = Integer.parseInt(numTextField.getText());
            if (partySize <= 0) {
                throw new InvalidNumberException();
            }

            Table table = restaurant.assignCustomers(restaurant.validTables(partySize), partySize);
            table.occupyTable();
            JOptionPane.showMessageDialog(this, "Assigned party to " + table.getName() + "!",
                    "Assigned", JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon("./data/images/woo!.png"));
            previousFrame.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Party Size Input",
                    "Failed Assign Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/!.png"));
            setVisible(true);
        }
    }
}
