package ui.gui;

import model.EventLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

// Restaurant Manager GUI
public class RestaurantMangerGUI extends JFrame implements ActionListener {

    private JPanel restaurantPanel;
    private JPanel loadPanel;
    private JPanel newPanel;
    private JPanel quitPanel;
    private JButton newButton;
    private JButton quitButton;
    private JButton loadButton;

    // EFFECTS: Constructs Frame
    public RestaurantMangerGUI() {
        super("Restaurant Manager");

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createButtons();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(restaurantPanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    // EFFECTS: displays splashscreen and runs program
    public static void runRestaurantManagerGUI() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException
                | InstantiationException | IllegalAccessException e) {
            // handle exception
        }

        SplashScreen splash = new SplashScreen();
        splash.show(3500);
        splash.hide();

        new RestaurantMangerGUI();
    }

    // MODIFIES: this
    // EFFECTS: initializes JButtons
    private void createButtons() {
        newButton = new JButton("Create New Restaurant");
        newButton.setActionCommand("new");
        newButton.addActionListener(this);

        loadButton = new JButton("Load Restaurant");
        loadButton.setActionCommand("load");
        loadButton.addActionListener(this);

        quitButton = new JButton("Quit Program");
        quitButton.setActionCommand("quit");
        quitButton.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: initializes JPanels
    private void createPanels() {
        restaurantPanel = new JPanel();
        newPanel = new JPanel();
        loadPanel = new JPanel();
        quitPanel = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates frame by adding JObjects to each other
    private void createFrame() {
        restaurantPanel.setLayout(new GridLayout(3, 1));

        newPanel.add(new JLabel("Create New Restaurant:"));
        newPanel.add(newButton);
        restaurantPanel.add(newPanel);

        loadPanel.add(new JLabel("Load A Restaurant:"));
        loadPanel.add(loadButton);
        restaurantPanel.add(loadPanel);
        quitPanel.add(new JLabel("Close Program:"));
        quitPanel.add(quitButton);
        restaurantPanel.add(quitPanel);

    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "new":
                setVisible(false);
                new NewFrame(this);
                break;
            case "load":
                setVisible(false);
                new LoadFrame(this);
                break;
            case "quit":
                printLog();
                System.exit(0);
                break;
        }
    }

    // EFFECTS: prints event log to console
    private void printLog() {
        for (model.Event event : EventLog.getInstance()) {
            System.out.println(event);
        }
    }
}
