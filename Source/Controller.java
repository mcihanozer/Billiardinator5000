
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

/**
*
* @author Cihan
*/

public class Controller extends JPanel{
    
    private int MAX_LIMIT = 20;
    private double ERROR_RATIO = 1;
    
    
    private int width;
    private int height;
    private int ballSize;
    private int holeNumber;
    private double holeRadius;
    private int[][] holes;
    private double[][]bands;
    private BilliardBall[] copyBalls;
    
    private int bounceCounter;
    private int holeBalls;

    JFrame frame; 
    
    public Controller( BilliardBall[] balls, BilliardTable table )
    {
        width = table.getWidth();
        height = table.getHeight();
        ballSize = balls.length;
        holeNumber = table.getHoleNumber();
        holes = new int[ holeNumber ][ holeNumber ];
        holeRadius = 13.0;
        initializeHoles( );
        copyBalls = balls;
        bands = new double[8][8];
        initializeBorders();
        
        bounceCounter = 0;
        holeBalls = 0;

    }
    
    private void constructWindow()
    {
        frame = new JFrame("Pool Table");
        frame.setSize(width+100, height+100);
        //frame.setLocation(300, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this, BorderLayout.CENTER);
		
        frame.setVisible(true);
    }
    
    private void initializeBorders()
    {
        // 1 start
        
        bands[0][0] = 0;
        bands[0][1] = holeRadius;
        
        //1 end
        
        bands[1][0] = 0;
        bands[1][1] = width - holeRadius;
        
        //2 start
        
        bands[2][0] = holeRadius;
        bands[2][1] = 0;
        
        //2 end
        
        bands[3][0] = height - holeRadius;
        bands[3][1] = 0;
        
        //3 start
        
        bands[4][0] = height;
        bands[4][1] = holeRadius;
        
        //3 end
        
        bands[5][0] = height;
        bands[5][1] = width - holeRadius;
        
        //4 start
        
        bands[6][0] = holeRadius;
        bands[6][1] = width;
        
        
        //4 end
        
        bands[7][0] = height - 1;
        bands[7][1] = width;
    }
    
    private void initializeHoles( )
    {
        if( holeNumber == 4 )
        {
        // 4'LÜK DELİK SAYILARINI ATA

            // holes[x][y]

            holes[0][0] = 0;
            holes[0][1] = 0;

            holes[1][0] = width;
            holes[1][1] = 0;

            holes[2][0] = 0;
            holes[2][1] = height;

            holes[3][0] = width;
            holes[3][1] = height;

        }
        else
        {
        // 6'LIK DELİK SAYILARINI ATA

            // holes[x][y]

            holes[0][0] = 0;
            holes[0][1] = 0;

            holes[1][0] = width/2;
            holes[1][1] = 0;

            holes[2][0] = width;
            holes[2][1] = 0;

            holes[3][0] = 0;
            holes[3][1] = height;

            holes[4][0] = width/2;
            holes[4][1] = height;

            holes[5][0] = width;
            holes[5][1] = height;
        }
    }
    
    public void startAnimation( )
    {
        constructWindow();
        
        int endCounter = 0;
        
        
        int i;
        int j;
        
        int copyBallSize = ballSize;

        while( true )
        {
            for( i = 0; i < copyBallSize; i++ )
            {

                if( copyBalls[i] == null || copyBalls[i].getSpeed() == 0.0 )
                {
                   endCounter++;
                   continue;
                }

                if( checkHoles( i ) )
                {
                    continue;
                }
                
                checkBandCollision( copyBalls[i], i );
                

                for( j = 0; j < copyBallSize; j++ )
                {
                    if( i == j || copyBalls[i] == null || copyBalls[j] == null )
                    {
                        continue;
                    }

                    if( copyBallSize > 1 )
                    {
                        if(  isCollide( copyBalls[i], copyBalls[j] )  )
                        {
                            copyBalls[i].makeItCollided();
                            copyBalls[j].makeItCollided();
                            collide( copyBalls[i], copyBalls[j] );
                            copyBalls[i].makeMove();
                            copyBalls[j].makeMove();
                        }
                    }

                } // inner loop

                 
                if( copyBalls[i] != null )
                {

                     if( !copyBalls[i].isCollided() )
                     {
                         copyBalls[i].makeMove();
                     }
                     else
                     {
                         copyBalls[i].rewindCollison();
                     }
                }
                 
            } // outter loop

            if( endCounter == ballSize )
            {
               JOptionPane.showMessageDialog(frame, "Simulation is ended!" );
               JOptionPane.showMessageDialog(frame, "Bounce: "+bounceCounter  );
               JOptionPane.showMessageDialog(frame, "Balls in the holes: "+holeBalls );
               break;
            }
            else
            {
                endCounter = 0;
            }
            
            this.repaint();
            try
            {
                Thread.sleep(30);
                rewinder();
            } catch (InterruptedException e)
            {
                System.exit(1);
            }
            
        } // while
        
    } // startAnimation()
    
    private void rewinder()
    {
        for(int i=0; i<copyBalls.length; i++)
        {
            if( copyBalls[i] == null )
            {
                continue;
            }
            copyBalls[i].rewindCollison();
        }
    }

