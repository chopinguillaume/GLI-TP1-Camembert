package view;

import controller.Controller;

/**
 * Created by guillaume on 16/09/16.
 */
public interface IView {

    void setController(Controller c);

    void notifyChange();

    void notifyItemSelected(int index);
}
