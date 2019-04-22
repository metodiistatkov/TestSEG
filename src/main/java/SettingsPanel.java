import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends Container {

    private View view;

    public void init(View view) {
        this.view = view;

        this.setLayout(new BorderLayout());
        JLabel text = new JLabel("<html><h2>Settings</h2></html>");
        text.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.setBackground(new Color(229,229,229));

        //this.add(text, BorderLayout.NORTH);

        Container wrapper = new Container();
        wrapper.setLayout(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.fill

        Container wrapper1 = new Container();
        wrapper1.setLayout(new BorderLayout());

        FileSelectPanel fileSelectPanel = new FileSelectPanel();
        fileSelectPanel.init(view);
        wrapper1.add(text, BorderLayout.NORTH);
        wrapper1.add(fileSelectPanel, BorderLayout.SOUTH);
        wrapper.add(wrapper1, BorderLayout.NORTH);

        Container wrapper2 = new Container();
        wrapper2.setLayout(new BorderLayout());

        ChangeAppearancePanel changeApperancePanel = new ChangeAppearancePanel();
        changeApperancePanel.init(view);
        wrapper2.add(changeApperancePanel, BorderLayout.NORTH);
        wrapper.add(wrapper2, BorderLayout.SOUTH);

        //this.setBackground(new Color(229,229,229));
        this.add(wrapper, BorderLayout.NORTH);
    }
}
