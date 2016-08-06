package application;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Random;

import com.sun.javafx.geom.Rectangle;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;

 
public class Ufo 
{
    
    ImageView ufo;
    double  x,y;
    
    Image image;
    
 
    public Ufo(Pane p,int w, int h)
	{
    	image = new Image(Main.class.getResourceAsStream("ufo.png"));
		ufo = new ImageView();
		ufo.setImage(image);

		Random rand = new Random();

		x = Math.random() * (w - 200);
		y = Math.random() * (h - 200);

		ufo.setLayoutX(x);
		ufo.setLayoutY(y);
		p.getChildren().add(ufo);

	}
   
	public void upucan(Pane p) {

		FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(100), ufo);
		fadeTransition2.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0)
			{
				ufo.setImage(null);
				
			}

		});

		fadeTransition2.play();
	}

	//da li je pogodjen
  	public boolean shotCollision(Shot shot)
  	{
  		
  		if(Math.pow(35,2) > Math.pow(shot.getX()-this.x,2)+Math.pow(shot.getY()-this.y,2))
  			return true;
  		
  		return false;
  	}
  	

	
  } 

