import { Component, Inject } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Lofi } from "@app/_models/lofi";
import { Playlist } from "@app/_models/playlist";
import { AccountService } from "@app/_services/account.service";
import { AlertService } from "@app/_services/alert.service";
import { PlaylistService } from "@app/_services/playlist.service";


@Component({templateUrl: 'remove.component.html', styleUrls:['remove.component.css']})
export class RemoveLofiComponenet{
    selectedPlaylist?: Playlist;
    lofi?: Lofi;
    removeForm!: FormGroup;
    submitted: boolean = false;
    loading: boolean = false;
    reasons = [
        {name: "No reason", value: "01"},
        {name: "This lofi doesn't fit to this playlist", value: "02"},
        {name: "I don't like this lofi", value: "03"},
    ]

    constructor(
        public dialogRef: MatDialogRef<RemoveLofiComponenet>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private formBuilder: FormBuilder,
        private playlistService: PlaylistService,
        private accountService: AccountService,
        private alertService: AlertService,
    ){
        this.removeForm = this.formBuilder.group(
            {
                selectedReason: ['', Validators.required],
                note: ['', Validators.required]
            });
        
        this.playlistService.getPlaylistById(this.data.playlistId).subscribe(playlist => this.selectedPlaylist = playlist);
        this.lofi = this.data.lofi;
    }

    get removeF(){ return this.removeForm.controls; }
    
    submitRemove(){

        this.alertService.clear();
        
        if(this.removeForm.invalid){
            return ;
        }

        if(this.lofi && this.selectedPlaylist?.playlistId){
            this.playlistService.removeLofiFromPlaylist(this.lofi, this.selectedPlaylist.playlistId).subscribe(
                () => {this.dialogRef.close()}
            )
        } else {
            this.alertService.error("Unexpected error occurs while removing the lofi " + this.lofi?.lofiName +"from the playlist " + this.selectedPlaylist?.playlistName);
        }
    }

    clickCancel(): void{
        this.dialogRef.close();
    }
}