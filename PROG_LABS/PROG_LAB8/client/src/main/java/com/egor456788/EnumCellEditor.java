package com.egor456788;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnumCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JComboBox<String> comboBox;

    public EnumCellEditor(List<String> enumValues) {
        comboBox = new JComboBox<>(enumValues.toArray(new String[0]));
    }

    public void setComboBox(List<String> enumValues) {
        comboBox = new JComboBox<>(enumValues.toArray(new String[0]));
    }

    @Override
    public Object getCellEditorValue() {

        return comboBox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        comboBox.setSelectedItem(value);
        return comboBox;
    }
}