package application;


import java.awt.Shape;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.*;

public class Ship {
	
	//definise oblik broda i njegov plamen, pocetne tacke, na pocetku igre, bez pomeranja
	final double[]  brodX={14,-10,-6,-10},brodY={0,-8,0,8}, 
					plamenX={-6,-23,-6}, plamenY={-3,0,3};
	
	final int radius=6; // radius of circle used to approximate the ship
	
	//promenljive koje se koriste za pomeranje
	double x, y, ugao, xBrzina, yBrzina, ubrzanje, velocityDecay, brzinaRotiranja; 
	//ubrzanje-koliko se ubrzava brod prilikom strelice na gore 
	
	boolean turningLeft, turningRight, ubrzati, pauza;
	//ubrzati-da li treba ubrzati brod ili ne
	//active igra radi, nije pauzirana
	
	//trenutna lokacija broda i plamena,ili nova na pocetku
	double[] xPts, yPts, plamenXPts, plamenYPts; 
	
	int shotDelay, shotDelayLeft; //used to determine the rate of firing
	
	private int brZivota=3;
	
	private Image brod;
	private ImageView selectedImage = new ImageView();

	private ImageView iv;

	private SnapshotParameters params;

	private WritableImage rotatedImage;

	private GraphicsContext graphic;
	
	public Ship(double x, double y, double angle, double acceleration,
			double velocityDecay, double rotationalSpeed, int shotDelay, int br){
			// this.x refers to the Ship's x, x refers to the x parameter
			this.x=x;
			this.y=y;
			
			this.ugao=angle;
			this.ubrzanje=acceleration;
			this.velocityDecay=velocityDecay;
			this.brzinaRotiranja=rotationalSpeed;
			
			xBrzina=0; // ne pomera se
			yBrzina=0;
			
			turningLeft=false; // nema okretanja levo ni desno
			turningRight=false;
			ubrzati=false; // nema ubrzanja
			pauza=false; // da li je igra pauzirana ili ne
			
			xPts=new double[4]; // allocate space for the arrays
			yPts=new double[4];
			
			//tacke za crtanje plamena
			plamenXPts=new double[3];
			plamenYPts=new double[3];
			this.shotDelay=shotDelay; // # of frames between shots
			shotDelayLeft=0; // spreman za pucanje
			
			brZivota=br;
		}

	public void draw(GraphicsContext graphic)
	{
		//System.out.println("draw");
		
		 brod = new Image(Main.class.getResourceAsStream("rsz_brod.png"));
		
		//ako je doslo do ubrzanja broda onda nacrtaj plamen
		if(ubrzati && pauza==false){ // draw flame if accelerating
			
			for(int i=0;i<3;i++){
			
				plamenXPts[i]=(int)(plamenXPts[i] * Math.cos(ugao) - plamenYPts[i] * Math.sin(ugao) + x + 0.5);
				plamenYPts[i]=(int)(plamenXPts[i] * Math.sin(ugao) + plamenYPts[i] * Math.cos(ugao) + y + 0.5);
			}
			
				//postaviti boju plamena
				graphic.setFill(Color.RED);
				graphic.setStroke(Color.RED);
				graphic.setLineWidth(10);
				
				//crtanje plamena na postavljenim tackama
				graphic.strokePolygon(plamenXPts, plamenYPts, 3);
				graphic.fillPolygon(plamenX, plamenY, 3);
			
		}
		for(int i=0;i<4;i++)
		{
			
			xPts[i]=(int)(brodX[i]*Math.cos(ugao)- brodY[i]*Math.sin(ugao)+	x+.5); //translate and round
			
			yPts[i]=(int)(brodX[i]*Math.sin(ugao)+ brodY[i]*Math.cos(ugao)+ y+.5); //translate and round		}
		}
		
		if(!pauza) // active means game is running (not paused)
		{
		//	graphic.drawImage(brod, x, y);
			graphic.setStroke(Color.WHITE);
		}
			
		else // draw the ship dark gray if the game is paused
		
		//	graphic.setStroke(Color.DARKGRAY);
	//		graphic.drawImage(brod, x, y);
	//	graphic.fillPolygon(xPts,yPts,4); // 4 is the number of points
		 graphic.strokePolygon(xPts, yPts, 4);
		
		this.graphic=graphic;
		drawRotatedImage(brod,  x, y, ugao, graphic);
		
	}
	
