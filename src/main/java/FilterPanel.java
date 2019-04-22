import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FilterPanel extends JPanel {

    private View view;
    private KeyMetricsPanel keyMetricsPanel;
    private ChartsPanel chartsPanel;
    private int canEnable = 0;
    private int canBeEnabled = 0;
    private JButton apply;
    private JButton reset;
    private HistogramOfClickCostsPanel histogramOfClickCostsPanel;
    private ArrayList<JCheckBox> filter1 = new ArrayList<>();
    private ArrayList<JCheckBox> filter2 = new ArrayList<>();
    private ArrayList<JCheckBox> filter3 = new ArrayList<>();
    private ArrayList<JCheckBox> filter4 = new ArrayList<>();
    private DatePicker startDate;
    private DatePicker endDate;

    public void disableFiltering(int canBeEnabled) {
        canEnable = 0;
        apply.setEnabled(false);
        reset.setEnabled(false);
        this.canBeEnabled = canBeEnabled;
    }

    public void enableFiltering() {
        if(canEnable != canBeEnabled)
            canEnable++;
        if(canEnable == canBeEnabled) {
            canEnable = 0;
            apply.setEnabled(true);
            reset.setEnabled(true);
            revalidate();
            repaint();
        }
    }

    public String function (String condition, String temp) {
        if(condition.equals("")) {
            if(!temp.equals("")) {
                condition += "(" + temp + ")";
            }
        } else {
            if(!temp.equals("")) {
                condition = condition + " AND " + "(" + temp + ")";
            }
        }
        return condition;
    }

    public String generateFiltersString () {
        String condition = "";
        String temp = generateFilterString(filter1, "i.Gender");
        condition = function(condition, temp);
        temp = generateFilterString(filter2, "i.AGE");
        condition = function(condition, temp);
        temp = generateFilterString(filter3, "i.Income");
        condition = function(condition, temp);
        temp = generateFilterString(filter4, "i.Context");
        condition = function(condition, temp);
        //System.out.println(condition);
        condition = function(condition,generateFilterStringForDate());
        //System.out.println(condition);
        return condition;
    }

    public String generateFilterStringForDate () {
        String condition = "";
        condition = "strftime('%Y-%m-%d', i.Entry_Date) >= '" + startDate.getDate() + "' AND " + "strftime('%Y-%m-%d', i.Entry_Date) <= '" + endDate.getDate() + "'";
        return condition;
    }

    public String generateFilterString (ArrayList<JCheckBox> filter, String columnName) {
        String condition = "";
        boolean flag = false;
        for(JCheckBox elem : filter) {
            if(!elem.isSelected()) {
                flag = true;
                break;
            }
        }
        if(flag == false)
            return "";
        for(JCheckBox elem : filter) {
            if(elem.isSelected()) {
                if (condition.equals("")) {
                    condition = condition + columnName + " = '" + elem.getText() + "'";
                } else {
                    condition = condition + " OR " + columnName + " = '" + elem.getText() + "'";
                }
            }
        }
        return condition;
    }

    public void init(View view, KeyMetricsPanel keyMetricsPanel, ChartsPanel chartsPanel, HistogramOfClickCostsPanel histogramOfClickCostsPanel) {
        this.view = view;
        this.keyMetricsPanel = keyMetricsPanel;
        this.chartsPanel = chartsPanel;
        this.histogramOfClickCostsPanel = histogramOfClickCostsPanel;

        this.setLayout(new BorderLayout());

        JLabel text = new JLabel("<html><h3>Filter the data:</h3></html>");
        text.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(text, BorderLayout.NORTH);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;

        //Data range wrapper
        JPanel dataRangeWapper = new JPanel();
        dataRangeWapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 20));
        dataRangeWapper.setOpaque(false);
        dataRangeWapper.setLayout(new BorderLayout());

        JLabel dataRangeText = new JLabel("<html><h4>Data range:</h4></html>");
        dataRangeText.setAlignmentX(Component.LEFT_ALIGNMENT);
        dataRangeWapper.add(dataRangeText, BorderLayout.NORTH);

        Container dataContainer = new Container();
        dataContainer.setLayout(new GridLayout(4,1));
        JLabel dataStartText = new JLabel("Start date:");
        JLabel dataEndText = new JLabel("End date:");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DatePickerSettings startDateSettings = new DatePickerSettings();
        startDate = new DatePicker(startDateSettings);
        startDate.setDate(LocalDate.parse(view.getMinDate(), formatter));

        DatePickerSettings endDateSettings = new DatePickerSettings();
        endDate = new DatePicker(endDateSettings);
        endDate.setDate(LocalDate.parse(view.getMaxDate(), formatter));

        endDate.addDateChangeListener(new DateChangeListener() {
            @Override
            public void dateChanged(DateChangeEvent dateChangeEvent) {
                if((startDate.getDate().compareTo(endDate.getDate()) > 0)) {
                    endDate.setDate(startDate.getDate());
                    endDate.closePopup();
                    JOptionPane.showMessageDialog(view.getDash(), "Your end date should be bigger or equal than start date!", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        startDate.addDateChangeListener(new DateChangeListener() {
            @Override
            public void dateChanged(DateChangeEvent dateChangeEvent) {
                if((startDate.getDate().compareTo(endDate.getDate()) > 0)) {
                    startDate.setDate(endDate.getDate());
                    startDate.closePopup();
                    JOptionPane.showMessageDialog(view.getDash(), "Your start date should be smaller or equal than end date!", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        startDateSettings.setDateRangeLimits(LocalDate.parse(view.getMinDate(), formatter), LocalDate.parse(view.getMaxDate(), formatter));
        startDateSettings.setAllowEmptyDates(false);
        endDateSettings.setDateRangeLimits(LocalDate.parse(view.getMinDate(), formatter), LocalDate.parse(view.getMaxDate(), formatter));
        endDateSettings.setAllowEmptyDates(false);
        view.setDateRestriction(generateFilterStringForDate());
        //startDate.set

        //startDate.addDateSelectionConstraint(new RangeConstraint(new Date(view.getMinDate()), new Date(view.getMaxDate())));
        //endDate.addDateSelectionConstraint(new RangeConstraint(new Date(view.getMinDate()), new Date(view.getMaxDate())));

        dataContainer.add(dataStartText);
        dataContainer.add(startDate);
        dataContainer.add(dataEndText);
        dataContainer.add(endDate);

        dataRangeWapper.add(dataContainer, BorderLayout.SOUTH);

        //Gender Wrapper
        JPanel genderWapper = new JPanel();
        genderWapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        genderWapper.setOpaque(false);
        genderWapper.setLayout(new BorderLayout());

        JLabel genderText = new JLabel("<html><h4>Gender:</h4></html>");
        genderText.setAlignmentX(Component.LEFT_ALIGNMENT);
        genderWapper.add(genderText, BorderLayout.NORTH);

        Container genderCheckContainer = new Container();
        genderCheckContainer.setLayout(new GridLayout(2,1));
        JCheckBox maleCheck = new JCheckBox("Male");
        JCheckBox femaleCheck = new JCheckBox("Female");
        genderCheckContainer.add(maleCheck);
        genderCheckContainer.add(femaleCheck);

        genderWapper.add(genderCheckContainer, BorderLayout.SOUTH);

        filter1.add(maleCheck);
        filter1.add(femaleCheck);

        //Age Wrapper
        JPanel ageWapper = new JPanel();
        ageWapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        ageWapper.setOpaque(false);
        ageWapper.setLayout(new BorderLayout());

        JLabel ageText = new JLabel("<html><h4>Age:</h4></html>");
        ageText.setAlignmentX(Component.LEFT_ALIGNMENT);
        ageWapper.add(ageText, BorderLayout.NORTH);

        Container ageCheckContainer = new Container();
        ageCheckContainer.setLayout(new GridLayout(5,1));
        JCheckBox age1Check = new JCheckBox("<25");
        JCheckBox age2Check = new JCheckBox("25-34");
        JCheckBox age3Check = new JCheckBox("35-44");
        JCheckBox age4Check = new JCheckBox("45-54");
        JCheckBox age5Check = new JCheckBox(">54");

        ageCheckContainer.add(age1Check);
        ageCheckContainer.add(age2Check);
        ageCheckContainer.add(age3Check);
        ageCheckContainer.add(age4Check);
        ageCheckContainer.add(age5Check);

        ageWapper.add(ageCheckContainer, BorderLayout.SOUTH);

        filter2.add(age1Check);
        filter2.add(age2Check);
        filter2.add(age3Check);
        filter2.add(age4Check);
        filter2.add(age5Check);

        //income wrapper
        JPanel incomeWapper = new JPanel();
        incomeWapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        incomeWapper.setOpaque(false);
        incomeWapper.setLayout(new BorderLayout());

        JLabel incomeText = new JLabel("<html><h4>Income:</h4></html>");
        incomeText.setAlignmentX(Component.LEFT_ALIGNMENT);
        incomeWapper.add(incomeText, BorderLayout.NORTH);

        Container incomeCheckContainer = new Container();
        incomeCheckContainer.setLayout(new GridLayout(3,1));
        JCheckBox income1Check = new JCheckBox("Low");
        JCheckBox income2Check = new JCheckBox("Medium");
        JCheckBox income3Check = new JCheckBox("High");

        incomeCheckContainer.add(income1Check);
        incomeCheckContainer.add(income2Check);
        incomeCheckContainer.add(income3Check);

        incomeWapper.add(incomeCheckContainer, BorderLayout.SOUTH);

        filter3.add(income1Check);
        filter3.add(income2Check);
        filter3.add(income3Check);

        //context wrapper
        JPanel contextWapper = new JPanel();
        contextWapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        contextWapper.setOpaque(false);
        contextWapper.setLayout(new BorderLayout());

        JLabel contextText = new JLabel("<html><h4>Context:</h4></html>");
        contextText.setAlignmentX(Component.LEFT_ALIGNMENT);
        contextWapper.add(contextText, BorderLayout.NORTH);

        Container contextCheckContainer = new Container();
        contextCheckContainer.setLayout(new GridLayout(6,1));
        JCheckBox context1Check = new JCheckBox("News");
        JCheckBox context2Check = new JCheckBox("Shopping");
        JCheckBox context3Check = new JCheckBox("Social Media");
        JCheckBox context4Check = new JCheckBox("Blog");
        JCheckBox context5Check = new JCheckBox("Hobbies");
        JCheckBox context6Check = new JCheckBox("Travel");

        contextCheckContainer.add(context1Check);
        contextCheckContainer.add(context2Check);
        contextCheckContainer.add(context3Check);
        contextCheckContainer.add(context4Check);
        contextCheckContainer.add(context5Check);
        contextCheckContainer.add(context6Check);

        contextWapper.add(contextCheckContainer, BorderLayout.SOUTH);
        filter4.add(context1Check);
        filter4.add(context2Check);
        filter4.add(context3Check);
        filter4.add(context4Check);
        filter4.add(context5Check);
        filter4.add(context6Check);

        //reset button
        reset = new JButton("Reset filters");
        apply = new JButton("Apply filters");

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context1Check.setSelected(false);
                context2Check.setSelected(false);
                context3Check.setSelected(false);
                context4Check.setSelected(false);
                context5Check.setSelected(false);
                context6Check.setSelected(false);

                income1Check.setSelected(false);
                income2Check.setSelected(false);
                income3Check.setSelected(false);

                age1Check.setSelected(false);
                age2Check.setSelected(false);
                age3Check.setSelected(false);
                age4Check.setSelected(false);
                age5Check.setSelected(false);

                maleCheck.setSelected(false);
                femaleCheck.setSelected(false);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(view.getMinDate(), formatter);
                startDate.setDate(localDate);
                localDate = LocalDate.parse(view.getMaxDate(), formatter);
                endDate.setDate(localDate);

                view.setDateRestriction(generateFilterStringForDate());

                keyMetricsPanel.changeKeyMetrics(generateFiltersString());
                chartsPanel.resetChart("");
                histogramOfClickCostsPanel.updateHistrogram("");

                disableFiltering(3);
            }
        });
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String conditions = generateFiltersString();

                view.setDateRestriction(generateFilterStringForDate());

                keyMetricsPanel.changeKeyMetrics(conditions);
                chartsPanel.resetChart(conditions);
                histogramOfClickCostsPanel.updateHistrogram(conditions);

                disableFiltering(3);
            }
        });

        //ignore me
        String conditions = generateFiltersString();
        view.setDateRestriction(generateFilterStringForDate());
        chartsPanel.resetChart(conditions);
        disableFiltering(1);

        Container buttonsWrapper = new Container();
        buttonsWrapper.setLayout(new GridLayout(2,1));
        buttonsWrapper.add(reset);
        buttonsWrapper.add(apply);

        wrapper.add(dataRangeWapper,gbc);
        wrapper.add(genderWapper,gbc);
        wrapper.add(ageWapper,gbc);
        wrapper.add(incomeWapper,gbc);
        wrapper.add(contextWapper,gbc);

        JPanel filterContainer = new JPanel();
        filterContainer.setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane(wrapper, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setOpaque(false);
        pane.setBorder(BorderFactory.createEmptyBorder());
        filterContainer.add(pane, BorderLayout.CENTER);
        //this.add(reset, BorderLayout.SOUTH);
        this.add(pane, BorderLayout.CENTER);
        this.add(buttonsWrapper, BorderLayout.SOUTH);


    }
}
