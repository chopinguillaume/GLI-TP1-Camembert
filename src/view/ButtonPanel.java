package view;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controller.Controller;
import model.Item;

/**
 * Created by guillaume on 20/09/16.
 */
public class ButtonPanel extends JPanel implements IView{

    private Controller controller;
    private String name = "Default name";

    private int selectedIndex;
    private JTextField textField;

    public ButtonPanel() {

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(300,50));

        Button addButton = new Button("New item");
        Button removeButton = new Button("Remove item");
        textField = new JTextField(name);

        add(addButton);
        add(removeButton);
        add(textField);

        addButton.addActionListener(actionEvent -> controller.addItem(new Item("new item","no description",1)));

        removeButton.addActionListener(actionEvent -> {
            if(selectedIndex >= 0) controller.removeItem(selectedIndex);
        });

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                handleChange();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                handleChange();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                handleChange();
            }

            private void handleChange(){
                controller.setName(textField.getText());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        textField.setText(name);
    }

    @Override
    public void setController(Controller c) {
        this.controller = c;
        name = controller.getName();
        repaint();
    }

    @Override
    public void notifyChange() {}

    @Override
    public void notifyItemSelected(int index) {
        selectedIndex = index;
    }
}
