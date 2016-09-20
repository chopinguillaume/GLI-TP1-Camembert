package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import model.IModel;
import model.Item;
import view.IView;

/**
 * Created by guillaume on 16/09/16.
 */
public class Controller implements IModel {

    private IModel model;
    private List<IView> views = new ArrayList<>();

    public Controller(IModel model) {
        this.model = model;
    }

    public void addView(IView view){
        views.add(view);
        view.setController(this);
    }

    @Override
    public String getName() {
        return model.getName();
    }

    @Override
    public void setName(String name) {
        model.setName(name);
        notifyChangeAllViews();
    }

    @Override
    public List<Item> getItems() {
        return model.getItems();
    }

    @Override
    public void setItems(List<Item> items) {
        model.setItems(items);
        notifyChangeAllViews();
    }

    @Override
    public void addItem(Item item) {
        model.addItem(item);
        notifyChangeAllViews();
    }

    @Override
    public void removeItem(int index) {
        model.removeItem(index);
        notifyChangeAllViews();
    }

    @Override
    public int getTotalAmount() {
        return model.getTotalAmount();
    }

    public void onClickItem(int index, IView source){
        for(IView view : views){
            if(!view.equals(source)) view.notifyItemSelected(index);
        }
    }

    private void notifyChangeAllViews(){
        for(IView view : views){
            view.notifyChange();
        }
    }

    public void onChangeItem(Object o, int row, int col) {
        if(row >= 0 || row < model.getItems().size()){
            Item item = model.getItems().get(row);
            switch (col) {
                case 0:
                    item.setName(String.valueOf(o));
                    break;
                case 1:
                    item.setAmount(Integer.parseInt(String.valueOf(o)));
                    break;
                case 2:
                    item.setDescription(String.valueOf(o));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }else{
            throw new IllegalArgumentException();
        }
        notifyChangeAllViews();
    }
}
