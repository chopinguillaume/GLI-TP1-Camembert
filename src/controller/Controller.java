package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import model.IModel;
import model.Item;
import view.IView;

/**
 * Created by guillaume on 16/09/16.
 */
public class Controller implements IModel {

    private IModel model;
    private IView view;

    public Controller(IModel model, IView view) {
        this.model = model;
        this.view = view;
        view.setController(this);
    }

    @Override
    public String getName() {
        return model.getName();
    }

    @Override
    public void setName(String name) {
        model.setName(name);
        view.notifyChange();
    }

    @Override
    public List<Item> getItems() {
        return model.getItems();
    }

    @Override
    public void setItems(List<Item> items) {
        model.setItems(items);
        view.notifyChange();
    }

    @Override
    public void addItem(Item item) {
        model.addItem(item);
        view.notifyChange();
    }

    @Override
    public void removeItem(Item item) {
        model.removeItem(item);
        view.notifyChange();
    }

    @Override
    public int getTotalAmount() {
        return model.getTotalAmount();
    }

    public void onClickItem(int index){
        Item selectedItem = model.getItems().get(index);
        view.notifyItemSelected(selectedItem.getName(), selectedItem.getDescription(), selectedItem.getAmount());
    }
}
