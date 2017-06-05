/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Cihan
 */
public class BilliardTable {
    
    private int width;
    private int height;
    private int holeNumber;
    private double friction;
    
    BilliardTable( int w, int h, int holes, double fs )
    {
        width = w;
        height = h;
        holeNumber = holes;
        friction = fs;
    }
    
    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }
    public int getHoleNumber()
    {
        return holeNumber;
    }
    public double getFriction()
    {
        return friction;
    }
    
}
