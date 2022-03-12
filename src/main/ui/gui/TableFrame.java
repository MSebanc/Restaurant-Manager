package ui.gui;

import model.Table;
import ui.exceptions.InvalidNameEnteredException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import static ui.gui.RestaurantManagerFrame.restaurant;
import static ui.gui.RestaurantManagerFrame.tableComboBox;

public class TableFrame extends JFrame implements ActionListener {

    private RestaurantManagerFrame previousFrame;
    static Table table;

    private JPanel tablePanel;
    private JPanel statusPanel;
    private JPanel actionPanel;
    private JPanel viewPanel;
    private JPanel historyPanel;
    private JPanel quitPanel;
    private JPanel removePanel;
    private JButton selectButton1;
    private JButton selectButton2;
    private JButton selectButton3;
    private JButton viewButton;
    private JButton quitButton;
    private JButton removeButton;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> actionComboBox;
    private JComboBox<String> historyComboBox;

    public TableFrame(Table table, RestaurantManagerFrame previousFrame) {
        super(table.getName() + " Manager");

        this.previousFrame = previousFrame;
        TableFrame.table = table;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createComboBoxes();
        createButtons();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(tablePanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    private void createComboBoxes() {
        statusComboBox = new JComboBox<>();
        statusComboBox.addItem("Mark Table As Clean");
        statusComboBox.addItem("Mark Table As Set");
        statusComboBox.addItem("Mark Table As Unoccupied");
        statusComboBox.addItem("Mark Food As Delivered");

        actionComboBox = new JComboBox<>();
        actionComboBox.addItem("Order Food");
        actionComboBox.addItem("Bill Table");

        historyComboBox = new JComboBox<>();
        historyComboBox.addItem("Cleaning History");
        historyComboBox.addItem("Delivery History");
        historyComboBox.addItem("Purchase History");
    }

    private void createButtons() {
        selectButton1 = new JButton("Select");
        selectButton1.setActionCommand("select1");
        selectButton1.addActionListener(this);

        selectButton2 = new JButton("Select");
        selectButton2.setActionCommand("select2");
        selectButton2.addActionListener(this);

        selectButton3 = new JButton("Select");
        selectButton3.setActionCommand("select3");
        selectButton3.addActionListener(this);

        viewButton = new JButton("View");
        viewButton.setActionCommand("view");
        viewButton.addActionListener(this);

        quitButton = new JButton("Close");
        quitButton.setActionCommand("close");
        quitButton.addActionListener(this);

        removeButton = new JButton("Remove");
        removeButton.setActionCommand("remove");
        removeButton.addActionListener(this);
    }

    private void createPanels() {
        tablePanel = new JPanel();
        viewPanel = new JPanel();
        actionPanel = new JPanel();
        statusPanel = new JPanel();
        historyPanel = new JPanel();
        quitPanel = new JPanel();
        removePanel = new JPanel();
    }

    private void createFrame() {
        tablePanel.setLayout(new GridLayout(6, 1));

        viewPanel.add(new JLabel("View Table Information:"));
        viewPanel.add(viewButton);
        tablePanel.add(viewPanel);

        statusPanel.add(new JLabel("Status Actions:"));
        statusPanel.add(statusComboBox);
        statusPanel.add(selectButton1);
        tablePanel.add(statusPanel);

        actionPanel.add(new JLabel("Customer Actions:"));
        actionPanel.add(actionComboBox);
        actionPanel.add(selectButton2);
        tablePanel.add(actionPanel);

        historyPanel.add(new JLabel("View History:"));
        historyPanel.add(historyComboBox);
        historyPanel.add(selectButton3);
        tablePanel.add(historyPanel);

        removePanel.add(new JLabel("Remove Table From Restaurant:"));
        removeButton.setBackground(Color.RED);
        removePanel.add(removeButton);
        tablePanel.add(removePanel);

        quitPanel.add(new JLabel("Close Manager For This Table:"));
        quitPanel.add(quitButton);
        tablePanel.add(quitPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "select1":
                statusTable();
                break;
            case "select2":
                actionTable();
                break;
            case "select3":
                viewHistoryTable();
                break;
            case "view":
                viewTable();
                break;
            case "close":
                closeTable();
                break;
            case "remove":
                removeTable();
                break;
        }
    }

    private void statusTable() {
        switch ((String) Objects.requireNonNull(statusComboBox.getSelectedItem())) {
            case "Mark Table As Clean":
                cleanTable();
                break;
            case "Mark Table As Set":
                setTable();
                break;
            case "Mark Table As Unoccupied":
                vacantTable();
                break;
            case "Mark Food As Delivered":
                deliverTable();
                break;
        }
    }

    private void cleanTable() {
        setVisible(false);

        if (table.getCleanStatus()) {
            JOptionPane.showMessageDialog(this, table.getName() + " Is Already Clean",
                    "Failed Clean Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/soap.png"));
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, table.getName() + " Has Been Marked As Clean!",
                    "Success Clean Message", JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon("./data/images/woo!.png"));
            table.cleanTable();
        }

        setVisible(true);
    }

