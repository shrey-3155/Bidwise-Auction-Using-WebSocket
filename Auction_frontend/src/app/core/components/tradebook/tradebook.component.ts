import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { TradebookService } from '../../services/tradebook-service/tradebook.service';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import { DatePipe } from '@angular/common';
import {formatDate } from '@angular/common';


@Component({
  selector: 'app-tradebook',
  templateUrl: './tradebook.component.html',
  styleUrl: './tradebook.component.css'
})
export class TradebookComponent {
  trades: any = [];
  invoice: any = [];

  constructor(
    private fb: FormBuilder,
    private tradebookservice: TradebookService,
    private route: ActivatedRoute,
    private router: Router
  ){

  }
  ngOnInit(): void {
    this.tradebookservice.getAllTrades().subscribe(
      (response) => {
        this.trades = response;
        console.log(this.trades);
      },
      (error) => {
        this.trades.data = '';
        console.error('Error fetching items:', error);
      }
    );
  }

  getInvoice(auctionId : number):void{
    this.tradebookservice.getInvoice(auctionId).subscribe(
      (response) => {
        this.invoice = response;
        console.log(this.invoice);
        this.generatePDF();

      },
      (error) => {
        console.error('Error fetching items:', error);
      }
    );
  }

  generatePDF(): void {
    const doc = new jsPDF('p', 'mm', 'a4');
    const margin = 10;
    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();
    const effectivePageWidth = pageWidth - margin * 2;
  
    // Add logo to the PDF (assuming you have the logo as a base64 string)
    const logoBase64 = 'https://res.cloudinary.com/dfctfgi4g/image/upload/v1722177292/q5jvnuuybxapaw63jzrr.png'; // Replace '...' with your actual base64 string
  
    // Add the logo image to the PDF
    const logoWidth = 30; // width of the logo
    const logoHeight = 30; // height of the logo
    doc.addImage(logoBase64, 'PNG', margin, margin, logoWidth, logoHeight);
  
    // Add title below the logo
    doc.setFontSize(18);
    doc.setFont('helvetica', 'bold');
    doc.text('Invoice Details', pageWidth / 2, margin + logoHeight + 5, { align: 'center' });
  
    // Add a horizontal line below the title
    doc.setLineWidth(0.5);
    doc.line(margin, margin + logoHeight + 10, pageWidth - margin, margin + logoHeight + 10);
  
    // Add invoice details with better layout and styling
    doc.setFontSize(12);
    doc.setFont('helvetica', 'normal');
  
    const startY = margin + logoHeight + 15;
    const lineSpacing = 10;
    const itemSpacing = 8;
  
    // Section: Auction and Invoice Dates
    doc.setFont('helvetica', 'bold');
    doc.text('Auction and Invoice Dates', margin, startY);
    doc.setFont('helvetica', 'normal');
    doc.text(`Date of Auction: ${formatDate(this.invoice.data.dateOfAuction, 'yyyy-MM-dd', 'en-US')}`, margin, startY + lineSpacing);
    doc.text(`Date of Invoice: ${formatDate(this.invoice.data.dateOfInvoice, 'yyyy-MM-dd', 'en-US')}`, margin, startY + lineSpacing * 2);
  
    // Section: Item Details
    const itemDetailsStartY = startY + lineSpacing * 4;
    doc.setFont('helvetica', 'bold');
    doc.text('Item Details', margin, itemDetailsStartY);
    doc.setFont('helvetica', 'normal');
    doc.text(`Item Category: ${this.invoice.data.itemCategory}`, margin, itemDetailsStartY + lineSpacing);
    doc.text(`Item Name: ${this.invoice.data.itemName}`, margin, itemDetailsStartY + lineSpacing * 2);
    doc.text(`Price to pay: ${this.invoice.data.pricePaid}`, margin, itemDetailsStartY + lineSpacing * 3);
  
    // Section: Seller Information
    const sellerInfoStartY = itemDetailsStartY + lineSpacing * 5;
    doc.setFont('helvetica', 'bold');
    doc.text('Seller Information', margin, sellerInfoStartY);
    doc.setFont('helvetica', 'normal');
    doc.text(`Seller Email: ${this.invoice.data.sellerEmail}`, margin, sellerInfoStartY + lineSpacing);
    doc.text(`Seller Name: ${this.invoice.data.sellerName}`, margin, sellerInfoStartY + lineSpacing * 2);
  
    // Draw a border around each section
    doc.setDrawColor(0, 0, 0);
    doc.setLineWidth(0.1);
    doc.rect(margin, startY - itemSpacing, effectivePageWidth, lineSpacing * 3 + itemSpacing * 2);
    doc.rect(margin, itemDetailsStartY - itemSpacing, effectivePageWidth, lineSpacing * 4 + itemSpacing * 2);
    doc.rect(margin, sellerInfoStartY - itemSpacing, effectivePageWidth, lineSpacing * 3 + itemSpacing * 2);
  
    // Save the PDF
    doc.save('Bidwise_invoice.pdf');
  }
  
}
