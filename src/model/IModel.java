package model;

import java.util.List;

/**
 * Created by guillaume on 16/09/16.
 */
public interface IModel {

    String getName();

    void setName(String name);

    List<Item> getItems();

    void setItems(List<Item> items);

    void addItem(Item item);

    void removeItem(int index);

    int getTotalAmount();
}
