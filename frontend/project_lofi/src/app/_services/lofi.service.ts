import { Injectable, ɵisEnvironmentProviders } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';

import { Lofi } from '@app/_models/lofi';

@Injectable({providedIn:'root'})
export class LofiService{
    private lofiSubject: BehaviorSubject<Lofi | null>;
    private lofi: Observable<Lofi | null>;

    constructor(
        private router: Router,
        private http: HttpClient
    ){
        this.lofiSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('lofi')!));
        this.lofi = this.lofiSubject.asObservable();
    }

    public get lofiValue(){
        return this.lofiSubject.value;
    }

    getAllLofies(){
        return this.http.get<Lofi[]>(`${environment.apiUrl}/lofi/all`);
    }

    getAllLofiesByPlaylistId(playlistId: number){
        return this.http.get<Lofi[]>(`${environment.apiUrl}/lofi/playlistid/${playlistId}`);
    }
    
    getAllLofiesByLofiPoolId(lofiPoolId: number){
        return this.http.get<Lofi[]>(`${environment.apiUrl}/lofi/lofipoolid/${lofiPoolId}`);
    }

    getAllLofiesByUserId(userId: number){
        return this.http.get<Lofi[]>(`${environment.apiUrl}/lofi/userid/${userId}`);
    }

    getLofiById(lofiId: number){
        return this.http.get<Lofi>(`${environment.apiUrl}/lofi/id/${lofiId}`)
            .pipe(map(l => {
                if (lofiId != this.lofiValue?.lofiId){
                    // update local storage
                    localStorage.setItem('lofi', JSON.stringify(l));
                    // publish current lofi to subscribers
                    this.lofiSubject.next(l);
                }
                return l;
            }))
    }

    getAllLofiByKeyword(lofiKeyword: string){
        return this.http.get<Lofi[]>(`${environment.apiUrl}/lofi/keyword/${lofiKeyword}`);
    }

    getAllLofiByLofiType(lofiType: string){
        return this.http.get<Lofi[]>(`${environment.apiUrl}/lofi/type/${lofiType}`);
    }
    
    redirectToHome(){
        localStorage.removeItem('lofi');
        this.lofiSubject.next(null);
        this.router.navigate(['']);
    }
}