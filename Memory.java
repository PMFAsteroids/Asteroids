package application;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Memory {
	

	public class MojeDugme extends Button implements EventHandler<ActionEvent> {
		
		int x,y;
			
			public  MojeDugme(int i,int j) {
				// TODO Auto-generated constructor stub
				x=i;
				y=j;
			}
		
			@Override
			public void handle(ActionEvent e) {
				MojeDugme dugme=(MojeDugme) e.getSource();
				int x,y,br=0;
				
				x=dugme.x;
				y=dugme.y;
				
				if(igra.otvoreno[x][y]==0)
				{
					if(timeSeconds.intValue()==0) 
					{
						//ako je vreme isteklo u toku igre (igra nije zavrsena) da ne dozvoli dalju igru
						next();	
					}
					
					if(flag==0)
					{
						br=0;
						//System.out.println("Prva karta");				
						for (int i = 0; i < 6; i++) {
							for (int j = 0; j < 6; j++) {
								
								if(igra.otvoreno[i][j]==1)
								{
									br++;
								}}}
		
						//ako ima dve karte znaci da je otvorena treca i da prve dve treba zatvoriti
						if(br==2)
						{
							for (int i = 0; i < 6; i++) {
								for (int j = 0; j < 6; j++) {
									
									if(igra.otvoreno[i][j]==1)
									{
										igra.otvoreno[i][j]=0;
									}
						}}}
						br=0;
						igra.otvoreno[x][y]=1;
						refresh();
						flag=1;
					}
					else if(flag==1)
					{
						//System.out.println("Druga karta");
						igra.otvoreno[x][y]=1;
						
						for (int i = 0; i < 6; i++) {
							for (int j = 0; j < 6; j++) {
								
								//System.out.println("usao");
								
								if((i!=x || j!=y) && igra.otvoreno[i][j]==1)
								{
									//znaci da su slike iste, i trebaju ostati otvorene
									if(igra.slike[i][j]==igra.slike[x][y])
									{
										igra.otvoreno[i][j]=2;
										igra.otvoreno[x][y]=2;
										
										addRezultat();
										refresh();
										
										
										break;
									}}}}
						refresh();
						flag=0;
					}}}
		}
		
	
	public MojeDugme[][] dugmici=new MojeDugme[6][6];
	public int flag=0;
	MemoryEngine igra=new MemoryEngine();

	//zbog glavnog prozora
	public Pane pane;
	public Scene scene;
	
	//stil
	InnerShadow is;
	
	//za LABELE, rezultat, naziv, vreme
	public int rez=0;
	Label l= new Label("Score:0/36");
	
	//za timer
	private static final Integer STARTTIME = 121;
	private Timeline timeline;
    private Label timerLabel = new Label();
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
   
    public Memory(Pane pane,Scene scene)
    {
    	this.pane=pane;
    	this.scene=scene;
    }

	public void startMemory(Stage primaryStage) 
	{
		Stage window=primaryStage;
		
			pane.getChildren().clear();
			pane.setId("pane");
			window.setScene(scene);
			window.setTitle("Bonus");
			
			//slova
			is = new InnerShadow(); /* za senku slova */
			is.setOffsetX(2.0f); /*kakva je senka */
			is.setOffsetY(2.0f);
			
			//rezultat
			l.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 20));
			l.setId("tekst"); /*  id zbog css-a*/
			l.setEffect(is); /* dodavanje senke */
			l.setLayoutX(40); /*pozicija labele u odnosu na x osu*/
			l.setLayoutY(30);/*pozicija labele u odnosu na y osu*/
			pane.getChildren().add(l); /* nije borderPane jer onda mora da se naglasi top,centar itd zato je samo pane*/
			
			//naslov igre
			Label naslov= new Label("Memory game");
			naslov.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
			naslov.setId("tekst"); /*  id zbog css-a*/
			naslov.setEffect(is); /* dodavanje senke */
			naslov.setLayoutX(350); /*pozicija labele u odnosu na x osu*/
			naslov.setLayoutY(20);/*pozicija labele u odnosu na y osu*/
			pane.getChildren().add(naslov); /* nije borderPane jer onda mora da se naglasi top,centar itd zato je samo pane*/
						
			//timer
			 timerLabel.textProperty().bind(timeSeconds.asString());
			 timerLabel.setId("tekst");
			 
			 	//stil teksta
			 	timerLabel.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 25));
			 	timerLabel.setEffect(is);
			 	
			 	timeSeconds.set(STARTTIME);
                timeline = new Timeline();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(STARTTIME+1),
                        new KeyValue(timeSeconds, 0)));
                
                timeline.playFromStart();
               
                
                timerLabel.setLayoutX(780); /*pozicija labele u odnosu na x osu*/
    			timerLabel.setLayoutY(30);/*pozicija labele u odnosu na y osu*/
    			    
                pane.getChildren().add(timerLabel);

			//dodavanje polja, tacnije dugmica
			
			int x,y;

            GridPane matrica = new GridPane();    
            //razmak izmedju dugmica, horizontalno i vertikalno
            matrica.setHgap(5); 
			matrica.setVgap(5);
			
            for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {
					
						dugmici[i][j]=new MojeDugme(i,j);
						dugmici[i][j].setId("btn1");
						
						dugmici[i][j].setOnAction(dugmici[i][j]);
						
						dugmici[i][j].setDisable(false);
	                    // menjanje indeksa uz petlju i dodavanje
	                    matrica.setRowIndex(dugmici[i][j],j);
	                    matrica.setColumnIndex(dugmici[i][j],i);    
	                    matrica.getChildren().add(dugmici[i][j]);

				}
			}        
            
            matrica.setLayoutX(50);
            matrica.setLayoutY(100);
            pane.getChildren().add(matrica);
            
            primaryStage.show();
	} 
		
	
	public void addRezultat()
	{
		rez += 2;
		l.setText("Score: " + rez + "/36");
	}


	private void refresh() 
	{
		int br=0;
		int r=0;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				
				if(igra.otvoreno[i][j]!=0)
				{
					r++;
					br=igra.slike[i][j];
					
					String s="s (" + br + ").png";
					
					//stavljanje slike
					Image imageDecline = new Image(getClass().getResourceAsStream(s));
					dugmici[i][j].setGraphic(new ImageView(imageDecline));
					
				}
				else
				{
					//uklanjanje slike
					dugmici[i][j].setGraphic(null);
				}
			}
			
			//ako je igrac zavrsio igru pre isteka vremena
			
			if(r==36 && timeSeconds.intValue()>=0)
			{
				//POBEDA
				Main.setPobeda(true);
				
				timeline.stop();
				//da prikaze dugme za dalje
				next();
			}
			else if(timeSeconds.intValue()==0)
			{
				//IZGUBIO
				Main.setPobeda(false);
				
				//da zaustavi vreme
				timeline.stop();
				//da prikaze dugme za dalje
				next();
			}
		}
		
	}

	private void next() {
		
		//dugme za dalje
		Button next=new Button("NEXT");
		next.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
		next.setLayoutX(830);
		next.setLayoutY(620);
        
		//onemogucavanje dugmica, ne dozvoljavamo klik
		for (int i = 0; i < dugmici.length; i++) {
			for (int j = 0; j < dugmici.length; j++) {
				
				dugmici[i][j].setDisable(true);
			}
		}

		next.setOnAction(new EventHandler<ActionEvent>() { /*da pozove tutorijal*/
			
			@Override
			public void handle(ActionEvent event) {
				Main.ind=3;
				pane.getChildren().clear();
				Main.gc=Main.canvas.getGraphicsContext2D();
				pane.getChildren().add(Main.canvas);
				pane.getChildren().add(Main.getScore());
				
				Main.timer.start();
				//postavlja lab na sredini i nakon 2 sekunde brise tekst

				Main.setRanjiv(false);
				Label label = new Label("Level " + Main.getLevel());
				label.setId("lf");
		        FadeTransition fader = Main.createFader(label);
		        label.setLayoutX(380); /*pozicija labele u odnosu na x osu*/
				label.setLayoutY(300);/*pozicija labele u odnosu na y osu*/
		        pane.getChildren().add(label);

		        
		       fader.play();
			}
		});

		
		
		//dodavanje dugmeta za dalje
		pane.getChildren().add(next);
		
	}


}
