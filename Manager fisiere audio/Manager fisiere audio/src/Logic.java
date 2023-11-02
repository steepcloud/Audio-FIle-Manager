import javax.swing.*;
import java.io.File;
import java.io.FileFilter;

public class Logic {
    // Logic classs where the logic behind the components is found
    private DefaultListModel<String> listModelFileNames;
    private DefaultListModel<String> listModelFilePaths;
    private String currentDirectory;

    // Setter for the current directory
    public void setCurrentDirectory(String path) {
        this.currentDirectory = path;
    }
    // Constructor of the Logic class where the current directory is initialized, the list containing only the name of the song and the list containing the absolute path of the file
    public Logic(DefaultListModel<String> listModelFileNames, DefaultListModel<String> listModelFilePaths, String currentDirectory) {
        this.currentDirectory = currentDirectory;
        this.listModelFileNames = listModelFileNames;
        this.listModelFilePaths = listModelFilePaths;
    }
    // Method for adding songs to the list (list of file names, list of absolute file path)
    public void loadSongs() {
        File dir = new File(currentDirectory);
        if (dir.exists() && dir.canRead()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    listModelFileNames.addElement(file.getName());
                    listModelFilePaths.addElement(file.getAbsolutePath());
                }
            }
        }
    }
    // Recursive search function starting with the current directory in all other subdirectories
    public void searchForFile(File currentDir, String searchFile, MusicPlayer player) {
        File[] files = currentDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().startsWith(searchFile);
            }
        });
        for (File file : files) {
            if (file.isDirectory()) {
                searchForFile(file, searchFile, player);
            } else {
                if (file.getName().toLowerCase().startsWith(searchFile)) {
                    listModelFileNames.clear();
                    listModelFilePaths.clear();
                    listModelFilePaths.addElement(file.getAbsolutePath());
                    listModelFileNames.addElement(file.getName());
                    break;
                }
            }
        }
        if (listModelFileNames.size() == 0)
            player.setSongNameLabel("-");
    }
}
