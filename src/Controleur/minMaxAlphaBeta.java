package Controleur;

public class minMaxAlphaBeta {

    private int score = 0;

    public int minMaxAlphaBeta(int position, String joueur, int alpha, int beta) {  // player may be "computer" or "opponent"
        int alphaTmp;
        int betaTmp;
        int maxScore = 0;
        if (position == 99999 || position == 0) {
            return score;
        }
        //si c<est notre tour
        if (joueur == "Max") {
            alphaTmp = -1;
            //pour tout les enfant de position
            for (int i = 0; i < 0; i++) {
                score = minMaxAlphaBeta(position + i, "Min", Math.max(alpha, alphaTmp), beta);
                alphaTmp = Math.max(maxScore, score);
                if (alphaTmp >= beta) {
                    return alphaTmp;
                }
            }
            return alphaTmp;

        } else if (joueur == "Min") {
            betaTmp = 101;
            //pour tout les enfant de position
            for (int i = 0; i < 0; i++) {
                score = minMaxAlphaBeta(position + i, "Min", alpha, Math.min(beta, betaTmp));
                betaTmp = Math.min(maxScore, score);
                if (betaTmp < beta) {
                    return betaTmp;
                }
            }
            return betaTmp;
        }
        return 0;


/*        children = all valid moves for this "player"
        if (player is computer, i.e., max's turn){
        // Find max and store in alpha
        for each child {
            score = minimax(level - 1, opponent, alpha, beta)
            if (score > alpha) alpha = score
            if (alpha >= beta) break;  // beta cut-off
        }
        return alpha
    } else (player is opponent, i.e., min's turn)
            // Find min and store in beta
            for each child {
        score = minimax(level - 1, computer, alpha, beta)
        if (score < beta) beta = score
        if (alpha >= beta) break;  // alpha cut-off
    }
       return beta
}*/
    }
}
