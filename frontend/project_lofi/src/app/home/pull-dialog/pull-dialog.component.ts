import { Component, Inject } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MplaylistGenres, MplaylistStatus } from "@app/_constants/playlist.constants";
import { Lofi } from "@app/_models/lofi";
import { Playlist } from "@app/_models/playlist";
import { LofiPool } from "@app/_models/pool";
import { AlertService } from "@app/_services/alert.service";
import { LofiService } from "@app/_services/lofi.service";
import { PlaylistService } from "@app/_services/playlist.service";
import { LofiPoolService } from "@app/_services/pool.service";

@Component({templateUrl: 'pull-dialog.component.html'})
export class PullDialogComponent {
    submitForm!: FormGroup;
    submitted: boolean = false;
    loading: boolean = false;
    isLastStep: boolean = false;
    playlistGenres!: any[];
    playlistStatuses!: any[];
    selectedPlaylist?: Playlist;
    selectedLofiPool?: LofiPool;
    selectedLofiPools?: LofiPool[];
    selectedLofies?: Lofi[];
    
    constructor(
        public dialogRef: MatDialogRef<PullDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private formBuilder: FormBuilder,
        private lofiService: LofiService,
        private lofiPoolService: LofiPoolService,
        private alertService: AlertService,
        private playlistService: PlaylistService
    ){}

    get submitF(){ return this.submitForm.controls; }

    ngOnInit(){
        this.submitForm = this.formBuilder.group(
            {
                lofiPool: ['', Validators.required],
                playlist: ['', Validators.required],
                numberOfLofies: ['', Validators.required]
            }
        );
        this.playlistGenres = Array.from(MplaylistGenres.keys());
        this.playlistStatuses = Array.from(MplaylistStatus.keys());
    }

    clickCancel(): void{
        this.dialogRef.close();
    }

    selectPlaylistToUpdate(playlist: Playlist){
        this.selectedPlaylist = playlist;
        for(let g of MplaylistGenres.keys()){
            if(MplaylistGenres.get(g) == playlist.playlistGenre){
                this.selectedPlaylist.playlistGenre = g;
            }
        }

        for(let s of MplaylistStatus.keys()){
            if(MplaylistStatus.get(s) == playlist.playlistStatus){
                this.selectedPlaylist.playlistStatus = s;
            }
        }

        if(this.selectedPlaylist){
            this.lofiPoolService.getAllLofiPools().subscribe(lofiPools=>{
                this.selectedLofiPools = lofiPools;
            })
        }
    }

    selectLofiPool(lofiPool: LofiPool){
        this.selectedLofiPool = lofiPool;
        if(this.selectedLofiPool.lofiPoolId){
            this.lofiService.getAllLofiesByLofiPoolId(this.selectedLofiPool.lofiPoolId).subscribe(lofies => this.selectedLofies = lofies);
        }
    }

    submitChange(){

        this.alertService.clear();

        if (this.submitForm.invalid){
            return ;
        }

        let playlistId = this.selectedPlaylist?.playlistId;
        let lofiPoolId = this.selectedLofiPool?.lofiPoolId;
        let numOfL = this.submitF['numberOfLofies'].value;

        console.log(playlistId, lofiPoolId, numOfL);

        if(lofiPoolId && playlistId){
            this.playlistService.pullLofiesFromLofiPool(lofiPoolId, numOfL, playlistId)
                .subscribe(
                    next=>{
                        this.alertService.info(numOfL +" lofies from " + this.selectedLofiPool?.lofiPoolName + " have been added to " + this.selectedPlaylist?.playlistName);
                        this.dialogRef.close()
                    }, 
                    error => {
                        this.alertService.error("Unexpected error occurs while pulling "+ numOfL +" lofies from" + this.selectedLofiPool?.lofiPoolName);
                        this.dialogRef.close()
                    });
        } else {
            this.alertService.error("Cannot find lofi pool or playlist");
            this.dialogRef.close()
        }
    }
}