package Donnees;

import javafx.scene.image.ImageView;

public class ImageVoiture {

	private ImageView imageView;
	private String strTypeAuto;
	private EnumOrientation enumOrientation;
	private String strCouleur;

	public ImageVoiture(ImageView imageView, String strTypeAuto, EnumOrientation enumOrientation, String strCouleur) {
		this.imageView = imageView;
		this.strTypeAuto = strTypeAuto;
		this.enumOrientation = enumOrientation;
		this.strCouleur = strCouleur;
	}

	@Override
	public String toString() {
		return "ImageVoiture [strTypeAuto=" + strTypeAuto + ", enumOrientation=" + enumOrientation + ", strCouleur="
				+ strCouleur + "]";
	}

	public ImageView getImageView() {
		return imageView;
	}
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public String getStrTypeAuto() {
		return strTypeAuto;
	}

	public EnumOrientation getEnumOrientation() {
		return enumOrientation;
	}

	public String getStrCouleur() {
		return strCouleur;
	}
}
