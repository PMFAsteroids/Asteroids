package application;

import java.awt.Canvas;
import java.awt.Graphics;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Asteroid {
	
	private double x, y, xVelocity, yVelocity, radius;
	private int hitsLeft, numSplit;
	
	//KONSTRUKTOR
	public Asteroid(double x,double y,double radius,double minVelocity,double maxVelocity,int hitsLeft,int numSplit)
	{
		this.x=x;
		this.y=y;
		this.radius=radius;
		this.hitsLeft=hitsLeft; //number of shots left to destroy it
		this.numSplit=numSplit; //broj asteroida na koje se deli kada je pogodjen
		
		//random pravac kretanja i brzina izmedju min i max brzine
		double vel=minVelocity + Math.random()*(maxVelocity-minVelocity),
		
		dir=2*Math.PI*Math.random(); // random pravac,smer
		xVelocity=vel*Math.cos(dir);
		yVelocity=vel*Math.sin(dir);
	}

	//fja za pomeranje
	public void move(int scrnWidth, int scrnHeight){
		
		x += xVelocity; //move the asteroid
		y += yVelocity;
		//wrap around code allowing the asteroid to go off the screen
		//to a distance equal to its radius before entering on the
		//other side. Otherwise, it would go halfway off the sceen,
		//then disappear and reappear halfway on the other side
		//of the screen.
		if(x < 0-radius)
			x += scrnWidth + 2*radius;
		else if(x > scrnWidth + radius)
			x -= scrnWidth + 2*radius;
		
		if(y<0-radius)
			y += scrnHeight + 2*radius;
		else if( y>scrnHeight + radius)
			y -= scrnHeight + 2*radius;
	}
	
	public void draw(GraphicsContext g)
	{
		Canvas canvas;
		//boja asteroida
	//	g.setFill(Color.CYAN);
	//	g.setStroke(Color.CYAN);
		Image image2=null;
		if(hitsLeft==3)
		{
		 image2 = new Image(Main.class.getResourceAsStream("rsz_kamen.png"));
		g.drawImage(image2,(int)(x-radius+.5),(int)(y-radius+.5));
		}
		if(hitsLeft==2)
		{
		 image2 = new Image(Main.class.getResourceAsStream("srednji1.png"));
		g.drawImage(image2,(int)(x-radius+.5),(int)(y-radius+.5));
		}
		if(hitsLeft==1)
		{
		 image2 = new Image(Main.class.getResourceAsStream("mali1.png"));
		g.drawImage(image2,(int)(x-radius+.5),(int)(y-radius+.5));
		}
		
		// nacrtati asteroid u tacki x i y;
		
	//	g.fillOval((int)(x-radius+.5),(int)(y-radius+.5),(int)(2*radius),(int)(2*radius));
		
	}
	
	public int getHitsLeft()
	{
		//koristi je Main da sazna da li treba da asteroid podeli na dva manja ili da ga potpuno unisti
		return hitsLeft;
	}
	
	public int getNumSplit(){
		return numSplit;
	}
	
	//Za asteroid je potrebno proveriti da li je pogodjen bulletom ili je doslo do sudara sa brodom
	
	//provera sudara sa brodom
	public boolean shipCollision(Ship ship){
		// Use the distance formula to check if the ship is touching this
		// asteroid: Distance^2 = (x1-x2)^2 + (y1-y2)^2 ("^" denotes
		// exponents). If the sum of the radii is greater than the
		// distance between the center of the ship and asteroid, they are
		// touching.
		// if (shipRadius + asteroidRadius)^2 > (x1-x2)^2 + (y1-y2)^2,
		// then they have collided.
		
		//ne provera da li je doslo do sudara kada igrac zapocinje novu igru ili kada je pauza
		
		if(Math.pow(radius+ship.getUgao(),2) > Math.pow(ship.getX()-x,2)+Math.pow(ship.getY()-y,2) && ship.isPauza()==false)
			return true;
		
		
		return false;
	}
	
	//da li je pogodjen
	public boolean shotCollision(Shot shot)
	{
		// Same idea as shipCollision, but using shotRadius = 0
		
		if(Math.pow(radius,2) > Math.pow(shot.getX()-x,2)+Math.pow(shot.getY()-y,2))
			{
			return true;
			}
		
		return false;
	}
	
	//podela asteroida
	public Asteroid createSplitAsteroid(double minVelocity,double maxVelocity)
	{
		/*kada je asteroid pogodjen ovaj metod je pozvan numSplit puta od strane AsteroidGame da kreira
		 * numSplit malih asteroida;
		 */
			//Dividing the radius by sqrt(numSplit) makes the
			//sum of the areas taken up by the smaller asteroids equal to
			//the area of this asteroid. Each smaller asteroid has one
			//less hit left before being completely destroyed.
		
		return new Asteroid(x,y,radius/Math.sqrt(numSplit),minVelocity,maxVelocity,hitsLeft-1,numSplit);
	}
	
	public int getScore()
	{
		double max;
		max=Main.astRadius; //ovo je najveci asteroid
		
		int br;
		
		if(radius==max) br=20;
		
			else if(Math.round(radius)==max/2) br=100;
		
				else br=50;
		
		return br;
	}
}
