package model;

import javax.swing.table.AbstractTableModel;

import controller.Controller;

/**
 * Created by guillaume on 19/09/16.
 */
public class TableAdapter extends AbstractTableModel {

    private final String[] columnNames = {"Name","Amount","Description"};
    private IModel model;

    public TableAdapter(IModel model) {
        this.model = model;
    }

    @Override
    public String getColumnName(int i) {
        return columnNames[i];
    }

    @Override
    public int getRowCount() {
        return model.getItems().size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Item item = model.getItems().get(i);
        switch (i1){
            case 0:
                return item.getName();
            case 1:
                return item.getAmount();
            case 2:
                return item.getDescription();
            default:
                throw new IllegalArgumentException();
        }
    }
}
