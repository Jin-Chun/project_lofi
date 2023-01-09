import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { LofiPool } from "@app/_models/pool";
import { environment } from "@environments/environment";

@Injectable({providedIn:'root'})
export class LofiPoolService {

    constructor(
        private router: Router,
        private http: HttpClient
    ){}

    getAllLofiPools(){
        return this.http.get<LofiPool[]>(`${environment.apiUrl}/lofipool/all`);
    }

    getLofiPoolById(lofiPoolId: number){
        return this.http.get<LofiPool>(`${environment.apiUrl}/lofipool/id/${lofiPoolId}`);
    }

    getLofiPoolByName(lofiPoolName: string){
        return this.http.get<LofiPool>(`${environment.apiUrl}/lofipool/name/${lofiPoolName}`);
    }

    assignLofiToLofiPool(lofiId: number, lofiPoolId: number){
        return this.http.post<LofiPool>(`${environment.apiUrl}/lofipool/assign/${lofiId}/to/${lofiPoolId}`, null);
    }

    removeLofiFromLofiPool(lofiId: number, lofiPoolId: number){
        return this.http.post<LofiPool>(`${environment.apiUrl}/lofipool/remove/${lofiId}/from/${lofiPoolId}`, null);
    }
}