package ui.gui;

import model.Bill;
import ui.exceptions.InvalidNumberException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.gui.RestaurantManagerFrame.DF;
import static ui.gui.RestaurantManagerFrame.restaurant;
import static ui.gui.TableFrame.table;

// Frame for when a table is being billed
public class BillFrame extends JFrame implements ActionListener {

    private TableFrame previousFrame;
    private JPanel billPanel;
    private JPanel tipsPanel;
    private JPanel quitPanel;
    private JTextField tipsTextField;
    private JButton quitButton;
    private JButton enterButton;

    // EFFECTS: Constructs Frame
    public BillFrame(TableFrame previousFrame) {
        super("Bill " + table.getName());

        this.previousFrame = previousFrame;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createTextFieldsAndButtons();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(billPanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes JTextFields and JButtons
    private void createTextFieldsAndButtons() {
        tipsTextField = new JTextField(15);

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
        billPanel = new JPanel();
        tipsPanel = new JPanel();
        quitPanel = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates frame by adding JObjects to each other
    private void createFrame() {
        billPanel.setLayout(new GridLayout(4, 1));

        JLabel label1 = new JLabel("Cost Of Food: $" + DF.format(table.getBill().getCost()));
        label1.setHorizontalAlignment(JLabel.CENTER);
        billPanel.add(label1);
        JLabel label2 = new JLabel("Enter Tip Amount:");
        label2.setHorizontalAlignment(JLabel.CENTER);
        billPanel.add(label2);

        tipsPanel.add(new JLabel("Tip Amount: $"));
        tipsPanel.add(tipsTextField);
        tipsPanel.add(enterButton);
        billPanel.add(tipsPanel);

        quitPanel.add(new JLabel("Go Back:"));
        quitPanel.add(quitButton);
        billPanel.add(quitPanel);
    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        switch (e.getActionCommand()) {
            case "enter":
                billTable();
                break;
            case "back":
                previousFrame.setVisible(true);
                break;
        }
    }

    // MODIFIES: this, Restaurant, Table, Bill
    // EFFECTS: bills and displays receipt using user input
    private void billTable() {
        try {
            double tip = Double.parseDouble(tipsTextField.getText());
            if (tip <= 0) {
                throw new InvalidNumberException();
            }
            table.getBill().setTip(tip);
            restaurant.tablePay(table.getName());

            JOptionPane.showMessageDialog(this, "Receipt:"
                    + "\n\tCost: $" + DF.format(table.getBill().getCost())
                    + "\n\tTax: $" + DF.format(table.getBill().getCost() * Bill.TAX)
                    + "\n\tTip: $" + DF.format(table.getBill().getTip())
                    + "\n------------------------"
                    + "\nTotal Cost: $" + DF.format(table.getBill().getTotalCost()),
                    "Receipt", JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon("./data/images/receipt.png"));
            previousFrame.setVisible(true);
        } catch (Exception e) {
            invalidNumberMessage();
            setVisible(true);
        }
    }

    // EFFECTS: displays invalid name error message
    private void invalidNumberMessage() {
        JOptionPane.showMessageDialog(this, "Invalid Tips Input",
                "Failed Tips Message", JOptionPane.ERROR_MESSAGE,
                new ImageIcon("./data/images/!.png"));
    }

}
