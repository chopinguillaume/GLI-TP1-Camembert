package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import controller.Controller;
import model.Item;

/**
 * Created by guillaume on 20/09/16.
 */
public class ButtonPanel extends JPanel implements IView{

    private Controller controller;
    private Button addButton;
    private Button removeButton;

    private int selectedIndex;

    public ButtonPanel() {

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(300,50));

        addButton = new Button("New item");
        removeButton = new Button("Remove item");
        add(addButton);
        add(removeButton);

        addButton.addActionListener(actionEvent -> controller.addItem(new Item("new item","no description",1)));

        removeButton.addActionListener(actionEvent -> {
            if(selectedIndex >= 0) controller.removeItem(selectedIndex);
        });
    }

    @Override
    public void setController(Controller c) {
        this.controller = c;
    }

    @Override
    public void notifyChange() {}

    @Override
    public void notifyItemSelected(int index) {
        selectedIndex = index;
    }
}
