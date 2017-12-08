package Controleur;
import Modele.Board;

public interface IStrategy {
    int execute(Board boardGenere);

    int countNbPawnsLeft(Board boardGenere);

    int verifierSiPionEstEnDanger(Board board);

    int verifierSiCasesPrioritairesOccupees(int[][] board);

    int verifierSiRoiEntoure(Board board);

    int findNearestKingExist(Board boardGenere);

    int countNbPawnsAdverseLeft(Board board);
}
