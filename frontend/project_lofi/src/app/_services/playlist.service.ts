import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';

import { Playlist } from '@app/_models/playlist';
import { PlaylistLofiAssignment } from '@app/_models/playlistLofiAssignment';
import { Lofi } from '@app/_models/lofi';

@Injectable({ providedIn: 'root' })
export class PlaylistService {
    private playlistSubject: BehaviorSubject<Playlist | null>;
    private playlist: Observable<Playlist | null>;

    constructor(
        private router: Router,
        private http: HttpClient
    ){
        this.playlistSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('playlist')!));
        this.playlist = this.playlistSubject.asObservable();
    }

    public get playlistValue(){
        return this.playlistSubject.value;
    }

    getAllPlaylistByUserId(userId: number){
        return this.http.get<Playlist[]>(
        `${environment.apiUrl}/playlist/userid/${userId}`
        );
    }

    getPlaylistById(playlistId: number){
        return this.http.get<Playlist>(`${environment.apiUrl}/playlist/id/${playlistId}`)
            .pipe(map(x => {
                if (playlistId != this.playlistValue?.playlistId){
                    // update local storage
                    localStorage.setItem('playlist', JSON.stringify(x));
                    // publish current playlist to subscribers
                    this.playlistSubject.next(x);
                }
                return x;
            }));
    }

    getAllPlaylistLofiAssignmentsByPlaylistId(playlistId: number){
        return this.http.get<PlaylistLofiAssignment[]>(`${environment.apiUrl}/playlist/assignment/${playlistId}`);
    }

    getPlaylistByName(playlistName: string){
        return this.http.get<Playlist[]>(`${environment.apiUrl}/playlist/name/${playlistName}`);
    }

    addPlaylist(playlist: Playlist){
        return this.http.post<Playlist>(`${environment.apiUrl}/playlist/add`, playlist);
    }

    updatePlaylistForUser(playlist: Playlist, userId: number){
        return this.http.post<Playlist>(`${environment.apiUrl}/playlist/update/for/${userId}`, playlist)
            .pipe(map(x => {
                // update stored playlist if the current playlist is updated
                if(playlist.playlistId == this.playlistValue?.playlistId){
                    // update local storage
                    localStorage.setItem('playlist', JSON.stringify(playlist));
                    // publish updated playlist to subscribers
                    this.playlistSubject.next(playlist);
                }
                return x;
            }));
    }

    deletePlaylist(playlist: Playlist){
        return this.http.post(`${environment.apiUrl}/playlist/delete`, playlist)
            .pipe(map(x => {
                // head to home automatically if the deleted playlist is the current one.
                if(playlist.playlistId == this.playlistValue?.playlistId){
                    this.redirectToHome();
                }
                return x;
            }))
    }

    pullLofiesFromLofiPool(lofiPoolId: number, numOfLofies: number, playlistId: number){
        return this.http.post<any[]>(`${environment.apiUrl}/playlist/pull/${numOfLofies}/from/${lofiPoolId}/for/${playlistId}`, null);
    }

    removeLofiFromPlaylist(lofi: Lofi, playlistId: number){
        return this.http.post<any>(`${environment.apiUrl}/playlist/remove/lofi/from/${playlistId}`, lofi);
    }

    assignLofiToPlaylist(lofiId: number, playlistId: number){
        return this.http.post<any>(`${environment.apiUrl}/playlist/assign/${lofiId}/to/${playlistId}`, null);
    }

    redirectToHome(){
        localStorage.removeItem('playlist');
        this.playlistSubject.next(null);
        this.router.navigate(['']);
    }
}