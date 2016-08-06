package application;

import com.sun.prism.paint.Color;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class Find
{
	static Button [][] matrix= new Button[8][12];
	static Button [] two= new Button[3];
	static FindEngine e= new FindEngine();
	static Label lab= new Label();
	private static final Integer STARTTIME = 251;
	private static Timeline timeline;
	static int ind=0;
    private static Label timerLabel = new Label();
    private static IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
    static int sum=0;
    
    public Find()
    {
    	
    }
    

	public static void Sum()
	{
		sum=0;
		for (int i = 0; i < 8; i++)
		{
		for (int j = 0; j < 12; j++)
		{
			if (e.matrix[i][j] == 0)
				sum++;
		}
		}
		
	}
	void startFind(Stage primaryStage,Pane pane,Scene scene)
	{
		pane.getChildren().clear();
		pane.setId("pane");
		primaryStage.setScene(scene);
		primaryStage.setTitle("Bonus");
		
		
		Label label= new Label();
		label.setText("Find these:");
		label.setId("l");
		label.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 50));
		label.setLayoutX(400); 
		label.setLayoutY(15);
		pane.getChildren().add(label);
		
		 
		Button b1=new Button("   ");
		b1.setId("b");
		b1.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
		b1.setLayoutX(580); /* u odnosu na x osu*/ 
		b1.setLayoutY(15);
		pane.getChildren().add(b1);
		two[0]=b1;
		
	
		Button b2=new Button("   ");
		b2.setId("b2");
		b2.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
		b2.setLayoutX(640); 
		b2.setLayoutY(15);
		pane.getChildren().add(b2);
		two[1]=b2;
		
		
		
		lab.setText("Score:0/96");
		lab.setId("lab");
		lab.setLayoutX(100); 
		lab.setLayoutY(50);
		pane.getChildren().add(lab);
		
		
		//timer
		 timerLabel.textProperty().bind(timeSeconds.asString());
		 timerLabel.setId("tekst");
		 timerLabel.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 25));
		
		
		 timeSeconds.set(STARTTIME);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(STARTTIME+1),
                new KeyValue(timeSeconds, 0)));
       
        timeline.playFromStart();
       
        timerLabel.setId("time");
        timerLabel.setLayoutX(800); /*pozicija labele u odnosu na x osu*/
			timerLabel.setLayoutY(50);/*pozicija labele u odnosu na y osu*/
			    
        pane.getChildren().add(timerLabel);
		 
		
		
		
		GridPane grid = new GridPane();
		grid.setId("g");
		
		for (int i = 0; i <8; i++) 
		{
			for (int j = 0; j <12; j++) 
			{
				matrix[i][j] = new Button(); 
                matrix[i][j].setText("   ");

                matrix[i][j].setOnAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent event) 
                    {
                    	int x=0;
                    	for (int i = 0; i <8; i++) 
                		{
                			for (int j = 0; j <12; j++) 
                			{
                				if(event.getSource()==matrix[i][j])
                                {
                					x=e.matrix[i][j];
                					if(e.two[0]==x  && e.two[1]==x)
                					{
                						BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("0.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            			                Background background = new Background(backgroundImage);
            			              
            			                matrix[i][j].setBackground(background);
            			                if(j+1<12 && e.matrix[i][j+1]==x) matrix[i][j+1].setBackground(background);
            			                else  if(j-1>=0)    matrix[i][j-1].setBackground(background);
            			               	e.matrix[i][j]=0;
            			               	if(j+1<12 && e.matrix[i][j+1]==x) e.matrix[i][j+1]=0;
            			                else   if(j-1>=0) e.matrix[i][j-1]=0;
            			               	End(pane);
            			               	break;
                					}
                					else if(e.two[0]==x )
                					{
                						if(j+1<12 && e.matrix[i][j+1]==e.two[1])
                						{
                							BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("0.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                			                Background background = new Background(backgroundImage);
                			              
                			                matrix[i][j].setBackground(background);
                			               	matrix[i][j+1].setBackground(background);
                			               	e.matrix[i][j]=0;
                			               	e.matrix[i][j+1]=0;
                			                End(pane);
                			               	break;
                			            }
                					}
                					else if( e.two[1]==x)
                					{
                						if(j-1>=0 && e.matrix[i][j-1]==e.two[0])
                						{
                							BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("0.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                							Background background = new Background(backgroundImage);
            			              
                							matrix[i][j].setBackground(background);
                							matrix[i][j-1].setBackground(background);
                							e.matrix[i][j]=0;
                			               	e.matrix[i][j-1]=0;  
                							End(pane);
                        					break;
                						}
                					}
            					
                                }
            				
                			}
	
                		}
                		
                    }

private void End(Pane pane) 
	{
		
		Sum();
			
		if (sum == 96 && timeSeconds.intValue() > 0)
		{
			//POBEDA
			Main.setPobeda(true);
			
			timeline.stop();
			
			disable();
			
			Button b4 = new Button("Next");
			b4.setId("button");
			b4.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
			b4.setLayoutX(750);
			b4.setLayoutY(620);
			
			
			b4.setOnAction(new EventHandler<ActionEvent>() { /*da pozove tutorijal*/
				
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
			
			
			pane.getChildren().add(b4);

			Label lab4 = new Label();
			lab4.setText("You have one more life!");
			lab4.setId("lab");
			lab4.setLayoutX(500);
			lab4.setLayoutY(650);
			pane.getChildren().add(lab4);
			changeScore();

		} 
		
		else if(timeSeconds.intValue()==0)
		{
			//IZGUBIO
			Main.setPobeda(false);
			
			disable();
			changeScore();
			Label lab4= new Label();
			lab4.setText("You don't have bonus life!");
			lab4.setId("lab");
			lab4.setLayoutX(500); 
			lab4.setLayoutY(650);
			pane.getChildren().add(lab4);
			
			Button b3=new Button("Next");
			b3.setId("button");
			b3.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
			b3.setLayoutX(750); 
			b3.setLayoutY(620);
			
			b3.setOnAction(new EventHandler<ActionEvent>() { /*da pozove tutorijal*/
				
				@Override
				public void handle(ActionEvent event) {
					Main.ind=3;
					pane.getChildren().clear();
					Main.gc=Main.canvas.getGraphicsContext2D();
					pane.getChildren().add(Main.canvas);
					pane.getChildren().add(Main.getScore());
					
					Main.timer.start();
					//postavlja lab na sredini i nakon 2 sekunde brise tekst

					Label label = new Label("Level " + Main.getLevel());
					label.setId("lf");
					Main.setRanjiv(false);
			        FadeTransition fader = Main.createFader(label);
			        label.setLayoutX(380); /*pozicija labele u odnosu na x osu*/
					label.setLayoutY(300);/*pozicija labele u odnosu na y osu*/
			        pane.getChildren().add(label);

			        
			       fader.play();
				}
			});

			
			
			pane.getChildren().add(b3);
		}
		else 
		{
			check_delete();		
			changeScore();
			setPicture();
		}
	
	}
	

					
                });       
                BackgroundImage backgroundImage = new BackgroundImage( new Image( Find.class.getResourceAsStream(e.matrix[i][j]+".png")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                Background background = new Background(backgroundImage);
              
                matrix[i][j].setId("img");
               matrix[i][j].setBackground(background);
               
               setPicture();
               
                GridPane.setConstraints(matrix[i][j], j, i);
                grid.getChildren().add(matrix[i][j]);
			}
		}
		
		grid.setLayoutX(150); 
		grid.setLayoutY(100);
		pane.getChildren().add(grid);
		
	}
			
	
	 protected void disable() {
		
		 for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 12; j++) {
				
				matrix[i][j].setDisable(true);
				
			}
		}
		 
	}


	static void  setPicture()
	{
		e.setPicture();
		

		 BackgroundImage backgroundImage = new BackgroundImage( new Image(Find.class.getResourceAsStream(e.two[0]+".png") ), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
         Background background = new Background(backgroundImage);      
         two[0].setId("img");
        two[0].setBackground(background);
        
        BackgroundImage backgroundImage2 = new BackgroundImage( new Image(Find.class.getResourceAsStream(e.two[1]+".png")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background2 = new Background(backgroundImage2);      
        two[1].setId("img");
       two[1].setBackground(background2);
       
       
    }
	
	
	
	private static void changeScore()
	{
		Sum();
		lab.setText("Score:"+sum+"/96");
		check_delete();
		
	}
	
	
	static void check_delete()
	{
		int br;
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				br=0;
				int pom=j*2;
				int pom2=pom+1;
				if(e.matrix[i][pom]==0) br++;
				if(e.matrix[i][pom2]==0) br++;
				if(br==1)
				{
					BackgroundImage backgroundImage = new BackgroundImage( new Image( Find.class.getResourceAsStream("0.png")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
					Background background = new Background(backgroundImage);
					matrix[i][pom].setBackground(background);
					matrix[i][pom2].setBackground(background);
					e.matrix[i][pom]=0;
	               	e.matrix[i][pom2]=0;
				}
			}
		}
	}



}   

