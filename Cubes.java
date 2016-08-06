package application;

import java.util.Random;

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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class Cubes 
{
	
	int [] niz = new int [5];
	ImageView iv1;
	int x;
	TextField textField;
	static Label lab2;
	private static final Integer STARTTIME = 61;
	private Timeline timeline;
	static int ind=0;
    private Label timerLabel = new Label();
    static IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
	
    public Cubes()
    {
    }
    

	public void Check(Pane pane)
	{
		Timeline timeline = new Timeline(new KeyFrame(
		        Duration.millis(1000),
		         new KeyValue(timeSeconds, 0)));
		timeline.play();
		
	}

	
		private static void doSomething(Pane pane) 
		{

			while(true)
			{
				if(timeSeconds.intValue()==0)
				{
					
					lab2.setText("You don't have bonus life!");
					lab2.setId("lab");
					lab2.setLayoutX(690); 
					lab2.setLayoutY(600);
					pane.getChildren().add(lab2);
				
				
				
					Button b3=new Button("Next");
					b3.setId("b");
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
					
			}
				
		}


	public void startCubes(Stage primaryStage,Pane pane,Scene scene)
	{
		pane.getChildren().clear();
		pane.setId("pane");
		primaryStage.setScene(scene);
		primaryStage.setTitle("Bonus");
		Stage window = primaryStage;
		window.setTitle("Count cubes");
		
		//timer
		 timerLabel.textProperty().bind(timeSeconds.asString());
		 timerLabel.setId("l");
		 timerLabel.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 25));
		
		
		 timeSeconds.set(STARTTIME);
         timeline = new Timeline();
         timeline.getKeyFrames().add(
                 new KeyFrame(Duration.seconds(STARTTIME+1),
                 new KeyValue(timeSeconds, 0)));
        
         timeline.playFromStart();
        
         
         timerLabel.setLayoutX(780); /*pozicija labele u odnosu na x osu*/
			timerLabel.setLayoutY(30);/*pozicija labele u odnosu na y osu*/
			    
         pane.getChildren().add(timerLabel);
		 
		
		Label label= new Label();
		label.setText("INSTRUCTIONS");
		label.setId("l");
		label.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 50));
		label.setLayoutX(100); 
		label.setLayoutY(35);
		pane.getChildren().add(label);
		
		 
		Button b1=new Button("Submit");
		b1.setId("b");
		b1.setOnAction(new EventHandler<ActionEvent>() 
		{

			@Override
			public void handle(ActionEvent event) 
			{
				if(ind==0)
				CheckNumber(pane);
				b1.setDisable(true);
			}

			
		});
		
		b1.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
	
		pane.getChildren().add(b1);
		

		b1.setLayoutX(800); /* u odnosu na x osu*/ 
		b1.setLayoutY(450);
		
		
		Button b2=new Button("Clear ");
		b2.setOnAction(new EventHandler<ActionEvent>() 
		{
			
			@Override
			public void handle(ActionEvent arg0)
			{
				textField.setText("");
				
			}
		});
		b2.setId("b");
		b2.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
		b2.setLayoutX(600); /* u odnosu na x osu*/ 
		b2.setLayoutY(450);
		pane.getChildren().add(b2);
		
		
		Label lab= new Label();
		lab.setText("Count the number of cubes you think there are and input the number by clicking the number "
				+ "buttons.\nBe careful... There could be cubes that you can't see!");
		lab.setId("lab");
		lab.setLayoutX(75); 
		lab.setLayoutY(95);
		pane.getChildren().add(lab);
		
	
      
        iv1 = new ImageView();
        iv1.setFitWidth(700);
        iv1.setFitHeight(650);
        iv1.setLayoutX(80); /* u odnosu na x osu*/ 
		iv1.setLayoutY(-20);
		setPicture();
        pane.getChildren().add(iv1);
        
        /*
        Label nc = new Label("Number of cubes:");
        TextField textField = new TextField ();
        HBox hb = new HBox();
        hb.getChildren().addAll(nc, textField);
        hb.setSpacing(10);*/
        
        
        Label nc = new Label("Number of cubes:");
        nc.setId("tekst");
        nc.setId("lab");
        textField = new TextField ();
        HBox hb = new HBox();
        hb.getChildren().addAll(nc, textField);
        hb.setSpacing(10);
        
        
        hb.setLayoutX(620);
        hb.setLayoutY(400);
        pane.getChildren().add(hb);
        
        Label lab2= new Label();
		lab2.setText("Count the cubes!");
		lab2.setId("lab");
		lab2.setLayoutX(690); 
		lab2.setLayoutY(350);
		pane.getChildren().add(lab2);
    
	}
			
	
	public void  setPicture()
	{
		niz[0]= 27;
		niz[1]=37;
		niz[2]=28;
		niz[3]=29;
		niz[4]=19;
		
		Random rand= new Random();
		x= rand.nextInt(4);
		
		Image image = new Image(Cubes.class.getResourceAsStream( niz[x]+".png"));
		iv1.setImage(image);
       
    }
	
	
	private void CheckNumber(Pane pane) 
	{
		ind++;
		int pom=0;
		
		if(textField.getText()!="")
			pom=Integer.parseInt(textField.getText());
		
		lab2= new Label("");
		if(niz[x]==pom && timeSeconds.intValue()>0) 
		{
				//POBEDA
				Main.setPobeda(true);
			
				timeline.stop();
				
				lab2.setText("Now, you have one more life!");
				lab2.setId("lab");
				lab2.setLayoutX(690); 
				lab2.setLayoutY(600);
				pane.getChildren().add(lab2);
				timeline.stop();
		}
		
		else if(timeSeconds.intValue()==0)
		{
			//IZGUBIO
			Main.setPobeda(false);
			
			lab2.setText("You don't have bonus life!");
			lab2.setId("lab");
			lab2.setLayoutX(690); 
			lab2.setLayoutY(600);
			pane.getChildren().add(lab2);
		}
		
		
		Button b3=new Button("Next");
		b3.setText("Next");
		b3.setId("b");
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

}
