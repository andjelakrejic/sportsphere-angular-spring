import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AdminService } from '../services/admin-service';

@Component({
  selector: 'app-login-admin',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login-admin.html',
  styleUrl: './login-admin.css',
})
export class LoginAdmin {
  username = ""
  password = ""
  message = ""

  private router = inject(Router)
  private adminService = inject(AdminService)

  ngOnInit(): void {
    this.username = "";
    this.password = "";
    this.message = "";
  }

  login() {
    this.adminService.loginAdmin(this.username, this.password).subscribe({
      next: (data) => {
        if (data == null) {
          this.message = "Username or password isn't correct";
        } else {
          localStorage.setItem("admin", JSON.stringify(data));
          this.message = "";
          this.router.navigate(["/adminAccounts"]);
        }
      },
      error: () => {
        this.message = "Username or password isn't correct";
      }
    });
  }
}