	//scrnWidth, scrnHeight, su duzina i visina prozora
	public void move(int scrnWidth, int scrnHeight)
	{
		//System.out.println("Pomeram se!");
		if(shotDelayLeft>0) //move() is called every frame that the game
			shotDelayLeft--; //is run, so this ticks down the shot delay
		
		
		//skretanje levo smanjuje ugao
		if(turningLeft) //this is backwards from typical polar coordinates
		{
			ugao -= brzinaRotiranja; //because positive y is downward.

			/*iv = new ImageView(brod);
			iv.setRotate(ugao);
			params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			rotatedImage = iv.snapshot(params, null);
			*/
			drawRotatedImage(brod,  x, y, ugao, graphic);
		}
		//skretanje desno povecava ugao
		if(turningRight) //Because of that, adding to the ugao is
		{
			ugao += brzinaRotiranja; //rotating clockwise (to the right)
			/*
			iv = new ImageView(brod);
			iv.setRotate(ugao);
			params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			rotatedImage = iv.snapshot(params, null);*/
			drawRotatedImage(brod,  x, y, ugao, graphic);
			
		}
		
		//ugao mora da bude u granicama 0 to 2*pi
		if(ugao>(2*Math.PI)) 
			ugao-=(2*Math.PI);
			else if(ugao<0)
				ugao+=(2*Math.PI);
		
		//ako treba ubrzati brod
		if(ubrzati){ 
			
			//POGLEDATI TRIGONOMETRIJSKI KRUG
			drawRotatedImage(brod,  x, y, ugao, graphic);
			xBrzina += ubrzanje*Math.cos(ugao);
			yBrzina += ubrzanje*Math.sin(ugao);
		}
		
		//pomeriti brod
		x += xBrzina; //move the ship by adding velocity to position
		y += yBrzina;
		
		//usporiti brod procentualno, jer je velocityDecay decimal izmedju 0 i 1
		xBrzina *= velocityDecay; //slows ship down by percentages (velDecay
		yBrzina *= velocityDecay; //should be a decimal between 0 and 1
		
		//ako je x<0 ili x>screenWidth, znaci da je izasao iz ekrana, i da se pojavi sa suprotne strane
		
		if(x<0) 
			x += scrnWidth;
		
		else if(x>scrnWidth)
			x -= scrnWidth;
		
		//isto kao za x
		if(y<0)
			y += scrnHeight;
		
		else if(y>scrnHeight)
			y -= scrnHeight;
	}

	//metod provera da li brod moze ponovo da puca ili mora da saceka
	public boolean daLiMoguDaPucam(){
		
		if(shotDelayLeft>0) 
			return false; 
		
		return true;
	}
	
	//metod za pucanje
	public Shot shoot()
	{
		shotDelayLeft=shotDelay; //set delay till next shot can be fired
		//a life of 40 makes the shot travel about the width of the
		//screen before disappearing
		
		return new Shot(x,y,ugao,xBrzina,yBrzina,40);
		
	}
	
	
	
	//OSTALE METODE, GETTERI I SETERI
	
	public boolean isPauza() {
		return pauza;
	}

	public void setTurningLeft(boolean turningLeft) {
		this.turningLeft = turningLeft;
	}

	public void setTurningRight(boolean turningRight) {
		this.turningRight = turningRight;
	}

	public void setPauza(boolean active) {
		this.pauza = active;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public void setX(double xx) {
		x=xx;
	}

	public void setY(double yy) {
		y=yy;
	}
	

	public double getUgao() {
		return ugao;
	}
	
	public void setUgao(double angle)
	{
		ugao=angle;
	}

	public void setUbrzati(boolean ubrzati) {
		this.ubrzati = ubrzati;
	}

	public int getBrZivota() {
		return brZivota;
	}

	public void setBrZivota(int brZivota) {
		this.brZivota = brZivota;
	}


	final double TO_RADIANS = Math.PI/180;
	
	void drawRotatedImage(Image image, double x, double y, double angle, GraphicsContext graphic) 
	{ 
		// save the current co-ordinate system 
		// before we screw with it
		graphic.save(); 
	 
		// move to the middle of where we want to draw our image
		graphic.translate(x, y);
	
		// rotate around that point, converting our 
		// angle from degrees to radians 
		graphic.rotate(80*angle);
		
		// draw it up and to the left by half the width
		// and height of the image 
		graphic.drawImage(image, -40,-40);
		// and restore the co-ords to how they were when we began
		graphic.restore(); 
	}

	
}
