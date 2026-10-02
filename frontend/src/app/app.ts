import { Component, signal } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { Footer } from './footer/footer';
import { filter } from 'rxjs';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Footer],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  hideFooterRoutes: string[] = ['/loginUser', '/register', '/system-access', '/forgottenPassword'];
  showFooter: boolean = true;


  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.showFooter = !this.hideFooterRoutes.some(route => event.urlAfterRedirects.startsWith(route));
    });
  }
}
