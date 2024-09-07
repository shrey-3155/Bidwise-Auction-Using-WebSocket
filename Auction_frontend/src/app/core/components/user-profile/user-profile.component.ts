import { Component, OnInit } from '@angular/core';
import { ChartType, ChartOptions, ChartDataset, Color } from 'chart.js';
import { UserProfileService } from '../../services/user-profile/user-profile.service';
import { AccountServiceService } from '../../services/account-service/account-service.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css',
})
export class UserProfileComponent implements OnInit {
  currentChart: string = 'chart1';
  funds: number = 0;  // To store the fetched funds

  // User data
  userProfile: any = {};
  auctionsParticipated: number = 0;

  // Chart 1 - Bar Chart
  public barChartOptions: ChartOptions = {
    responsive: true,
  };
  public barChartLabels: string[] = [];
  public barChartType: ChartType = 'bar';
  public barChartLegend = true;
  public barChartData: ChartDataset<'bar'>[] = [
    { data: [], label: 'Won Bids' },
    { data: [], label: 'Lost Bids' }
  ];

  // Chart 2 - Pie Chart
  public pieChartOptions: ChartOptions = {
    responsive: true,
  };
  public pieChartLabels: string[] = [];
  public pieChartType: ChartType = 'pie';
  public pieChartLegend = true;
  public pieChartData: ChartDataset<'pie'>[] = [
    { data: [] }
  ];

  constructor(private userProfileService: UserProfileService,
              private accountService: AccountServiceService
  ) {}

  ngOnInit(): void {
    const userId = 1; // Replace with the actual user ID

    this.userProfileService.getUserProfile(userId).subscribe(data => {
      this.userProfile = data.data;
    });

    this.userProfileService.getAuctionParticipation(userId).subscribe(data => {
      this.auctionsParticipated = data.data;
    });

    this.userProfileService.getBidStats(userId).subscribe(data => {
      const bidStats = data.data;
      const wonAuctions: number[] = [];
      const lostAuctions: number[] = [];
      const labels: string[] = [];
    
      for (let i = 1; i <= 12; i++) {
        const stat = bidStats.find((s: any) => s.month === i) || { wonAuctions: 0, totalParticipatedAuctions: 0 };
        wonAuctions.push(stat.wonAuctions);
        lostAuctions.push(stat.totalParticipatedAuctions - stat.wonAuctions);
        labels.push(new Date(0, i - 1).toLocaleString('default', { month: 'long' }));
      }
    
      this.barChartLabels = labels;
      this.barChartData[0].data = wonAuctions;
      this.barChartData[1].data = lostAuctions;
    });    

    this.userProfileService.getCategoryBidStats(userId).subscribe(data => {
      const categoryStats = data.data;
      const labels: string[] = [];
      const counts: number[] = [];

      categoryStats.forEach((stat: any) => {
        labels.push(stat.categoryName);
        counts.push(stat.bidCount);
      });

      this.pieChartLabels = labels;
      this.pieChartData[0].data = counts;
    });

    // Fetch account balance
    this.accountService.getAccountFunds().subscribe((response: any) => {
      this.funds = response.data;
    });
  }

  showChart(chartId: string): void {
    this.currentChart = chartId;
  }
}
