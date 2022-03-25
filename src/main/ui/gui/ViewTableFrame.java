package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.gui.TableFrame.table;

// Frame for when the information for a table needs to be shown
public class ViewTableFrame extends JFrame implements ActionListener {

    private TableFrame previousFrame;

    private JPanel tablePanel;
    private JPanel quitPanel;
    private JScrollPane scrollPane;
    private JButton quitButton;

    // EFFECTS: Constructs Frame
    public ViewTableFrame(TableFrame previousFrame) {
        super(table.getName());

        this.previousFrame = previousFrame;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createButtonsAndScrollBar();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(tablePanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes JScrollPane and JButtons
    private void createButtonsAndScrollBar() {
        quitButton = new JButton("Close");
        quitButton.setActionCommand("close");
        quitButton.addActionListener(this);

        JTextArea textArea = new JTextArea(stringTable());
        textArea.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
        scrollPane = new JScrollPane(textArea);
    }

    // MODIFIES: this
    // EFFECTS: initializes JPanels
    private void createPanels() {
        tablePanel = new JPanel();
        quitPanel = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates frame by adding JObjects to each other
    private void createFrame() {
        tablePanel.setLayout(new BorderLayout());

        quitPanel.add(new JLabel("Close Table View:"));
        quitPanel.add(quitButton);
        tablePanel.add(quitPanel, BorderLayout.PAGE_END);

        tablePanel.setPreferredSize(new Dimension(800, 550));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

    }

    // EFFECTS: returns string of table information
    private String stringTable() {
        return table.getName() + ":"
                + "\n\tMax Occupancy: " + table.getMaxOccupancy()
                + "\n\tClean Status: " + table.getCleanStatus()
                + "\n\tSet Status: " + table.getSetStatus()
                + "\n\tAvailability Status: " + table.getAvailabilityStatus()
                + "\n\tFood Delivery Status: " + table.getFoodDeliveryStatus()
                + tableHistory()
                + "\n\tBill:"
                + "\n\t\tCost: " + table.getBill().getCost()
                + "\n\t\tTip: " + table.getBill().getTip()
                + "\n\t\tPay Status: " + table.getBill().getPayStatus();
    }

    // EFFECTS: returns string of table history
    private String tableHistory() {
        StringBuilder tablesString = new StringBuilder();
        tablesString.append("\n\tCleaning History: ");
        for (String date : table.getCleaningHistory()) {
            tablesString.append("\n\t\t").append(date);
        }

        tablesString.append("\n\tDelivery History:");
        for (String date : table.getDeliveryHistory()) {
            tablesString.append("\n\t\t").append(date);
        }

        tablesString.append("\n\tPurchase History:");
        for (String date : table.getPurchaseHistory()) {
            tablesString.append("\n\t\t").append(date);
        }
        return tablesString.toString();
    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("close".equals(e.getActionCommand())) {
            setVisible(false);
            previousFrame.setVisible(true);
        }
    }

}