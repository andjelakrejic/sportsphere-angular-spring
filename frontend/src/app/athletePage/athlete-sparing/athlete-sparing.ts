import { Component, inject, OnInit } from '@angular/core';
import { AthleteNavBar } from '../athlete-nav-bar/athlete-nav-bar';
import { Athlete } from '../../models/athlete';
import { TeammateService } from '../../services/teammate-service';
import { TeammateAd } from '../../models/teammateAd';
import { TeammateRequest } from '../../models/teammateRequest';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Sport } from '../../models/sport';
import { FacilityService } from '../../services/facility-service';

@Component({
  selector: 'app-athlete-sparing',
  imports: [AthleteNavBar, FormsModule, CommonModule],
  templateUrl: './athlete-sparing.html',
  styleUrl: './athlete-sparing.css',
})
export class AthleteSparing implements OnInit {

  athlete: Athlete = new Athlete();
  activeAds: TeammateAd[] = [];

  allSports: Sport[] = [];
  sportMap: { [key: number]: string } = {}; 
  incomingRequests: { [adId: number]: TeammateRequest[] } = {}; 
  sentRequestAdIds: Set<number> = new Set(); 

  myTeams: TeammateAd[] = [];
  approvedPlayers: { [adId: number]: TeammateRequest[] } = {};

  myRequestStatuses: { [adId: number]: string } = {};
  
  
  // Model za formu novog oglasa
  newAd = {
    sportId: 0,
    city: '',
    date: '',
    timeSlot: '',
    totalPlayersNeeded: 1
  };

  private teammateService = inject(TeammateService);
  private facilityService = inject(FacilityService);

  ngOnInit(): void {
    const athleteData = localStorage.getItem("athlete");
    if (!athleteData) {
      alert("No athlete in local storage");
      return;
    }
    
    this.athlete = JSON.parse(athleteData);
    this.loadActiveAds();
    this.loadMyTeams();

    this.teammateService.getMySentRequests(this.athlete.id).subscribe({
      next: (ids) => {
        this.sentRequestAdIds = new Set(ids);
      },
      error: (err) => console.error("Error loading sent requests", err)
    });

    this.facilityService.getAllSportsObject().subscribe({
      next: (data) => {
        this.allSports = data;
        this.sportMap = this.allSports.reduce((map, sport) => {
          map[sport.id] = sport.name;
          return map;
        }, {} as { [key: number]: string });
      },
      error: (err) => alert("Could not load sports list")
    });

    this.teammateService.getMySentRequestsWithStatus(this.athlete.id).subscribe({
      next: (requests: TeammateRequest[]) => {
        this.myRequestStatuses = requests.reduce((map, req) => {
          map[req.adId] = req.status; // npr. map[5] = 'REJECTED'
          return map;
        }, {} as { [adId: number]: string });
      },
      error: (err) => console.error("Error loading sent requests", err)
    });
  }  

  loadMyTeams(): void {
    this.teammateService.getMyTeams(this.athlete.id).subscribe({
      next: (teams) => {
        this.myTeams = teams;
        this.myTeams.forEach(team => this.loadApprovedPlayers(team.id));
      },
      error: (err) => console.error("Error occurred while loading my teams", err)
    });
  }

  loadApprovedPlayers(adId: number): void {
    this.teammateService.getApprovedPlayers(adId).subscribe({
      next: (players) => {
        this.approvedPlayers[adId] = players;
      },
      error: (err) => console.error(`Error loading approved players for ad ${adId}`, err)
    });
  }

  loadActiveAds(): void {
    this.teammateService.getAllActiveAds().subscribe({
      next: (ads) => {
        // Filtriramo aktivne oglase
        this.activeAds = ads;

        this.activeAds.forEach(ad => {
          if (ad.athleteId === this.athlete.id) {
            this.loadRequestsForAd(ad.id);
          }
        });
      },
      error: (err) => console.error("Error occurred while loading active ads", err)
    });
  }

