import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MLofiType } from '@app/_constants/lofi.constants';
import { Lofi } from '@app/_models/lofi';
import { User } from '@app/_models/user';
import { AccountService } from '@app/_services/account.service';
import { AlertService } from '@app/_services/alert.service';
import { LofiService } from '@app/_services/lofi.service';

@Component({templateUrl:'lofi.component.html', styleUrls: ['lofi.component.css']})
export class LofiComponent{
    isExpanded = true;
    showSubmenu: boolean = false;
    isShowing = false;
    isSummary: boolean = true;

    user: User | null;
    lofiId!: number;
    selectedLofi?: Lofi;
    lofiType?: string;

    @ViewChild('audio') audio?: ElementRef;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private lofiService: LofiService,
        private alertService: AlertService,
        private accountService: AccountService ) {
            this.user = this.accountService.userValue;
        }
        

    ngOnInit(){
        this.lofiId = Number(this.route.snapshot.paramMap.get('lofiId'));

        this.lofiService.getLofiById(this.lofiId).subscribe(
            next => {
                this.selectedLofi = next;
                for(let key of MLofiType.keys()){
                    if(MLofiType.get(key) == this.selectedLofi.lofiType){
                        this.lofiType = key;
                    }
                }
            },
            error => {
                this.alertService.error("Unexpected error occurs while retrieving lofi data");
            }
        );       

        this.isExpanded = true;
        this.showSubmenu = true;
    }

    
    mouseenter() {
        if (!this.isExpanded) {
        this.isShowing = true;
        }
    }

    mouseleave() {
        if (!this.isExpanded) {
        this.isShowing = false;
        }
    }

    assignLofi(){

    }

    removeLofi(){

    }

    play() {
        if(this.audio){
            this.audio.nativeElement.play();
        }
    }

    pause() {
        if(this.audio){
            this.audio.nativeElement.pause();
        }
    }

    onEnded() {
        console.log('Audio has ended', 'replay again');
        this.play();
    }
}