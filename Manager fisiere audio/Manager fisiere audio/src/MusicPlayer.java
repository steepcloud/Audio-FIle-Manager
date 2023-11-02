import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MusicPlayer extends JFrame {
    // variable panel in which we store all GUI components
    private JPanel panel;

    // variable songNameLabel in which we store the name of the song
    private JLabel songNameLabel;

    // variable chooseButton in which we store the button for choosing the song
    private JButton chooseButton;

    // JList variable songList in which we store the list of songs
    private JList<String> songList;

    // variable listModelAbsolute in which we store the absolute path of the file
    private DefaultListModel<String> listModelAbsolute;

    // variable listModelName in which we store the name of the song
    private DefaultListModel<String> listModelName;

    // variable currentDirectory in which we store the current directory
    private String currentDirectory;

    // Setter for the song label with the text received as a parameter
    public void setSongNameLabel(String text) {
        songNameLabel.setText(text);
    }

    // MusicPlayer class constructor
    public MusicPlayer() {
        // Setting the current directory (initial)
        currentDirectory = "Music";

        // Creating the panel
        Color panelColor = new Color(27, 27, 27);
        panel = new JPanel();
        panel.setBackground(panelColor);
        panel.setSize(new Dimension(312, 540));
        panel.setLayout(null);

        // Set the panel to not be maximized
        setResizable(false);

        // Create the list of songs
        listModelAbsolute = new DefaultListModel<String>();
        listModelName = new DefaultListModel<String>();
        Logic logic = new Logic(listModelName, listModelAbsolute, currentDirectory);
        logic.loadSongs();
        songList = new JList<>(listModelName);
        songList.setSize(new Dimension(296, 370));
        songList.setLocation(0, 130);

        Color listBGColor = Color.decode("#4A4A4A");
        Color listFGColor = Color.decode("#FF9648");

        // Adding the list of songs
        songList.setBackground(listBGColor);
        songList.setForeground(listFGColor);
        songList.setFont(new Font("Courier", Font.BOLD, 15));
        songList.setVisible(true);
        panel.add(songList);

        // Creating the label with the name of the song (initially no song is selected so it is "-")
        songNameLabel = new JLabel("-");
        songNameLabel.setSize(new Dimension(312, 43));
        songNameLabel.setLocation(0, 18);
        songNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        songNameLabel.setVerticalAlignment(SwingConstants.CENTER);
        songNameLabel.setFont(new Font("System", Font.PLAIN, 15));
        songNameLabel.setForeground(Color.ORANGE);
        panel.add(songNameLabel); // Adding the label to the panel

        // Adding a listener for the list
        songList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // boolean returns a value indicating whether the selection is in the process of changing or not
                    String selectedName = songList.getSelectedValue();
                    if (selectedName != null) {
                        selectedName = selectedName.toString();
                        String selectedAbsolutePath = listModelAbsolute.get(songList.getSelectedIndex());
                        if (selectedName != null && (selectedName.endsWith(".mp3") || selectedName.endsWith(".wav") || selectedName.endsWith(".flac") || selectedName.endsWith(".m4a"))) {
                            songNameLabel.setText(selectedName); // If a song with one of the verified extensions has been selected, we set the label with the name of the song
                        }
                    }
                }
            }
        });

        // Creating the search bar
        Color searchBGColor = Color.decode("#272727");
        JTextField searchField = new JTextField();
        searchField.setText("Search file");
        searchField.setSize(new Dimension(149, 20));
        searchField.setLocation(150,0);
        searchField.setForeground(Color.GRAY);
        searchField.setBackground(searchBGColor);
        panel.add(searchField); // Adding the search bar to the panel

        // Adding a mouse listener for the search field to remove the already present text
        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (searchField.getText().equals("Search file")) // If the search field was pressed, the text is removed
                    searchField.setText("");
            }
        });

        // Adding a focus listener for the search field to reset the text if it was selected before
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!searchField.getText().equals("Search file"))
                    searchField.setText("Search file");
            }
        });

        // Adding an action listener for the search field to reset the text if it was selected before and if the song name was already selected, it resets the name for the new song
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchFile = searchField.getText();
                if (!searchFile.equals("Search file")) {
                    if (!songNameLabel.getText().equals("-")) {
                        songNameLabel.setText("-");
                    }
                }
                
                // We call the recursive search from the Logic class
                logic.searchForFile(new File(currentDirectory), searchFile.toLowerCase(), MusicPlayer.this);
            }
        });

        
        // Creating the button for choosing the folder
        chooseButton = new JButton("Choose");
        chooseButton.setSize(new Dimension(80, 20));
        chooseButton.setLocation(0, 0);
        chooseButton.setBackground(Color.BLACK);
        chooseButton.setForeground(Color.ORANGE);
        panel.add(chooseButton); // Adding the button to the panel

        // Adding an action listener for the button
        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Creating a pop-up file dialog
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choose a directory");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    if (!songNameLabel.getText().equals("-")) {
                        songNameLabel.setText("-");
                    }
                    // If the file was found, we clear the list of songs already present
                    songList.clearSelection();
                    listModelAbsolute.clear();
                    listModelName.clear();
                    currentDirectory = chooser.getSelectedFile().getAbsolutePath();
                    logic.setCurrentDirectory(currentDirectory); // Setting the new directory as the initial directory
                    File[] files = new File(currentDirectory).listFiles();
                    if (new File(currentDirectory) != null) {
                        if (files != null) {
                            for (File file : files) {
                                if (file.isFile() && (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")
                                        || file.getName().endsWith(".m4a") || file.getName().endsWith(".flac"))) {
                                    // Populating the list with all the audio files found in the folder
                                    listModelName.addElement(file.getName());
                                    listModelAbsolute.addElement(file.getAbsolutePath());
                                }
                            }
                        }
                    }
                }
            }
        });

        // Creating an icon for the play button
        ImageIcon iconPlay = new ImageIcon(getClass().getResource("play.png"));

        // Creating the play button
        JButton playButton = new JButton(iconPlay);
        playButton.setSize(new Dimension(50, 50));
        playButton.setLocation(120, 60);
        playButton.setBackground(Color.WHITE);
        panel.add(playButton); // Adding the button to the panel

        // Adding an action listener for the play button to play the audio file using the native application of the operating system
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = songList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedAbsolutePath = listModelAbsolute.get(selectedIndex);
                    File audioFile = new File(selectedAbsolutePath);
                    try {
                        Desktop.getDesktop().open(audioFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Create a JPopupMenu for the file rename options, add prefix / suffix
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem renameFile = new JMenuItem("Rename");
        JMenuItem addSuffix = new JMenuItem("Add suffix");
        JMenuItem addPrefix = new JMenuItem("Add prefix");

        // Adding the menu items to the menu
        popupMenu.add(renameFile);
        popupMenu.add(addSuffix);
        popupMenu.add(addPrefix);
        
        // Adding a mouse listener for the song list, when right click is pressed, the menu appears
        songList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int index = songList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        songList.setSelectedIndex(index);
                        popupMenu.show(songList, e.getX(), e.getY());
                    }
                }
            }
        });

        // Listener for renaming the files in which we also check if the new name is equivalent to an already existing file
        // must add the extension of the file, else it doesn't work
        renameFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = songList.getSelectedValue();
                String selectedAbsolutePath = listModelAbsolute.get(songList.getSelectedIndex());
                String newName = JOptionPane.showInputDialog(MusicPlayer.this, "Enter new name: ");
                File oldFile = new File(selectedAbsolutePath);
                File newFile = new File(oldFile.getParent() + "\\" + newName);
                if (newFile.exists()) {
                    JOptionPane.showMessageDialog(MusicPlayer.this, "A file with the same name already exists. Please choose a different name");
                    return;
                } else {
                    oldFile.renameTo(newFile);
                    listModelAbsolute.setElementAt(newFile.getAbsolutePath(), songList.getSelectedIndex());
                    listModelName.setElementAt(newName, songList.getSelectedIndex());
                    songNameLabel.setText(newName);
                    songList.updateUI();
                }

            }
        });
        
        // Listener for suffix adding in which we also check if the new name is equivalent to an already existing file
        addSuffix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = songList.getSelectedValue();
                String selectedAbsolutePath = listModelAbsolute.get(songList.getSelectedIndex());
                String suffix = JOptionPane.showInputDialog(MusicPlayer.this, "Enter suffix:");
                File oldFile = new File(selectedAbsolutePath);
                String newName = selectedName.substring(0, selectedName.lastIndexOf(".")) + suffix + selectedName.substring(selectedName.lastIndexOf("."));
                File newFile = new File(oldFile.getParent() + "\\" + newName);
                if (newFile.exists()) {
                    JOptionPane.showMessageDialog(MusicPlayer.this, "A file with the same name already exists. Please choose a different name");
                    return;
                } else {
                    oldFile.renameTo(newFile);
                    listModelAbsolute.setElementAt(newFile.getAbsolutePath(), songList.getSelectedIndex());
                    listModelName.setElementAt(newName, songList.getSelectedIndex());
                    songNameLabel.setText(newName);
                    songList.updateUI();
                }
            }
        });
        // Listener for prefix adding in which we also check if the new name is equivalent to an already existing file
        addPrefix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = songList.getSelectedValue();
                String selectedAbsolutePath = listModelAbsolute.get(songList.getSelectedIndex());
                String prefix = JOptionPane.showInputDialog(MusicPlayer.this, "Enter prefix:");
                File oldFile = new File(selectedAbsolutePath);
                String newName = prefix + selectedName;
                File newFile = new File(oldFile.getParent() + "\\" + newName);
                if (newFile.exists()) {
                    JOptionPane.showMessageDialog(MusicPlayer.this, "A file with the same name already exists. Please choose a different name");
                    return;
                } else {
                    oldFile.renameTo(newFile);
                    listModelAbsolute.setElementAt(newFile.getAbsolutePath(), songList.getSelectedIndex());
                    listModelName.setElementAt(newName, songList.getSelectedIndex());
                    songNameLabel.setText(newName);
                    songList.updateUI();
                }
            }
        });
        
        // Create the scrollbar for the songs, in case there are more
        JScrollPane scrollPane = new JScrollPane(songList);
        scrollPane.setBounds(0, 130, 296, 370);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        panel.add(scrollPane);

        // Adding the panel to the frame
        add(panel);
        setTitle("Music Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(312, 540));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
