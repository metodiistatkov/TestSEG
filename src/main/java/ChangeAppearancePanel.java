import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeAppearancePanel extends Container {

    private View view;
    private Color colorToSet = new JLabel().getForeground();
    private Color backgroundColorToSet = new JPanel().getBackground();

    public void init(View view) {
        this.view = view;
        this.setLayout(new BorderLayout());

        Container wrapper = new Container();
        wrapper.setLayout(new BorderLayout());

        JLabel text = new JLabel("<html><h3>Change the appearance of the application:</h3></html>");
        text.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(text, BorderLayout.NORTH);

        Container changeApperancePanel = new Container();
        changeApperancePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;

        SpinnerModel valueFontSize = new SpinnerNumberModel(new JLabel().getFont().getSize(), 1, 72, 1);
        JSpinner fontSize = new JSpinner(valueFontSize);

        changeApperancePanel.add(new JLabel("Change Font Size:"));
        changeApperancePanel.add(fontSize);

        String[] optionList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();  ;
        JComboBox optionsList = new JComboBox(optionList);
        int option = 0;
        for(int i = 0; i < optionList.length; i++)
            if(optionList[i].equals(new JLabel().getFont().getName())) {
                option = i;
                break;
            }
        optionsList.setSelectedIndex(option);

            changeApperancePanel.add(new JLabel("Change Font Family:"));
            changeApperancePanel.add(optionsList);

        JButton textColor = new JButton("Change Text Colour");

        changeApperancePanel.add(textColor);

        //adds the color action to the color button
        textColor.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                //opens a pop-up windows where the user can choose the colour for drawing
                Color newColor = JColorChooser.showDialog(getParent(), "Choose Text Colour", new JLabel().getForeground());
                if(newColor != null){
                    colorToSet = newColor;
                } else {
                    colorToSet = new JLabel().getForeground();
                }
            }

        });


        wrapper.add(changeApperancePanel, BorderLayout.SOUTH);

        this.add(wrapper, BorderLayout.NORTH);

        JButton resetButton = new JButton("Change the font");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.changeStyle((int) fontSize.getValue(), (String) optionsList.getItemAt(optionsList.getSelectedIndex()), colorToSet);
            }
        });
        this.add(resetButton, BorderLayout.SOUTH);
    }
}
