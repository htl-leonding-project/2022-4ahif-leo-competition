import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Match } from 'src/app/models/match.model';
import { TournamentService } from 'src/app/services/tournament.service';
import { MatchService } from 'src/app/services/match.service';

@Component({
  selector: 'app-play-tournament',
  templateUrl: './play-tournament.component.html',
  styleUrls: ['./play-tournament.component.css']
})
export class PlayTournamentComponent implements OnInit {

  finishedAllMatches:Boolean=false;
  tournamentName = ""
  matches:Match[]=[];
  selected:Match = {
    id: 0,
    team1: {id: 0, name:"", abbr:"", winAmount:0},
    team2: {id: 0, name:"", abbr:"", winAmount:0},
    pointsTeam1: 0,
    pointsTeam2: 0,
    finished: false,
    phase: 0
  }

  constructor(
    private route: ActivatedRoute,
    private router:Router, 
    private tournamentService: TournamentService,
    private matchService: MatchService) { }

  ngOnInit(): void {
    this.route.params.subscribe(
      params => {
        this.tournamentName = params['name'];
        this.checkForTournamentCompletion();
        this.refreshMatches(this.tournamentName);
      }
    )
    
  }

  refreshMatches(name: String){
    this.tournamentService.getMatchesForTournament(name).subscribe(
      {next:
        data =>{
          this.matches = data
          this.matches.sort((a,b) => b.phase - a.phase)
        },
        error: error =>{
          alert('Error loading matches');
        }
      });
  }

  finishTournament():void{
    this.tournamentService.createDiagram(this.tournamentName).subscribe({
      next:
        data =>{
          console.log("Generated Diagram");
          this.router.navigate(['/result/'+this.tournamentName.replace(' ', '_')]);
        },
        error: error =>{
          alert('Error generating diagram')
        }
    })
  }

  clicked(){

    if(this.selected.pointsTeam1 != this.selected.pointsTeam2 && 
      this.selected.pointsTeam1 <= 10 && this.selected.pointsTeam2 <= 10 &&
      this.selected.pointsTeam1 >= 0 && this.selected.pointsTeam2 >= 0){
      this.matchService.updateMatch(this.selected).subscribe({
        next:
          data =>{   
            this.refreshMatches(this.tournamentName);
            this.checkForTournamentCompletion();
          },
          error: error =>{
            alert('Error loading matches');
          }
      })

      this.selected={
        id: 0,
        team1: {id: 0, name:"", abbr:"", winAmount:0},
        team2: {id: 0, name:"", abbr:"", winAmount:0},
        pointsTeam1: 0,
        pointsTeam2: 0,
        finished: false,
        phase: 0
      }
    }
    else{
      alert('Only points ranging 0-10 and not equal allowed')
    }
  }

  select(match:Match){
    this.selected=match;
  }

  checkForTournamentCompletion(){
    
    this.tournamentService.isLastMatchDone(this.tournamentName).subscribe({
      next:
        data =>{
          this.finishedAllMatches = data;
      },
      error: error =>{
        alert('Error checking for completion');
      }
    });
  }
}
