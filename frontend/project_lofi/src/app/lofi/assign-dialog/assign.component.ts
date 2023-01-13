import { Component, Inject } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { MplaylistGenres, MplaylistStatus } from "@app/_constants/playlist.constants";
import { Playlist } from "@app/_models/playlist";
import { AlertService } from "@app/_services/alert.service";
import { PlaylistService } from "@app/_services/playlist.service";
import { AccountService } from "@app/_services/account.service";
import { Lofi } from "@app/_models/lofi";
import { User } from "@app/_models/user";
import { LofiPool } from "@app/_models/pool";
import { LofiPoolService } from "@app/_services/pool.service";

@Component({ templateUrl: 'assign.component.html', styleUrls:['assign.component.css'] })
export class AssignLofiComponent{
    assignToMeForm!: FormGroup;
    assignToUserForm!: FormGroup;
    assignToLofiPoolForm!: FormGroup;

    isAssignToMe: boolean = false;
    isAssignToUser: boolean = false;
    isAssignToLofiPool: boolean = false;

    isWrongUserName: boolean = false;

    lofi!: Lofi | null;
    user!: User | null;
    playlists?: Playlist[];
    lofiPools?: LofiPool[];
    selectedPlaylist?: Playlist;
    selectedLofiPool?: LofiPool;
    userNameToAssign?: string;

    submitted: boolean = false;
    loading: boolean = false;

    reasonForMe = [
        {name: "This is perfect for my playlist"},
        {name: "I would like to have it"},
        {name: "No reason"}
    ];

    reasonForUser = [
        {name: "This is perfect for your playlist"},
        {name: "I would like you to have it"},
        {name: "No reason"}
    ];

    reasonForLofiPool = [
        {name: "This is perfect for this lofi pool"},
        {name: "I would like to store it into the lofi pool"},
        {name: "No reason"}
    ];

    constructor(
        public dialogRef: MatDialogRef<AssignLofiComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private formBuilder: FormBuilder,
        private playlistService: PlaylistService,
        private accountService: AccountService,
        private alertService: AlertService,
        private lofiPoolService: LofiPoolService
    ){
        this.assignToMeForm = this.formBuilder.group({
            reason: ['', Validators.required],
            note: ['', Validators.required]
        });

        this.assignToUserForm = this.formBuilder.group({
            reason: ['', Validators.required],
            note: ['', Validators.required]
        });

        this.assignToLofiPoolForm = this.formBuilder.group({
            reason: ['', Validators.required],
            note: ['', Validators.required]
        });

        this.lofi = this.data.lofi;
        this.user = this.accountService.userValue;
    }

    get assignToMeF() { return this.assignToMeForm.controls; }
    get assignToUserF() { return this.assignToUserForm.controls; }
    get assignToLofiPoolF() { return this.assignToLofiPoolForm.controls; }

    selectAssignToMe(){
        this.playlists = undefined;
        this.selectedPlaylist = undefined;
        if(this.user && this.user.userId){
            this.playlistService.getAllPlaylistByUserId(this.user?.userId).subscribe(playlists => this.playlists = playlists);
            
            this.isAssignToMe = !this.isAssignToMe;
            this.isAssignToLofiPool = false;
            this.isAssignToUser = false;
        }
    }

    selectAssignToUser(){
        this.playlists = undefined;
        this.selectedPlaylist = undefined;
        this.isAssignToMe = false;
        this.isAssignToLofiPool = false;
        this.isAssignToUser = !this.isAssignToUser;
    }

    selectAssignToLofiPool(){
        this.lofiPoolService.getAllLofiPools().subscribe(
            lofiPools => this.lofiPools = lofiPools
        );
        
        this.playlists = undefined;
        this.selectedPlaylist = undefined;
        this.isAssignToMe = false;
        this.isAssignToLofiPool = !this.isAssignToLofiPool
        this.isAssignToUser = false;
    }

    clickCancel(): void {
        this.dialogRef.close();
    }

