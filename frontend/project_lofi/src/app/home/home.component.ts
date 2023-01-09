import { Component, ViewChild, OnInit } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Lofi } from '@app/_models/lofi';
import { Playlist } from '@app/_models/playlist';
import { LofiService } from '@app/_services/lofi.service';
import { PlaylistService } from '@app/_services/playlist.service';
import { first } from 'rxjs';
import { User } from '../_models/user';
import { AccountService } from '../_services/account.service';
import { Router } from '@angular/router';
import { StreamState } from '@app/_interfaces/stream.state';
import { MatDialog } from '@angular/material/dialog';
import { PlaylistDialogComponent } from './playlist-dialog/playlist-dialog.component';

@Component({ templateUrl: 'home.component.html', styleUrls: ['home.component.css']})
export class HomeComponent implements OnInit{

    @ViewChild('sidenav') sidenav?: MatSidenav;

    user: User | null;
    selectedPlaylist: Playlist | null;
    playlists?: any[];
    lofies?: any[];
    state?: StreamState;
    selectedLofi?: Lofi;
    
    isExpanded = true;
    showSubmenu: boolean = false;
    isShowing = false;
    showSubSubMenu: boolean = false;
    showSubSubMenu_2: boolean = false;
    showOption: boolean = false;

    constructor(
        private accountService: AccountService,
        private playlistService: PlaylistService,
        private lofiService: LofiService,
        private router: Router,
        public dialog: MatDialog
        ) {
            this.user = this.accountService.userValue;
            this.selectedPlaylist = null;
        }

    ngOnInit() {
        this.playlistService.getAllPlaylistByUserId(this.user!.userId!)
            .pipe(first())
            .subscribe(playlists => this.playlists = playlists);
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

    selectPlaylist(playlistId: number){
        this.selectedPlaylist = this.playlists!.find(p => p.playlistId === playlistId);
        this.lofiService.getAllLofiesByPlaylistId(playlistId)
            .pipe(first()).subscribe(lofies => this.lofies = lofies);
    }

    selectIntegratedPlaylists(){
        const integratedPlaylist = new Playlist();
        integratedPlaylist.playlistName = "Integrated Playlist"
        this.selectedPlaylist = integratedPlaylist;

        if (this.user && this.user.userId){
            this.lofiService.getAllLofiesByUserId(this.user.userId)
                .pipe(first())
                .subscribe(lofies => this.lofies = lofies);
        }
    }

    showReferences(){
        const reference = new Playlist();
        reference.playlistName = "Reference";
        this.selectedPlaylist = reference;

        this.lofiService.getAllLofies().pipe(first()).subscribe(lofies=> this.lofies = lofies);
    }

    clickLofiRow(lofi: Lofi, playlistId?: number){
        if(playlistId){
            console.log(lofi.lofiName + ":" +playlistId);    
        } else {
            console.log(lofi.lofiName);
        }
        this.router.navigateByUrl(`/lofi/id/${lofi.lofiId}`);
    }

    startPlaylistDialog(){
        
        let playlistDialogRef = this.dialog.open(PlaylistDialogComponent, {
            width: '400px',
            data: {user: this.user, playlists : this.playlists}
        });

        playlistDialogRef.afterClosed().subscribe(() => {
            this.ngOnInit();
        })
    }

}
