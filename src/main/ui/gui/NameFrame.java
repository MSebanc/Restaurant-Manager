package ui.gui;

import model.Restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Frame for when a name is needed for a new restaurant
public class NameFrame extends JFrame implements ActionListener {

    private NewFrame previousFrame;
    private JPanel namePanel;
    private JPanel newNamePanel;
    private JPanel quitPanel;
    private JTextField newNameTextField;
    private JButton quitButton;
    private JButton enterButton;

    private String jsonStore;
    private RestaurantMangerGUI gui;

    // EFFECTS: Constructs Frame
    public NameFrame(String jsonStore, NewFrame previousFrame, RestaurantMangerGUI gui) {
        super("Name Restaurant");

        this.jsonStore = jsonStore;
        this.previousFrame = previousFrame;
        this.gui = gui;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        createTextFieldsAndButtons();
        createPanels();
        createFrame();

        setLocationRelativeTo(null);
        add(namePanel);
        pack();

        setResizable(true);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes JTextFields and JButtons
    private void createTextFieldsAndButtons() {
        newNameTextField = new JTextField(15);

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
        namePanel = new JPanel();
        newNamePanel = new JPanel();
        quitPanel = new JPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates frame by adding JObjects to each other
    private void createFrame() {
        namePanel.setLayout(new GridLayout(3, 1));

        JLabel label = new JLabel("Enter Name For Restaurant:");
        label.setHorizontalAlignment(JLabel.CENTER);
        namePanel.add(label);

        newNamePanel.add(new JLabel("New Name:"));
        newNamePanel.add(newNameTextField);
        newNamePanel.add(enterButton);
        namePanel.add(newNamePanel);

        quitPanel.add(new JLabel("Go Back:"));
        quitPanel.add(quitButton);
        namePanel.add(quitPanel);
    }

    // MODIFIES: this
    // EFFECTS: Processes Action Listener commands
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        switch (e.getActionCommand()) {
            case "enter":
                JOptionPane.showMessageDialog(this, newNameTextField.getText()
                                + " has been successfully created!",
                        "New Restaurant Message", JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon("./data/images/woo!.png"));
                new RestaurantManagerFrame(new Restaurant(newNameTextField.getText()), jsonStore, gui);
                break;
            case "back":
                previousFrame.setVisible(true);
                break;
        }
    }
}
