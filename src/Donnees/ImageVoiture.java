package Donnees;

public class ImageVoiture {

	private String strTypeAuto, strCouleur;
	private EnumOrientation enumOrientation;
	
	public ImageVoiture(String strTypeAuto, EnumOrientation enumOrientation, String strCouleur) {
		this.strTypeAuto = strTypeAuto;
		this.enumOrientation = enumOrientation;
		this.strCouleur = strCouleur;
	}
	
	@Override
	public String toString() {
		return "ImageVoiture [strTypeAuto=" + strTypeAuto + ", enumOrientation=" + enumOrientation
				+ ", strCouleur=" + strCouleur + "]";
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