    clickPlaylistRow(playlist: Playlist){
        if(this.playlists){
            for(let p of this.playlists){
                if(p.isSelected != playlist.playlistId && playlist.isSelected){
                    p.isSelected = false;
                }
            }
        }
        playlist.isSelected = !playlist.isSelected;

        if(playlist.isSelected){
            this.selectedPlaylist = playlist;
        } else {
            this.selectedPlaylist = undefined;
        }
    }
    
    clickLofiPoolRow(lofiPool: LofiPool) {
        if(this.lofiPools){
            for(let l of this.lofiPools){
                if(l.isSelected != lofiPool.lofiPoolId && lofiPool.isSelected){
                    l.isSelected = false;
                }
            }
        }
        lofiPool.isSelected = !lofiPool.isSelected;

        if(lofiPool.isSelected){
            this.selectedLofiPool = lofiPool;
        } else {
            this.selectedLofiPool = undefined;
        }
    }

    clickToGetUserPlaylist(){
        this.isWrongUserName = false;
        this.playlists = undefined;
        this.selectedPlaylist = undefined;

        if (this.userNameToAssign && (this.userNameToAssign !== this.user?.userName)){
            this.accountService.getByUserName(this.userNameToAssign).subscribe(
                user => {
                    if(user && user.userId){
                        this.playlistService.getAllPlaylistByUserId(user.userId).subscribe(
                            playlists => this.playlists = playlists
                        );
                    } else {
                        this.isWrongUserName = true;
                    }
                }
            )
        } else {
            this.isWrongUserName = true;
        }
    }

    submitChange(){
        this.submitted = true;
        if(this.isAssignToMe){
            this.assignToMe();
        } else if(this.isAssignToUser){
            this.assignToUser();
        } else if(this.isAssignToLofiPool){
            this.assignToLofiPool();
        }
    }

    assignToMe(){
        this.alertService.clear();

        if(this.assignToMeForm.invalid){
            return ;
        }

        if (this.lofi && this.lofi.lofiId && this.selectedPlaylist && this.selectedPlaylist.playlistId){
            this.playlistService.assignLofiToPlaylist(this.lofi.lofiId, this.selectedPlaylist.playlistId).subscribe(
                next => {
                    this.alertService.info("The lofi: " + this.lofi?.lofiName +" has been assigned to the playlist: " + this.selectedPlaylist?.playlistName);
                    this.dialogRef.close()},
                error => this.alertService.error("Unexpected error occurs while assigning lofi: " + this.lofi?.lofiName +" to the playlist: " + this.selectedPlaylist?.playlistName)
            )
        }
    }

    assignToUser(){
        this.alertService.clear();

        if(this.assignToUserForm.invalid){
            return ;
        }

        if (this.lofi && this.lofi.lofiId && this.selectedPlaylist && this.selectedPlaylist.playlistId){
            this.playlistService.assignLofiToPlaylist(this.lofi.lofiId, this.selectedPlaylist.playlistId).subscribe(
                next => {
                    this.alertService.info("The lofi: " + this.lofi?.lofiName +" has been assigned to the playlist: " + this.selectedPlaylist?.playlistName);
                    this.dialogRef.close()},
                error => this.alertService.error("Unexpected error occurs while assigning lofi: " + this.lofi?.lofiName +" to the playlist: " + this.selectedPlaylist?.playlistName)
            )
        }
    }

    assignToLofiPool(){
        this.alertService.clear();

        if(this.assignToLofiPoolForm.invalid){
            return ;
        }

        if (this.lofi && this.lofi.lofiId && this.selectedLofiPool && this.selectedLofiPool.lofiPoolId){
            this.lofiPoolService.assignLofiToLofiPool(this.lofi.lofiId, this.selectedLofiPool.lofiPoolId).subscribe(
                next => {
                    this.alertService.info("The lofi: " + this.lofi?.lofiName +" has been assigned to the lofi pool: " + this.selectedLofiPool?.lofiPoolName);
                    this.dialogRef.close()},
                error => this.alertService.error("Unexpected error occurs while assigning lofi: " + this.lofi?.lofiName +" to the lofi pool: " + this.selectedLofiPool?.lofiPoolName)
            )
        }
    }
}