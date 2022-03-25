package ui.gui;

import model.Food;
import ui.exceptions.FoodSuccessException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.gui.RestaurantManagerFrame.DF;
import static ui.gui.RestaurantManagerFrame.restaurant;
import static ui.gui.TableFrame.table;

// Frame for when food is being ordered to a table
public class OrderFrame extends JFrame implements ActionListener {

    private TableFrame previousFrame;
    private JPanel orderPanel;
    private JPanel foodPanel;
    private JPanel quitPanel;
    private JPanel viewPanel;
    private JTextField foodTextField;
    private JButton viewButton;
    private JButton quitButton;
    private JButton enterButton;

    // EFFECTS: Constructs Frame
    public OrderFrame(TableFrame previousFrame) {
        super("Order Food For " + table.getName());

        this.previousFrame = previousFrame;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createTextFieldsAndButtons();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(orderPanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes JTextFields and JButtons
    private void createTextFieldsAndButtons() {
        foodTextField = new JTextField(15);

        quitButton = new JButton("Back");
        quitButton.setActionCommand("back");
        quitButton.addActionListener(this);

        enterButton = new JButton("Order");
        enterButton.setActionCommand("order");
        enterButton.addActionListener(this);

        viewButton = new JButton("View");
        viewButton.setActionCommand("view");
        viewButton.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: initializes JPanels
    private void createPanels() {
        orderPanel = new JPanel();
        foodPanel = new JPanel();
        quitPanel = new JPanel();
        viewPanel = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates frame by adding JObjects to each other
    private void createFrame() {
        orderPanel.setLayout(new GridLayout(4, 1));

        JLabel label = new JLabel("Enter Name Of Food Being Ordered:");
        label.setHorizontalAlignment(JLabel.CENTER);
        orderPanel.add(label);

        foodPanel.add(new JLabel("Food Name:"));
        foodPanel.add(foodTextField);
        foodPanel.add(enterButton);
        orderPanel.add(foodPanel);

        viewPanel.add(new JLabel("View Menu:"));
        viewPanel.add(viewButton);
        orderPanel.add(viewPanel);

        quitPanel.add(new JLabel("Go Back:"));
        quitPanel.add(quitButton);
        orderPanel.add(quitPanel);
    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        switch (e.getActionCommand()) {
            case "order":
                orderFood();
                break;
            case "view":
                JOptionPane.showMessageDialog(this, printMenu(),
                        "Menu", JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon("./data/images/burger.png"));
                setVisible(true);
                break;
            case "back":
                previousFrame.setVisible(true);
                break;
        }
    }

    // EFFECTS: returns string of menu
    private String printMenu() {
        StringBuilder menuString =  new StringBuilder();
        menuString.append("Menu:");
        for (Food food : restaurant.getMenu()) {
            menuString.append("\n")
                    .append(food.getName())
                    .append(": $")
                    .append(DF.format(food.getPrice()));
        }
        return menuString.toString();
    }

    // MODIFIES: this, table
    // EFFECTS: orders food to table using user input
    private void orderFood() {
        try {
            for (Food food: restaurant.getMenu()) {
                if (foodTextField.getText().equalsIgnoreCase(food.getName())) {
                    throw new FoodSuccessException();
                }
            }
            JOptionPane.showMessageDialog(this, foodTextField.getText()
                            + " Is Not On The Menu",
                    "Order Error", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/!.png"));
        } catch (FoodSuccessException foodSuccessException) {
            JOptionPane.showMessageDialog(this, foodTextField.getText()
                            + " has been successfully ordered!",
                    "Order Success", JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon("./data/images/woo!.png"));
            restaurant.orderFood(table.getName(), foodTextField.getText());
        } finally {
            setVisible(true);
        }
    }

}
