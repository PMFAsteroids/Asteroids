package application;

import java.io.File;
import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Star {
	private double x, y, radius;
	private int z;
	ImageView starView;
	Image image;

	public Star(double w, double h, Pane p) {
		// random pozicija za zvezdu
		Random rand = new Random();
		x = Math.random() * (w - 200);
		y = Math.random() * (h - 200);

		// za boju zvezde
		z = rand.nextInt(3);

		radius = 30;

		// random bonus nivo->random zvezda
		image = new Image(Main.class.getResourceAsStream("star" + z + ".png"));
		starView = new ImageView();
		starView.setImage(image);
		starView.setLayoutX(x);
		starView.setLayoutY(y);

		p.getChildren().add(starView);
	}

	public boolean shotCollision(Shot shot) {
		
		if (Math.pow(180, 2) > Math.pow(shot.getX() - x, 2) + Math.pow(shot.getY() - y, 2))
			return true;

		return false;
	}

	public void draw(GraphicsContext g) {
		g.drawImage(image, x, y);

	}

	// animacije, kretanje zvezde
	public void Efect() {

		FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), starView);
		fadeTransition.setFromValue(1.0f);
		fadeTransition.setToValue(0.3f);
		fadeTransition.setCycleCount(1);
		fadeTransition.setAutoReverse(true);

		FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(500), starView);
		fadeTransition2.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				starView.setImage(null);

			}

		});

		RotateTransition rotateTransition = new RotateTransition(Duration.millis(2000), starView);
		rotateTransition.setByAngle(180f);
		rotateTransition.setCycleCount(2);
		rotateTransition.setAutoReverse(true);

		SequentialTransition sequentialTransition = new SequentialTransition();

		sequentialTransition.getChildren().addAll(

				rotateTransition, fadeTransition, fadeTransition2

		);
		sequentialTransition.setCycleCount(2);
		sequentialTransition.setAutoReverse(true);

		sequentialTransition.play();
	}

	public int getZ() {
		return z;
	}

}
