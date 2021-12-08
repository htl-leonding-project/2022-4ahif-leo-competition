package prototype.entity;

import com.arjuna.ats.jta.exceptions.NotImplementedException;

public class Match {

    Team team1;
    Team team2;
    int[] resultOfMatch = new int[2];
    //resultOfMatch[0] -> GoalsTeam1
    //resultOfMatch[1] -> GoalsTeam2

    public Match() {
    }

    //region Getter & Setter
    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public int[] getResultOfMatch() {
        return resultOfMatch;
    }

    public void setResultOfMatch(int[] resultOfMatch) {
        this.resultOfMatch = resultOfMatch;
    }
    //endregion

    public Team getWinningTeam(){
        if(resultOfMatch[0] > resultOfMatch[1])
            return team1;
        else if (resultOfMatch[1] > resultOfMatch[0])
            return team2;

        return null;//Bei Gleichstand noch unklar
    }
}