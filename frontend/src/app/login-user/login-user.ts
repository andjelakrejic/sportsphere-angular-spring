import { Component, inject } from '@angular/core';
import { Athlete } from '../models/athlete';
import { Worker } from '../models/worker';
import { User } from '../models/user';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../services/user-service';
import { CommonModule } from '@angular/common';
import { WorkerService } from '../services/worker-service';


@Component({
  selector: 'app-login-user',
  imports: [FormsModule, RouterLink, CommonModule],
  templateUrl: './login-user.html',
  styleUrl: './login-user.css',
})
export class LoginUser {

  athlete: Athlete = new Athlete()
  worker: Worker = new Worker()
  user: User = new User()

  private router = inject(Router)
  private userService = inject(UserService)
  private workerService = inject(WorkerService)

  username = ""
  password = ""
  type = ""
  message = ""

  ngOnInit(): void {
    this.username = "";
    this.password = "";
    this.message = "";
    this.type = "";
  }


  login() {
    if (this.type === "athlete") {
      console.log(this.username, this.password, this.type)
      this.userService.loginAthlete(this.username, this.password).subscribe({
        next: (data) => {
          localStorage.setItem("athlete", JSON.stringify(data));
          this.router.navigate(["athleteProfile"]);
        },
        error: () => {
          this.message = "Username or password isn't correct";
        }
      });
    } else {
      this.workerService.loginWorker(this.username, this.password).subscribe({
        next: (data) => {
          localStorage.setItem("worker", JSON.stringify(data));
          this.router.navigate(["workerProfile"]);
        },
        error: () => {
          this.message = "Username or password isn't correct";
        }
      });
    }
  }

}
