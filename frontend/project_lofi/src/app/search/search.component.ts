import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { Lofi } from "@app/_models/lofi";
import { AlertService } from "@app/_services/alert.service";
import { LofiService } from "@app/_services/lofi.service";

@Component({templateUrl:"search.component.html"})
export class SearchComponent{
    lofiKeyword = '';
    lofiResults?: any[];
    isLoading:boolean = false;

    constructor(
        private router: Router, 
        private lofiService:LofiService,
        private alertService:AlertService,
        ){}

    search() {
        this.alertService.clear();
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
        this.router.navigate(['/home']);
    }

    clickLofiRow(lofi: Lofi){
        if(lofi && lofi.lofiId){
            this.router.navigate(['/lofi', {lofiId: lofi.lofiId}]);
        }
    }
}