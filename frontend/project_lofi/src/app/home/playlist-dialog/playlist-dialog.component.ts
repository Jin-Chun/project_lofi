import { Component, Inject } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MplaylistGenres, MplaylistStatus, PlaylistStatus } from "@app/_constants/playlist.constants";
import { Playlist } from "@app/_models/playlist";
import { AlertService } from "@app/_services/alert.service";
import { PlaylistService } from "@app/_services/playlist.service";
import { formatDate } from "@angular/common";
import { AccountService } from "@app/_services/account.service";
import { Lofi } from "@app/_models/lofi";
import { LofiService } from "@app/_services/lofi.service";
import { first } from "rxjs";

@Component({ templateUrl: 'playlist-dialog.component.html' })
export class PlaylistDialogComponent {
    createForm!: FormGroup;
    deleteForm!: FormGroup;
    isCreate: boolean = false;
    isDelete: boolean = false;
    submitted: boolean = false;
    loading: boolean = false;
    playlistGenres!: any[];
    playlistStatuses!: any[];
    selectedPlaylist?: Playlist;
    selectedPlaylistLofies?: Lofi[];
    
    constructor(
        public dialogRef: MatDialogRef<PlaylistDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private formBuilder: FormBuilder,
        private playlistService: PlaylistService,
        private accountService: AccountService,
        private alertService: AlertService,
        private lofiService: LofiService
        ){}
    
    ngOnInit() {
        this.createForm = this.formBuilder.group({
            playlistName: ['', Validators.required],
            playlistGenre: ['', Validators.required],
            playlistStatus: ['', Validators.required]
        });

        this.deleteForm = this.formBuilder.group({
            playlist: ['', Validators.required]
        })

        this.playlistGenres = Array.from(MplaylistGenres.keys());
        this.playlistStatuses = Array.from(MplaylistStatus.keys());
    }

    get createF() { return this.createForm.controls; }
    get deleteF() { return this.deleteForm.controls; }

    clickCancel(): void {
        this.dialogRef.close();
    }

    selectCreate(){
        this.isCreate = !this.isCreate;
        this.isDelete = false;

    }

    selectDelete(){
        this.isDelete = !this.isDelete;
        this.isCreate = false;

    }


    submitChange(){
        this.submitted = true;

        if(this.isCreate){
            this.createPlaylist();
        } else if(this.isDelete){
            this.deletePlaylist();
        }
    }

    deletePlaylist(){
        this.alertService.clear();

        if (this.deleteForm.invalid){
            return ;
        }
        let playlistToDelete = this.deleteF['playlist'].value;

        this.accountService.removePlaylistFromUser(playlistToDelete.playlistId, this.data.user.userId).pipe().subscribe(() => {
            this.alertService.info("The playlist: "+playlistToDelete.playlistName+" has been deleted!");
            this.dialogRef.close()});
    }

    createPlaylist(){
        this.alertService.clear();

        if (this.createForm.invalid){
            return ;
        }

        let newPlaylist = new Playlist();
        newPlaylist.playlistName = String(this.createF['playlistName'].value);

        let genre = String(this.createF['playlistGenre'].value);
        newPlaylist.playlistGenre = MplaylistGenres.get(genre);

        
        let today = new Date();
        newPlaylist.playlistCreated = formatDate(today, 'yyyy-MM-dd hh:mm:ss', 'en-US').replace(' ','T');
        newPlaylist.playlistUpdated = newPlaylist.playlistCreated;

        let playlistStatus = MplaylistStatus.get(String(this.createF['playlistStatus'].value));
        newPlaylist.playlistStatus = playlistStatus;
       
        this.accountService.createPlaylistForUser(newPlaylist, this.data.user.userId).pipe().subscribe(()=>{
            this.alertService.info("The new playlist: "+newPlaylist.playlistName+" has been created!");
            this.dialogRef.close()})
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

        if(playlist.playlistId){
            this.lofiService.getAllLofiesByPlaylistId(playlist.playlistId).pipe(first()).subscribe(lofies => this.selectedPlaylistLofies = lofies);
        }
    }
}