package at.htl.control;

import at.htl.entity.*;
import at.htl.filewriter.Filewriter;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@QuarkusTest
@Transactional
public class KOSystemTest {

    @Inject
    TournamentRepository TournamentRepo;
    @Inject
    TeamRepository TeamRepo;
    @Inject
    MatchRepository MatchRepo;
    @Inject
    NodeRepository NodeRepo;

    @Test
    public void Match_Test() {

        Tournament smallMatch = new Tournament("smallMatch");

        Phase phaseOne = new Phase(1);
        Phase phaseTwo = new Phase(2);

        List<Team> teams = TeamRepo.setTeamsForTournament(4);

        Node nodeOne = new Node(new Match(teams.get(0), teams.get(1)));
        Node nodeTwo = new Node(new Match(teams.get(2), teams.get(3)));

        nodeOne.getCurMatch().setPointsTeam1(2);
        nodeOne.getCurMatch().setPointsTeam2(1);
        MatchRepo.persist(nodeOne.getCurMatch());

        nodeTwo.getCurMatch().setPointsTeam1(0);
        nodeTwo.getCurMatch().setPointsTeam2(2);
        MatchRepo.persist(nodeTwo.getCurMatch());

        Node nodeThree = new Node(new Match(
                nodeOne.getCurMatch().getWinningTeam(),
                nodeTwo.getCurMatch().getWinningTeam()
        ));

        smallMatch.addPhase(phaseOne);
        smallMatch.addPhase(phaseTwo);

        phaseTwo.addNode(nodeOne);
        phaseTwo.addNode(nodeTwo);

        phaseOne.addNode(nodeThree);

        nodeThree.getCurMatch().setPointsTeam1(0);
        nodeThree.getCurMatch().setPointsTeam2(1);
        MatchRepo.persist(nodeThree.getCurMatch());

        nodeThree.setLeftNode(nodeOne);
        nodeThree.setRightNode(nodeTwo);

        NodeRepo.getNodesAsList(nodeThree);

        TeamRepo.persist(teams);
        TournamentRepo.persist(smallMatch);

        Filewriter filewriter = new Filewriter();

        filewriter.writeFinalResult(nodeThree, smallMatch);
    }

    @Test
    public void jsonTest(){
        List<Team> teams = new LinkedList<>();

        teams.add(new Team("Team1", "T1", -1L));
        teams.add(new Team("Team2", "T2", -1L));
        teams.add(new Team("Team3", "T3", -1L));
        teams.add(new Team("Team4", "T4", -1L));

        TeamRepo.persist(teams);

        TournamentRepo.setUpTournament("NoMatch2", teams);
        Tournament json = TournamentRepo.findByName("NoMatch2");

        TournamentRepo.generateMatches("NoMatch2", teams);
    }
}
