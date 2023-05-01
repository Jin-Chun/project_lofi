import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { AlertService } from '@app/_services/alert.service';
import { AccountService } from '@app/_services/account.service';
import { User } from '@app/_models/user';
import { UserType } from '@app/_constants/user.constants';

@Component({ templateUrl: 'add-edit.component.html' })
export class AddEditComponent implements OnInit {
    @ViewChild("adminCheck") adminCheck!: ElementRef;
    form!: FormGroup;
    id?: string;
    title!: string;
    loading = false;
    submitting = false;
    submitted = false;
    isAdmin = false;
    userEmail?: string;
    userName?: string;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private accountService: AccountService,
        private alertService: AlertService
    ) { }

    ngOnInit() {
        // form with validation rules
        this.form = this.formBuilder.group({
            username: ['', Validators.required],
            email: ['', Validators.required],
            // password only required in add mode
            password: ['', [Validators.minLength(6), ...(!this.id ? [Validators.required] : [])]],
            isAdmin: [false]
        });
        
        this.id = this.route.snapshot.params['userid'];
        this.title = 'Add User';
        this.userEmail = '';
        this.userName = '';

        if (this.id) {
            // edit mode
            this.title = 'Edit User';
            this.loading = true;
            this.accountService.getById(Number(this.id))
                .pipe(first())
                .subscribe(x => {
                    this.form.patchValue(x);
                    this.userName = x.userName;
                    this.userEmail = x.userEmail;
                    this.loading = false;
                });
        }
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
        user.userId = Number(this.id);
        user.userName = String(this.f['username'].value);
        user.userEmail = String(this.f['email'].value);
        user.userPassword = String(this.f['password'].value);
        user.userType = this.f['isAdmin'].value? UserType.ADMIN: UserType.GUEST;
        
        this.submitting = true;
        
        this.saveUser(user)
            .pipe(first())
            .subscribe({
                next: () => {
                    this.alertService.success('Please check user email to complete saving the user', { keepAfterRouteChange: true });
                    this.router.navigateByUrl('/users');
                },
                error: error => {
                    this.alertService.error(error);
                    this.submitting = false;
                }
            })
    }

    private saveUser(user: User) {
        // create or update user based on id param
        return this.id
            ? this.accountService.update(user)
            : this.accountService.register(user);
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