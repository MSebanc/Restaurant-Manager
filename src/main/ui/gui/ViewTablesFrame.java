package ui.gui;

import model.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ViewTablesFrame extends JFrame implements ActionListener {

    private List<Table> tables;
    private RestaurantManagerFrame previousFrame;

    private JPanel tablesPanel;
    private JPanel quitPanel;
    private JScrollPane scrollPane;
    private JButton quitButton;
    private JTextArea textArea;

    public ViewTablesFrame(List<Table> tables, RestaurantManagerFrame previousFrame) {
        super("Tables");

        this.tables = tables;
        this.previousFrame = previousFrame;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createButtonsAndScrollBar();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(tablesPanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    private void createButtonsAndScrollBar() {
        quitButton = new JButton("Close");
        quitButton.setActionCommand("close");
        quitButton.addActionListener(this);

        textArea = new JTextArea(stringTables());
        textArea.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
        scrollPane = new JScrollPane(textArea);
    }

    private void createPanels() {
        tablesPanel = new JPanel();
        quitPanel = new JPanel();
    }

    private void createFrame() {
        tablesPanel.setLayout(new BorderLayout());

        quitPanel.add(new JLabel("Close Table View:"));
        quitPanel.add(quitButton);
        tablesPanel.add(quitPanel, BorderLayout.PAGE_END);

        tablesPanel.setPreferredSize(new Dimension(800, 550));
        tablesPanel.add(scrollPane, BorderLayout.CENTER);

    }

    private String stringTables() {
        StringBuilder tablesString = new StringBuilder();
        tablesString.append("--------------------------------------------------------------------------\n");
        for (Table table : tables) {
            tablesString.append(table.getName()).append(":")
                    .append("\n\tMax Occupancy: ").append(table.getMaxOccupancy())
                    .append("\n\tClean Status: ").append(table.getCleanStatus())
                    .append("\n\tSet Status: ").append(table.getSetStatus())
                    .append("\n\tAvailability Status: ").append(table.getAvailabilityStatus())
                    .append("\n\tFood Delivery Status: ").append(table.getFoodDeliveryStatus())
                    .append(tableHistory(table))
                    .append("\n\tBill:")
                    .append("\n\t\tCost: ").append(table.getBill().getCost())
                    .append("\n\t\tTip: ").append(table.getBill().getTip())
                    .append("\n\t\tPay Status: ").append(table.getBill().getPayStatus())
                    .append("\n--------------------------------------------------------------------------\n");
        }
        return tablesString.toString();
    }

    private String tableHistory(Table table) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("close".equals(e.getActionCommand())) {
            setVisible(false);
            previousFrame.setVisible(true);
        }
    }

}
