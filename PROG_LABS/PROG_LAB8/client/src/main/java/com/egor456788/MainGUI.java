package com.egor456788;

import com.egor456788.common.Devotions;
import com.egor456788.common.Genders;
import com.egor456788.common.Races;
import com.egor456788.entities.Entity;
import com.egor456788.entities.Hattifattener;
import com.egor456788.entities.Hemulen;

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

import static com.egor456788.EnumLocalization.*;

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
        enumValues.put("devotion", Arrays.asList("Flowers", "Barometer", "Namira", "Light", "Boethiah"));
        enumValues.put("race", Arrays.asList("Hemulen", "Hattifattner"));
        enumValues.put("gender", Arrays.asList("Male", "Female", "Helicopter"));

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

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 8) {
                    return false;
                }

                String rowUsername = (String) getValueAt(row, 8);
                return rowUsername.equals(username);
            }
        };

        collectionTable = new JTable(tableModel);
        collectionTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(collectionTable);

        add(scrollPane, BorderLayout.CENTER);


        // Create the visualization panel
        visualizationPanel = new VisualizationPanel();
        visualizationPanel.setPreferredSize(new Dimension(200, 600));
        visualizationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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


        // 1) Боковые границы filterPanel должны совпадать с границами таблицы.
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 197));

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
                    String[] data = (String[]) Stream.of(model.getDataVector().elementAt(row).toArray()).map(Object::toString).toArray(String[]::new);
                    Entity entity = null;
                    if (Objects.equals(getRaceByLocalizedValue(data[3]), "HEMULEN")) {

                        //int id,String name, Devotions devotion, int age, int height, int weight, Genders gender, Races race, String creatorName
                        entity = new Hemulen(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Genders.valueOf(getGenderByLocalizedValue(data[7])), Races.HEMULEN, data[8]);
                    } else
                        entity = new Hattifattener(Integer.parseInt(data[0]), data[1], Devotions.valueOf(getDevotionByLocalizedValue(data[2])), Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Genders.valueOf(getGenderByLocalizedValue(data[7])), Races.Hattifattner, data[8]);
                    Sender.sendToServer("update_id", String.valueOf(entity.getId()), entity);
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

    public List<String> getTranslatedEnumValues(String field) {
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
        // 3) При нажатии на кнопку "Меню команд" должно открываться окно в котором 9 кнопок под команды.
        JDialog commandDialog = new JDialog(this, bundle.getString("command_menu"), true);
        commandDialog.setSize(400, 300);
        commandDialog.setLayout(new GridLayout(3, 3));

        for (int i = 1; i <= 9; i++) {
            JButton commandButton = new JButton(bundle.getString("command") + " " + i);
            commandButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle command button action here
                }
            });
            commandDialog.add(commandButton);
        }

        commandDialog.setVisible(true);
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
        ((VisualizationPanel) visualizationPanel).updateObjects(model.getDataVector());

    }

    // 4) Реализовать область визуализации объектов коллекции.
    private class VisualizationPanel extends JPanel {
        private List<Vector> objects;

        public VisualizationPanel() {
            this.objects = new ArrayList<>();
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (Vector object : objects) {
                        int id = Integer.parseInt(object.get(0).toString());
                        int x = calculateX(id, Float.parseFloat((String) object.get(5))/Float.parseFloat((String) object.get(6)));
                        int y = calculateY(id, Float.parseFloat((String) object.get(6))/Float.parseFloat((String) object.get(4)));
                        int radius = calculateRadius(Integer.parseInt((String) object.get(5)));

                        if (e.getX() >= x - radius && e.getX() <= x + radius && e.getY() >= y - radius && e.getY() <= y + radius) {
                            // Display object information
                            JOptionPane.showMessageDialog(null, object.toString(), bundle.getString("object_information"), JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            });
        }
        public int calculateRadius(int x){
            return Math.max((int) ((Math.atan(x)) * 10), 2);
        }

        public void updateObjects(Vector<Vector> dataVector) {
            this.objects = dataVector;
            repaint();
        }

        private int calculateX(int id , float k) {
            if(((int) (id*10*k))%180 >10) {
                return ((int) (id * 10 * k)) % 180;
            }
            return id%4*10 + 10;


        }

        private int calculateY(int id,float k) {

            if (((int) (id*10*k)%530) > 10) {
                return ((int) (id * 10 * k)) % 530;
            }
            return id%7*15 + 10;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Vector object : objects) {
                int id = Integer.parseInt(object.get(0).toString());
                int x = calculateX(id, Float.parseFloat((String) object.get(5))/Float.parseFloat((String) object.get(6)));
                int y = calculateY(id, Float.parseFloat((String) object.get(6))/Float.parseFloat((String) object.get(4)));
                int radius = calculateRadius(Integer.parseInt((String) object.get(5)));


                // 3) Цвет объектов в области визуализации должен вычисляться по какой-нибудь формуле которая однозначно выдаёт цвет на основе имени пользователя.
                String user = object.get(8).toString();
                g.setColor(getUserColor(user));
                g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
            }
        }

        private Color getUserColor(String user) {
            // Пример формулы для вычисления цвета на основе имени пользователя
            int hash = user.hashCode();
            int r = (hash & 0xFF0000) >> 16;
            int g = (hash & 0x00FF00) >> 8;
            int b = hash & 0x0000FF;
            return new Color(r, g, b);
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
