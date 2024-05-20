package com.egor456788;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainGUI extends JFrame {
    private String username;
    private static ResourceBundle bundle;
    private Locale currentLocale;

    private JLabel userLabel;
    private JButton changeLanguageButton;
    private JButton commandMenuButton;
    private JTable collectionTable;
    private JPanel visualizationPanel;
    private JPanel filterPanel;
    private JTextField filterTextField;

    private String[] enumFields = {"devotion", "race", "gender"};
    private static Map<String, List<String>> enumValues;

    public MainGUI(String username, Locale initialLocale) {
        this.username = username;
        this.currentLocale = initialLocale;
        this.bundle = ResourceBundle.getBundle("messages", currentLocale);

        enumValues = new HashMap<>();
        enumValues.put("devotion", Arrays.asList("Flowers", "Barometer","Namira", "Light", "Boethiah"));
        enumValues.put("race", Arrays.asList("Hemulen", "Hattifattner"));
        enumValues.put("gender", Arrays.asList("Male", "Female","Helicopter"));

        createUI();
        loadData();
    }

    private void createUI() {
        setTitle(bundle.getString("title"));
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userLabel = new JLabel(bundle.getString("current_user") + ": " + username);
        changeLanguageButton = new JButton(bundle.getString("change_language"));
        commandMenuButton = new JButton(bundle.getString("command_menu"));

        topPanel.add(userLabel);
        topPanel.add(changeLanguageButton);
        topPanel.add(commandMenuButton);

        add(topPanel, BorderLayout.NORTH);

        // Create the table
        String[] columnNames = {
                bundle.getString("column1"), bundle.getString("column2"), bundle.getString("column3"),
                bundle.getString("column4"), bundle.getString("column5"), bundle.getString("column6"),
                bundle.getString("column7"), bundle.getString("column8"), bundle.getString("column9")
        };

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        collectionTable = new JTable(tableModel);
        collectionTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(collectionTable);

        add(scrollPane, BorderLayout.CENTER);



        // Create the visualization panel
        visualizationPanel = new JPanel();
        visualizationPanel.setPreferredSize(new Dimension(200, 600));
        add(visualizationPanel, BorderLayout.EAST);

        // Create the filter panel
        filterPanel = new JPanel(new GridLayout(1, columnNames.length));
        filterTextField = new JTextField(20);
        for (int i = 0; i < columnNames.length; i++) {
            JTextField textField = new JTextField(10);
            filterPanel.add(new JLabel(columnNames[i] + ":"));
            filterPanel.add(textField);


            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filterTable();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filterTable();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filterTable();
                }
            });
        }

        add(filterPanel, BorderLayout.SOUTH);



        // Button actions
        changeLanguageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLanguage();
            }
        });

        commandMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCommandMenu();
            }
        });


        collectionTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    DefaultTableModel model = (DefaultTableModel) e.getSource();
                    String[] data = (String[]) Stream.of(model.getDataVector().elementAt(row).toArray()).map(Object::toString).toArray();

                }
            }
        });
        collectionTable.getColumnModel().getColumn(2).setCellEditor(new EnumCellEditor(getTranslatedEnumValues("devotion")));
        collectionTable.getColumnModel().getColumn(3).setCellEditor(new EnumCellEditor(getTranslatedEnumValues("race")));
        collectionTable.getColumnModel().getColumn(7).setCellEditor(new EnumCellEditor(getTranslatedEnumValues("gender")));
    }
    // Метод filterColumn для фильтрации по столбцу с заданным значением

    // Метод filterTable для фильтрации по всем столбцам
    private void filterTable() {
        DefaultTableModel model = (DefaultTableModel) collectionTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        collectionTable.setRowSorter(sorter);
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        for (int i = 0; i < model.getColumnCount(); i++) {
            String text = ((JTextField) filterPanel.getComponent(i * 2 + 1)).getText();
            if (!text.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + text, i));
            }
        }
        if (!filters.isEmpty()) {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        } else {
            sorter.setRowFilter(null);
        }
    }

    public  List<String> getTranslatedEnumValues(String field) {
        return enumValues.get(field).stream()
                .map(value -> bundle.getString(value))
                .collect(Collectors.toList());
    }



    private void changeLanguage() {
        String[] options = {"English", "Русский", "Íslenska", "Latviešu", "Español"};
        int choice = JOptionPane.showOptionDialog(
                this,
                bundle.getString("select_language"),
                bundle.getString("change_language"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choice) {
            case 0:
                setLoc(new Locale("en"));
                break;
            case 1:
                setLoc(new Locale("ru"));
                break;
            case 2:
                setLoc(new Locale("is"));
                break;
            case 3:
                setLoc(new Locale("lv"));
                break;
            case 4:
                setLoc(new Locale("es", "EC"));
                break;
        }
        collectionTable.getColumnModel().getColumn(2).setCellEditor(new EnumCellEditor(getTranslatedEnumValues("devotion")));
        collectionTable.getColumnModel().getColumn(3).setCellEditor(new EnumCellEditor(getTranslatedEnumValues("race")));
        collectionTable.getColumnModel().getColumn(7).setCellEditor(new EnumCellEditor(getTranslatedEnumValues("gender")));
    }

    private void setLoc(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateTexts();
        loadData();
        updateFilters();
    }

    private void updateFilters() {
        String[] columnNames = {
                bundle.getString("column1"), bundle.getString("column2"), bundle.getString("column3"),
                bundle.getString("column4"), bundle.getString("column5"), bundle.getString("column6"),
                bundle.getString("column7"), bundle.getString("column8"), bundle.getString("column9")
        };
        int i = 0;
        for (Component component : filterPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                // Обновляем текст подписи
                label.setText(columnNames[i]);
                i++;
            }
        }
    }

    private void updateTexts() {
        setTitle(bundle.getString("title"));
        userLabel.setText(bundle.getString("current_user") + ": " + username);
        changeLanguageButton.setText(bundle.getString("change_language"));
        commandMenuButton.setText(bundle.getString("command_menu"));

        // Update table headers
        String[] columnNames = {
                bundle.getString("column1"), bundle.getString("column2"), bundle.getString("column3"),
                bundle.getString("column4"), bundle.getString("column5"), bundle.getString("column6"),
                bundle.getString("column7"), bundle.getString("column8"), bundle.getString("column9")
        };
        for (int i = 0; i < columnNames.length; i++) {
            collectionTable.getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);
        }
        collectionTable.getTableHeader().repaint();
    }

    private void showCommandMenu() {
        JOptionPane.showMessageDialog(this, bundle.getString("command_menu_placeholder"), bundle.getString("command_menu"), JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadData() {
        String response = Sender.sendToServer("show");
        DefaultTableModel model = (DefaultTableModel) collectionTable.getModel();

        // Clear existing data
        model.setRowCount(0);

        // Regex to match entries
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(response);

        while (matcher.find()) {
            String entry = matcher.group(1);
            String[] fields = entry.split(", ");
            Object[] rowData = new Object[fields.length];

            for (int i = 0; i < fields.length; i++) {
                String[] keyValue = fields[i].split("=");
                String key = keyValue[0].trim();
                String value = keyValue[1].replace("'", "").trim();

                // Translate enum fields
                if (Stream.of(enumFields).anyMatch(key::equals)) {
                    value = bundle.getString(value);
                }
                rowData[i] = value;
            }

            model.addRow(rowData);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Locale initialLocale = new Locale("en");
                new MainGUI("User123", initialLocale).setVisible(true);
            }
        });
    }
}
