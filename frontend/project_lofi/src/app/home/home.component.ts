import { Component, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Playlist } from '@app/_models/playlist';
import { PlaylistService } from '@app/_services/playlist.service';
import { first } from 'rxjs';
import { User } from '../_models/user';
import { AccountService } from '../_services/account.service';

@Component({ templateUrl: 'home.component.html', styleUrls: ['home.component.css']})
export class HomeComponent {
    user: User | null;
    selectedPlaylist: Playlist | null;
    playlists?: any[];

    @ViewChild('sidenav') sidenav?: MatSidenav;
    isExpanded = true;
    showSubmenu: boolean = false;
    isShowing = false;
    showSubSubMenu: boolean = false;
    showSubSubMenu_2: boolean = false;
    selected: boolean = false;

    constructor(
        private accountService: AccountService,
        private playlistService: PlaylistService
        ) {
        this.user = this.accountService.userValue;
        this.playlistService.getAllPlaylistByUserId(this.user!.userId!)
            .pipe(first())
            .subscribe(playlists => this.playlists = playlists);
        this.selectedPlaylist = null;
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

    selectPlaylist(playlistId: number): void{
        this.selected = true;
        this.selectedPlaylist = this.playlists!.find(p => p.playlistId === playlistId);
        console.log(this.selectedPlaylist);
    }
}
