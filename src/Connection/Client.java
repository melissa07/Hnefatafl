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

            while(1 == 1) {
                char cmd = 0;

                cmd = (char)input.read();

                // Début de la partie en joueur rouge
                if(cmd == '1'){
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    nouveauBoard = new Board(boardValues);

                    System.out.println("Nouvelle partie! Vous jouer rouge, entrez votre premier coup : ");
                    String move = null;
                    couleurJoueur = joueurRouge;
                    move = MinMaxAlphaBeta.doMinMax(nouveauBoard, couleurJoueur);
                    System.out.println("Move: " +move);
                    // todo algo
                    /*
                    if(movesCounter == 0)
                        move = "A9 - A11";
                    else
                        move = MinMaxAlphaBeta.buildStrategy(nouveauBoard, couleurJoueur);*/

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
                }


                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joué.
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer);
                    if(couleurJoueur == joueurRouge){
                        nouveauBoard.modifyBoard(s, joueurNoir);
                    }else if(couleurJoueur == joueurNoir){
                        nouveauBoard.modifyBoard(s, joueurRouge);
                    }
                    System.out.println("Entrez votre coup : ");
                    String move = null;

                    move = MinMaxAlphaBeta.doMinMax(nouveauBoard, couleurJoueur);
                    if(movesCounter == 0)
                        move = "A9 - A11";
                    
                    nouveauBoard.modifyBoard(move, couleurJoueur);
                    System.out.println("Move: " +move);
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
                // Le dernier coup est invalide
                if(cmd == '4'){
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                    // todo Send another valid move

                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }
}
