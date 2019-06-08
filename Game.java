/*Cette application reprend le  fameux principe du jeu "Bubble Shooter" qui a pour but
d'enlever les Bubbles en en lancant une autre sur celles déjà présente*/
/*Des Bubbles disparaissent si la Bubble qu'on a lancé forme une liaison de 3 Bubbles minimum
avec celles déja présentes sur le jeu (qui sont évidemment de la même couleur que celle lancée)*/
/*Plus on eclate de Bubble a la fois, plus on gagne de points*/
/*Les Bubbles à lancer sont tirées aléatoirement selon les couleurs des Bubbles qu'il reste sur le jeu*/
/*Si une Bubble n'as aucun voisin et qu'elle ne touche pas un bord, alors elle disparait*/
/*Nous ne pouvons pas perdre*/
/*Il y a un compteur de points*/
/*Si nous redemarrons la partis, le jeu génère une nouvelle partie*/
/*Nous pouvons placer la Bubble où nous le voulons*/
/*Il faut clicker la où nous voulons placer la Bubble pour l'inserer*/

import javafx.application.Application;

import javafx.scene.layout.BorderPane;

import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.shape.*;
import javafx.scene.paint.Color;

import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import javafx.scene.text.*;
import java.util.*;

public class Game extends Application{
    public int width = 800;
    public int height = 600;
    public int pts = 0;    //Compteur de points
	public BorderPane root= new BorderPane();
	public Scene scene = new Scene(root,width,height);
	public BorderPane gauche= new BorderPane();
    public BorderPane droite= new BorderPane();
    public BorderPane fond = new BorderPane();
    public Bubble principale = new Bubble((root.getWidth() * 0.75)/2,(root.getHeight() * 0.9),1); //Bubble a lancer
    public Bubble tab[][]; //notre tableau de Bubble, là ou se déroule le jeu

    public Text score = new Text(root.getWidth() * 0.80, root.getHeight() * 0.1, "Points:");
    public Text points = new Text(root.getWidth() * 0.84, root.getHeight() * 0.17, Integer.toString(pts));
    public Text stop = new Text(root.getWidth() * 0.82  , root.getHeight() * 0.79, "  Stop");
    public Text restart = new Text(root.getWidth() * 0.79, root.getHeight() * 0.94, "   Restart");
    public Text gagner = new Text(root.getWidth() * 0.12, root.getHeight() * 0.4, "");

    /*Fonction qui affiche la Bubble en argument*/
    private void actualiserBubble(Bubble b){
        b.a.setStartAngle(45);
        b.a.setLength(360);

        b.a.centerXProperty().bind((root.widthProperty().multiply(b.x).divide(width)));
        b.a.centerYProperty().bind((root.heightProperty().multiply(b.y).divide(height)));
        
        //Taille de la Bubble proportionelle à la moyenne de la hauteur et la largeur de la fenetre
        b.a.radiusXProperty().bind(((root.widthProperty().add(root.heightProperty())).divide(2)).multiply(0.75).multiply(0.045));
        b.a.radiusYProperty().bind(((root.widthProperty().add(root.heightProperty())).divide(2)).multiply(0.75).multiply(0.045));
        b.a.setFill(Color.web(b.color,1));

        gauche.getChildren().addAll(b.a);
    }

    /*Actualise tout notre tableau de Bubbles graphiquement, supprime les formes affichées et affiche les nouvelles*/
    private void actualiserJeu(Bubble[][] tab){
        for(int i=0; i<11; i++){
            for(int j=0; j<12; j++){
                if(tab[i][j].etat == 1){
                    gauche.getChildren().remove(tab[i][j].a);
                }
            }
        }
        for(int i=0; i<11; i++){
            for(int j=0; j<12; j++){
                if(tab[i][j].etat == 1){
                    actualiserBubble(tab[i][j]);
                }
            }
        }
    }

