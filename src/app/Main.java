package app;

import java.awt.*;

import javax.swing.*;

import controller.Controller;
import model.IModel;
import model.Item;
import model.Model;
import view.Camembert;

/**
 * Created by guillaume on 16/09/16.
 */
public class Main {

    public static void main(String[] args){
        IModel model = new Model("Le camembert");
        Camembert camembert = new Camembert(model);
        Controller controller = new Controller(model, camembert);


        controller.addItem(new Item("item1","Ceci est l'item 1",50));
        controller.addItem(new Item("item2","Ceci est l'item 2",50));
        controller.addItem(new Item("item3","Ceci est l'item 3",50));
        controller.addItem(new Item("item3","Ceci est l'item 3",25));
        controller.addItem(new Item("item3","Ceci est l'item 3",30));
        controller.addItem(new Item("item3","Ceci est l'item 3",35));



        //1. Create the frame.
        JFrame frame = new JFrame("Camembert interactif");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(camembert, BorderLayout.CENTER);
        frame.addMouseListener(camembert);

//4. Size the frame.
        frame.setMinimumSize(new Dimension(800,550));
        frame.pack();

//5. Show it.
        frame.setVisible(true);

    }
}
