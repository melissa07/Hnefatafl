package Controleur;

public class AttackerStrategy implements IStrategy {
    private static AttackerStrategy attackerSingleton = null;


    public static AttackerStrategy getInstance() {
        if(attackerSingleton == null) {
            attackerSingleton = new AttackerStrategy();
        }
        return attackerSingleton;
    }

    @Override
    public int execute() {
        int attackerScore = 0;
        attackerScore += countNbPawnsLeft();
        return -1;
    }

    @Override
    public int countNbPawnsLeft() {
        return 0;
    }

    public int findNearestKingExist() {
        return -1;
    }
}
