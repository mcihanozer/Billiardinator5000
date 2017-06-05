
import java.awt.Color;
import java.awt.geom.Point2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Cihan
 */
public class BilliardBall
{
    // ARADAKİ UZAKLIK <= 1.TOP.RADIUS + 2.TOP.RADIUS => ÇARPIŞIR

        private double G = 9.8;
        private double BALL_TIME = 1;
    
	private Point2D.Double ballPosition;
	private Point2D.Double ballVelocity;
        private Point2D.Double ballAcc;
	
	private double mass;
	private double radius;
	private Color ballColor;
        
        private boolean collided;
        
        private double fs;
	
	public BilliardBall( double posX, double posY, double velX, double velY, double m, double r, String color, double frictionFactor )
	{
		ballPosition = new Point2D.Double(posX, posY);
                ballVelocity = new Point2D.Double(velX, velY );
                ballAcc = new Point2D.Double();
                mass = m;
                radius = r;
                ballColor = new Color( 0 );
                ballColor = Color.decode( color );
                collided = false;
                fs = frictionFactor;
	}
	
	public void setBallPosition( double x, double y )
	{
		ballPosition.setLocation(x, y);
	}
	public Point2D getBallPosition()
	{
		return ballPosition;
	}
	
	public void setBallVelocity( double x, double y )
	{
		 ballVelocity.setLocation(x, y );
	}
	public Point2D getBallVelocity()
	{
		return ballVelocity;
	}
        public double getVelocityX()
        {
            return ballVelocity.getX();
        }
	public void setVelocityX( double x )
        {
            ballVelocity.setLocation(x, ballVelocity.getY() );
        }
        public double getVelocityY()
        {
            return ballVelocity.getY();
        }
	public void setVelocityY( double y )
        {
            ballVelocity.setLocation( ballVelocity.getX(), y );
        }
	
	public double getMass()
	{
		return mass;
	}
	
	public double getRadius()
	{
		return radius;
	}

	public Color getColour()
	{
		return ballColor;
	}
	
	private void calculateAcc()
	{
            double ax, ay;
            
            if( getSpeed() == 0 )
            {
                ax = 0;
                ay = 0;
            }
            else
            {
                ax = ( ( fs*G*ballVelocity.getX() ) / getSpeed() )*-1;
                ay = ( ( fs*G*ballVelocity.getY() ) / getSpeed() )*-1;
            }
            
            ballAcc.setLocation(ax, ay);
		// ÖNCE İVME HESAPLANIR
	}
	private void calculateVelocity()
	{
            // 2. HIZI HESAPLA
            // HIZI SETLE

            double vx = ballVelocity.getX() + ballAcc.getX()*BALL_TIME;
            double vy = ballVelocity.getY() + ballAcc.getY()*BALL_TIME;
            
            if( ( ballVelocity.getX() > 0 && vx < 0 ) || ( ballVelocity.getX() < 0 && vx > 0 ) )
            {
                vx = 0;
            }
            
            if( ( ballVelocity.getY() > 0 && vy < 0 ) || ( ballVelocity.getY() < 0 && vy > 0 ) )
            {
                vy = 0;
            }

            setBallVelocity( vx, vy);
	}
	public void calculatePosition()
	{
            // SON OLARAK POSITION'I HESAPLA

            double xx = ballPosition.getX() + ballVelocity.getX()*BALL_TIME + ( ballAcc.getX()*BALL_TIME*BALL_TIME )/2;
            double xy = ballPosition.getY() + ballVelocity.getY()*BALL_TIME + ( ballAcc.getY()*BALL_TIME*BALL_TIME )/2;
                    
            setBallPosition( xx, xy );
	}
        public void makeMove()
        {
            calculateAcc();
            calculateVelocity();
           
            calculatePosition();  
        }
	
        public double getSpeed()
        {
            return Math.sqrt( ( Math.pow(ballVelocity.getX(), 2)+ Math.pow( ballVelocity.getY(),2) ) );
        }
        
        public double getDistanceToBall( Point2D secondBall )
        {
            return ballPosition.distance(secondBall);
        }
        public double getDistanceToHole( double x, double y )
        {
            return ballPosition.distance(x, y);
        }
        
        public void makeItCollided()
        {
            collided = true;
        }
        public void rewindCollison()
        {
            collided = false;
        }
        public boolean isCollided()
        {
            return collided;
        }
       
}
