package Controleur;
import Modele.Board;

public interface IStrategy {
    int execute(Board boardGenere);

    int countNbPawnsLeft(Board boardGenere);

    boolean verifierSiPionEstEnDanger(Board board);

    int verifierSiCasesPrioritairesOccupees(int[][] board);

    boolean verifierSiRoiEntoure(Board board);

    int findNearestKingExist(Board boardGenere);
}
