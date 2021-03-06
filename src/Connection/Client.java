package Connection;
import Controleur.MinMaxAlphaBeta;
import Modele.Board;

import java.io.*;
import java.net.*;


class Client {
    public static void main(String[] args) {
        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        int[][] board = new int[13][13];
        Board nouveauBoard = null;
        int joueurRouge = 4;
        int joueurNoir = 2;
        int couleurJoueur =0;
        int movesCounter = 0;

        try {
            MyClient = new Socket("localhost", 8888);
            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            MinMaxAlphaBeta minmax = new MinMaxAlphaBeta();

            while(1 == 1) {
                char cmd = 0;

                cmd = (char)input.read();

                // Début de la partie en joueur rouge
                if(cmd == '1'){
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    nouveauBoard = new Board(boardValues);

                    nouveauBoard.setCouleurJoueur(4);
                    nouveauBoard.setCouleurAdverse(2);
                    nouveauBoard.setJoueurCourant(4);

                    nouveauBoard.setKingPositionY(6);
                    nouveauBoard.setKingPositionX(6);

                    System.out.println("Nouvelle partie! Vous jouez rouge, entrez votre premier coup : ");
                    String move = null;
                    couleurJoueur = joueurRouge;
                    System.out.println("Entering minmax algorithm ...");

                    System.out.println("Move: " +move);

                    if(movesCounter == 0 && couleurJoueur == joueurRouge)
                        move = "A9 - A11";
                    else
                        move = minmax.doMinMax(nouveauBoard, couleurJoueur);

                    nouveauBoard.modifyBoard(move, couleurJoueur);
                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                    movesCounter++;
                }
                // Début de la partie en joueur Noir
                if(cmd == '2'){
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    couleurJoueur = joueurNoir;
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    nouveauBoard = new Board(boardValues);
                    nouveauBoard.setKingPositionX(6);
                    nouveauBoard.setKingPositionY(6);

                    nouveauBoard.setCouleurJoueur(2);
                    nouveauBoard.setCouleurAdverse(4);
                    nouveauBoard.setJoueurCourant(4); // le 4(rouge) commence
                }


                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joué.
                if(cmd == '3'){
                    // change le joueur courant
                    if(nouveauBoard.getJoueurCourant() == 4)
                        nouveauBoard.setJoueurCourant(2);
                    else
                        nouveauBoard.setJoueurCourant(4);

                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer);
                    //coup de l'adversaire
                    if(couleurJoueur == joueurRouge){
                        nouveauBoard.modifyBoard(s, joueurNoir);
                    }else if(couleurJoueur == joueurNoir){
                        nouveauBoard.modifyBoard(s, joueurRouge);
                    }
                    System.out.println("Entrez votre coup : ");
                    String move = null;

                    if(nouveauBoard.getJoueurCourant() == 4)
                        nouveauBoard.setJoueurCourant(2);
                    else
                        nouveauBoard.setJoueurCourant(4);

                    if(movesCounter == 0 && couleurJoueur == joueurRouge)
                        move = "A9 - A11";
                    else
                        move = minmax.doMinMax(nouveauBoard, couleurJoueur);

                    nouveauBoard.modifyBoard(move, couleurJoueur);
                    System.out.println("Move: " +move);
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
                // Le dernier coup est invalide
                if(cmd == '4'){
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
                    String move = null;

                    move = minmax.doMinMax(nouveauBoard, couleurJoueur);
                    nouveauBoard.modifyBoard(move, couleurJoueur);
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }
}
