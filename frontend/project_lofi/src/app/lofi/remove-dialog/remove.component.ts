import { Component, Inject } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { ValidationComponent } from "@app/_components/validation/validation.component";
import { MGenresPlaylist, MStatusPlaylist } from "@app/_constants/playlist.constants";
import { Lofi } from "@app/_models/lofi";
import { Playlist } from "@app/_models/playlist";
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
        private alertService: AlertService,
        public dialog: MatDialog
    ){
        this.removeForm = this.formBuilder.group(
            {
                selectedReason: ['', Validators.required],
                note: ['', Validators.required]
            });
        
        this.playlistService.getPlaylistById(this.data.playlistId).subscribe(playlist => 
            {
                this.selectedPlaylist = playlist;
                if(this.selectedPlaylist.playlistGenre){
                    this.selectedPlaylist.playlistGenre = MGenresPlaylist.get(this.selectedPlaylist.playlistGenre);
                }
                if(this.selectedPlaylist.playlistStatus){
                    this.selectedPlaylist.playlistStatus = MStatusPlaylist.get(this.selectedPlaylist.playlistStatus);
                }
            });
        this.lofi = this.data.lofi;
    }

    get removeF(){ return this.removeForm.controls; }
    
    submitRemove(){

        this.alertService.clear();
        
        if (this.removeForm.invalid){
            this.dialog.open(ValidationComponent,
                {
                    width: '25%',
                    data: {message: "Please fill out all the mandatory fields"}
                });
            return ;
        }

        if(this.lofi && this.selectedPlaylist?.playlistId){
            this.playlistService.removeLofiFromPlaylist(this.lofi, this.selectedPlaylist.playlistId).subscribe(
                () => {
                    this.alertService.info("The lofi " + this.lofi?.lofiName +" has been removed from the playlist " + this.selectedPlaylist?.playlistName, {autoClose: true});
                    this.dialogRef.close()
                }
            )
        } else {
            this.alertService.error("Unexpected error occurs while removing the lofi " + this.lofi?.lofiName +"from the playlist " + this.selectedPlaylist?.playlistName);
        }
    }

    clickCancel(): void{
        this.dialogRef.close();
    }
}