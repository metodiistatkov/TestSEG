import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileSelectPanel extends Container {

    private View view;

    public void init(View view) {
        this.view = view;
        this.setLayout(new BorderLayout());

        Container wrapper = new Container();
        wrapper.setLayout(new BorderLayout());

        JLabel text = new JLabel("<html><h3>Select the log files for your marketing campaign:</h3></html>");
        text.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(text, BorderLayout.NORTH);

        JFileChooser fileDialog = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
        fileDialog.setFileFilter(filter);

        JPanel fileChooserWrapper = new JPanel();
        fileChooserWrapper.setLayout(new GridLayout(6,1));

        fileChooserWrapper.add(new JLabel("<html><h4>Select file for impression_log.csv:</h4><html>"));

        Container fileChooserWrapper1 = new Container();
        fileChooserWrapper1.setLayout(new GridLayout(1,2, 20, 20));
        JLabel impressionLogFileText = new JLabel("No file selected.");
        JButton impressionLogFileButton = new JButton("Select file...");
        final String[] LogFilePath = new String[3];
        impressionLogFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(getParent());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getSelectedFile();
                    impressionLogFileText.setText(file.getPath());
                    LogFilePath[0] = file.getPath();
                }
            }
        });

        fileChooserWrapper1.add(impressionLogFileText);
        fileChooserWrapper1.add(impressionLogFileButton);
        fileChooserWrapper.add(fileChooserWrapper1);

        fileChooserWrapper.add(new JLabel("<html><h4>Select file for click_log.csv:</h4><html>"));

        Container fileChooserWrapper2 = new Container();
        fileChooserWrapper2.setLayout(new GridLayout(1,2, 20, 20));
        JLabel clickLogFileText = new JLabel("No file selected.");
        JButton clickLogFileButton = new JButton("Select file...");
        clickLogFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(getParent());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getSelectedFile();
                    clickLogFileText.setText(file.getPath());
                    LogFilePath[1] = file.getPath();
                }
            }
        });

        fileChooserWrapper2.add(clickLogFileText);
        fileChooserWrapper2.add(clickLogFileButton);
        fileChooserWrapper.add(fileChooserWrapper2);

        fileChooserWrapper.add(new JLabel("<html><h4>Select file for server_log.csv:</h4><html>"));

        Container fileChooserWrapper3 = new Container();
        fileChooserWrapper3.setLayout(new GridLayout(1,2, 20, 20));
        JLabel serverLogFileText = new JLabel("No file selected.");
        JButton serverLogFileButton = new JButton("Select file...");
        serverLogFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(getParent());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getSelectedFile();
                    serverLogFileText.setText(file.getPath());
                    LogFilePath[2] = file.getPath();
                }
            }
        });

        fileChooserWrapper3.add(serverLogFileText);
        fileChooserWrapper3.add(serverLogFileButton);
        fileChooserWrapper.add(fileChooserWrapper3);

        fileChooserWrapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        fileChooserWrapper.setOpaque(false);
        wrapper.add(fileChooserWrapper, BorderLayout.CENTER);


        this.add(wrapper, BorderLayout.NORTH);

        JButton resetButton = new JButton("Open the dashboard using the new files");
        resetButton.setSize(resetButton.getWidth(), serverLogFileButton.getHeight());
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                for(int i = 0; i < 3; i++) {
                    if(LogFilePath[i] == null) {
                        flag = false;
                        break;
                    }


                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(LogFilePath[i]));
                        try {
                            String firstLine = br.readLine();
                            long count = firstLine.chars().filter(ch -> ch == ',').count();

                            switch (i) {
                                case 0: if(count != 6) flag = false; break;
                                case 1: if(count != 2) flag = false; break;
                                case 2: if(count != 4) flag = false; break;
                            }
                            br.close();
                        } catch (IOException e1) {
                            e1.printStackTrace() ;
                        }
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }

                }
                if(flag)
                    view.reset(LogFilePath);
                else
                    JOptionPane.showMessageDialog(null, "All files selected should be the appropriate one!", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        this.add(resetButton, BorderLayout.SOUTH);
    }
}
