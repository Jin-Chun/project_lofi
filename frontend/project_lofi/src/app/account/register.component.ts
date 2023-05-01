import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { AccountService } from '../_services/account.service';
import { AlertService } from '../_services/alert.service';
import { User } from '@app/_models/user';
import { UserType } from '@app/_constants/user.constants';

@Component({ templateUrl: 'register.component.html' })
export class RegisterComponent implements OnInit {
    @ViewChild("adminCheck") adminCheck!: ElementRef;
    form!: FormGroup;
    loading = false;
    submitted = false;
    isAdmin = false;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private accountService: AccountService,
        private alertService: AlertService
    ) { }

    ngOnInit() {
        this.form = this.formBuilder.group({
            username: ['', Validators.required],
            email: ['', Validators.required],
            password: ['', [Validators.required, Validators.minLength(6)]],
            isAdmin: [false]
        });
    }

    // convenience getter for easy access to form fields
    get f() { return this.form.controls; }

    onSubmit() {
        this.submitted = true;

        // reset alerts on submit
        this.alertService.clear();

        // stop here if form is invalid
        if (this.form.invalid) {
            return;
        }

        let user = new User();
        user.userName = String(this.f['username'].value);
        user.userPassword = String(this.f['password'].value);
        user.userEmail = String(this.f['email'].value);
        user.userType = this.f['isAdmin'].value? UserType.ADMIN: UserType.GUEST;
        this.loading = true;
        this.accountService.register(user)
            .pipe(first())
            .subscribe({
                next: () => {
                    this.alertService.success('Please check your email to complete registering', { keepAfterRouteChange: true });
                    this.router.navigate(['../login'], { relativeTo: this.route });
                },
                error: error => {
                    this.alertService.error(error);
                    this.loading = false;
                }
            });
    }

    isAdminChecked(){
        this.isAdmin = !this.isAdmin;
        if(this.isAdmin){
            this.adminCheck.nativeElement.checked;
        } else {
            this.adminCheck.nativeElement.unchecked;
        }
    }
}