import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { Lofi } from "@app/_models/lofi";
import { AlertService } from "@app/_services/alert.service";
import { LofiService } from "@app/_services/lofi.service";

@Component({templateUrl:"search.component.html", styleUrls:["search.component.css"]})
export class SearchComponent{
    lofiKeyword = '';
    lofiResults?: any[];
    isLoading:boolean = false;
    isPlaying:boolean = false;
    audio = new Audio();

    constructor(
        private router: Router, 
        private lofiService:LofiService,
        private alertService:AlertService,
        ){}

    search() {
        this.alertService.clear();
        this.audio.pause();
        console.log("Searching", this.lofiKeyword);

        if(this.lofiKeyword){
            this.lofiService.getAllLofiByKeyword(this.lofiKeyword).subscribe(
                lofies => {
                    this.lofiResults = lofies
                },
                error => this.alertService.info("No lofies found!"));
        }
    }

    toHome(){
        this.audio.pause();
        this.router.navigate(['/home']);
    }

    clickLofiRow(lofi: Lofi){
        if(this.lofiResults){
            for(let l of this.lofiResults){
                if(l.lofiId != lofi.lofiId && l.isSelected){
                    l.isSelected = false;
                }
            }
        }
        lofi.isSelected = !lofi.isSelected;

        this.play(lofi);
    }

    play(lofi: Lofi){
        if(this.isPlaying){
            this.audio.pause();
        } else {
            this.isPlaying = !this.isPlaying;
        }
        if(lofi && lofi.lofiLocation && lofi.isSelected){
            console.log(lofi.lofiLocation);
            this.audio.src = lofi.lofiLocation;
            this.audio.play();
        }
    }
    
    dbClickLofiRow(lofi: Lofi){
        this.audio.pause();
        if(lofi && lofi.lofiId){
            this.router.navigate(['/lofi', {lofiId: lofi.lofiId, previous: "search"}]);
        }
    }
}