    /*Initialise notre tableau de Bubble*/
    /*Les ligne paires possède 12 Bubbles, les impaires 11*/
    private Bubble[][] initBubble(){
        int i,j;
        double y = (height/12)/2;
        double x;
        Bubble tab[][] = new Bubble[11][12]; //Bubble [hauteur][largeur]
        for(i=0; i<11; i++){
            if(i%2 == 1){
                x = ((width*0.75)/12);
                for(j=0; j<11; j++){
                    if(i>=5){
                        tab[i][j] = new Bubble (x,y,0);
                        x+=((width*0.75)/12);
                    }
                    else{
                        tab [i][j]= new Bubble (x,y,1);
                        x+=((width*0.75)/12);
                    }
                }
                tab [i][11]= new Bubble (x,y,-1); //On créer une Bubble pour qui sert a rien (etat -1), pour combler le tableau
                x = ((width*0.75)/12);
            }
            else{
                x= ((width*0.75)/12)/2;
                for(j=0; j<12; j++){
                    if(i>=5){
                        tab[i][j] = new Bubble (x,y,0);
                        x+=((width*0.75)/12);
                    }
                    else{
                        tab[i][j]= new Bubble (x,y,1);
                        x+=((width*0.75)/12);
                    }
                }
                x = ((width*0.75)/12)/2;
            }
            y+=(height/13.5);
        }
        return tab;
    }

    //Retourne les indexs de la Bubble correspondante.
    public Index idxCase(Bubble [][] tabB, MouseEvent e){
        int i, iTmp = 0, iTmp2 = 0, tmp, tmp2 = 100000;
        Index idx = new Index(0,0);
        for(i=0; i<11; i++){
            tmp = (int) Math.abs(e.getY() - tabB[i][0].y); //valeur absolu de la distance entre notre balle pas encore placé et les case y
            if(tmp < tmp2){
                tmp2 = tmp;
                iTmp = i;
            }
        }
        idx.y = iTmp;

        tmp2 = 100000;
        if(idx.y%2 == 0){
            for(i=0; i<12; i++){
                tmp = (int) Math.abs(e.getX() - tabB[0][i].x);
                if(tmp < tmp2){
                    tmp2 = tmp;
                    iTmp2 = i;
                }
            }
            idx.x = iTmp2;
        }
        else{
            double cellule = (width*0.75)/12;
            //System.out.println("ok");
            if(e.getX() < cellule/2){
                idx.x = 0;
            }
            if(e.getX() > (cellule/2) && e.getX() < (cellule)/2 + cellule){
                idx.x = 0;
            }
            if(e.getX() > (cellule)/2 + cellule && e.getX() < ((cellule)/2 + cellule*2)){
                idx.x = 1;
            }
            if(e.getX() > ((cellule)/2 + cellule*2) && e.getX() < ((cellule)/2 + cellule*3)){
                idx.x = 2;
            }
            if(e.getX() > ((cellule)/2 + cellule*3) && e.getX() < ((cellule)/2 + cellule*4)){
                idx.x = 3;
            }
            if(e.getX() > ((cellule)/2 + cellule*4) && e.getX() < ((cellule)/2 + cellule*5)){
                idx.x = 4;
            }
            if(e.getX() > ((cellule)/2 + cellule*5) && e.getX() < ((cellule)/2 + cellule*6)){
                idx.x = 5;
            }
            if(e.getX() > ((cellule)/2 + cellule*6) && e.getX() < ((cellule)/2 + cellule*7)){
                idx.x = 6;
            }
            if(e.getX() > ((cellule)/2 + cellule*7) && e.getX() < ((cellule)/2 + cellule*8)){
                idx.x = 7;
            }
            if(e.getX() > ((cellule)/2 + cellule*8) && e.getX() < ((cellule)/2 + cellule*9)){
                idx.x = 8;
            }
            if(e.getX() > ((cellule)/2 + cellule*9) && e.getX() < ((cellule)/2 + cellule*10)){
                idx.x = 9;
            }
            if(e.getX() > ((cellule)/2 + cellule*10) && e.getX() < ((cellule)/2 + cellule*11)){
                idx.x = 10;
            }
            if(e.getX() > ((cellule)/2 + cellule*11)){
                idx.x = 10;
            }        
        }
        return idx;
    }

