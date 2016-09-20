package app;

import java.awt.*;
import java.awt.event.MouseListener;

import javax.swing.*;

import controller.Controller;
import model.IModel;
import model.Item;
import model.Model;
import model.TableAdapter;
import view.ButtonPanel;
import view.Camembert;
import view.IView;
import view.JTableView;

/**
 * Created by guillaume on 16/09/16.
 */
public class Main {

    public static void main(String[] args){
        IModel model = new Model("Le camembert");
        IView camembert = new Camembert(model);

        TableAdapter modelAdapter = new TableAdapter(model);
        IView jtable = new JTableView(modelAdapter);
        Controller controller = new Controller(model);
        JPanel buttons = new ButtonPanel();

        JScrollPane scrollTable = new JScrollPane((Component) jtable);

        controller.addView(camembert);
        controller.addView(jtable);
        controller.addView((IView) buttons);


        JPanel right_pane = new JPanel();
        right_pane.setLayout(new BoxLayout(right_pane, BoxLayout.PAGE_AXIS));
        right_pane.add(buttons);
        right_pane.add(Box.createRigidArea(new Dimension(0, 10)));
        right_pane.add(scrollTable);


        //Initialisation des données de test
        for (int i = 0; i < 10; i++) {
            controller.addItem(new Item("item "+i,"Ceci est l'item "+i, (int) (Math.random()*50+1)));
        }

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                (Component) camembert, right_pane);


        //1. Create the frame.
        JFrame frame = new JFrame("Camembert interactif");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);
        splitPane.addMouseListener((MouseListener) camembert);
        splitPane.setDividerLocation(700);

//4. Size the frame.
        frame.setMinimumSize(new Dimension(1100,550));
        frame.pack();

//5. Show it.
        frame.setVisible(true);

    }
}
