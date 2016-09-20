package view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

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

    private final Color[] colors = {Color.red, Color.green, Color.yellow, Color.cyan, Color.lightGray, Color.magenta, Color.orange, Color.pink};

    private java.util.List<Arc2D> arcs = new ArrayList<>();
    private int arcIndex = -1; // -1 when not selected, index if >= 0

    private Ellipse2D.Double circleout;
    private Polygon leftArrow;
    private Polygon rightArrow;
    private Graphics2D g2d;

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

        Rectangle rectangleOut = new Rectangle(0,0,500,500);
        g2d.setColor(Color.white);
        g2d.fill(rectangleOut);

        Rectangle rectangle = new Rectangle(50, 50, 400, 400);

        int totalAmount = model.getTotalAmount();
        double startingAngle = 0.;

        arcs = new ArrayList<>();
        for (int ind = model.getItems().size()-1; ind >= 0; ind--) {
            g2d.setColor(colors[ind % colors.length]);

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
            startingAngle += 360.*percentage;
            arcs.add(0,arc);
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
        g2d.setColor(Color.blue);
        g2d.fill(circlein);

        drawCenteredTitle(g2d,model.getName(), model.getTotalAmount(), rectangle);

        drawSelectedArcInfo(g2d);

        int[]xs = {505,535,535};
        int[]ys = {215,200,230};
        leftArrow = new Polygon(xs, ys, 3);

        int[]xs2 = {575,545,545};
        int[]ys2 = {215,200,230};
        rightArrow = new Polygon(xs2, ys2, 3);

        drawSelectionArrows(g2d);
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

            g.setColor(Color.blue);
            g.drawRect(500,0,200,200);
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