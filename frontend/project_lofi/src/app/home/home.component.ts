import { Component, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { User } from '../_models/user';
import { AccountService } from '../_services/account.service';

@Component({ templateUrl: 'home.component.html', styleUrls: ['home.component.css']})
export class HomeComponent {
    user: User | null;

    @ViewChild('sidenav') sidenav?: MatSidenav;
    isExpanded = true;
    showSubmenu: boolean = false;
    isShowing = false;
    showSubSubMenu: boolean = false;

    constructor(private accountService: AccountService) {
        this.user = this.accountService.userValue;
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
}
