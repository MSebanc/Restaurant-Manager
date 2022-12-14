package ui.gui;

import model.Table;
import ui.exceptions.InvalidNameEnteredException;
import ui.exceptions.InvalidNumberException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.gui.RestaurantManagerFrame.restaurant;
import static ui.gui.RestaurantManagerFrame.tableComboBox;

// Frame for when a table is being added to restaurant
public class AddFrame extends JFrame implements ActionListener {

    private RestaurantManagerFrame previousFrame;
    private JPanel addPanel;
    private JPanel namePanel;
    private JPanel maxPanel;
    private JPanel quitPanel;
    private JPanel enterPanel;
    private JTextField nameTextField;
    private JTextField maxTextField;
    private JButton quitButton;
    private JButton enterButton;

    // EFFECTS: Constructs Frame
    public AddFrame(RestaurantManagerFrame previousFrame) {
        super("Add Table");

        this.previousFrame = previousFrame;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createTextFieldsAndButtons();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(addPanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes JTextFields and JButtons
    private void createTextFieldsAndButtons() {
        nameTextField = new JTextField(15);
        maxTextField = new JTextField(10);

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
        addPanel = new JPanel();
        namePanel = new JPanel();
        quitPanel = new JPanel();
        maxPanel = new JPanel();
        enterPanel = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates frame by adding JObjects to each other
    private void createFrame() {
        addPanel.setLayout(new GridLayout(5, 1));

        JLabel label = new JLabel("Enter Name And Max Occupancy For New Table:");
        label.setHorizontalAlignment(JLabel.CENTER);
        addPanel.add(label);

        namePanel.add(new JLabel("Table Name:"));
        namePanel.add(nameTextField);
        addPanel.add(namePanel);

        maxPanel.add(new JLabel("Table Max Occupancy:"));
        maxPanel.add(maxTextField);
        addPanel.add(maxPanel);

        enterPanel.add(new JLabel("Add Table:"));
        enterPanel.add(enterButton);
        addPanel.add(enterPanel);


        quitPanel.add(new JLabel("Go Back:"));
        quitPanel.add(quitButton);
        addPanel.add(quitPanel);
    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        switch (e.getActionCommand()) {
            case "enter":
                addTableToRestaurant();
                break;
            case "back":
                previousFrame.setVisible(true);
                break;
        }
    }

    // MODIFIES: this, restaurant
    // EFFECTS: adds new table to restaurant using user input
    private void addTableToRestaurant() {
        try {
            String name = nameTextField.getText();
            for (Table table : restaurant.getTables()) {
                if (name.equalsIgnoreCase(table.getName()) || name.equalsIgnoreCase("")) {
                    throw new InvalidNameEnteredException();
                }
            }
            int max = Integer.parseInt(maxTextField.getText());
            if (max <= 0) {
                throw new InvalidNumberException();
            }

            addTable(max, name);

        } catch (InvalidNameEnteredException e) {
            invalidNameMessage();
            setVisible(true);
        } catch (Exception e) {
            invalidNumberMessage();
            setVisible(true);
        }
    }

    // MODIFIES: this, restaurant
    // EFFECTS: adds table with given max occupancy and name to restaurant
    private void addTable(int max, String name) {
        restaurant.addTable(max, name);

        JOptionPane.showMessageDialog(this, "Successfully Added " + name + "!",
                "Added Table", JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon("./data/images/woo!.png"));

        tableComboBox.addItem(name);

        previousFrame.pack();
        previousFrame.setVisible(true);
    }

    // EFFECTS: displays invalid name error message
    private void invalidNameMessage() {
        JOptionPane.showMessageDialog(this, "Invalid Name Input",
                "Failed Add Message", JOptionPane.ERROR_MESSAGE,
                new ImageIcon("./data/images/!.png"));
    }

    // EFFECTS: displays invalid name error message
    private void invalidNumberMessage() {
        JOptionPane.showMessageDialog(this, "Invalid Max Occupancy Input",
                "Failed Add Message", JOptionPane.ERROR_MESSAGE,
                new ImageIcon("./data/images/!.png"));
    }

}
