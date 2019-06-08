import javafx.scene.shape.*;
import javafx.scene.paint.Color;

public class Bubble extends Arc{
	public double x; //coordonnée x
	public double y; //coordonnée y
	Arc a;	//shape rond
	public String color;
	public int etat; //1 existe, 0 existe pas
	public int verif; //1 si la bulle est vérifié, 0 sinon.
	public int nbColor = 6; //Nombre de couleurs differentes par defaut
	public String[] rand; //tableau avec nos couleurs possibles

	public Bubble(){}
	public Bubble(double x, double y, int etat){
		a = new Arc();
		this.x = x;
		this.y = y;
		this.etat = etat;
		verif=0;
		rand = new String[] { "0xe5dd6e", "0x3f6342", "0x3a468e", "0xb23c44","0x3d8c8c","0x5d457a"};
		color = rand[(int)( Math.random() * nbColor)]; //attribut une couleur aléatoirement.
	}
	/*Même constructeur on on rentre nous même le nombre de couleurs ainsi qu'un tableau de couleurs*/
	public Bubble(double x, double y, int etat, int nbColor, String[] rand){
		a = new Arc();
		this.x = x;
		this.y = y;
		this.etat = etat;
		verif=0;
		this.rand= rand;		
		color = rand[(int)( Math.random() * nbColor)];
	}

	public void setX(double x){
		this.x = x;
	}

	public void setY(double y){
		this.y = y;
	}
	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}
}