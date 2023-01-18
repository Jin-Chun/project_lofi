import { Component, ViewChild, OnInit, HostListener } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Lofi } from '@app/_models/lofi';
import { Playlist } from '@app/_models/playlist';
import { LofiService } from '@app/_services/lofi.service';
import { PlaylistService } from '@app/_services/playlist.service';
import { first } from 'rxjs';
import { User } from '../_models/user';
import { AccountService } from '../_services/account.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { PlaylistDialogComponent } from './playlist-dialog/playlist-dialog.component';
import { PullDialogComponent } from './pull-dialog/pull-dialog.component';

@Component({ templateUrl: 'home.component.html', styleUrls: ['home.component.css']})
export class HomeComponent implements OnInit{

    @ViewChild('sidenav') sidenav?: MatSidenav;

    user: User | null;
    selectedPlaylist: Playlist | null;
    playlists?: any[];
    lofies?: any[];
    lofiesToDisplay?: any[];
    selectedLofi?: Lofi;
    audio = new Audio();
    
    isExpanded = true;
    showSubmenu: boolean = false;
    isShowing = false;
    showSubSubMenu: boolean = false;
    showSubSubMenu_2: boolean = false;
    showOption: boolean = false;
    isPlaying: boolean = false;

    searchTerm: string = "";

    constructor(
        private accountService: AccountService,
        private playlistService: PlaylistService,
        private lofiService: LofiService,
        private router: Router,
        public dialog: MatDialog
        ) {
            this.user = this.accountService.userValue;
            this.selectedPlaylist = null;
            this.selectIntegratedPlaylists();
        }

    ngOnInit() {
        this.playlists = [];
        this.playlistService.getAllPlaylistByUserId(this.user!.userId!)
            .pipe(first())
            .subscribe(playlists => {
                this.playlists = playlists;
                this.showSubmenu = !this.showSubmenu;
            });        
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

    refresh(){
        if(this.selectedPlaylist && this.selectedPlaylist.playlistId){
            this.selectPlaylist(this.selectedPlaylist.playlistId);
        } else {
            this.selectIntegratedPlaylists();
        }
    }

    selectPlaylist(playlistId: number){
        this.selectedPlaylist = this.playlists!.find(p => p.playlistId === playlistId);
        this.lofiService.getAllLofiesByPlaylistId(playlistId)
            .pipe(first()).subscribe(lofies =>{
                this.lofies = lofies;
                this.lofiesToDisplay = lofies;
            });
    }

    selectIntegratedPlaylists(){
        if(this.lofies){
            this.lofies = [];
        }

        const integratedPlaylist = new Playlist();
        integratedPlaylist.playlistName = "Integrated Playlist"
        this.selectedPlaylist = integratedPlaylist;

        if (this.user && this.user.userId){
            this.lofiService.getAllLofiesByUserId(this.user.userId)
                .pipe(first())
                .subscribe(lofies => {
                    this.lofies = lofies;
                    this.lofiesToDisplay = lofies;
                });
        }
    }

    showRefers(){
        const reference = new Playlist();
        reference.playlistName = "Reference";
        this.selectedPlaylist = reference;

        this.lofiService.getAllLofies().pipe(first()).subscribe(lofies=> {
            this.lofies = lofies;
            this.lofiesToDisplay = lofies;
        });
    }

    dbClickLofiRow(lofi: Lofi){
        this.audio.pause();
        if(lofi && lofi.lofiId){
            this.router.navigate(['/lofi', {lofiId: lofi.lofiId, previous: "home", playlistId: this.selectedPlaylist?.playlistId}]);
        }
    }

    clickLofiRow(lofi: Lofi){
        if(this.lofies){
            for(let l of this.lofies){
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
            
            this.audio.src = lofi.lofiLocation;
            this.audio.play();
        }
    }

    startPlaylistDialog(){        
        let playlistDialogRef = this.dialog.open(PlaylistDialogComponent, {
            width: '35%',
            data: {user: this.user, playlists: this.playlists}
        });

        playlistDialogRef.afterClosed().subscribe(() => {
            this.ngOnInit();
        })
    }

    startPullDialog(){
        let pullDialogRef = this.dialog.open(PullDialogComponent, {
            width: '35%',
            data: {user: this.user, playlists: this.playlists}
        });

        pullDialogRef.afterClosed().subscribe(() => {
            this.refresh()
        })
    }

    goToSearch(){
        this.audio.pause();
        this.router.navigate(['/search', {previous: 'home'}]);
    }

    @HostListener('window:keydown.control.s', ['$event'])
    keyboardShortcutSearch(event: KeyboardEvent){
        event.preventDefault();
        this.goToSearch();
    }

    // ctrl + p -> tap to select option -> space to open option -> tab to select fields -> space to decide -> enter to trigger the button
    @HostListener('window:keydown.control.p', ['$event'])
    keyboardShortcutPlaylist(event: KeyboardEvent){
        event.preventDefault();
        this.startPlaylistDialog();
    }

    @HostListener('window:keydown.control.u', ['$event'])
    keyboardShortcutPull(event: KeyboardEvent){
        event.preventDefault();
        this.startPullDialog();
    }

    sortData(key: string){
        this.lofiesToDisplay?.sort((a, b) =>{
        if (a[key] < b[key]) {
            return -1;
            }
            if (a[key] > b[key]) {
            return 1;
            }
            return 0;
        });
    }

    onSearchTermChange(searchTerm: string){
        this.lofiesToDisplay = this.lofies;
        
        if (this.lofiesToDisplay){

            
            let temp: Lofi[] = [];

            this.lofiesToDisplay.forEach(lofi => {
                if(lofi.lofiName.toLowerCase().includes(searchTerm.toLowerCase())){
                    temp.push(lofi);
                }
            })

            this.lofiesToDisplay = temp;
        }
    }
}
