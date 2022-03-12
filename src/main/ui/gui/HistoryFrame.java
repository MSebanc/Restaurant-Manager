package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.gui.TableFrame.table;

public class HistoryFrame extends JFrame implements ActionListener {

    private String history;
    private TableFrame previousFrame;

    private JPanel historyPanel;
    private JPanel quitPanel;
    private JScrollPane scrollPane;
    private JButton quitButton;

    public HistoryFrame(TableFrame previousFrame, String history) {
        super(history);

        this.previousFrame = previousFrame;
        this.history = history;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createButtonsAndScrollBar();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(historyPanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    private void createButtonsAndScrollBar() {
        quitButton = new JButton("Close");
        quitButton.setActionCommand("close");
        quitButton.addActionListener(this);

        JTextArea textArea = new JTextArea(tableHistory());
        textArea.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
        scrollPane = new JScrollPane(textArea);
    }

    private void createPanels() {
        historyPanel = new JPanel();
        quitPanel = new JPanel();
    }

    private void createFrame() {
        historyPanel.setLayout(new BorderLayout());

        quitPanel.add(new JLabel("Close Table View:"));
        quitPanel.add(quitButton);
        historyPanel.add(quitPanel, BorderLayout.PAGE_END);

        historyPanel.setPreferredSize(new Dimension(800, 550));
        historyPanel.add(scrollPane, BorderLayout.CENTER);

    }

    private String tableHistory() {
        StringBuilder tablesString = new StringBuilder();

        switch (history) {
            case "Cleaning History":
                tablesString.append("Cleaning History: ");
                for (String date : table.getCleaningHistory()) {
                    tablesString.append("\n\t").append(date);
                }
                break;
            case "Delivery History":
                tablesString.append("Delivery History:");
                for (String date : table.getDeliveryHistory()) {
                    tablesString.append("\n\t").append(date);
                }
                break;
            case "Purchase History":
                tablesString.append("\nPurchase History:");
                for (String date : table.getPurchaseHistory()) {
                    tablesString.append("\n\t").append(date);
                }
                break;
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
