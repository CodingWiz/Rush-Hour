package Interface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Donnees.EnumOrientation;
import Donnees.ImageVoiture;
import Donnees.Voiture;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Menu extends Application {

	private final String strSource = "src" + System.getProperty("file.separator") + "Fichiers"
			+ System.getProperty("file.separator"),
			strPathFichiersTextes = strSource + "Textes" + System.getProperty("file.separator"),
			strPathFichiersImages = "Fichiers" + System.getProperty("file.separator") + "Images"
					+ System.getProperty("file.separator");

	private final String[] strTextes = { strPathFichiersTextes + "f1.txt", strPathFichiersTextes + "f2.txt",
			strPathFichiersTextes + "f3.txt" };

	private ArrayList<Voiture> arrVoituresFacile, arrVoituresMoyen, arrVoituresDifficile;
	private ArrayList<ImageVoiture> arrImagesVoitures;

	private ImageView[] tbImageViewMini, tbImageViewLogoGrille;
	private Button[] arrBtnMenu;

	private Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// initialisation des variables de session
		arrVoituresFacile = new ArrayList<>();
		arrVoituresMoyen = new ArrayList<>();
		arrVoituresDifficile = new ArrayList<>();

		arrImagesVoitures = new ArrayList<>();

		stage = primaryStage;

		// lecture des fichiers
		for (int i = 0; i < strTextes.length; i++)
			lireFichierText(i);

		// affectation des images dans la array ImagesVoitures et dans les ImagesViews
		for (File file : new File(strSource + "Images").listFiles()) {
			if (file.getName().substring(0, 4).equals("auto"))
				arrImagesVoitures.add(new ImageVoiture(new ImageView(new Image(strPathFichiersImages + file.getName())),
						file.getName().substring(0, 4),
						file.getName().substring(5, 6).equals("H") ? EnumOrientation.H : EnumOrientation.V,
						file.getName().substring(7, file.getName().length() - 4)));

			else if (file.getName().substring(0, 6).equals("camion"))
				arrImagesVoitures.add(new ImageVoiture(new ImageView(new Image(strPathFichiersImages + file.getName())),
						file.getName().substring(0, 6),
						file.getName().substring(7, 8).equals("H") ? EnumOrientation.H : EnumOrientation.V,
						file.getName().substring(9, file.getName().length() - 4)));
		}

		tbImageViewLogoGrille = new ImageView[] { new ImageView(new Image(strPathFichiersImages + "logo.gif")),
				new ImageView(new Image(strPathFichiersImages + "grille.gif")) };
		tbImageViewLogoGrille[1].setId("grille");

		tbImageViewMini = new ImageView[] { new ImageView(new Image(strPathFichiersImages + "mini_facile.png")),
				new ImageView(new Image(strPathFichiersImages + "mini_moyen.png")),
				new ImageView(new Image(strPathFichiersImages + "mini_diff.png")) };
		int intID = 0;
		for (ImageView imageView : tbImageViewMini) {
			imageView.setId(Integer.toString(intID++));

			imageView.setOnMouseClicked(new actionMouseEvent());
			imageView.setOnMouseEntered(new enterMouseEvent());
			imageView.setOnMouseExited(new exitMouseEvent());
		}

		// construction de l'interface graphique
		BorderPane root = new BorderPane();

		TilePane tilePaneLogo = new TilePane();
		tilePaneLogo.setAlignment(Pos.CENTER);
		tilePaneLogo.setPadding(new Insets(15));
		tilePaneLogo.getChildren().add(tbImageViewLogoGrille[0]);

		HBox hBoxPrincipal = new HBox();
		hBoxPrincipal.setAlignment(Pos.CENTER);
		hBoxPrincipal.setSpacing(60);

		arrBtnMenu = new Button[] { new Button("Facile"), new Button("Moyen"), new Button("Difficile") };
		for (int i = 0; i < arrBtnMenu.length; i++) {
			arrBtnMenu[i].setId(Integer.toString(i));
			arrBtnMenu[i].setFont(font());
			arrBtnMenu[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			arrBtnMenu[i].addEventHandler(MouseEvent.MOUSE_CLICKED, new actionMouseEvent());
			arrBtnMenu[i].setOnMouseEntered(new enterMouseEvent());
			arrBtnMenu[i].setOnMouseExited(new exitMouseEvent());
		}

		VBox[] arrVBoxMenu = new VBox[] { new VBox(), new VBox(), new VBox() };
		for (int i = 0; i < arrVBoxMenu.length; i++) {
			arrVBoxMenu[i].setSpacing(15);
			arrVBoxMenu[i].setAlignment(Pos.CENTER);
			arrVBoxMenu[i].getChildren().addAll(tbImageViewMini[i], arrBtnMenu[i]);
		}

		hBoxPrincipal.getChildren().addAll(arrVBoxMenu);

		BorderPane.setAlignment(hBoxPrincipal, Pos.CENTER);
		root.setPadding(new Insets(10));
		root.setTop(tilePaneLogo);
		root.setCenter(hBoxPrincipal);
		root.setBackground(background(new Image(strPathFichiersImages + "jaunenoir.gif")));

		primaryStage.setTitle("Rush Hour - Menu Principal");
		primaryStage.setScene(new Scene(root, 600, 400));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void lireFichierText(int i) {
		BufferedReader brFichier = null;

		try {
			brFichier = new BufferedReader(new FileReader(strTextes[i]));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		try {
			String strLigne;

			while ((strLigne = brFichier.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(strLigne, ",");
				Voiture voiture = new Voiture(st.nextToken().trim(), st.nextToken().trim(), st.nextToken().trim(),
						st.nextToken().trim(),
						(st.nextToken().trim().equals("H") ? EnumOrientation.H : EnumOrientation.V));

				if (i == 0)
					arrVoituresFacile.add(voiture);
				else if (i == 1)
					arrVoituresMoyen.add(voiture);
				else if (i == 2)
					arrVoituresDifficile.add(voiture);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class actionMouseEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			String strID = null;

			if (event.getSource() instanceof ImageView)
				strID = ((ImageView) event.getSource()).getId();
			else if (event.getSource() instanceof Button)
				strID = ((Button) event.getSource()).getId();

			new Jeu(((strID.equals("0")) ? "Facile" : ((strID.equals("1")) ? "Moyen" : "Difficile")),
					tbImageViewLogoGrille,
					((strID.equals("0")) ? arrVoituresFacile
							: ((strID.equals("1")) ? arrVoituresMoyen : arrVoituresDifficile)),
					arrImagesVoitures).show();
			stage.close();
		}
	}

	private class enterMouseEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			if (event.getSource() instanceof ImageView) { // Change la couleur when hover pour les imageview
				ImageView imageView = (ImageView) event.getSource();
				imageView.setEffect(new DropShadow(75, Color.WHITE));

				for (Button button : arrBtnMenu)
					if (button.getId().equals(imageView.getId())) {
						button.setTextFill(Color.RED);
						break;
					}

				/*
				 * if (arrBtnMenu[0].getId().equals(imageView.getId()))
				 * arrBtnMenu[0].setTextFill(Color.RED); else if
				 * (arrBtnMenu[1].getId().equals(imageView.getId()))
				 * arrBtnMenu[1].setTextFill(Color.RED); else if
				 * (arrBtnMenu[2].getId().equals(imageView.getId()))
				 * arrBtnMenu[2].setTextFill(Color.RED);
				 */
			} else if (event.getSource() instanceof Button) { // Change la couleur when hover pour les boutons
				Button button = (Button) event.getSource();
				button.setTextFill(Color.RED);

				for (ImageView imageView : tbImageViewMini)
					if (imageView.getId().equals(button.getId())) {
						imageView.setEffect(new DropShadow(75, Color.WHITE));
						break;
					}

				/*
				 * if (tbImageViewMini[1].getId().equals(button.getId()))
				 * tbImageViewMini[1].setEffect(new DropShadow(75, Color.WHITE)); else if
				 * (tbImageViewMini[2].getId().equals(button.getId()))
				 * tbImageViewMini[2].setEffect(new DropShadow(75, Color.WHITE)); else if
				 * (tbImageViewMini[3].getId().equals(button.getId()))
				 * tbImageViewMini[3].setEffect(new DropShadow(75, Color.WHITE));
				 */
			}
		}
	}

	private class exitMouseEvent implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			if (event.getSource() instanceof ImageView) { // Change la couleur when hover pour les imageview
				ImageView imageView = (ImageView) event.getSource();
				imageView.setEffect(null);

				for (Button button : arrBtnMenu)
					if (button.getId().equals(imageView.getId())) {
						button.setTextFill(Color.BLACK);
						break;
					}

				/*
				 * if (arrBtnMenu[0].getId().equals(imageView.getId()))
				 * arrBtnMenu[0].setTextFill(Color.BLACK); else if
				 * (arrBtnMenu[1].getId().equals(imageView.getId()))
				 * arrBtnMenu[1].setTextFill(Color.BLACK); else if
				 * (arrBtnMenu[2].getId().equals(imageView.getId()))
				 * arrBtnMenu[2].setTextFill(Color.BLACK);
				 */
			} else if (event.getSource() instanceof Button) { // Change la couleur when hover pour les boutons
				Button button = (Button) event.getSource();
				button.setTextFill(Color.BLACK);

				for (ImageView imageView : tbImageViewMini)
					if (imageView.getId().equals(button.getId())) {
						imageView.setEffect(null);
						break;
					}

				/*
				 * if (tbImageViewMini[1].getId().equals(button.getId()))
				 * tbImageViewMini[1].setEffect(null); else if
				 * (tbImageViewMini[2].getId().equals(button.getId()))
				 * tbImageViewMini[2].setEffect(null); else if
				 * (tbImageViewMini[3].getId().equals(button.getId()))
				 * tbImageViewMini[3].setEffect(null);
				 */
			}
		}
	}

	private Background background(Image image) {
		return new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
	}

	private Font font() {
		return Font.font("Serif", FontWeight.BOLD, 15);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
