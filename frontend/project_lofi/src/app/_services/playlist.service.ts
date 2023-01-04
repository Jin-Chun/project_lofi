import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';

import { Playlist } from '@app/_models/playlist';

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
                    // publish updated playlist to subscribers
                    this.playlistSubject.next(x);
                }
                return x;
            }));
    }

    getPlaylistByName(playlistName: string){
        return this.http.get<Playlist[]>(`${environment.apiUrl}/playlist/name/${playlistName}`);
    }

    addPlaylist(playlist: Playlist){
        return this.http.post<Playlist>(`${environment.apiUrl}/playlist/add`, playlist);
    }

    updatePlaylist(playlist: Playlist){
        return this.http.put<Playlist>(`${environment.apiUrl}/playlist/update`, playlist)
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

    redirectToHome(){
        localStorage.removeItem('playlist');
        this.playlistSubject.next(null);
        this.router.navigate(['']);
    }
}