    //bool pour dire si il y a une voisin occupé a coté de la case (case valide).
    public boolean voisin(Bubble [][]tabB,Index idx){
        if((idx.y%2 == 0 && idx.x != 0 && idx.x != 11 && idx.y != 0) && (tabB[idx.y-1][idx.x-1].etat == 1 || tabB[idx.y-1][idx.x].etat == 1 || tabB[idx.y][idx.x +1].etat == 1 || tabB[idx.y+1][idx.x].etat == 1 || tabB[idx.y+1][idx.x-1].etat == 1 || tabB[idx.y][idx.x-1].etat == 1))
                return true;
        else if((idx.y%2 == 1 && idx.x != 0 && idx.x != 10) && (tabB[idx.y-1][idx.x].etat == 1 || tabB[idx.y-1][idx.x+1].etat == 1 || tabB[idx.y][idx.x+1].etat == 1 || tabB[idx.y+1][idx.x+1].etat == 1 || tabB[idx.y+1][idx.x].etat == 1 || tabB[idx.y][idx.x-1].etat == 1))
                return true;

        else if((idx.y%2 == 0 && idx.x == 0 && idx.y != 0) && (tabB[idx.y-1][idx.x].etat == 1 || tabB[idx.y][idx.x +1].etat == 1 || tabB[idx.y+1][idx.x].etat == 1))
                return true;

        else if((idx.y%2 == 1 && idx.x == 0) && (tabB[idx.y-1][idx.x].etat == 1 || tabB[idx.y-1][idx.x+1].etat == 1 || tabB[idx.y][idx.x+1].etat == 1 || tabB[idx.y+1][idx.x+1].etat == 1 || tabB[idx.y+1][idx.x].etat == 1 ))
                return true;
        else if((idx.y%2 == 0 && idx.x == 11 && idx.y != 0) && (tabB[idx.y-1][idx.x-1].etat == 1 || tabB[idx.y+1][idx.x-1].etat == 1 || tabB[idx.y][idx.x-1].etat == 1))
                return true;

        else if((idx.y%2 == 1 && idx.x == 10) && (tabB[idx.y-1][idx.x].etat == 1 || tabB[idx.y-1][idx.x+1].etat == 1 || tabB[idx.y][idx.x+1].etat == 1 || tabB[idx.y+1][idx.x+1].etat == 1 || tabB[idx.y+1][idx.x].etat == 1 || tabB[idx.y][idx.x-1].etat == 1))
                return true;

        else if((idx.y == 0 && idx.x == 0)&& (tabB[idx.y][idx.x + 1].etat == 1 || tabB[idx.y+1][idx.x].etat == 1 || tabB[idx.y+1][idx.x + 1].etat == 1)){
            return true;
        }
        else if((idx.y == 0 && idx.x == 11)&& (tabB[idx.y][idx.x - 1].etat == 1 || tabB[idx.y-1][idx.x-1].etat == 1)){
            return true;
        }
        else if((idx.y == 0 && idx.x != 0 && idx.x != 11) && (tabB[idx.y][idx.x +1].etat == 1 || tabB[idx.y+1][idx.x].etat == 1 || tabB[idx.y+1][idx.x-1].etat == 1 || tabB[idx.y][idx.x-1].etat == 1))
            return true;
        else
            return false;
    }