    private void setTable() {
        setVisible(false);

        if (table.getSetStatus()) {
            JOptionPane.showMessageDialog(this, table.getName() + " Is Already Set",
                    "Failed Set Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/plate.png"));
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, table.getName() + " Has Been Marked As Set!",
                    "Success Set Message", JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon("./data/images/woo!.png"));
            table.trueSetTable();
        }

        setVisible(true);
    }

    private void vacantTable() {
        setVisible(false);

        if (table.getAvailabilityStatus()) {
            JOptionPane.showMessageDialog(this, table.getName() + " Already Is Unoccupied",
                    "Failed Vacant Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/empty.png"));
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, table.getName() + " Has Been Marked As "
                            + "Unoccupied!",
                    "Success Vacant Message", JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon("./data/images/woo!.png"));
            table.emptyTable();
        }

        setVisible(true);
    }

    private void deliverTable() {

        setVisible(false);

        if (table.getFoodDeliveryStatus()) {
            JOptionPane.showMessageDialog(this, "No Food To Deliver To " + table.getName(),
                    "Failed Deliver Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/burger.png"));
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Food Has Been Marked As Delivered To "
                    + table.getName() + "!",
                    "Success Deliver Message", JOptionPane.INFORMATION_MESSAGE,
                    new ImageIcon("./data/images/woo!.png"));
            table.deliverFood();
        }

        setVisible(true);

    }

    private void actionTable() {
        switch ((String) Objects.requireNonNull(actionComboBox.getSelectedItem())) {
            case "Order Food":
                orderTable();
                break;
            case "Bill Table":
                billTable();
                break;
        }
    }

    private void orderTable() {
        setVisible(false);
        if (table.getAvailabilityStatus()) {
            JOptionPane.showMessageDialog(this, "No Customers At Table",
                    "Order Fail", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/!.png"));
            setVisible(true);
        } else {
            new OrderFrame(this);
        }
    }

    private void billTable() {
        setVisible(false);
        if (table.getAvailabilityStatus()) {
            JOptionPane.showMessageDialog(this, "No Customers At Table",
                    "Pay Bill Fail Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/!.png"));
            setVisible(true);
        } else if (table.getFoodDeliveryStatus() || table.getBill().getCost() == 0.00) {
            JOptionPane.showMessageDialog(this, "No Food Needs To Be Paid For",
                    "Pay Bill Fail Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/!.png"));
            setVisible(true);
        } else if (table.getBill().getPayStatus()) {
            JOptionPane.showMessageDialog(this, "Food Has Already Been Paid For",
                    "Pay Bill Fail Message", JOptionPane.ERROR_MESSAGE,
                    new ImageIcon("./data/images/!.png"));
            setVisible(true);
        } else {
            new BillFrame(this);
        }
    }

    private void viewHistoryTable() {
        setVisible(false);
        switch ((String) Objects.requireNonNull(historyComboBox.getSelectedItem())) {
            case "Cleaning History":
                new HistoryFrame(this, "Cleaning History");
                break;
            case "Delivery History":
                new HistoryFrame(this, "Delivery History");
                break;
            case "Purchase History":
                new HistoryFrame(this, "Purchase History");
                break;
        }
    }

    private void viewTable() {
        setVisible(false);
        new ViewTableFrame(this);
    }

    private void removeTable() {
        try {
            setVisible(false);
            if (JOptionPane.showConfirmDialog(this,
                    "Are You Sure You Want To Remove This Table From The System? You Cannot Get The Table "
                            + "Back.",
                    "Remove Table Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                    new ImageIcon("./data/images/!.png")) == 1) {
                throw new InvalidNameEnteredException();
            }
            removeTableFromRestaurant();
        } catch (InvalidNameEnteredException e) {
            setVisible(true);
        }
    }

    private void removeTableFromRestaurant() {
        restaurant.removeTable(table.getName());
        JOptionPane.showMessageDialog(this, table.getName() + " has been successfully removed!",
                "New Restaurant Message", JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon("./data/images/woo!.png"));
        tableComboBox.removeItem(table.getName());
        previousFrame.pack();
        previousFrame.setVisible(true);
    }

    private void closeTable() {
        setVisible(false);
        previousFrame.setVisible(true);
    }
}
