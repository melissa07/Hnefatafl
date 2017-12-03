package Controleur;

public class DefenderStrategy implements IStrategy {

    private static DefenderStrategy defenderSingleton = null;

    public static DefenderStrategy getInstance() {
        if(defenderSingleton == null) {
            defenderSingleton = new DefenderStrategy();
        }
        return defenderSingleton;
    }

    @Override
    public int execute() {
        int defenderScore = 0;

        defenderScore += countNbPawnsLeft();
        return -1;
    }

    @Override
    public int countNbPawnsLeft() {
        return 0;
    }
}
