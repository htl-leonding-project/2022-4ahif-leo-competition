package at.htl.boundary;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import at.htl.control.TeamRepository;
import at.htl.control.TournamentRepository;
import at.htl.entity.GroupGP;
import at.htl.entity.Node;
import at.htl.entity.Team;
import at.htl.entity.Tournament;
import at.htl.filewriter.Filewriter;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Path("/tournaments")
public class TournamentResource {


    @Inject
    TeamRepository teamRepository;

    @Inject
    TournamentRepository tournamentRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance TeamsSelect();
        public static native TemplateInstance teamSelection();
        public static native TemplateInstance showEndResult(String name);
    }

    @GET
    @Path("/TeamsSelect")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getTeams(){

        return TournamentResource.Templates.TeamsSelect();
    }

    @GET
    @Path("/TeamSelection")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance selectTeam() {
        return TournamentResource.Templates.teamSelection();
    }

    @Path("/showEndResult/{name}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showEndResult(@PathParam("name") String name){
        return TournamentResource.Templates.showEndResult(name);
    }

    public Tournament randomGroups(List<Team> teams, int teamsToCreate){

        Tournament tournament = tournamentRepository.findByName("BierPong");
        if(tournament == null) {
            tournament = new Tournament("BierPong");
        }

        int nrOfTeams = teams.size();

        if(teamsToCreate > nrOfTeams){
            return null;
        }

        Random random = new Random();
        List<Integer> randomNumbers = new LinkedList<>();
        // zufälliges befüllen der Gruppen
        int i = 0;
        while(i < teamsToCreate)
        {
            int randomNumber = random.nextInt(nrOfTeams);
            boolean isDuplicate = false;
            for(int j = 0; j < i; j++)
            {
                if (randomNumbers.contains(randomNumber)) {
                    isDuplicate = true;
                    break;
                }
            }
            if(!isDuplicate)
            {
                randomNumbers.add(randomNumber);
                i++;
            }
        }

        List<Team> listofGroup1 = new ArrayList<>();
        for (int j = 0; j < teamsToCreate; j++) {
            Team team = teams.get(randomNumbers.get(j));
            listofGroup1.add(team);
        }

        /*Team team2 = teams.get(randomNumbers.get(1));
        Team team3 = teams.get(randomNumbers.get(2));
        Team team4 = teams.get(randomNumbers.get(3))
        listofGroup1.add(team2);
        listofGroup1.add(team3);
        listofGroup1.add(team4);*/

        GroupGP group1 = new GroupGP("Gruppe A", listofGroup1);

        tournament.addGroup(group1);
        tournamentRepository.persist(tournament);

        Node finalNode = tournamentRepository.setUpTournament(listofGroup1);
        Filewriter newFile = new Filewriter();
        newFile.writeFinalResult(finalNode, tournament);

        return tournament;
    }

    @POST
    @Transactional
    @Path("create/{nrOfTeams}")
    public Response createGroup(@PathParam("nrOfTeams") int nrOfTeams){
        List<Team> teams = teamRepository.listAll();
        Tournament tournament = randomGroups(teams, nrOfTeams);
        //List<GroupGP> groups = new ArrayList<>(tournament.getGroups());
        return Response
                .temporaryRedirect(URI.create("/groups"))
                .status(301)
                .build();
    }

    @Path("/teamSelection")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response show(
            @Context UriInfo uriInfo
            , @FormParam("name") String name
    ) {
        if (name.equals("")) {

            TournamentResource.Templates.teamSelection();
            return Response.status(301)
                    .location(URI.create("/teams/addTeam"))
                    .build();
        }
        else {
            return Response.status(301)
                    .location(URI.create("/TeamSelection/{name}"))
                    .build();
        }
    }
}
