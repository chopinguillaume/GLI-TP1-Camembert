package view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;

import controller.Controller;
import model.IModel;
import model.Item;

/**
 * Created by guillaume on 16/09/16.
 */
public class Camembert extends JComponent implements IView, MouseListener {

    private IModel model;
    private Controller controller;

    private final Color[] colors = {
        new Color(32,64,75),
        new Color(31, 94, 105),
        new Color(6,100,100),
        new Color(108, 157, 153),
        new Color(125, 150,162),
    };

    private java.util.List<Arc2D> arcs = new ArrayList<>();
    private int arcIndex = -1; // -1 when not selected, index if >= 0

    private Ellipse2D.Double circleout;
    private Polygon leftArrow;
    private Polygon rightArrow;
    private Graphics2D g2d;
    private java.util.List<String> itemnames = new ArrayList<>();
    private Rectangle rectangle;
    private Rectangle rectangleOut;

    public Camembert(IModel model) {
        this.model = model;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g2d = (Graphics2D) g;

        rectangleOut = new Rectangle(0,0,500,500);
        g2d.setColor(Color.white);
        g2d.fill(rectangleOut);

        rectangle = new Rectangle(50, 50, 400, 400);

        int totalAmount = model.getTotalAmount();
        double startingAngle = 0.;

        arcs = new ArrayList<>();
        for (int ind = model.getItems().size()-1; ind >= 0; ind--) {
            g2d.setColor(colors[ind % colors.length]);

            if(ind == 0){
                if(model.getItems().size() % colors.length == 1){
                    g2d.setColor(colors[colors.length-1 % colors.length]);
                }
            }

            Item item = model.getItems().get(ind);
            int amount = item.getAmount();
            double percentage = (double)amount / (double)totalAmount;
            Arc2D.Double arc;
            if(ind == arcIndex){
                arc = new Arc2D.Double(rectangleOut,startingAngle,360.*percentage-1,Arc2D.PIE);
            }else{
                arc = new Arc2D.Double(rectangle,startingAngle,360.*percentage-1,Arc2D.PIE);
            }
            g2d.fill(arc);
            arcs.add(0,arc);
            itemnames.add(0,item.getName());

            startingAngle += 360.*percentage;
        }

        circleout = new Ellipse2D.Double(
                rectangle.getX() + rectangle.getWidth()/4-50,
                rectangle.getY() + rectangle.getHeight()/4-50,
                rectangle.getX() + rectangle.getWidth()/2+50,
                rectangle.getY() + rectangle.getHeight()/2+50);
        g2d.setColor(Color.white);
        g2d.fill(circleout);

        Ellipse2D.Double circlein = new Ellipse2D.Double(
                rectangle.getX() + rectangle.getWidth()/4,
                rectangle.getY() + rectangle.getHeight()/4,
                rectangle.getX() + rectangle.getWidth()/2-50,
                rectangle.getY() + rectangle.getHeight()/2-50);
        g2d.setColor(new Color(37, 98, 110));
        g2d.fill(circlein);

        drawCenteredTitle(g2d,model.getName(), model.getTotalAmount(), rectangle);

        drawSelectedArcInfo(g2d);

        int[]xs = {520,505,535};
        int[]ys = {200,230,230};
        leftArrow = new Polygon(xs, ys, 3);

        int[]xs2 = {545,575,560};
        int[]ys2 = {200,200,230};
        rightArrow = new Polygon(xs2, ys2, 3);

        drawSelectionArrows(g2d);

        drawItemNames(g2d);
    }

    private void drawItemNames(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Calibri",Font.BOLD,14));

        for (int i = arcs.size() - 1; i >= 0; i--) {
            Arc2D arc = arcs.get(i);
            String itemname = itemnames.get(i);

            int rayon;
            int centre;

            if(i == arcIndex){
                rayon = (int) rectangleOut.getWidth()/2;
                centre = (int) rectangleOut.getCenterX();
            }else{
                rayon = (int) rectangle.getWidth()/2;
                centre = (int) rectangle.getCenterX();
            }

            double startingAngle = arc.getAngleStart() + arc.getAngleExtent()/2;
            int x = (int) (rayon * Math.cos(Math.toRadians(startingAngle)) + centre);
            int y = (int) (rayon * Math.sin(Math.toRadians(-startingAngle)) + centre);

            g2d.drawString(itemname,x,y);
        }
    }

    private void drawSelectionArrows(Graphics2D g) {
        if(arcIndex >= 0){
            g.setColor(Color.black);
            g.fillPolygon(leftArrow);
            g.fillPolygon(rightArrow);
        }
    }

    private void drawSelectedArcInfo(Graphics g) {
        if(arcIndex >= 0){
            String arcName = model.getItems().get(arcIndex).getName();
            int arcAmount = model.getItems().get(arcIndex).getAmount();
            String arcDescr = model.getItems().get(arcIndex).getDescription();

            g.setColor(Color.black);
            g.drawRect(500,0,180,200);
            g.setColor(Color.black);
            g.drawString(arcName,505,35);
            g.drawString("Amount = "+arcAmount,505,65);
            g.drawString(arcDescr,505,95);
        }
    }

    private void drawCenteredTitle(Graphics g, String title, int totalAmount, Rectangle rect) {
        Font font = new Font("Arial", Font.BOLD, 14);
        FontMetrics metrics = g.getFontMetrics(font);

        int x = 50+ (rect.width - metrics.stringWidth(title)) / 2;
        int y = 50+ ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);
        g.setColor(Color.white);

        g.drawString(title, x, y);

        x = 50+ (rect.width - metrics.stringWidth("Total = "+totalAmount)) / 2;
        g.drawString("Total = "+totalAmount,x,y+30);
    }

    @Override
    public void notifyChange() {
        repaint();
    }

    @Override
    public void notifyItemSelected(int index) {
        arcIndex = index;
        notifyChange();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        boolean selectArc = false;

        if(arcIndex >=0){
            if(leftArrow.contains(e.getX(),e.getY())){
                arcIndex = (arcIndex - 1 + arcs.size()) % arcs.size();
                selectArc=true;
            }else if(rightArrow.contains(e.getX(), e.getY())){
                arcIndex = (arcIndex + 1 + arcs.size()) % arcs.size();
                selectArc=true;
            }
        }

        for (int i=0; i<arcs.size(); i++){
            Arc2D arc = arcs.get(i);
            if(arc.contains(e.getX(),e.getY())
                    && !circleout.contains(e.getX(),e.getY())){
                arcIndex = i;
                selectArc=true;
            }
        }

        if(!selectArc){
            arcIndex = -1;
        }else{
            controller.onClickItem(arcIndex, this);
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}