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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
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
                bundle.getString("ent_id"), bundle.getString("name"), bundle.getString("devotion"),
                bundle.getString("race"), bundle.getString("age"), bundle.getString("height"),
                bundle.getString("weight"), bundle.getString("gender"), bundle.getString("creator")
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

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        collectionTable.setRowSorter(sorter);
        sorter.setComparator(0, new NumericStringComparator());
        sorter.setComparator(4, new NumericStringComparator());
        sorter.setComparator(5, new NumericStringComparator());
        sorter.setComparator(6, new NumericStringComparator());


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
                    for (int i = 4; i <= 6; i++) {
                        if (data[i].isEmpty()) {
                            data[i] = "1";
                        }
                    }
                    if (Objects.equals(getRaceByLocalizedValue(data[3]), "HEMULEN")) {


                        entity = new Hemulen(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Genders.valueOf(getGenderByLocalizedValue(data[7])), Races.HEMULEN, data[8]);
                    } else
                        entity = new Hattifattener(Integer.parseInt(data[0]), data[1], Devotions.valueOf(getDevotionByLocalizedValue(data[2])), Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Genders.valueOf(getGenderByLocalizedValue(data[7])), Races.HATTIFATTNER, data[8]);
                    Sender.sendToServer("update_id", String.valueOf(entity.getId()), entity);
                    loadData();
                    visualizationPanel.repaint();

                }
            }
        });
        collectionTable.getColumnModel().getColumn(2).setCellEditor(new EnumCellEditor(getTranslatedEnumValues("devotion")));
        collectionTable.getColumnModel().getColumn(3).setCellEditor(new EnumCellEditor(getTranslatedEnumValues("race")));
        collectionTable.getColumnModel().getColumn(7).setCellEditor(new EnumCellEditor(getTranslatedEnumValues("gender")));

        collectionTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(createPositiveIntegerField()));
        collectionTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(createPositiveIntegerField()));
        collectionTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(createPositiveIntegerField()));


    }

    class NumericStringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            if (s1 == null || s1.isEmpty()) return -1;
            if (s2 == null || s2.isEmpty()) return 1;
            try {
                int num1 = Integer.parseInt(s1);
                int num2 = Integer.parseInt(s2);
                return Integer.compare(num1, num2);
            } catch (NumberFormatException e) {
                return s1.compareTo(s2);
            }
        }
    }


    private JTextField createPositiveIntegerField() {
        JTextField textField = new JTextField();
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new PositiveIntegerFilterWithDefault(textField));
        return textField;
    }


    // Метод filterTable для фильтрации по всем столбцам
    private void filterTable() {
        DefaultTableModel model = (DefaultTableModel) collectionTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) collectionTable.getRowSorter();
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
        JDialog commandMenuDialog = new JDialog(this, bundle.getString("command_menu"), true);
        commandMenuDialog.setLocationRelativeTo(this);
        commandMenuDialog.setLayout(new GridLayout(3, 3));
        commandMenuDialog.setSize(400, 300);


        JButton addButton = new JButton(bundle.getString("add"));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreateObjectDialog(false);
            }
        });
        commandMenuDialog.add(addButton);

        JButton addIfMinButton = new JButton(bundle.getString("add_if_min"));
        addIfMinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreateObjectDialog(true);
            }
        });
        commandMenuDialog.add(addIfMinButton);

        JButton clearButton = new JButton(bundle.getString("clear"));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String response = Sender.sendToServer("clear", null, null, username);
                loadData();
                JOptionPane.showMessageDialog(clearButton, "collection_cleared");
            }
        });
        commandMenuDialog.add(clearButton);

        JButton removeByIdButton = new JButton(bundle.getString("remove_by_id"));
        removeByIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                showRemoveByIdDialog();
            }
        });
        commandMenuDialog.add(removeByIdButton);

        JButton removeLowerButton = new JButton(bundle.getString("remove_lower"));
        removeLowerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                showRemoveLowerDialog();
            }
        });
        commandMenuDialog.add(removeLowerButton);


        commandMenuDialog.setVisible(true);
    }

    private void showRemoveByIdDialog() {
        JDialog removeByIdObjectDialog = new JDialog(this, bundle.getString("remove_by_id"), true);
        removeByIdObjectDialog.setLocationRelativeTo(this);
        removeByIdObjectDialog.setLayout(new GridLayout(2, 1));
        removeByIdObjectDialog.setSize(100, 100);

        JTextField idField = new JTextField();
        ((AbstractDocument) idField.getDocument()).setDocumentFilter(new PositiveIntegerFilterWithDefault(idField));


        JButton createButton = new JButton(bundle.getString("remove"));

        removeByIdObjectDialog.add(idField);
        removeByIdObjectDialog.add(createButton);



        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String response = Sender.sendToServer("remove_by_id", idField.getText(), null, username);
                loadData();
                System.out.println(response);
                if (response.contains("Элемент не найден")) {
                    JOptionPane.showMessageDialog(removeByIdObjectDialog, bundle.getString("element_not_found"));
                } else {
                    if (response.contains("Отказано в доступе")) {
                        JOptionPane.showMessageDialog(removeByIdObjectDialog, bundle.getString("access_denied"));
                    }
                    else {
                        loadData();
                        JOptionPane.showMessageDialog(removeByIdObjectDialog, bundle.getString("element_removed"));
                        removeByIdObjectDialog.dispose();
                    }
                }

            }
        });

        removeByIdObjectDialog.setVisible(true);
    }

    private void showRemoveLowerDialog() {
        JDialog removeLowerDialog = new JDialog(this, bundle.getString("remove_lower"), true);
        removeLowerDialog.setLocationRelativeTo(this);
        removeLowerDialog.setLayout(new GridLayout(2, 1));
        removeLowerDialog.setSize(100, 100);

        JTextField ageField = new JTextField();
        ((AbstractDocument) ageField.getDocument()).setDocumentFilter(new PositiveIntegerFilterWithDefault(ageField));


        JButton createButton = new JButton(bundle.getString("remove"));
        removeLowerDialog.add(ageField);
        removeLowerDialog.add(createButton);


        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String response = Sender.sendToServer("remove_lower", ageField.getText(), null, username);
                loadData();

                    loadData();
                    JOptionPane.showMessageDialog(removeLowerDialog, "elements_removed");

            }
        });

        removeLowerDialog.setVisible(true);
    }



    private void showCreateObjectDialog(boolean hasCondition) {
        JDialog createObjectDialog = new JDialog(this, bundle.getString("create_object"), true);
        createObjectDialog.setLocationRelativeTo(this);
        createObjectDialog.setLayout(new GridLayout(8, 2));
        createObjectDialog.setSize(400, 300);

        JTextField nameField = new JTextField();
        JComboBox<String> devotionComboBox = new JComboBox<>(getTranslatedEnumValues("devotion").toArray(new String[0]));
        JTextField ageField = new JTextField();
        JTextField heightField = new JTextField();
        JTextField weightField = new JTextField();
        JComboBox<String> genderComboBox = new JComboBox<>(getTranslatedEnumValues("gender").toArray(new String[0]));
        JComboBox<String> raceComboBox = new JComboBox<>(getTranslatedEnumValues("race").toArray(new String[0]));
        JTextField creatorNameField = new JTextField();

        createObjectDialog.add(new JLabel(bundle.getString("name")));
        createObjectDialog.add(nameField);
        createObjectDialog.add(new JLabel(bundle.getString("devotion")));
        createObjectDialog.add(devotionComboBox);
        createObjectDialog.add(new JLabel(bundle.getString("age")));
        createObjectDialog.add(ageField);
        createObjectDialog.add(new JLabel(bundle.getString("height")));
        createObjectDialog.add(heightField);
        createObjectDialog.add(new JLabel(bundle.getString("weight")));
        createObjectDialog.add(weightField);
        createObjectDialog.add(new JLabel(bundle.getString("gender")));
        createObjectDialog.add(genderComboBox);
        createObjectDialog.add(new JLabel(bundle.getString("race")));
        createObjectDialog.add(raceComboBox);


        JButton createButton = new JButton(bundle.getString("create"));
        createObjectDialog.add(new JLabel());
        createObjectDialog.add(createButton);

        // Set positive integer filters
        ((AbstractDocument) ageField.getDocument()).setDocumentFilter(new PositiveIntegerFilterWithDefault(ageField));
        ((AbstractDocument) heightField.getDocument()).setDocumentFilter(new PositiveIntegerFilterWithDefault(heightField));
        ((AbstractDocument) weightField.getDocument()).setDocumentFilter(new PositiveIntegerFilterWithDefault(weightField));

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Object created with data:");
                System.out.println("Name: " + nameField.getText());
                System.out.println("Devotion: " + devotionComboBox.getSelectedItem());
                System.out.println("Age: " + ageField.getText());
                System.out.println("Height: " + heightField.getText());
                System.out.println("Weight: " + weightField.getText());
                System.out.println("Gender: " + genderComboBox.getSelectedItem());
                System.out.println("Race: " + raceComboBox.getSelectedItem());
                boolean isShowed = false;

                String response = "";
                try {
                    Entity entity;
                    if (Races.valueOf(getRaceByLocalizedValue((String) raceComboBox.getSelectedItem())) == Races.HATTIFATTNER) {
                        entity = new Hattifattener(nameField.getText(), Devotions.valueOf(getDevotionByLocalizedValue((String) devotionComboBox.getSelectedItem())), Integer.parseInt(ageField.getText()), Integer.parseInt(heightField.getText()), Integer.parseInt(weightField.getText()), Genders.valueOf(getGenderByLocalizedValue((String) genderComboBox.getSelectedItem())), Races.valueOf(getRaceByLocalizedValue((String) raceComboBox.getSelectedItem())), username);
                    } else {
                        entity = new Hemulen(nameField.getText(), Devotions.valueOf(getDevotionByLocalizedValue((String) devotionComboBox.getSelectedItem())), Integer.parseInt(ageField.getText()), Integer.parseInt(heightField.getText()), Integer.parseInt(weightField.getText()), Genders.valueOf(getGenderByLocalizedValue((String) genderComboBox.getSelectedItem())), Races.valueOf(getRaceByLocalizedValue((String) raceComboBox.getSelectedItem())), username);
                    }
                    if (hasCondition) {
                        response = Sender.sendToServer("add_if_min", null, entity, username);
                    } else {
                        Sender.sendToServer("add", null, entity, username);
                    }
                    loadData();
                    visualizationPanel.repaint();
                } catch (RuntimeException ex) {
                    System.out.println(ex);
                    JOptionPane.showMessageDialog(createObjectDialog, bundle.getString("incorrect_input"));
                    isShowed = true;
                }
                System.out.println(response);
                if (hasCondition && response.contains("Возраст больше минимального. Элемент не будет добавлен")) {
                    JOptionPane.showMessageDialog(createObjectDialog, bundle.getString("object_not_min"));
                    createObjectDialog.dispose();
                    isShowed = true;
                }
                if (!isShowed) {
                    JOptionPane.showMessageDialog(createObjectDialog, bundle.getString("object_created"));
                    createObjectDialog.dispose();
                }
            }
        });

        createObjectDialog.setVisible(true);
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

    public class PositiveIntegerFilterWithDefault extends DocumentFilter {
        private JTextField textField;

        public PositiveIntegerFilterWithDefault(JTextField textField) {
            this.textField = textField;
            textField.setInputVerifier(new PositiveIntegerInputVerifier(textField));
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (isValidInput(string)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (isValidInput(text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean isValidInput(String text) {
            return text.matches("\\d*") && (!text.isEmpty());
        }

        class PositiveIntegerInputVerifier extends InputVerifier {
            private JTextField textField;

            public PositiveIntegerInputVerifier(JTextField textField) {
                this.textField = textField;
            }

            @Override
            public boolean verify(JComponent input) {
                String text = textField.getText();
                if (text.isEmpty()) {
                    textField.setText("1");
                }
                return true;
            }
        }
    }


    private class VisualizationPanel extends JPanel {
        private List<Vector> objects;

        public VisualizationPanel() {
            this.objects = new ArrayList<>();
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (Vector object : objects) {
                        int id = Integer.parseInt(object.get(0).toString());
                        int x = calculateX(id, Float.parseFloat((String) object.get(5)) / Float.parseFloat((String) object.get(6)));
                        int y = calculateY(id, Float.parseFloat((String) object.get(6)) / Float.parseFloat((String) object.get(4)));
                        int radius = calculateRadius(Integer.parseInt((String) object.get(5)));

                        if (e.getX() >= x - radius && e.getX() <= x + radius && e.getY() >= y - radius && e.getY() <= y + radius) {
                            // Display object information
                            JOptionPane.showMessageDialog(null, object.toString(), bundle.getString("object_information"), JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            });
        }

        public int calculateRadius(int x) {
            return Math.max((int) ((Math.atan(x)) * 10), 2);
        }

        public void updateObjects(Vector<Vector> dataVector) {
            this.objects = dataVector;
            repaint();
        }


        private int calculateX(int id, float k) {
            if (((int) (id * 10 * k)) % 180 > 10) {
                return ((int) (id * 10 * k)) % 180;
            }
            return id % 4 * 10 + 10;


        }

        private int calculateY(int id, float k) {

            if (((int) (id * 10 * k) % 530) > 10) {
                return ((int) (id * 10 * k)) % 530;
            }
            return id % 7 * 15 + 10;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Vector object : objects) {
                int id = Integer.parseInt(object.get(0).toString());
                int x = calculateX(id, Float.parseFloat((String) object.get(5)) / Float.parseFloat((String) object.get(6)));
                int y = calculateY(id, Float.parseFloat((String) object.get(6)) / Float.parseFloat((String) object.get(4)));
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
