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

    private void createPanels() {
        addPanel = new JPanel();
        namePanel = new JPanel();
        quitPanel = new JPanel();
        maxPanel = new JPanel();
        enterPanel = new JPanel();
    }

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

    private void addTable(int max, String name) {
        restaurant.addTable(max, name);

        JOptionPane.showMessageDialog(this, "Successfully Added " + name + "!",
                "Added Table", JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon("./data/images/woo!.png"));

        tableComboBox.addItem(name);

        previousFrame.pack();
        previousFrame.setVisible(true);
    }

    private void invalidNameMessage() {
        JOptionPane.showMessageDialog(this, "Invalid Name Input",
                "Failed Add Message", JOptionPane.ERROR_MESSAGE,
                new ImageIcon("./data/images/!.png"));
    }

    private void invalidNumberMessage() {
        JOptionPane.showMessageDialog(this, "Invalid Max Occupancy Input",
                "Failed Add Message", JOptionPane.ERROR_MESSAGE,
                new ImageIcon("./data/images/!.png"));
    }

}
