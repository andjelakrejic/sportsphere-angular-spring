import { AfterViewInit, Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core';
import { AthleteNavBar } from '../athlete-nav-bar/athlete-nav-bar';
import { Chart } from 'chart.js/auto';
import { ChartService } from '../../services/chart-service';
import { Athlete } from '../../models/athlete';

@Component({
  selector: 'app-athlete-statistics',
  imports: [AthleteNavBar],
  templateUrl: './athlete-statistics.html',
  styleUrl: './athlete-statistics.css',
})
export class AthleteStatistics implements AfterViewInit {

  @ViewChild('barCanvas') barCanvasRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('lineCanvas') lineCanvasRef!: ElementRef<HTMLCanvasElement>;

  private athlete: Athlete = new Athlete();
  public totalSpending: number = 0;
  private monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']; // mora i po godinama... ili samo promeni bazu

  private barChart?: Chart;
  private lineChart?: Chart;

  private chartService = inject(ChartService);

  ngAfterViewInit(): void {
    const userJson = localStorage.getItem("athlete");
    if (!userJson) return;

    this.athlete = JSON.parse(userJson);
    this.loadDashboardData();
  }


  private loadDashboardData(): void {
    this.chartService.countResPerSport(this.athlete.id).subscribe(reserved => {
      this.chartService.countPlayedResPerSport(this.athlete.id).subscribe(played => {
        const labels = Object.keys(reserved);
        const reservedData = labels.map(sport => reserved[sport] ?? 0);
        const playedData = labels.map(sport => played[sport] ?? 0);
        this.renderBarChart(labels, reservedData, playedData);
      });
    });

    this.chartService.reservationsPerMonth(this.athlete.id).subscribe(data => {
      console.log('RAW DATA:', data); 
      const typedData = data as { [key: string]: number };
      const sortedKeys = Object.keys(typedData).sort();
      const labels = sortedKeys.map(key => {
        const [year, month] = key.split('-');
        return `${this.monthNames[parseInt(month) - 1]} ${year}`;
      });
      const values = sortedKeys.map(key => typedData[key]);
      this.renderLineChart(labels, values);
    });

    this.chartService.getTotalEquipmentSpending(this.athlete.id).subscribe(amount => {
      this.totalSpending = amount;
    });
  }

  private renderBarChart(labels: string[], reserved: number[], played: number[]): void {
    this.barChart?.destroy();
    this.barChart = new Chart(this.barCanvasRef.nativeElement, {
      type: 'bar',
      data: {
        labels,
        datasets: [
          { label: 'Reserved', data: reserved, backgroundColor: '#3f51b5' },
          { label: 'Played', data: played, backgroundColor: '#8c9eff' }
        ]
      },
      options: {
        responsive: true,
        scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
      }
    });
  }

  private renderLineChart(labels: string[], values: number[]): void {
    this.lineChart?.destroy();
    this.lineChart = new Chart(this.lineCanvasRef.nativeElement, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: 'Monthly Reservations',
          data: values,
          borderColor: '#ff4081',
          backgroundColor: 'rgba(255, 64, 129, 0.2)',
          fill: true,
          tension: 0.3
        }]
      },
      options: {
        responsive: true,
        scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
      }
    });
  }
}