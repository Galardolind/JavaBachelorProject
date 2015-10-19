package MailSender.GUI;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

/**
 * this class creates a graphical component to trace time historic lines. this
 * component is strecthable.
 */
public class UserInterfaceGraph extends JComponent {

    /**
     * Contains the length of the historic values.
     */
    private int nbPoints;
    /**
     * Contains Values array to process befroe drawing
     */
    private float[] values;
    /**
     * Contains the shape List (to draw)
     */
    private ArrayList<Shape> shapes;
    private Shape currentShape;
    /**
     * Contains value of the maximum state diplayed.
     */
    private float maxValue;
    /**
     * Contains shapes group colors.
     */
    private Color bg = Color.BLACK;
    private Color grid = Color.WHITE;
    private Color line = new Color(173, 255, 47);
    /**
     * Contains the margin systems.
     */
    private int retrieveBarTop;
    private int retrieveBarBottom;
    private int retrieveBarLeft;
    private int retrieveBarRight;
    /**
     * Contains name of the value traced
     */
    private String name;

    /**
     * Initialize the component with the following parameters. the graph trace
     * nbPoints, with the name name.
     *
     * @param nbPoints Number of points to be traced in the historic.
     * @param top margin-top of the drawing zone
     * @param left margin-left of the drawing zone
     * @param bottom margin-bottom of the drawing zone
     * @param right margin-right of the drawing zone
     * @param name name of the variable beeing traced.
     */
    public UserInterfaceGraph(int nbPoints, int top, int left, int bottom, int right, String name) {

        //init vars
        this.name = name;
        this.retrieveBarTop = top;
        this.retrieveBarLeft = left;
        this.retrieveBarBottom = bottom;
        this.retrieveBarRight = right;
        this.nbPoints = nbPoints;

        //init values to "0" (bottom of the drawing zone)
        values = new float[nbPoints];
        for (int i = 0; i < this.nbPoints; i++) {
            values[i] = 0; //initialise le tableau (valeur de base)
        }
    }

    /**
     * paint the component (background, grid, curve) with parameters given in
     * constructor and the component size (auto resize)
     *
     * @param grphcs
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        //casting graphics2d
        Graphics2D g = (Graphics2D) grphcs;
        //anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //drawing groups
        drawBackground(g);
        drawCurve(g);
        drawGrid(g);
        drawString2(g, 15, 65, -1 * (Math.PI / 2.0), name);
        String maxv = this.maxValue+"";
        if(this.maxValue >= 10){
            maxv = (int)this.maxValue+"";
        } else if(maxv.length() > 4){
            maxv = maxv.substring(0,4);
        }
        drawRect(g, 22, 0 , 45, 17);
        drawString2(g, 35, 15, 0, maxv);
    }
    
    /**
     * Draw a rectangle in canvas at the x,y position and background color.
     * @param g Graphics2D of the component (casted from getGraphics).
     * @param x X start coordonate
     * @param y Y start coordonate
     * @param x2 X end coordonate
     * @param y2 Y end coordonate
     */
    private void drawRect(Graphics2D g,int x, int y, int x2, int y2){
        g.setPaint(bg);
        g.fillRect(x, y, x2, y2);
    }

    /**
     * Drawer : draw the background of the component.
     *
     * @param g Graphics2D of the component (casted from getGraphics).
     */
    private void drawBackground(Graphics2D g) {
        g.setPaint(bg);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Drawer : draw the historic curve of the component.
     *
     * @param g Graphics2D of the component (casted from getGraphics).
     */
    private void drawCurve(Graphics2D g) {
        //calculate resizing an graphical zone.
        float spaceBetween = (float) ((this.getWidth() - (retrieveBarLeft + retrieveBarRight)) / (1.0 * nbPoints));
        float multiplicatorHeight = (float) ((this.getHeight() - (retrieveBarTop + retrieveBarBottom)) / (1.0 * maxValue));

        shapes = new ArrayList<>();
        for (int i = 0; i < values.length - 1; i++) {
            //generating shape array
            Point p1 = new Point((int) (retrieveBarLeft + (i * spaceBetween)), (int) ((getHeight() - retrieveBarBottom) - (values[i] * multiplicatorHeight)));
            Point p2 = new Point((int) (retrieveBarLeft + ((i + 1) * spaceBetween)), (int) ((getHeight() - retrieveBarBottom) - (values[i + 1] * multiplicatorHeight)));
            currentShape = new Line2D.Double(p1, p2);
            shapes.add(currentShape);
        }

        //draw the curve from shape array
        g.setPaint(line);
        g.setStroke(new BasicStroke(1));
        for (Shape shape : shapes) {
            g.draw(shape);
        }
    }

    /**
     * Drawer : draw the grid of the component.
     *
     * @param g Graphics2D of the component (casted from getGraphics).
     */
    private void drawGrid(Graphics2D g) {
        g.setPaint(grid);
        g.setStroke(new BasicStroke(1));
        g.draw(new Line2D.Double(retrieveBarLeft, this.getHeight() - retrieveBarBottom, this.getWidth() - retrieveBarRight, this.getHeight() - retrieveBarBottom)); //abcisses
        g.draw(new Line2D.Double(retrieveBarLeft, this.getHeight() - retrieveBarBottom, retrieveBarLeft, retrieveBarTop));      //ordonnees
    }
    
    /**
     * Drawer : draw the variable label of the component.
     * 
     * @param g Graphics2D of the component (casted from getGraphics).
     * @param x x-cordinates of the label
     * @param y y-cordinates of the label
     * @param theta rotation (radians) of the label
     * @param label Drawn String.
     */
    private void drawString2(Graphics2D g, double x, double y, double theta, String label) {
        g.setPaint(grid);
        AffineTransform fontAT = new AffineTransform();
        Font theFont = g.getFont();
        fontAT.rotate(theta);
        Font theDerivedFont = theFont.deriveFont(fontAT);
        g.setFont(theDerivedFont);
        g.drawString(label, (int) x, (int) y);
        g.setFont(theFont);
    }
    
    /**
     * gives the value of the last entry in historic, 
     * 
     * @return float value.
     */
    public float getHeadValue() {
        return values[values.length - 1];
    }
    
    /**
     * Add entry to the historic (shift the array and destroy the first value of historic)
     * 
     * @param value value of the new entry
     */
    public void addPoint(float value) {
        maxValue = 0;
        for (int i = 1; i < values.length; i++) {
            maxValue = Math.max(values[i], maxValue);
            values[i - 1] = values[i];
        }
        values[values.length - 1] = value;
        maxValue = Math.max(value, maxValue);
        repaint();
    }
    
    /**
     * Set the color of the background
     * 
     * @param bg Color of background
     */
    public void setColorBackgroung(Color bg){
        if(bg == null)return;
        this.bg = bg;
    }
    
    /**
     * Set the color of the Grid
     * 
     * @param grid Color of grid
     */
    public void setColorGrid(Color grid){
        if(grid == null)return;
        this.grid = grid;
    }
    
    /**
     * Set yhe color of the curve
     * 
     * @param curve Color of the curve
     */
    public void setColorCurve(Color curve){
        if(curve == null)return;
        this.line = curve;
    }
}
