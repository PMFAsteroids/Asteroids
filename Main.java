package application;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.*;
import javafx.geometry.Dimension2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Main extends Application {

	int x, y; // these are all new variables

	int xVelocity, yVelocity; // velocity variables store the

	private boolean paused;

	// game
	private Stage window;
	private Pane pane;
	static Canvas canvas;
	static GraphicsContext gc; // used to draw on the back buffer
	private int width = 900, height = 900;
	private static Label score;

	// za brod
	private Ship ship;
	static AnimationTimer timer;
	private int brZivota, rezultat = 0;
	// true je ako je ranjiv to je na samom pocetku nivoa, i kada izgubi zivot
	// pa se kreira novi brod
	private static boolean ranjiv;

	// za pucanje
	Shot[] shots = new Shot[41]; // niz metaka
	int numShots; // broj metaka u nizu
	boolean shooting; // da li brod trenutno puca, true ako da

	// asteroidi
	private Asteroid[] asteroids = new Asteroid[100]; // niz asteroida
	private int numAsteroids; // koliko trenutno asteroida ima u nizu
	public static double astRadius, minAstVel, maxAstVel; // vrednosti koje se
															// koriste za
															// kreiranje
															// asteroida
	private int astNumHits, astNumSplit;
	private static int level; // trenutni nivo na kom je igrac

	// zvezda
	Image star;
	ImageView starView;

	// za bonus igre
	private static boolean pobeda;

	// ufo niz
	Ufo[] Ufo = new Ufo[5];
	String ufomp3 = "ufo.mp3";
	Media ufoZvuk = new Media(new File(ufomp3).toURI().toString());
	MediaPlayer ufoPlayer = new MediaPlayer(ufoZvuk);


	// timer za ufo
	private static final Integer STARTTIME = 31;

	private static final int IntegerProperty = 0;
	private Timeline timeline;
	private Label timerLabel = new Label();
	public IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

	public void start(Stage primaryStage) {
		try {
			Pane root = new Pane();
			root.setId("pane");
			Scene scene = new Scene(root, width, height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("ASTEROIDS");
			InnerShadow is = new InnerShadow(); /* za senku slova */
			is.setOffsetX(4.0f); /* kakva je senka */
			is.setOffsetY(4.0f);

			Label l = new Label("ASTEROIDS");
			l.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 100));
			l.setId("tekst"); /* id zbog css-a */
			l.setEffect(is); /* dodavanje senke */
			l.setLayoutX(width /4 - 50); /* pozicija labele u odnosu na x osu */
			l.setLayoutY(height / 6);/* pozicija labele u odnosu na y osu */
			root.getChildren()
					.add(l); /*
								 * nije borderPane jer onda mora da se naglasi
								 * top,centar itd zato je samo pane
								 */

			Button b1 = new Button("   ONE PLAYER  ");
			b1.setId("btn"); /* id se stavlja zbog cssa */
			b1.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
			b1.setLayoutX(width / 3 + 15); /* u odnosu na x osu */
			b1.setLayoutY(height / 3 + 10); /* u odnosu na y osu */
			b1.setOnAction(
					new EventHandler<ActionEvent>() { /* da pozove tutorijal */

						@Override
						public void handle(ActionEvent event) {
							start1(primaryStage);
						}
					});
			root.getChildren().add(b1);
			Button b2 = new Button("TWO PLAYERS");
			b2.setId("btn");
			b2.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
			b2.setLayoutX(width / 2 - 135);
			b2.setLayoutY(height / 2 - 50); // dole
			root.getChildren().add(b2);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* pocetak tutorijala za jednog igraca */
	public void start1(Stage primaryStage) {
		try {
			Pane root = new Pane();
			root.setId("pane");
			Scene scene = new Scene(root, width, height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("ASTEROIDS");

			InnerShadow is = new InnerShadow(); /* za senku slova */
			is.setOffsetX(4.0f); /* kakva je senka */
			is.setOffsetY(4.0f);
			Label l = new Label("Tutorial for one player");
			l.setFont(Font.font("Century Gothic", FontWeight.BOLD, 45));
			l.setId("tekst"); /* id zbog css-a */
			l.setEffect(is);
			l.setLayoutX(width / 2 - 220); /* pozicija labele u odnosu na x osu */
			l.setLayoutY(height/4 - 120);/* pozicija labele u odnosu na y osu */
			root.getChildren()
					.add(l); /*
								 * nije borderPane jer onda mora da se naglasi
								 * top,centar itd zato je samo pane
								 */

			Label l2 = new Label(
					"       Use the left and right arrow keys to rotate your ship.\n             Push the up arrow key to use your thrusters. \n                              Press SPACE to fire.");
			l2.setFont(Font.font("Century Gothic", FontWeight.BOLD, 25));
			l2.setId("tekst");
			l2.setLayoutX(width / 2 - 360); /* pozicija labele u odnosu na x osu */
			l2.setLayoutY(height/4 - 50);
			root.getChildren().add(l2); /*
								 * nije borderPane jer onda mora da se naglasi
								 * top,centar itd zato je samo pane
								 */

			/* slika */
			Image image = new Image(Main.class.getResourceAsStream(
					"strelice.jpg")); /* slika sa strelicama koju dodajem */
			ImageView iv = new ImageView();
			iv.setId("sl");
			iv.setImage(image);
			iv.setFitWidth(350); /* sirina slike */
			iv.setFitHeight(150); /* visina slike */
			iv.setLayoutX(width / 2 - 190);
			iv.setLayoutY(height/4 + 70);
			root.getChildren().add(iv);

			/* dugmici za skip i next */
			Button b3 = new Button("Skip");
			b3.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					startGame(primaryStage);

				}
			});
			Button b4 = new Button("Next");
			b4.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					start2(primaryStage);
				}
			});
			InnerShadow is1 = new InnerShadow(); /* za senku slova */
			is1.setOffsetX(4.0f); /* kakva je senka */
			is1.setOffsetY(4.0f);
			b3.setId("btn");
			b3.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
			b3.setLayoutX(width/2 + 80); /* u odnosu na x osu */
			b3.setLayoutY(530); /* u odnosu na y osu */
			b4.setId("btn");
			b4.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
			b4.setLayoutX(width/2 + 200); /* u odnosu na x osu */
			b4.setLayoutY(530); /* u odnosu na y osu */

			root.getChildren().add(b3);
			root.getChildren().add(b4);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* drugi deo tutorijala za jednog igraca */
	public void start2(Stage primaryStage) {
		try {
			Pane root = new Pane();
			root.setId("pane");
			Scene scene = new Scene(root, width, height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("ASTEROIDS");

			InnerShadow is = new InnerShadow(); /* za senku slova */
			is.setOffsetX(4.0f); /* kakva je senka */
			is.setOffsetY(4.0f);
			Label l = new Label("Points");
			l.setFont(Font.font("Century Gothic", FontWeight.BOLD, 45));
			l.setId("tekst"); /* id zbog css-a */
			l.setEffect(is);
			l.setLayoutX(width / 2 - 150); /* pozicija labele u odnosu na x osu */
			l.setLayoutY(height/4 - 120);/* pozicija labele u odnosu na y osu */
			root.getChildren()
					.add(l); /*
								 * nije borderPane jer onda mora da se naglasi
								 * top,centar itd zato je samo pane
								 */

			/*
			 * najveci kamen-20, srednji kamen-50, najmanji kamen-100, brod-200
			 */
			Label l3 = new Label(
					"The biggest asteroid - 20 points \n The middle asteroid - 50 points \n The smallest asteroid - 100 points \n The spaceship - 200 points");
			l3.setFont(Font.font("Century Gothic", FontWeight.BOLD, 30));
			l3.setId("tekst");
			l3.setLayoutX(width / 2 - 300); /* pozicija labele u odnosu na x osu */
			l3.setLayoutY(height/4 - 50);
			root.getChildren().add(l3);

			Label l2 = new Label(
					"You have three lives.\n You lose one life when asteroid hits your ship or when \n other ship hits you. When you hit the star you have the \n opportunity to play a bonus level that if you cross brings \n you one life.");
			l2.setFont(Font.font("Century Gothic", FontWeight.BOLD, 25));
			l2.setId("tekst");
			l2.setLayoutX(width / 2 - 300); /* pozicija labele u odnosu na x osu */
			l2.setLayoutY(height/4 + 120);
			root.getChildren()
					.add(l2); /*
								 * nije borderPane jer onda mora da se naglasi
								 * top,centar itd zato je samo pane
								 */

			/* dugme za start */

			Button b5 = new Button("Start");
			b5.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					startGame(primaryStage);
				}
			});
			InnerShadow is1 = new InnerShadow(); /* za senku slova */
			is1.setOffsetX(4.0f); /* kakva je senka */
			is1.setOffsetY(4.0f);

			b5.setId("btn");
			b5.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
			b5.setLayoutX(width / 2 + 240); /* u odnosu na x osu */
			b5.setLayoutY(height/4 + 400); /* u odnosu na y osu */

			root.getChildren().add(b5);

			/* slika */
			Image image = new Image(Main.class.getResourceAsStream(
					"stars.png")); /* slika sa strelicama koju dodajem */
			ImageView iv = new ImageView();
			iv.setId("sl");
			iv.setImage(image);
			iv.setFitWidth(200); /* sirina slike */
			iv.setFitHeight(60); /* visina slike */
			iv.setLayoutX(width / 2 - 300);
			iv.setLayoutY(height/2 + 100);
			root.getChildren().add(iv);

			Image image1 = new Image(Main.class.getResourceAsStream(
					"srce.png")); /* slika sa strelicama koju dodajem */
			ImageView iv1 = new ImageView();
			iv1.setId("sl");
			iv1.setImage(image1);
			iv1.setFitWidth(60); /* sirina slike */
			iv1.setFitHeight(60); /* visina slike */
			iv1.setLayoutX(width / 2 - 10);
			iv1.setLayoutY(height/2 + 100);
			root.getChildren().add(iv1);

			Label l4 = new Label("- stars");
			l4.setFont(Font.font("Century Gothic", FontWeight.BOLD, 25));
			l4.setId("tekst");
			l4.setLayoutX(width / 2 - 100); /* pozicija labele u odnosu na x osu */
			l4.setLayoutY(height/2 + 110);
			root.getChildren().add(l4);

			Label l5 = new Label("- life");
			l5.setFont(Font.font("Century Gothic", FontWeight.BOLD, 25));
			l5.setId("tekst");
			l5.setLayoutX(width / 2 + 50); /* pozicija labele u odnosu na x osu */
			l5.setLayoutY(height/2 + 110);
			root.getChildren().add(l5);

			Image image2 = new Image(Main.class.getResourceAsStream(
					"brod.png")); 
			ImageView iv2 = new ImageView();
			iv2.setId("sl");
			iv2.setImage(image2);
			iv2.setFitWidth(60); /* sirina slike */
			iv2.setFitHeight(60); /* visina slike */
			iv2.setLayoutX(width / 2 + 100);
			iv2.setLayoutY(height/2 + 100);
			root.getChildren().add(iv2);

			Label l6 = new Label("- spaceship");
			l6.setFont(Font.font("Century Gothic", FontWeight.BOLD, 25));
			l6.setId("tekst");
			l6.setLayoutX(width / 2 + 150); /* pozicija labele u odnosu na x osu */
			l6.setLayoutY(height/2 + 110);
			root.getChildren().add(l6);

			Label l7 = new Label("P - pause");
			l7.setFont(Font.font("Century Gothic", FontWeight.BOLD, 25));
			l7.setId("tekst");
			l7.setLayoutX(width / 2 + 300); /* pozicija labele u odnosu na x osu */
			l7.setLayoutY(height/2 + 110);
			root.getChildren().add(l7);

			
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* kada izgubi */
	/* zavrsena samo nije nigde pozvana */
	public void izgubio(Stage primaryStage) {
		try {

			Pane root = new Pane();
			root.setId("izgubio");
			Scene scene = new Scene(root, width, height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("ASTEROIDS");
			InnerShadow is = new InnerShadow(); /* za senku slova */
			is.setOffsetX(4.0f); /* kakva je senka */
			is.setOffsetY(4.0f);
			Label l = new Label();

			if (ship.getBrZivota() == 0)
				l.setText("Game over!");

			else if (level > 5)
				l.setText("Congratulations");

			l.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 70));
			l.setId("tekst"); /* id zbog css-a */
			l.setEffect(is);
			l.setLayoutX(240); /* pozicija labele u odnosu na x osu */
			l.setLayoutY(110);/* pozicija labele u odnosu na y osu */
			root.getChildren().add(l);

			int br;

			if (level == 6)
				br = 5;

			else
				br = level;

			Label l1 = new Label("Level: " + br + "\nYour result: " + rezultat);
			l1.setFont(Font.font("Century Gothic", FontWeight.BOLD, 50));
			/* id zbog css-a */
			l1.setLayoutX(240); /* pozicija labele u odnosu na x osu */
			l1.setLayoutY(240);/* pozicija labele u odnosu na y osu */
			l1.setTextFill(Color.LIGHTGRAY);
			root.getChildren().add(l1);
			Button b = new Button("Play again");
			b.setId("btn");
			b.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
			b.setLayoutX(400);
			b.setLayoutY(400); // dole

			b.setOnAction(
					new EventHandler<ActionEvent>() { /* da pozove tutorijal */

						@Override
						public void handle(ActionEvent event) {
							start(primaryStage);
						}
					});

			root.getChildren().add(b);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*pobednik*/
	public void pobeda(Stage primaryStage) {
		try {

			Pane root = new Pane();
			root.setId("izgubio");
			Scene scene = new Scene(root, width, height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("ASTEROIDS");
			InnerShadow is = new InnerShadow(); /* za senku slova */
			is.setOffsetX(4.0f); /* kakva je senka */
			is.setOffsetY(4.0f);
			Label l = new Label();

			if (ship.getBrZivota() == 0)
				l.setText("Game over!");

			else if (level > 5)
				l.setText("Congratulations");

			l.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 70));
			l.setId("tekst"); /* id zbog css-a */
			l.setEffect(is);
			l.setLayoutX(240); /* pozicija labele u odnosu na x osu */
			l.setLayoutY(110);/* pozicija labele u odnosu na y osu */
			root.getChildren().add(l);

			int br;

			if (level == 6)
				br = 5;

			else
				br = level;

			Label l1 = new Label("Level: " + br + "\nYour result: " + rezultat);
			l1.setFont(Font.font("Century Gothic", FontWeight.BOLD, 50));
			/* id zbog css-a */
			l1.setLayoutX(240); /* pozicija labele u odnosu na x osu */
			l1.setLayoutY(240);/* pozicija labele u odnosu na y osu */
			l1.setTextFill(Color.LIGHTGRAY);
			root.getChildren().add(l1);
			Button b = new Button("Play again");
			b.setId("btn");
			b.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 30));
			b.setLayoutX(400);
			b.setLayoutY(400); // dole

			b.setOnAction(
					new EventHandler<ActionEvent>() { /* da pozove tutorijal */

						@Override
						public void handle(ActionEvent event) {
							start(primaryStage);
						}
					});

			root.getChildren().add(b);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Scene scene;
	static int ind = 0;

	// zvezda
	Star s = null;
	boolean bonus;

	public void startGame(Stage primaryStage) {
		paused = false; // igra nije pauzirana

		ranjiv = false;
		rezultat = 0;
		brZivota = 3;
		bonus = false;

		// asteroidi
		numAsteroids = 0;
		level = 0; // uvecace se za 1 kada prvi nivo bude postavljen
		// vrednosti za asteroide
		astRadius = 60;
		// brzina, min i max
		minAstVel = 1;
		maxAstVel = 3;

		astNumHits = 3;
		astNumSplit = 2;

		window = primaryStage;
		window.setTitle("Asteroids");

		pane = new Pane();
		pane.setId("game");

		// CANVAS
		canvas = new Canvas(width, height);
		gc = canvas.getGraphicsContext2D();
		pane.getChildren().add(canvas);

		scene = new Scene(pane, width, height);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		window.setScene(scene);
		window.show();

		// Rezultat, nivo

		score = new Label();
		score.setText("Score: " + rezultat);
		score.setId("tekst");
		score.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 25));
		score.setLayoutX(10);
		score.setLayoutY(10);

		pane.getChildren().add(score);

		ship = new Ship(width / 2, height / 2, 60, .15, .98, .1, 15, brZivota);

		// muzika - metak,pogodak zvezde
		String metak = "bullet.mp3";
		Media bullet = new Media(new File(metak).toURI().toString());
		MediaPlayer bulletPlayer = new MediaPlayer(bullet);

		String zvezda = "star.mp3";
		Media zvezdaZvuk = new Media(new File(zvezda).toURI().toString());
		MediaPlayer zvezdaPlayer = new MediaPlayer(zvezdaZvuk);

		// slike
		Image brod = new Image(Main.class.getResourceAsStream("brod.png"));
		Image space = new Image(Main.class.getResourceAsStream("svemir.png"));
		star = new Image(Main.class.getResourceAsStream("star0.png"));

		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {

				// svemir

				gc.drawImage(space, 0, 0);

				gc.setFill(Color.WHITE);
				gc.setStroke(Color.WHITE);

				// funkcija koja proverava da li je upucan ufo i da li je vreme
				// isteklo
				CheckCollisionUfo();

				// ako je igrac razbio sve asteroide predji na sledeci nivo
				if (numAsteroids <= 0) {

					bonus=false;
					zvezdaPlayer.stop();

					// mora da se ocisti sve jer ne znamo da li je zvezda
					// pogodjena ili nije
					pane.getChildren().clear();
				//	gc = canvas.getGraphicsContext2D();
					pane.getChildren().add(canvas);
					pane.getChildren().add(score);

					if (ind == 2) {

						bonus = true;
						s = null;
						pane.getChildren().clear();

						Random rand = new Random();
						// 0-find, 1-memorija, 2-cubes
						int br = rand.nextInt(3);

						if (br == 0) {
							Find find = new Find();
							find.startFind(primaryStage, pane, scene);

						} else if (br == 1) {
							Memory memory = new Memory(pane, scene);
							memory.startMemory(primaryStage);
						} else {
							Cubes cubes = new Cubes();
							cubes.startCubes(primaryStage, pane, scene);
							cubes.Check(pane);
						}
						timer.stop();

					}

					if (ind == 3) {
						if (pobeda == true) {
							brZivota++;
							// System.out.println("Crtam broj zivota");

							double x = 10, y = 20;

							String s = "zivot" + brZivota + ".png";
							Image zivot = new Image(Main.class.getResourceAsStream(s));

							gc.drawImage(zivot, x, y);

						}

						numAsteroids = 0;

						pane.setId("game");
						window.setScene(scene);
						window.show();
						gc.drawImage(space, 0, 0);

						gc.setFill(Color.WHITE);
						gc.setStroke(Color.WHITE);
						bonus = true;

					}
					
					setUpNextLevel();

					Label label = new Label();
					if (ind == 2)
						label.setText("Bonus level");

					else
						label.setText("Level " + level);

					
					if(level==1 || level==5) ind=-1;
					else
					{
						ind=0;
						bonus=false;
					}
					
					paused = false;

					// postavlja lab na sredini i nakon 2 sekunde brise tekst

					label.setId("lf");
					FadeTransition fader = createFader(label);
					label.setLayoutX(
							380); /* pozicija labele u odnosu na x osu */
					label.setLayoutY(
							300);/* pozicija labele u odnosu na y osu */
					pane.getChildren().add(label);

					fader.play();

				}

				if (!paused) {
					ship.draw(gc);
					ship.move(width, height);

					// ako ima zivota da iscrta ostatak
					if (brZivota <= 0 || level > 5) {
						izgubio(primaryStage);
						timer.stop();
					} else {
						// System.out.println("Crtam broj zivota");

						double x = 10, y = 50;

						if(pobeda==true)
						{
							pobeda=false;
							brZivota++;
						}
						
						String s = "zivot" + brZivota + ".png";
						Image zivot = new Image(Main.class.getResourceAsStream(s));

						gc.drawImage(zivot, x, y);

					}

					// shots
					for (int i = 0; i < numShots; i++) {
						shots[i].draw(gc);

						// pomera metke
						shots[i].move(width, height);

						// brise metke ako su otisli predaleko a nisu nista
						// pogodili
						if (shots[i].getLifeLeft() <= 0) {

							deleteShot(i);
							i--;
						}
					}

					for (int i = 0; i < asteroids.length; i++) {
						if (asteroids[i] != null) {
							asteroids[i].draw(gc);

						}

					}

					try {
						updateAsteroids();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					score.setText("Score " + rezultat);

					if(level==3) bonus=false;
					
					if (rezultat > level*2000 && ind == 0 && bonus == false) {
						s = new Star(width, height, pane);

						ind = 1;
					}

					// ind je promenljiva koja sluzi za zvezdu, kada je ind=0 to
					// znaci da se zvezda nije pojavljivala na tekucem nivou
					// ind=1 znaci da je ispunjen uslov za pojavu zvezde
					// ind=2 znaci da je neki metak pogodio zvezdu, pri cemu se
					// onda pokrece animacija i zvezda nestaje, a po
					// zavrsetku tekuceg nivoa se pojavljuje bonus nivo
					if (ind == 1) {

						// shots
						for (int i = 0; i < numShots; i++) {
	//						shots[i].draw(gc);

							// pomera metke
							shots[i].move(width, height);

							// brise metke ako su otisli predaleko a nisu nista
							// pogodili
							if (shots[i].getLifeLeft() <= 0) {

								deleteShot(i);
								i--;
							}
						}

						s.draw(gc);

						for (int j = 0; j < numShots; j++) {
							if (s.shotCollision(shots[j])) {

								deleteShot(j);
								zvezdaPlayer.play();
								s.Efect();
								ind = 2;
							}

						}

					}

					if (shooting && ship.daLiMoguDaPucam()) {
						shots[numShots] = ship.shoot();
						numShots++;
					}

				}
			}

		};

		timer.start();

		primaryStage.show();

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				// kada nije pauza brod moze da se pomera
				if (paused == false) {
					if (e.getCode() == KeyCode.UP) {
						// System.out.println("gore");
						ship.setUbrzati(true);

					} else if (e.getCode() == KeyCode.LEFT) {
						// System.out.println("levo");
						ship.setTurningLeft(true);
					} else if (e.getCode() == KeyCode.RIGHT) {
						// System.out.println("desno");
						ship.setTurningRight(true);
					} else if (e.getCode() == KeyCode.SPACE) {
						// System.out.println("space");
						bulletPlayer.play();
						shooting = true;

					}
				}
				if (e.getCode() == KeyCode.P) {
					// PAUZA
					if (paused == false) {
						paused = true;
						ship.setPauza(true);
						timer.stop();

					} else {
						// NORMALNA IGRA
						paused = false;
						ship.setPauza(false);
						timer.start();
					}
				}
			}

		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {

				if (e.getCode() == KeyCode.UP)
					ship.setUbrzati(false);

				else if (e.getCode() == KeyCode.LEFT)
					ship.setTurningLeft(false);

				else if (e.getCode() == KeyCode.RIGHT)
					ship.setTurningRight(false);

				else if (e.getCode() == KeyCode.SPACE) {
					shooting = false;
					bulletPlayer.stop();
				}
			}
		});

	}

	private void CheckCollisionUfo()
	{

		if (timeSeconds.intValue() > 0)
		{
			for (int i = 0; i < Ufo.length; i++) 
			{
				for (int j = 0; j < numShots; j++) 
				{
					if (Ufo[i] != null && Ufo[i].shotCollision(shots[j]))
					{
						ufoPlayer.stop();

						ufoPlayer.play();
						
						Ufo[i].upucan(pane);
						Ufo[i] = null;
					}
				}
			}

		} else if (timeSeconds.intValue() == 0) {
			int sum = 0;
			for (int i = 0; i < Ufo.length; i++) {
				if (Ufo[i] != null) {
					sum++;
					Ufo[i].upucan(pane);
					Ufo[i] = null;
				}

			}
			if (sum > 0) {
				for (int i = 0; i < 20; i++) {
					Random rand = new Random();

					x = rand.nextInt(900);
					y = rand.nextInt(900);
					Asteroid a = new Asteroid(x, y, 5, 1, 1, 1, 1);
					addAsteroid(a);
				}

			}
			timeSeconds.setValue(-1);
			// da nestane time
			FadeTransition fader = createFader2(timerLabel);
			fader.play();

		}

	}

	protected void prozor() {

		pane = new Pane();
		pane.setId("p");

		// CANVAS
		canvas = new Canvas(width, height);
		gc = canvas.getGraphicsContext2D();
		pane.getChildren().add(canvas);

		Scene scene = new Scene(pane, width, height);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		window.setScene(scene);
		window.show();

	}

	private void deleteShot(int index) {
		// delete shot and move all shots after it up in the array
		numShots--;

		for (int i = index; i < numShots; i++)
			shots[i] = shots[i + 1];

		shots[numShots] = null;
	}

	// fja za sledeci nivo
	public void setUpNextLevel() {
		double w, h;

		// System.out.println("usao sam");
		level++;
		
		
		// podesavamo za ufo, crtamo ih i postavljamo timer
		if (level == 5) 
		{
			for (int i = 0; i < 5; i++) 
			{
				Ufo[i] = new Ufo(pane, width, height);	
			}

			
			timerLabel.textProperty().bind(timeSeconds.asString());
			timerLabel.setId("l");
			timerLabel.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 3));

			timeSeconds.set(31);
			timeline = new Timeline();
			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(30+ 1), new KeyValue(timeSeconds, 0)));

			// timer
						FadeTransition fader = createFader3(timerLabel);
						fader.play();
			timeline.playFromStart();

			timerLabel.setLayoutX(780); /* pozicija labele u odnosu na x osu */
			timerLabel.setLayoutY(30);/* pozicija labele u odnosu na y osu */

			pane.getChildren().add(timerLabel);
			Check();

		}
		else if (level == 4) 
		{
			for (int i = 0; i < 2; i++) 
			{
				Ufo[i] = new Ufo(pane, width, height);	
			}

			// timer
			timerLabel.textProperty().bind(timeSeconds.asString());
			timerLabel.setId("l");
			timerLabel.setFont(Font.font("Showcard Gothic", FontWeight.BOLD, 3));

			timeSeconds.set(STARTTIME);
			timeline = new Timeline();
			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(STARTTIME + 1), new KeyValue(timeSeconds, 0)));

			timeline.playFromStart();

			timerLabel.setLayoutX(780); /* pozicija labele u odnosu na x osu */
			timerLabel.setLayoutY(30);/* pozicija labele u odnosu na y osu */

			pane.getChildren().add(timerLabel);
			Check();

		}

		// kreiramo novi brod koji se nalazi u centru prozora
		ship = new Ship(width / 2, height / 2, 60, .15, .98, .1, 15, brZivota);

		numShots = 0; // nema metaka na ekranu na pocetku nivoa
		paused = false; // igrica je u toku nije pauzirana
		shooting = false; // i brod jos uvek ne puca

		// create an array large enough to hold the biggest number
		// of asteroids possible on this level (plus one because
		// the split asteroids are created first, then the original
		// one is deleted). The level number is equal to the
		// number of asteroids at it's start.

		asteroids = new Asteroid[(level + 20) * (int) Math.pow(astNumSplit, astNumHits - 1) + 30];
		numAsteroids = level+3;

		
		if(level==1) numAsteroids -=1;
		if(level==3) numAsteroids += 2;
		
		// kreiramo asteroide sa nasumicnim rasporedom na ekranu
		for (int i = 0; i < numAsteroids; i++) {
			w = Math.random() * width;
			h = Math.random() * height;

			while (Math.abs(w - ship.getX()) < 1 && Math.abs(h - ship.getY()) < 1) {
				w = Math.random() * width;
				h = Math.random() * height;
			}

			asteroids[i] = new Asteroid(w, h, astRadius, minAstVel, maxAstVel, astNumHits, astNumSplit);

		}
	}


	
	
	// brisem asteroid iz niza
	private void deleteAsteroid(int index) {

		numAsteroids--;

		for (int i = index; i < numAsteroids; i++)
			asteroids[i] = asteroids[i + 1];

		asteroids[numAsteroids] = null;
	}

	// dodajem asteroid u niz
	private void addAsteroid(Asteroid ast) {

		/*
		 * if(asteroids[numAsteroids]==null) System.out.println("Numasteroids "
		 * + numAsteroids + " NULL Je"); else System.out.println("Numasteroids "
		 * + numAsteroids + " nIje null");
		 */
		asteroids[numAsteroids] = ast;

		numAsteroids++;
	}

	private void updateAsteroids() throws InterruptedException {
		// System.out.println("update Asteroids");
		double w, h;

		for (int i = 0; i < numAsteroids; i++) {
			asteroids[i].move(width, height);

			if (asteroids[i].shipCollision(ship) && ranjiv) {
				brZivota--;

				ship = new Ship(width / 2, height / 2, 60, .15, .98, .1, 15, brZivota);

				ranjiv = false;

				// menja broj zivota
				Label label = new Label("Lives: " + brZivota);
				// label.setId("lf");
				FadeTransition fader = createFader(label);
				label.setLayoutX(850); /* pozicija labele u odnosu na x osu */
				label.setLayoutY(30);/* pozicija labele u odnosu na y osu */
				pane.getChildren().add(label);

				fader.play();

				return;
			}

			// da li je pogodjen neki asteroid
			for (int j = 0; j < numShots; j++) {
				if (asteroids[i].shotCollision(shots[j])) {
					// ako je metak pog odio neki asteroid obrisi ga iz niza
					deleteShot(j);

					rezultat += asteroids[i].getScore();

					// podeli asteroid
					if (asteroids[i].getHitsLeft() > 1) {
						for (int k = 0; k < asteroids[i].getNumSplit(); k++)
							addAsteroid(asteroids[i].createSplitAsteroid(minAstVel, maxAstVel));
					}

					// obrisi originalan(stari) asteroid
					deleteAsteroid(i);
					j = numShots; // break out of inner loop - it has
					// already been hit, so dont need to check
					// for collision with other shots

					i--; // dont skip asteroid shifted back into
					// the deleted asteroid's position
				}
			}
		}
	}

	private static void Check() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000)));
		timeline.play();
	}

	// funkcija za fading text
	static FadeTransition createFader(Node node) {
		FadeTransition fade = new FadeTransition(Duration.seconds(3), node);
		fade.setFromValue(1);
		fade.setToValue(0);

		fade.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ranjiv = true;
			}
		});
		return fade;
	}

	// funkcija za fading text, brzo,
	static FadeTransition createFader2(Node node) {
		FadeTransition fade = new FadeTransition(Duration.seconds(0.2), node);
		fade.setFromValue(1);
		fade.setToValue(0);
		return fade;
	}
	

	// funkcija za vracanje iz fading u normalno
		static FadeTransition createFader3(Node node) {
			FadeTransition fade = new FadeTransition(Duration.seconds(0.2), node);
			fade.setFromValue(0);
			fade.setToValue(1);
			return fade;
		}


	public static int getLevel() {
		return level;
	}

	public static Label getScore() {
		return score;
	}

	public static void setRanjiv(boolean b) {

		ranjiv = b;
	}

	public static void setPobeda(boolean p) {
		pobeda = p;
	}

	public static void main(String[] args) {
		launch(args);

	}

}
