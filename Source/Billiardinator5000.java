import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Cihan
 */
public class Billiardinator5000 {
    
    private Billiardinator5000 theInstance = null;
    
    private BilliardTable table;
    private BilliardBall balls[];
    private Controller controller;
    
    
    public Billiardinator5000()
    {
        if (theInstance == null)
        {
            if( readFile() == true )
            {
                controller = new Controller( balls, table );
                controller.startAnimation();
            }
            else
            {
                JOptionPane.showMessageDialog(null,"File couldn't be read!");
            }
            
            
        }
        else 
        {
            getInstance();
        }

    }
    
    public Billiardinator5000 getInstance()
    {
        return theInstance;
    }
    
   private boolean readFile()
   {
       try
       {
        FileInputStream fstream = new FileInputStream("d:/bilardo.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));


        String strLine;
        strLine = br.readLine(); 

        String[] tokens = strLine.split(" ");
        int w,h,holes,ballNumber;
        double fs;
        double posX;
        double posY;
        double velX;
        double velY;
        double m;
        double r;
        String color;

        w = Integer.parseInt(tokens[0]);
        if(w<=0)
        {
            // 
            
            System.out.println("Width always must be positive value!");
            
            return false;
        }
        /*else if(w>1000)
        {
            w=750;
        }*/
        
        h=Integer.parseInt(tokens[1]);
        if(h<=0)
        {
            System.out.println("Height always must be positive value!");
            return false;
        }
        /*else if(h>1000)
        {
            h=w/2;
        }*/

        holes=Integer.parseInt(tokens[2]);
        if(holes<4)
        {
            holes=4;
        }
        else if(holes>6 || holes==5)
        {
            holes=6;
        }

        fs=Double.parseDouble(tokens[3]);
        if(fs<0)
        {
            System.out.println("Friction always must be necessary");
            return false;
        }


        ballNumber = Integer.parseInt(tokens[4]);
        if( ballNumber <= 0 )
        {
            // HATA PENCERESİ
            
            return false;
        }
        balls = new BilliardBall[ballNumber];
        
        table = new BilliardTable(w,h,holes,fs);

        for(int i=0; i < ballNumber; i++)
        {
            strLine = br.readLine(); 
            String[] array = strLine.split(" ");

            posX = Double.parseDouble(array[0]);
            if(posX<=0 || posX>w)
            {
                System.out.println("\nPosX value is invalid!You are out of table now!");
                
                return false;
            }
            
            posY = Double.parseDouble(array[1]);
            if(posY<=0 || posY>h)
            {
                System.out.println("\nPosY value is invalid.You are out of table now!");
                
                return false;
            }
            
            velX = Double.parseDouble(array[2]);
            /*if(velX < 0)
            {
                System.out.println("Velocity X must be positive value!");
                
                return false;
            }*/
            
            velY = Double.parseDouble(array[3]);
           /* if(velY < 0)
            {
                System.out.println("Velocity Y must be positive value!");
                
                return false;
            }*/
            
            m = Double.parseDouble(array[4]);
            if (m < 0)
            {
                System.out.println("Mass always must be positive and it can not 0 !");
                
                return false;
            }
            
            r = Double.parseDouble(array[5]);
            if(r <= 0)
            {
                System.out.println("Radius of ball always must be positive and it can not 0!");
                
                return false;
            }
            
            color = convertColor( array[6] ); 
            
            balls[i] = new BilliardBall( posX, posY, velX, velY, m, r, color, fs );
        } 

        in.close();
        return true;
       } // try block
       catch (Exception e)
       {
            System.err.println("Error: " + e.getMessage());
            return false;
       }
		    
   } // end of readFile()
   
   private String convertColor( String clr )
   {
       String color;
       
       if( clr.equals("red") )
       {
           color = ColorClass.RED;
       }
       else if( clr.equals("black") )
       {
           color = ColorClass.BLACK;
       }
       else if( clr.equals("white") )
       {
           color = ColorClass.WHITE;
       }
       else if( clr.equals("blue") )
       {
           color = ColorClass.BLUE;
       }
       else if( clr.equals("brown") )
       {
           color = ColorClass.BROWN;
       }
       else if( clr.equals("orange") )
       {
           color = ColorClass.ORANGE;
       }
       else if( clr.equals("yellow") )
       {
           color = ColorClass.YELLOW;
       }
       else if( clr.equals("green") )
       {
           color = ColorClass.GREEN;
       }
       else if( clr.equals("gray") )
       {
           color = ColorClass.GRAY;
       }
       else
       {
           color = ColorClass.DEFAULT;
       }
       
       return color;
   }
    
    
} // end of class
