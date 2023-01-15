import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";


@Component({templateUrl:'validation.component.html', styleUrls:['validation.component.css']})
export class ValidationComponent{

    message?: string;

    constructor(
        public dialogRef: MatDialogRef<ValidationComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        ){
            this.message = this.data.message;
        }
    
    clickOK(){
        this.dialogRef.close();
    }
}