package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "T_Match")
public class Match{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "M_ID")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "M_Team1")
    public Team team1;
    @ManyToOne
    @JoinColumn(name = "M_Team2")
    public Team team2;
    @Column(name = "M_PointsTeam1")
    public int pointsTeam1;
    @Column(name = "M_PointsTeam2")
    public int pointsTeam2;

    @JoinColumn(name = "M_TOURNAMENT")
    @ManyToOne
    public Tournament tournament;

    //resultOfMatch[0] -> GoalsTeam1
    //resultOfMatch[1] -> GoalsTeam2

    public Match() {
    }

    public Match(Team team1, Team team2, Tournament tournament) {
        this.team1 = team1;
        this.team2 = team2;
        this.tournament = tournament;
    }

    public Match(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
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

    public int getPointsTeam1(){return this.pointsTeam1;}

    public void setPointsTeam1(int amount){ this.pointsTeam1 = amount; }

    public int getPointsTeam2(){return this.pointsTeam2;}

    public void setPointsTeam2(int amount){ this.pointsTeam2 = amount; }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void endMatch(){
        getWinningTeam().incrementWinAmount();
    }
    //endregion


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getWinningTeam(){
        if(pointsTeam1 > pointsTeam2) {
            return team1;
        }
        else{
            return team2;
        }
    }

    public String getMatchResultString() {
        return getTeam1().getName() +" "+getPointsTeam1()+" \\n"+getTeam2().getName()
                +" "+getPointsTeam2();
    }

    @Override
    public String toString() {
        return "Match: "+team1+":"+pointsTeam1+" "+team2+":"+pointsTeam2;
    }
}
