import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../services/user-service';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css',
})

export class Header {
  menuOpen = false;

  constructor() {}

  private router = inject(Router)

  openLogin() {
    this.router.navigate(['/loginUser']);
  }

  openRegister() {
    this.router.navigate(['/register']);
  }
}
