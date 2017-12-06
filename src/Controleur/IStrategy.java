package Controleur;
import Modele.Board;

public interface IStrategy {
    int execute(Board boardGenere);

    int countNbPawnsLeft(Board boardGenere);

    int buildStrategy(Board boardGenere/*, int player*/);

    boolean verifierSiPionEstEnDanger(Board board);

    boolean verifierSiCasesPrioritairesOccupees(int[][] board);

    boolean verifierSiRoiEntoure(Board board);

    int findNearestKingExist(Board boardGenere);
}
