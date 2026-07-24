package com.example.backend.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import com.example.backend.models.helpers.CourtOccupancyDTO;
import com.example.backend.models.helpers.EquipmentTurnoverDTO;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportGenerator {

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 16, Font.BOLD);
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 11, Font.NORMAL, java.awt.Color.GRAY);
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, java.awt.Color.WHITE);
    private static final Font CELL_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);
    private static final Font TOTAL_FONT = new Font(Font.HELVETICA, 10, Font.BOLD);
    private static final java.awt.Color HEADER_BG = new java.awt.Color(0x37, 0x8A, 0xDD);

    public byte[] generateOccupancyReport(String facilityName, LocalDate monthStart, LocalDate monthEnd,
                                           List<CourtOccupancyDTO> data) throws DocumentException {
        Document doc = new Document(PageSize.A4, 40, 40, 50, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);
        doc.open();

        doc.add(new Paragraph("Court Occupancy Report", TITLE_FONT));
        doc.add(new Paragraph(facilityName, SUBTITLE_FONT));
        doc.add(new Paragraph(formatPeriod(monthStart, monthEnd), SUBTITLE_FONT));
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 2, 2, 2});

        addHeaderCell(table, "Court / Hall");
        addHeaderCell(table, "Booked (h)");
        addHeaderCell(table, "Available (h)");
        addHeaderCell(table, "Occupancy");

        double totalBooked = 0;
        double totalAvailable = 0;

        for (CourtOccupancyDTO c : data) {
            addCell(table, c.getCourtName() + " (" + c.getCourtType() + ")");
            addCell(table, String.format("%.1f", c.getBookedHours()));
            addCell(table, String.format("%.1f", c.getAvailableHours()));
            addCell(table, String.format("%.1f%%", c.getOccupancyPercent()));

            totalBooked += c.getBookedHours();
            totalAvailable += c.getAvailableHours();
        }

        double overallPercent = totalAvailable > 0 ? (totalBooked / totalAvailable) * 100 : 0;

        PdfPCell totalLabel = new PdfPCell(new Phrase("TOTAL", TOTAL_FONT));
        totalLabel.setPadding(6);
        table.addCell(totalLabel);
        addTotalCell(table, String.format("%.1f", totalBooked));
        addTotalCell(table, String.format("%.1f", totalAvailable));
        addTotalCell(table, String.format("%.1f%%", overallPercent));

        doc.add(table);
        doc.close();

        return out.toByteArray();
    }

    public byte[] generateEquipmentReport(LocalDate monthStart, LocalDate monthEnd,
                                           List<EquipmentTurnoverDTO> data) throws DocumentException {
        Document doc = new Document(PageSize.A4, 40, 40, 50, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);
        doc.open();

        doc.add(new Paragraph("Equipment Turnover Report", TITLE_FONT));
        doc.add(new Paragraph(formatPeriod(monthStart, monthEnd), SUBTITLE_FONT));
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4, 2, 2});

        addHeaderCell(table, "Item");
        addHeaderCell(table, "Sold (pcs)");
        addHeaderCell(table, "Revenue (RSD)");

        int totalQty = 0;
        double totalRevenue = 0;

        for (EquipmentTurnoverDTO e : data) {
            addCell(table, e.getEquipmentName());
            addCell(table, String.valueOf(e.getQuantitySold()));
            addCell(table, String.format("%.2f", e.getRevenue()));

            totalQty += e.getQuantitySold();
            totalRevenue += e.getRevenue();
        }

        PdfPCell totalLabel = new PdfPCell(new Phrase("TOTAL", TOTAL_FONT));
        totalLabel.setPadding(6);
        table.addCell(totalLabel);
        addTotalCell(table, String.valueOf(totalQty));
        addTotalCell(table, String.format("%.2f", totalRevenue));

        doc.add(table);
        doc.close();

        return out.toByteArray();
    }

    private String formatPeriod(LocalDate start, LocalDate end) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return "Period: " + start.format(fmt) + " - " + end.format(fmt);
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(HEADER_BG);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, CELL_FONT));
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void addTotalCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, TOTAL_FONT));
        cell.setPadding(6);
        table.addCell(cell);
    }
}