  loadRequestsForAd(adId: number): void {
    this.teammateService.getTeammateRequests(adId).subscribe({
      next: (requests) => {
        // Prikazuju se samo zahtevi sa statusom PENDING
        this.incomingRequests[adId] = requests.filter(r => r.status === 'PENDING');
      },
      error: (err) => console.error(`Error loading requests for ad ${adId}`, err)
    });
  }

  // Kreiranje novog oglasa
  onCreateAd(): void {
    this.teammateService.createAd(
      this.athlete.id,
      this.newAd.sportId,
      this.newAd.city,
      this.newAd.date,
      this.newAd.timeSlot,
      this.newAd.totalPlayersNeeded
    ).subscribe({
      next: () => {
        alert("Ad successfully uploaded!");
        this.loadActiveAds();
        this.newAd = { sportId: 0, city: '', date: '', timeSlot: '', totalPlayersNeeded: 1 };
      },
      error: () => alert("Error when creating an ad")
    });
  }

  // Osveži mapu statusa nakon slanja novog zahteva
  onSendRequest(adId: number): void {
    this.teammateService.sendRequest(adId, this.athlete.id).subscribe({
      next: (response) => {
        if (response.message === 'OK' || response.message === 'ALREADY_SENT') {
          this.myRequestStatuses[adId] = 'PENDING';
          alert("Request sent!");
        }
      },
      error: () => alert("An error occurred")
    });
  }

  // Odobravanje zahteva
  onApproveRequest(requestId: number, adId: number): void {
    this.teammateService.approveRequest(requestId, adId).subscribe({
      next: () => {
        alert("Request accepted!");
        // Osvežavamo oglase i timove — ako je mesto popunjeno, ad će spasti sa activeAds liste na backu
        this.loadActiveAds();
        this.loadMyTeams();
      },
      error: () => alert("Error when accepting the request")
    });
  }

  // Odbijanje zahteva
  onRejectRequest(requestId: number, adId: number): void {
    this.teammateService.rejectRequest(requestId).subscribe({
      next: () => {
        alert("Request denied");
        this.loadRequestsForAd(adId);
      },
      error: () => alert("Error when denying the request")
    });
  }

  // Vlasnik zatvara oglas ručno
  onCloseAd(adId: number): void {
    this.teammateService.closeAd(adId).subscribe({
      next: () => {
        alert("Ad successfully closed!");
        this.loadActiveAds();
        this.loadMyTeams();
      },
      error: () => alert("Error occurred when closing ad")
    });
  }

  // Vlasnik izbacuje igrača iz odobrene ekipe
  onRemovePlayer(adId: number, athleteId: number): void {
    if (confirm("Are you sure you want to remove this player from the team?")) {
      this.teammateService.removePlayerFromTeam(adId, athleteId).subscribe({
        next: () => {
          // Osvežavamo i liste jer oglas opet može postati aktivan
          this.loadMyTeams();
          this.loadActiveAds();
        },
        error: () => alert("Error occurred while removing the player")
      });
    }
  }

  // Pomoćna provera da li je oglas popunjen (opciono za UI)
  isAdFull(ad: TeammateAd): boolean {
    return ad.missingPlayers <= 0;
  }

  isEventPast(dateStr: string): boolean {
    return new Date(dateStr).getTime() < new Date().setHours(0,0,0,0);
  }

  getTeamStatus(team: TeammateAd): { text: string; cssClass: string } {
    const isPast = new Date(team.date).getTime() < new Date().setHours(0, 0, 0, 0);

    if (isPast) {
      return { text: 'Completed', cssClass: 'status-badge--gray' };
    }

    if (team.missingPlayers <= 0) {
      return { text: 'Full', cssClass: 'status-badge--blue' };
    }

    return { text: 'Recruiting', cssClass: 'status-badge--green' };
  }
}