    /*Fonction qui vérifie si plusieurs Bubbles de la mêmes couleurs se suivent succesivement*/
    /*flag index (dernier argument) savoir si on doit refaire une recursion (à la toute fin de la fonction)*/
    /*Fonction faisant du cas par cas donc très long, le principe est le même pour chaque condition*/
    private void verif(Bubble[][]tabB,Index idx, ArrayList<Index> tabI, int index){
        Index tmp = new Index(0,0);
        int etat = 0;
        for(int i = 0; i < tabI.size(); i++){
            if(tabI.get(i).x == idx.x && tabI.get(i).y == idx.y){
                etat = 1;
                break;
            }
        }
        if(etat == 0)
            tabI.add(new Index(idx.x, idx.y));
        //System.out.println(idx.x + " " + idx.y);
        if(idx.y%2 == 0 && idx.x != 0 && idx.x != 11 && idx.y != 0){
            if (tabB[idx.y][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x-1].etat == 1 && tabB[idx.y][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
            }
            else if (tabB[idx.y-1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y-1][idx.x].etat == 1 && tabB[idx.y-1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y-1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y][idx.x +1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x +1].etat == 1 && tabB[idx.y][idx.x +1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if (tabB[idx.y+1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x].etat == 1 && tabB[idx.y+1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if (tabB[idx.y+1][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x-1].etat == 1 && tabB[idx.y+1][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y-1][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y-1][idx.x-1].etat == 1 && tabB[idx.y-1][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y-1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
        }


        else if(idx.y%2 == 1 && idx.x != 0 && idx.x != 10){
            if(tabB[idx.y][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x-1].etat == 1 && tabB[idx.y][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y-1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y-1][idx.x].etat == 1 && tabB[idx.y-1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y-1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y-1][idx.x+1].color == tabB[idx.y][idx.x].color && tabB[idx.y-1][idx.x+1].etat == 1 && tabB[idx.y-1][idx.x+1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y-1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y][idx.x+1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x+1].etat == 1 && tabB[idx.y][idx.x+1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x+1].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x+1].etat == 1 && tabB[idx.y+1][idx.x+1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x].etat == 1 && tabB[idx.y+1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
        }


        else if(idx.y%2 == 0 && idx.x == 0 && idx.y != 0){
            if(tabB[idx.y-1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y-1][idx.x].etat == 1 && tabB[idx.y-1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y-1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y][idx.x +1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x +1].etat == 1 && tabB[idx.y][idx.x +1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x].etat == 1 && tabB[idx.y+1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
        }


        else if(idx.y%2 == 1 && idx.x == 0){
            if(tabB[idx.y-1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y-1][idx.x].etat == 1 && tabB[idx.y-1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y-1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y-1][idx.x+1].color == tabB[idx.y][idx.x].color && tabB[idx.y-1][idx.x+1].etat == 1 && tabB[idx.y-1][idx.x+1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y-1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y][idx.x+1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x+1].etat == 1 && tabB[idx.y][idx.x+1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x+1].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x+1].etat == 1 && tabB[idx.y+1][idx.x+1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x].etat == 1 && tabB[idx.y+1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
        }


        else if(idx.y%2 == 0 && idx.x == 11 && idx.y != 0){
            if(tabB[idx.y-1][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y-1][idx.x-1].etat == 1 && tabB[idx.y-1][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y-1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x-1].etat == 1 && tabB[idx.y+1][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x-1].etat == 1 && tabB[idx.y][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
        }


        else if(idx.y%2 == 1 && idx.x == 10){
            if(tabB[idx.y-1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y-1][idx.x].etat == 1 && tabB[idx.y-1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y-1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y-1][idx.x+1].color == tabB[idx.y][idx.x].color && tabB[idx.y-1][idx.x+1].etat == 1 && tabB[idx.y-1][idx.x+1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y-1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y][idx.x+1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x+1].etat == 1 && tabB[idx.y][idx.x+1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x+1].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x+1].etat == 1 && tabB[idx.y+1][idx.x+1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x].etat == 1 && tabB[idx.y+1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x-1].etat == 1 && tabB[idx.y][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
        }


        else if(idx.y == 0 && idx.x == 0){
            if(tabB[idx.y][idx.x + 1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x + 1].etat == 1 && tabB[idx.y][idx.x + 1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x].etat == 1 && tabB[idx.y+1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x + 1].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x + 1].etat == 1 && tabB[idx.y+1][idx.x + 1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
        }
        else if(idx.y == 0 && idx.x == 11){
            if(tabB[idx.y][idx.x - 1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x - 1].etat == 1 && tabB[idx.y][idx.x - 1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x-1].etat == 1 && tabB[idx.y+1][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
        }
        else if(idx.y == 0 && idx.x != 0 && idx.x != 11){
            if(tabB[idx.y][idx.x +1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x +1].etat == 1 && tabB[idx.y][idx.x +1].verif==0){
                tmp.x=idx.x+1;
                tmp.y=idx.y;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x].etat == 1 && tabB[idx.y+1][idx.x].verif==0){
                tmp.x=idx.x;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y+1][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y+1][idx.x-1].etat == 1 && tabB[idx.y+1][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y+1;
            
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
                }
            else if(tabB[idx.y][idx.x-1].color == tabB[idx.y][idx.x].color && tabB[idx.y][idx.x-1].etat == 1 && tabB[idx.y][idx.x-1].verif==0){
                tmp.x=idx.x-1;
                tmp.y=idx.y;
                
                        tabB[tmp.y][tmp.x].verif=1;
                        verif(tabB,tmp,tabI,index);
            }
        }
        /*Si notre flag est inferieur a la taille de notre tableau d'index, alors on rappel*/   
                index++;
                if(index < tabI.size())
                    verif(tabB,tabI.get(index),tabI,index);
        return;
        
    }

    /*Si une Bubble est toute seule et non contre un mur, alors elle tombe (disparait)*/
    /*Même principe que la fonction d'avant pour le cas par cas, sans récursion*/
    public void fall(Bubble[][]tabB,int flag){
        int cpt;//compteur de voisins vides.
        for(int i=0; i<10; i++){
            for(int j=0; j<11; j++){  
                cpt=0;
                if(i%2 == 0 && j != 0 && j != 11 && i != 0){
                    if (tabB[i][j-1].etat==0){
                        cpt++;
                    }
                    if (tabB[i-1][j].etat==0){
                        cpt++;
                    }
                    if(tabB[i][j +1].etat==0){
                        cpt++;
                    }
                    if (tabB[i+1][j].etat==0){
                        cpt++;
                    }
                    if (tabB[i+1][j-1].etat==0){
                        cpt++;
                    }
                    if(tabB[i-1][j-1].etat==0){
                        cpt++;
                    }
                    if(cpt > 5){
                        if(tab[i][j].etat ==1){
                            gauche.getChildren().remove(tab[i][j].a);
                            tab[i][j].etat = 0;
                        }
                    }
                }


                else if(i%2 == 1 && j != 0 && j != 10){
                    if(tabB[i][j-1].etat==0){
                        cpt++;
                    }
                    if(tabB[i-1][j].etat==0){
                        cpt++;
                    }
                    if(tabB[i-1][j+1].etat==0){
                        cpt++;
                    }
                    if(tabB[i][j+1].etat==0){
                        cpt++;
                    }
                    if(tabB[i+1][j+1].etat==0){
                        cpt++;
                    }
                    if(tabB[i+1][j].etat==0){
                        cpt++;
                    }
                    if(cpt > 5){
                        if(tab[i][j].etat ==1){
                            gauche.getChildren().remove(tab[i][j].a);
                            tab[i][j].etat = 0;
                        }
                    }
                }

                else if(i%2 == 1 && j == 0){
                    if(tabB[i-1][j].etat==0){
                        cpt++;
                    }
                    if(tabB[i-1][j+1].etat==0){
                        cpt++;
                    }
                    if(tabB[i][j+1].etat==0){
                        cpt++;
                    }
                    if(tabB[i+1][j+1].etat==0){
                        cpt++;
                    }
                    if(tabB[i+1][j].etat==0){
                        cpt++;
                    }
                    if(cpt > 4){
                        if(tab[i][j].etat ==1){
                            gauche.getChildren().remove(tab[i][j].a);
                            tab[i][j].etat = 0;
                        }
                    }
                }

                else if(i%2 == 1 && j == 10){
                    if(tabB[i-1][j].etat==0){
                        cpt++;
                    }
                    if(tabB[i-1][j+1].etat==0){
                        cpt++;
                    }
                    if(tabB[i][j+1].etat==0){
                        cpt++;
                    }
                    if(tabB[i+1][j+1].etat==0){
                        cpt++;
                    }
                    if(tabB[i+1][j].etat==0){
                        cpt++;
                    }
                    if(tabB[i][j-1].etat==0){
                        cpt++;
                    }
                    if(cpt > 4){
                        if(tab[i][j].etat ==1){
                            gauche.getChildren().remove(tab[i][j].a);
                            tab[i][j].etat = 0;
                        }
                    }
                }
            }
        }
        return;
    }

    /*Vérifie simplement si il y a des Bubbles affichées dans le jeu*/
    public void gagner(Bubble[][]tab, int pts){
        int win = 1;
        for(int i=0; i<11; i++){
            for(int j=0; j<12; j++){
                if(tab[i][j].etat==1)
                    win=0;
            }
        }
        if(win == 1){
            gagner.setText("C'est gagné avec " + Integer.toString(pts) + " points !!!");  
            gauche.getChildren().addAll(gagner);
        }
    }

	@Override
    public void start(Stage primaryStage) {

    	primaryStage.setTitle("Bubble Shooter by Etienne PENAULT");

        root.getChildren().addAll(fond,gauche,droite);

            /*Rectangle bleu, fond de l'application*/
            Rectangle rf = new Rectangle();
            rf.setFill(Color.web("0x1b2656",1.0));
            rf.setX(0);
            rf.setY(0);
            rf.widthProperty().bind(root.widthProperty());
            rf.heightProperty().bind(root.heightProperty());

        	/*Rectangle ou le jeu se déroule*/
        	Rectangle rg = new Rectangle();

        	rg.setX(0);
        	rg.setY(0);

        	rg.setFill(Color.web("0x97af9f",1.0));

        	rg.widthProperty().bind(root.widthProperty().multiply(0.75));
        	rg.heightProperty().bind(root.heightProperty());
            rg.arcWidthProperty().bind(root.widthProperty().multiply(0.07));
            rg.arcHeightProperty().bind(root.heightProperty().multiply(0.07));

        	/*Rectangle ou les infos se déroule*/
        	Rectangle rd = new Rectangle();

        	rd.setY(0);
        	
        	rd.setFill(Color.web("0x669375",1.0));

        	rd.xProperty().bind(rg.widthProperty());
        	rd.widthProperty().bind(root.widthProperty().multiply(0.25));
        	rd.heightProperty().bind(root.heightProperty().multiply(0.70));
            rd.arcWidthProperty().bind(root.widthProperty().multiply(0.07));
            rd.arcHeightProperty().bind(root.heightProperty().multiply(0.07));

            /*Le rectangle pour arreter*/
            Rectangle arreter = new Rectangle();

            arreter.setY(height* 0.70);
            
            arreter.setFill(Color.web("#af5541",1.0));

            arreter.xProperty().bind(rg.widthProperty());
            arreter.widthProperty().bind(root.widthProperty().multiply(0.25));
            arreter.heightProperty().bind(root.heightProperty().multiply(0.15));
            arreter.arcWidthProperty().bind(root.widthProperty().multiply(0.07));
            arreter.arcHeightProperty().bind(root.heightProperty().multiply(0.07));

            /*Le rectangle pour redemarrer*/
            Rectangle redemarrer = new Rectangle();

            redemarrer.setY(height* 0.85);
            
            redemarrer.setFill(Color.web("#a88c4a",1.0));

            redemarrer.xProperty().bind(rg.widthProperty());
            redemarrer.widthProperty().bind(root.widthProperty().multiply(0.25));
            redemarrer.heightProperty().bind(root.heightProperty().multiply(0.15));
            redemarrer.arcWidthProperty().bind(root.widthProperty().multiply(0.07));
            redemarrer.arcHeightProperty().bind(root.heightProperty().multiply(0.07));


            /*Notre zone de tire*/
        	QuadCurve q = new QuadCurve();

        	q.setFill(Color.web("0xf7fc92",0.4));

        	q.startXProperty().bind(root.widthProperty().multiply(0.75).multiply(0.1));
        	q.startYProperty().bind(root.heightProperty());
        	q.endXProperty().bind(root.widthProperty().multiply(0.75).multiply(0.9));
        	q.endYProperty().bind(root.heightProperty());
        	q.controlXProperty().bind(root.widthProperty().multiply(0.75).multiply(0.5));
        	q.controlYProperty().bind(root.heightProperty().multiply(0.6));

            tab= initBubble();//On initialise notre tableau avec des Bubbles

            /*On declare la taille de nos textes*/
            score.setFont(new Font(24));
            points.setFont(new Font(24));
            stop.setFont(new Font(24));
            restart.setFont(new Font(24));
            gagner.setFont(new Font(27));

            /*EventHandler pour arreter l'application*/
            arreter.addEventHandler(MouseEvent.MOUSE_RELEASED, (event)->{
                System.exit(0);
            });
            stop.addEventHandler(MouseEvent.MOUSE_RELEASED, (event)->{
                System.exit(0);
            });

            /*EventHandler pour redemarrer l'application*/
            redemarrer.addEventHandler(MouseEvent.MOUSE_RELEASED, (event)->{
                gauche.getChildren().remove(gagner); //On supprime le message si il est affiché
                pts = 0; //on remet les points a zero
                points.setText(Integer.toString(pts));
                for(int i=0; i<11; i++){
                    for(int j=0; j<12; j++){
                        if(tab[i][j].etat==1)
                            gauche.getChildren().remove(tab[i][j].a); //On supprime tout les éléments affichés
                    }
                }
                tab= initBubble(); //on attribue de nouvelles Bubbles a notre tableau
                actualiserJeu(tab); //On rafraichit l'affichage
                /*Puis on fait la même chose pour la boule qu'on lance*/
                gauche.getChildren().remove(principale.a);
                principale = new Bubble((root.getWidth() * 0.75)/2,(root.getHeight() * 0.9),1);
                actualiserBubble(principale);
            });
            restart.addEventHandler(MouseEvent.MOUSE_RELEASED, (event)->{
                gauche.getChildren().remove(gagner); //On supprime le message si il est affiché
                pts = 0; //on remet les points a zero
                points.setText(Integer.toString(pts));
                for(int i=0; i<11; i++){
                    for(int j=0; j<12; j++){
                        if(tab[i][j].etat==1)
                            gauche.getChildren().remove(tab[i][j].a); //On supprime tout les éléments affichés
                    }
                }
                tab= initBubble(); //on attribue de nouvelles Bubbles a notre tableau
                actualiserJeu(tab); //On rafraichit l'affichage
                /*Puis on fait la même chose pour la boule qu'on lance*/
                gauche.getChildren().remove(principale.a);
                principale = new Bubble((root.getWidth() * 0.75)/2,(root.getHeight() * 0.9),1);
                actualiserBubble(principale);
            });

            primaryStage.addEventHandler(MouseEvent.MOUSE_RELEASED, (event)->{
                if(event.getX() < (root.getWidth() * 0.75) && event.getY() < (root.getHeight() * 0.9)){
                    Index idx = idxCase(tab, event); //On récupère l'index de la case ciblée
                    if(voisin(tab,idx)){    //Si la case est jouable (au moins un voisin)
                        //System.out.println(idx.x + " " + idx.y);
                        if(tab[idx.y][idx.x].etat != 1){ //Si la Bubble correspondant aux indexs données sur le tableau de Bubble n'est pas affichée.
                            ArrayList<Index> BubbleConnexes = new ArrayList<Index>();
                            String[] couleurs = new String[] {"","","","","",""};
                            int cptColor=0; //compteur du nombres de couleurs
                            tab[idx.y][idx.x].color = principale.color; //on attribut la couleur a la Bubble en question
                            tab[idx.y][idx.x].etat = 1; //On lui met l'etat 1 pour l'afficher
                            actualiserBubble(tab[idx.y][idx.x]); 

                            gauche.getChildren().remove(principale.a); //On supprime la boule qu'on a lancé

                            /*On récupère les indexs de toute les Bubbles de même couleurs connexes a celle qu'on a lancé*/
                            /*dans notre ArrayList "BubbleConnexes"*/
                            verif(tab,idx,BubbleConnexes,-1);
                            /*System.out.println("");
                            for(int i = 0; i < BubbleConnexes.size(); ++i){
                                System.out.println(BubbleConnexes.get(i).x + " " + BubbleConnexes.get(i).y);    
                            } */
                            /*Si le tableau comporte au moins trois indexs de Bubbles de même couleurs connexes*/
                            if(BubbleConnexes.size() >=3){
                                /*On augmente le score poportionnelement aux nombres de Bubbles eclatées*/
                                if(BubbleConnexes.size() == 3)
                                    pts += (BubbleConnexes.size() * 100);
                                else
                                    pts += (BubbleConnexes.size() * 100 *(BubbleConnexes.size()/2));
                                /*On n'affiche plus les Bubbles qu'on a eclatées, et on actualise le texte des points*/
                                for(int i = 0; i < BubbleConnexes.size(); ++i){
                                    gauche.getChildren().remove(tab[BubbleConnexes.get(i).y][BubbleConnexes.get(i).x].a);
                                    tab[BubbleConnexes.get(i).y][BubbleConnexes.get(i).x].etat = 0;
                                    points.setText(Integer.toString(pts));
                                }   
                            }
                            actualiserJeu(tab);
                            /*System.out.println("");
                            //Garde seulement les couleurs qu'il y a dans le jeu poour principale
                            /*Cela nous permet de finir plus facilement et ne pas tourner en rond*/
                            for(int i=0; i<11; i++){
                                for(int j=0; j<12; j++){
                                    /*Si notre tableau de couleurs ne contient pas deja la couleurs de la Bubble parcouru*/
                                    if(!Arrays.asList(couleurs).contains(tab[i][j].color) && tab[i][j].etat == 1){
                                        /*On l'ajoute*/
                                        couleurs[cptColor]=tab[i][j].color;
                                        //System.out.println(couleurs[cptColor]);
                                        cptColor++; // et on incrémente notre compteur de couleurs
                                    }
                                    /*Et on remet toute les Bubbles a un état 0 de vérification pour que la fonction "verif" 
                                    comprenne qu'on n'a pas vérifié ces Bubbles aux prochain appel*/
                                    tab[i][j].verif=0;
                                }
                            }
                            gagner(tab,pts); //On vérifie si on a gagné
                            if(couleurs[0] == "")//Si il n'y a pas de couleurs dans le tableau (le jeu est terminé) alors on créer une Bubble de couleur aléatoire
                                principale = new Bubble((root.getWidth() * 0.75)/2,(root.getHeight() * 0.9),1);
                            else//Sinon on se cantonne aux couleurs restante dans le jeu
                                principale = new Bubble((root.getWidth() * 0.75)/2,(root.getHeight() * 0.9),1,cptColor,couleurs);
                            fall(tab,-1);//On regarde si on doit supprimer des Bubbles isolé de leurs voisin.
                            
                            actualiserBubble(principale);
                            
                        }
                    }
                    else
                        System.out.println("N'a aucun voisin!!!");
                }
            });
        
        /*On affiche*/
        fond.getChildren().addAll(rf);
		droite.getChildren().addAll(rd,score,points,arreter,redemarrer,stop,restart);
		gauche.getChildren().addAll(rg);
		gauche.getChildren().addAll(q);
        actualiserJeu(tab);
        actualiserBubble(principale);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 	
 	public static void main(String[] args) {
        launch(args);
    }
}