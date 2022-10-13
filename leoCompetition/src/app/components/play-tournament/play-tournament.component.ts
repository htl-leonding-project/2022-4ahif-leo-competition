import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route } from '@angular/router';
import { Match } from 'src/app/models/match.model';
import { MatchService } from 'src/app/services/match.service';
import { TournamentService } from 'src/app/services/tournament.service';

@Component({
  selector: 'app-play-tournament',
  templateUrl: './play-tournament.component.html',
  styleUrls: ['./play-tournament.component.css']
})
export class PlayTournamentComponent implements OnInit {

  finishedAllMatches:Boolean=false;
  matches:Match[]=[];

  constructor(private route: ActivatedRoute, private tournametnService: TournamentService) { }

  ngOnInit(): void {
    this.route.params.subscribe(
      params => {this.refreshMatches(params['name'])}
    )
  }

  refreshMatches(name: String){
    this.tournametnService.getMatchesForTournament(name).subscribe(
      {next:
        data =>{
          this.matches = data  
        },
        error: error =>{
          alert('Error loading teams');
        }
      });
  }
  
}
