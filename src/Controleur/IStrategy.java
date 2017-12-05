package Controleur;
import Modele.Board;

public interface IStrategy {
    public int execute(Board board);
    public int countNbPawnsLeft(Board board);
}
