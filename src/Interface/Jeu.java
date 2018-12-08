package Interface;

import java.util.ArrayList;
import java.util.Optional;

import Donnees.EnumOrientation;
import Donnees.ImageVoiture;
import Donnees.Voiture;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Jeu extends Stage implements Runnable {

	private final BooleanProperty dragModeActiveProperty = new SimpleBooleanProperty(this, "dragModeActive", true);

	private final String strPathFichiersImages = "Fichiers" + System.getProperty("file.separator") + "Images"
			+ System.getProperty("file.separator");

	private final int intPosXInitial = 49, intPosYInitial = 69, intTauxEspaceEntreVoitures = 71,
			intTauxTranslation = 71;

	private ImageView[] tbImageViewLogoGrille;
	private ArrayList<Voiture> arrFichiersVoitures;
	private ArrayList<ImageVoiture> arrImagesVoitures;

	private Thread thread;
	private int intTemps = 0;
	private Label lblTemps;

	private int[][] tbGrille;
	private int intNbDeplacements;
	private Label lblNbDeplacements;

	private Voiture voitureRouge;

	public Jeu(String strDifficulte, ImageView[] tbImageViewLogoGrille, ArrayList<Voiture> arrFichiersVoitures,
			ArrayList<ImageVoiture> arrImagesVoitures) {
		this.tbImageViewLogoGrille = tbImageViewLogoGrille;
		this.arrFichiersVoitures = arrFichiersVoitures;
		this.arrImagesVoitures = arrImagesVoitures;

		this.setOnCloseRequest(e -> {
			e.consume();

			thread.interrupt();
			this.close();
		});

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1200, 800);

		TilePane tilePaneLogo = new TilePane();
		tilePaneLogo.setAlignment(Pos.CENTER);
		tilePaneLogo.setPadding(new Insets(15));
		tilePaneLogo.getChildren().add(tbImageViewLogoGrille[0]);

		HBox hBoxPrincipal = new HBox();
		// hBoxPrincipal.setAlignment(Pos.BASELINE_LEFT);
		hBoxPrincipal.setSpacing(150);

		hBoxPrincipal.getChildren().addAll(vBoxGrille(), vBoxDroite());

		root.setPadding(new Insets(25));
		root.setTop(tilePaneLogo);
		root.setBottom(hBoxPrincipal);
		// root.setAlignment(hBoxPrincipal, Pos.BASELINE_LEFT);
		root.setBackground(background(new Image(strPathFichiersImages + "jaunenoir.gif", 1250, 1260, false, false)));

		this.setTitle("Jeu - " + strDifficulte);
		this.setScene(scene);
		this.setResizable(false);
		this.show();
	}

	private VBox vBoxGrille() {
		VBox vBoxGrille = new VBox();
		vBoxGrille.setSpacing(15);
		// vBoxGrille.setAlignment(Pos.BASELINE_LEFT);

		Pane paneJeu = new Pane();

		// Grille
		Pane paneGrille = new Pane();
		paneGrille.getChildren().add(tbImageViewLogoGrille[1]);

		// Voitures
		Pane paneAutos = new Pane();

		// Initialisation a zero de la grille
		tbGrille = new int[6][6];
		for (int y = 0; y < tbGrille.length; y++)
			for (int x = 0; x < tbGrille.length; x++)
				tbGrille[x][y] = 0;

		// Associe chaque image de voiture a chaque voiture du fichier
		for (int i = 0; i < arrFichiersVoitures.size(); i++)
			for (ImageVoiture imageVoiture : arrImagesVoitures) {
				// pour la voiture image
				String strCouleurImage = imageVoiture.getStrCouleur(), strTypeAutoImage = imageVoiture.getStrTypeAuto();
				EnumOrientation enumOrientationImage = imageVoiture.getEnumOrientation();

				// pour la voiture fichier
				String strCouleurFichier = arrFichiersVoitures.get(i).getStrCouleur(),
						strTypeAutoFichier = arrFichiersVoitures.get(i).getStrLongueur().equals("2") ? "auto"
								: "camion";
				EnumOrientation enumOrientationFichier = arrFichiersVoitures.get(i).getEnumOrientation();

				// verifie si la voitureimage est egal au voiture fichier i
				if (strCouleurImage.equals(strCouleurFichier) && enumOrientationImage == enumOrientationFichier
						&& strTypeAutoImage.equals(strTypeAutoFichier)) {
					arrFichiersVoitures.get(i).setIntNoVoiture(i + 1);

					if (strCouleurFichier.equals("rouge"))
						voitureRouge = arrFichiersVoitures.get(i);

					// la position que va prendre l'image
					int intImagePosX = Integer.parseInt(arrFichiersVoitures.get(i).getStrPosX())
							* intTauxEspaceEntreVoitures + intPosXInitial + intTauxTranslation * 0,
							intImagePosY = Integer.parseInt(arrFichiersVoitures.get(i).getStrPosY())
									* intTauxEspaceEntreVoitures + intPosYInitial + intTauxTranslation * 0;

					// l'image
					imageVoiture.getImageView().setLayoutX(intImagePosX);
					imageVoiture.getImageView().setLayoutY(intImagePosY);

					// remplissage de la grille 6x6 avec les voitures
					if (enumOrientationFichier == EnumOrientation.H) // Orientation horizontale
						// reste des pos horizontales de la voiture
						for (int a = 0; a < Integer.parseInt(arrFichiersVoitures.get(i).getStrLongueur()); a++)
							tbGrille[Integer.parseInt(arrFichiersVoitures.get(i).getStrPosX()) + a] // posX
							[Integer.parseInt(arrFichiersVoitures.get(i).getStrPosY())] = i + 1; // posY
					else // Orientation verticale
							// reste des pos verticales de la voiture
						for (int b = 0; b < Integer.parseInt(arrFichiersVoitures.get(i).getStrLongueur()); b++)
							tbGrille[Integer.parseInt(arrFichiersVoitures.get(i).getStrPosX())] // posX
							[Integer.parseInt(arrFichiersVoitures.get(i).getStrPosY()) + b] = i + 1; // posY

					// tbGrille[4][2] = 2; Pos que la voiture rouge doit se trouver pour avoir un
					// combinaison gagnante. Il faut toutefois verifier [5][2] aussi

					ImageView imageView = (ImageView) makeDraggable(imageVoiture.getImageView());
					imageVoiture.setImageView(imageView);

					// ajout des images de voiture dans l'interface grille
					paneAutos.getChildren().add(imageView); // ajout des images

					// ajout des eventhandlers aux imageviews des voitures pour les deplacer
					/*
					 * TranslateTransition transition = new TranslateTransition();
					 * 
					 * imageVoiture.getImageView().setOnMouseDragged(e -> {
					 * 
					 * });
					 */

					break;
				}
			}

		/*
		 * System.out.println("Configuration initiale"); afficherGrille(tbGrille);
		 * System.out.println(voitureRouge.getIntNoVoiture());
		 */

		paneJeu.getChildren().addAll(paneGrille, paneAutos);

		vBoxGrille.getChildren().addAll(paneJeu);

		return vBoxGrille;
	}

	private static final class DragContext {
		public double mouseAnchorX;
		public double mouseAnchorY;
		public double initialTranslateX;
		public double initialTranslateY;
	}

	private Node makeDraggable(Node node) {
		final DragContext dragContext = new DragContext();
		final Group wrapGroup = new Group(node);

		wrapGroup.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
			public void handle(final MouseEvent mouseEvent) {
				if (dragModeActiveProperty.get()) {
					// disable mouse events for all children
					mouseEvent.consume();
				}
			}
		});

		wrapGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			public void handle(final MouseEvent mouseEvent) {
				if (dragModeActiveProperty.get()) {
					// remember initial mouse cursor coordinates
					// and node position
					dragContext.mouseAnchorX = mouseEvent.getX();
					dragContext.mouseAnchorY = mouseEvent.getY();
					dragContext.initialTranslateX = node.getTranslateX();
					dragContext.initialTranslateY = node.getTranslateY();
				}
			}
		});

		wrapGroup.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			public void handle(final MouseEvent mouseEvent) {
				if (dragModeActiveProperty.get()) {
					// shift node from its initial position by delta
					// calculated from mouse cursor movement
					node.setTranslateX(dragContext.initialTranslateX + mouseEvent.getX() - dragContext.mouseAnchorX);
					node.setTranslateY(dragContext.initialTranslateY + mouseEvent.getY() - dragContext.mouseAnchorY);
				}
			}
		});

		return wrapGroup;
	}

	private VBox vBoxDroite() {
		VBox vBoxDroite = new VBox();
		vBoxDroite.setAlignment(Pos.TOP_CENTER);
		vBoxDroite.setSpacing(75);
		vBoxDroite.setPadding(new Insets(15));
		vBoxDroite.setPrefWidth(375);
		vBoxDroite.setBackground(background(Color.SILVER));

		lblTemps = new Label();
		lblTemps.setTextAlignment(TextAlignment.CENTER);
		lblTemps.setAlignment(Pos.TOP_CENTER);
		lblTemps.setFont(Font.font("Serif", FontWeight.BOLD, FontPosture.REGULAR, 50));
		lblTemps.setTextFill(Color.GREEN);

		thread = new Thread(this);
		thread.start();

		Button btnReinitialiser = new Button("Réinitialiser la grille"), btnRetour = new Button("Retour au menu");

		btnReinitialiser.setMaxWidth(Double.MAX_VALUE);
		btnReinitialiser.setFont(font());
		btnReinitialiser.setAlignment(Pos.CENTER);
		btnReinitialiser.setOnMouseEntered(new enterMouseEvent());
		btnReinitialiser.setOnMouseExited(new exitMouseEvent());
		btnReinitialiser.setOnAction(t -> {
			t.consume();

			intTemps = 0;
			lblTemps.setTextFill(Color.GREEN);

			thread.interrupt();

			thread = new Thread(this);
			thread.start();

			/*
			 * System.out.println("Configuration initiale"); afficherGrille(tbGrille);
			 */
		});

		btnRetour.setMaxWidth(Double.MAX_VALUE);
		btnRetour.setFont(font());
		btnRetour.setAlignment(Pos.CENTER);
		btnRetour.setOnMouseEntered(new enterMouseEvent());
		btnRetour.setOnMouseExited(new exitMouseEvent());
		btnRetour.setOnAction(t -> {
			t.consume();

			retour();
		});

		vBoxDroite.getChildren().addAll(lblTemps, btnReinitialiser, btnRetour);

		return vBoxDroite;
	}

	private class enterMouseEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			if (event.getSource() instanceof Button)
				((Button) event.getSource()).setTextFill(Color.RED);
		}
	}

	private class exitMouseEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			if (event.getSource() instanceof Button)
				((Button) event.getSource()).setTextFill(Color.BLACK);
		}
	}

	private Background background(Image image) {
		return new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
	}

	private Background background(Color color) {
		return new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
	}

	private Border border(Color color) {
		return new Border(new BorderStroke(color,
				new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.BEVEL, StrokeLineCap.BUTT, 10, 0, null),
				CornerRadii.EMPTY, new BorderWidths(2)));
	}

	private Font font() {
		return Font.font("Serif", FontWeight.BOLD, 25);
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Platform.runLater(() -> {
					intTemps++;

					lblTemps.setText(String.format("%02d:%02d", (intTemps % 3600) / 60, intTemps % 60));
				});

				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void retour() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Êtes-vous sûr de vouloir retourner au menu principal ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			thread.interrupt();
			this.close();

			try {
				new Menu().start(new Stage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void afficherGrille(int[][] tbGrille) {
		System.out.println("-----------------------");
		for (int y = 0; y < tbGrille.length; y++) {
			for (int x = 0; x < tbGrille.length; x++)
				System.out.print(tbGrille[x][y] + " | ");

			System.out.println();
		}
		System.out.println("-----------------------\n\n");
	}
}
