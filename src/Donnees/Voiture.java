package Donnees;

public class Voiture {

	private String strCouleur, strLongueur, strPosX, strPosY;
	private EnumOrientation enumOrientation;

	private int intNoVoiture;
	// private int[][] intGrille;

	public Voiture(String strCouleur, String strLongueur, String strPosX, String strPosY,
			EnumOrientation enumOrientation) {
		this.strCouleur = strCouleur;
		this.strLongueur = strLongueur;
		this.strPosX = strPosX;
		this.strPosY = strPosY;
		this.enumOrientation = enumOrientation;
	}

	@Override
	public String toString() {
		return "Voiture [strCouleur=" + strCouleur + ", strLongueur=" + strLongueur + ", strPosX=" + strPosX
				+ ", strPosY=" + strPosY + ", enumOrientation=" + enumOrientation + "]";
	}

	public String getStrPosX() {
		return strPosX;
	}

	public String getStrPosY() {
		return strPosY;
	}

	public String getStrCouleur() {
		return strCouleur;
	}

	public String getStrLongueur() {
		return strLongueur;
	}

	public EnumOrientation getEnumOrientation() {
		return enumOrientation;
	}

	public int getIntNoVoiture() {
		return intNoVoiture;
	}

	public void setIntNoVoiture(int intNoVoiture) {
		this.intNoVoiture = intNoVoiture;
	}

	/*
	 * public int[][] getIntGrille() { return intGrille; } public void
	 * setIntGrille(int[][] intGrille) { this.intGrille = intGrille; }
	 */
}
