package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import controller.Controller;
import model.TableAdapter;

/**
 * Created by guillaume on 19/09/16.
 */
public class JTableView extends JTable implements IView {

    private TableAdapter modelAdapter;
    private Controller controller;
    private int selectedRow = -1;

    public JTableView(TableAdapter modelAdapter) {
        super(modelAdapter);
        this.modelAdapter = modelAdapter;

        setSurrendersFocusOnKeystroke(true);

        getSelectionModel().addListSelectionListener(event -> {
            int i = getSelectedRow();
            if(selectedRow != i){
                selectedRow = i;
                controller.onClickItem(i,this);
            }
        });

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment( JLabel.LEFT );
        setDefaultRenderer(Integer.class, leftRenderer);
    }

    @Override
    public Class<?> getColumnClass(int i) {
        if(i==1){
            return Integer.class;
        }else{
            return String.class;
        }
    }

    @Override
    public void setController(Controller c) {
        controller = c;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return true;
    }

    @Override
    public void notifyChange() {
        modelAdapter.fireTableDataChanged();
    }

    @Override
    public void notifyItemSelected(int index) {
        if(selectedRow != index){
            selectedRow = index;
            if(index >= 0) setRowSelectionInterval(index,index);
            if(index == -1) clearSelection();
        }
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {
        controller.onChangeItem(o, i, i1);
    }


}
