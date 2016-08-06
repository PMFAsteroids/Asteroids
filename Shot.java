package application;


import javafx.scene.paint.*;

import javafx.scene.canvas.GraphicsContext;

public class Shot
{
		final double brzina=12; //brzina kojom se metak pomera, u pikselima po frejmu
		double x,y,xKorak,yKorak; //variables for movement
		int lifeLeft; //causes the shot to eventually disappear if it doesn’t hit anything
		
		
		public Shot(double x, double y, double angle, double shipXKorak, double shipYKorak, int lifeLeft)
		{
			 this.x=x;
			 this.y=y;
			
			 xKorak=brzina*Math.cos(angle)+shipXKorak;
			 yKorak=brzina*Math.sin(angle)+shipYKorak;
			 //broj frejmova koliko ce metak da traje pre nego sto nestane
			 this.lifeLeft=lifeLeft;
		 }
		
		public void move(int scrnWidth, int scrnHeight)
		{	
			lifeLeft--; 
			 
			 x += xKorak; 
			 y += yKorak;
			 
			 //ako izadje van ekrana da se odmah pojavi na drugoj strani
			 
			 if(x<0) 
				 x+=scrnWidth; 
			 else if(x>scrnWidth)
				 x-=scrnWidth;
			 
			 if(y<0)
				 y+=scrnHeight;
			 else if(y>scrnHeight)
				 y-=scrnHeight;
		}

			public void draw(GraphicsContext g){
				
				g.setFill(Color.YELLOW); 
				g.fillOval((int)(x-.5), (int)(y-.5), 4, 4);
			 }

			public double getX(){
			 return x;
			 }

			public double getY(){
			 return y;
			 }

			public int getLifeLeft(){
				return lifeLeft;
			} 
		
}

