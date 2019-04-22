import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class KeyMetricContainer extends Container {

    private JLabel name;
    private JLabel value;
    private DecimalFormat df;

    public KeyMetricContainer(String keyMetricName, double initialValue) {
        this.setLayout(new BorderLayout());

        df = new DecimalFormat("#.###");

        name = new JLabel(keyMetricName);
        value = new JLabel("" + df.format(initialValue));

        this.add(name, BorderLayout.CENTER);
        this.add(value, BorderLayout.EAST);
    }

    public void setValue(double value) {
       this.value.setText("" + df.format(value));
    }
}
