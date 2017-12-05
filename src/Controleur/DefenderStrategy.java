package Controleur;

import Modele.Board;

public class DefenderStrategy implements IStrategy {

    private static DefenderStrategy defenderSingleton = null;

    public static DefenderStrategy getInstance() {
        if(defenderSingleton == null) {
            defenderSingleton = new DefenderStrategy();
        }
        return defenderSingleton;
    }

    @Override
    public int execute(Board board) {
        int defenderScore = 0;

        defenderScore += countNbPawnsLeft(board);
        return -1;
    }

    @Override
    public int countNbPawnsLeft(Board board) {
        return 0;
    }
}
