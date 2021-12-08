package prototype.entity;

public class Team {

    String name;
    int points;

    //region Constructor
    public Team() {
    }

    public Team(String name) {
        this.name = name;
        this.points = 0;
    }
    //endregion
    //region Getter & Setter
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion

}