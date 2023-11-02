import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    public Login() {

        // Creating a frame for login
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(250, 250);
        loginFrame.getContentPane().setBackground(Color.BLACK);
        loginFrame.setResizable(false);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new FlowLayout());
        loginFrame.setLayout(null);

        // Creating a login button
        JButton loginButton = new JButton("Log In");

        // Creating a field for username and a field for password
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        // Setting the position
        usernameField.setBounds(18, 30, 200, 20);
        passwordField.setBounds(18, 85, 200, 20);
        loginButton.setBounds(80, 150, 70, 20);

        loginButton.setBackground(Color.decode("#00306D"));
        loginButton.setForeground(Color.WHITE);

        // Create labels for the message that is displayed, username and password
        JLabel messageLabel = new JLabel("Introduce data");
        JLabel usernameLabel = new JLabel("USERNAME");
        JLabel passwordLabel = new JLabel("PASSWORD");

        // Setting their location, font, colors
        messageLabel.setBounds(50, 110, 250, 20);
        messageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        usernameLabel.setBounds(65, 5, 250, 20);
        passwordLabel.setBounds(65, 60, 250, 20);

        messageLabel.setFont(new Font("Courier", Font.BOLD, 15));
        messageLabel.setForeground(Color.ORANGE);

        usernameLabel.setFont(new Font("Courier", Font.BOLD, 18));
        usernameLabel.setForeground(Color.ORANGE);

        passwordLabel.setFont(new Font("Courier", Font.BOLD, 18));
        passwordLabel.setForeground(Color.ORANGE);

        // Adding the elements to the frame
        loginFrame.add(usernameLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passwordField);
        loginFrame.add(passwordLabel);
        loginFrame.add(loginButton);
        loginFrame.add(messageLabel);
        
        // Adding a listener for the login button when it is pressed
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                // Checking the credentials (bad practice - not recommended)
                if (username.equals("Admin") && password.equals("music123")) {
                    messageLabel.setText("Success!");
                    messageLabel.setBounds(90, 110, 250, 20);
                    messageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

                    MusicPlayer mp = new MusicPlayer();
                    loginFrame.dispose();
                } else {
                    messageLabel.setText("Username or password incorrect!");
                    messageLabel.setBounds(10, 110, 250, 20);
                    messageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                }
            }
        });

        // Setting the frame to be centered and visible
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
}
