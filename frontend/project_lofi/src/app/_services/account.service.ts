import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { User } from '../_models/user';
import { Playlist } from '@app/_models/playlist';

@Injectable({ providedIn: 'root' })
export class AccountService {
    private userSubject: BehaviorSubject<User | null>;
    public user: Observable<User | null>;

    constructor(
        private router: Router,
        private http: HttpClient
    ) {
        this.userSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('user')!));
        this.user = this.userSubject.asObservable();
    }

    public get userValue() {
        return this.userSubject.value;
    }

    login(userName: string, userPassword: string) {
        return this.http.post<User>(`${environment.apiUrl}/user/login/${userName}/${userPassword}`, null)
            .pipe(map(user => {
                // store user details and jwt token in local storage to keep user logged in between page refreshes
                localStorage.setItem('user', JSON.stringify(user));
                this.userSubject.next(user);
                return user;
            }));
    }

    logout() {
        // remove user from local storage and set current user to null
        localStorage.removeItem('user');
        this.userSubject.next(null);
        this.router.navigate(['/account/login']);
    }

    register(user: User) {
        return this.http.post(`${environment.apiUrl}/user/add`, user);
    }

    getAll() {
        return this.http.get<User[]>(`${environment.apiUrl}/user/all`);
    }

    getById(id: number) {
        return this.http.get<User>(`${environment.apiUrl}/user/id/${id}`);
    }

    update(user: User) {
        return this.http.put(`${environment.apiUrl}/user/update`, user)
            .pipe(map(x => {
                // update stored user if the logged in user updated their own record
                if (user.userId == this.userValue?.userId) {
                    // update local storage
                    localStorage.setItem('user', JSON.stringify(user));

                    // publish updated user to subscribers
                    this.userSubject.next(user);
                }
                return x;
            }));
    }

    delete(user: User) {
        return this.http.post(`${environment.apiUrl}/user/delete`, user)
            .pipe(map(x => {
                // auto logout if the logged in user deleted their own record
                if (user.userId == this.userValue?.userId) {
                    this.logout();
                }
                return x;
            }));
    }

    createPlaylistForUser(playlist: Playlist, userId: number){
        return this.http.post(`${environment.apiUrl}/user/create/playlist/${userId}`, playlist);
    }

    removePlaylistFromUser(playlistId: number, userId: number){
        return this.http.post(`${environment.apiUrl}/user/remove/playlist/${playlistId}/from/${userId}`, null);
    }
}