    private boolean isCollide( BilliardBall ball1, BilliardBall ball2 )
    {

        double d = ball1.getDistanceToBall( ball2.getBallPosition() );
        
        
        if( d <= ( ball1.getRadius() + ball2.getRadius() ) )
        {
            double diff = d- ball1.getRadius() - ball2.getRadius();
            if( d < 0 )
            {
                ball1.setBallPosition( ball1.getBallPosition().getX() + ( ( diff*ball1.getVelocityX() ) / 20 ) , ball1.getBallPosition().getY() + ( ( diff*ball1.getVelocityY() ) / 20 ) );
            }
            return true;
        }

        return false;
    }

    private void collide( BilliardBall ball1, BilliardBall ball2 )
    {
       double x1 = ball1.getBallPosition().getX();
       double y1 = ball1.getBallPosition().getY();
       
       double x2 = ball2.getBallPosition().getX();
       double y2 = ball2.getBallPosition().getY();
       
       double alpha = Math.atan( ( ( y1-y2 ) / ( x2-x1 ) ) );
       double sinAlpha = Math.sin( alpha );
       double cosAlpha = Math.cos( alpha );
       
       double ball1V1 = ball1.getVelocityX()*cosAlpha - ball1.getVelocityY()*sinAlpha;
       double ball1V2 = ball1.getVelocityX()*sinAlpha + ball1.getVelocityY()*cosAlpha;
       
       double ball2V1 = ball2.getVelocityX()*cosAlpha - ball2.getVelocityY()*sinAlpha;
       double ball2V2 = ball2.getVelocityX()*sinAlpha + ball2.getVelocityY()*cosAlpha;
       
       double m1 = ball1.getMass();
       double m2 = ball2.getMass();
       double m1Pm2 = m1+m2;
        
       double ball1V1New = ( 2*m2*ball2V2 - ball1V1*( m2 - m1 ) )/m1Pm2;
       
       double ball2V1New = ( 2*m1*ball1V1 - ball2V2*( m2-m1 ) )/m1Pm2;
       
       double v1xNew = ball1V1New*cosAlpha + ball1V2 * sinAlpha;
       double v1yNew = -1*( ball1V1New*sinAlpha ) + ball1V2 * cosAlpha;
       
       ball1.setBallVelocity(v1xNew, v1yNew);
       
       double v2xNew = ball2V1New * cosAlpha + ball2V2 * sinAlpha;
       double v2yNew = -1*( ball2V1New * sinAlpha ) + ball2V2 * cosAlpha;

       ball2.setBallVelocity(v2xNew, v2yNew);
    }
    
    private void checkBandCollision( BilliardBall ball, int place )
    {
        double ballX = ball.getBallPosition().getX();
        double ballY = ball.getBallPosition().getY();
        double radius = ball.getRadius();
        
        if( ballX < (-1*MAX_LIMIT) || ballX > ( width+MAX_LIMIT ) || ballY < (-1*MAX_LIMIT) || ballY > ( height+MAX_LIMIT )  )
        {
            // TOP MASADAN UÇTU
            copyBalls[ place ] = null;
        }        
        else
        {
            if( ( ballX+radius >= width ) || ( ballX - radius <= 0 ) )
            {
               bounceCounter++; 
                
                ball.setVelocityX( -1 * ball.getVelocityX() );
                
                if( ballX+radius >= width )
                {
                    ball.setBallPosition( ( width-radius-ERROR_RATIO ), ballY);
                }
                else
                {
                    ball.setBallPosition( ( radius+ERROR_RATIO ), ballY);
                }
            }

            if( ( ballY + radius >= height ) || ( ballY - radius <= 0 ) )
            {
                bounceCounter++; 
                
                ball.setVelocityY( -1 * ball.getVelocityY() );
                
                if( ballY+radius >= height )
                {
                    ball.setBallPosition( ballX, ( height-radius-ERROR_RATIO ));
                }
                else
                {
                    ball.setBallPosition( ballX, ( radius+ERROR_RATIO ) );
                }
            }
        }
    }

    private boolean checkHoles( int i )
    {
        
        for( int x = 0; x < holeNumber; x++  )
        {
            double distance = copyBalls[i].getDistanceToHole( holes[x][0], holes[x][1] );
            
            if( distance <= holeRadius )
            {
                copyBalls[i] = null;
                holeBalls++;
                return true;
            }
            
        }
        
        return false;
    }
        
    
    @Override
    public void paintComponent(Graphics g)
    {
        
        super.paintComponent(g);
        g.setColor(Color.decode("#7E2217"));
        g.fillRect(0, 0, (int)(width+2*holeRadius), (int)(height+2*holeRadius) );
        g.setColor(Color.decode("#437C17"));
        g.fillRect( (int)holeRadius, (int)holeRadius, (int)(width), (int)(height) );
	
        g.setColor( Color.decode(ColorClass.WHITE ) );
        g.drawOval( (int )(width-height/2), (int)holeRadius, (int)(height), height);
        
        for (int i = 0; i < copyBalls.length; i++)
        {
            if( copyBalls[i] == null )
            {
                continue;
            }
            g.setColor(copyBalls[i].getColour() );
            g.fillOval( (int)copyBalls[i].getBallPosition().getX(), (int)copyBalls[i].getBallPosition().getY(), (int)( 2*copyBalls[i].getRadius() ), (int)( 2*copyBalls[i].getRadius() ) );
        }
        
        g.setColor(Color.BLACK);
        for( int i = 0; i < holeNumber; i++ )
        {
            g.fillOval( holes[i][0], holes[i][1], (int)(2*holeRadius), (int)(2*holeRadius));
        }
        
     } // paintComponent
    
} // class
