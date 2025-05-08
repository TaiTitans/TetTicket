package com.tetticket.ddd.application.service.export.impl;

import com.tetticket.ddd.application.service.export.TicketExportService;
import com.tetticket.ddd.domain.model.entity.TicketDetail;
import com.tetticket.ddd.domain.service.TicketDetailDomainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class TicketExportServiceImpl implements TicketExportService {

    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    @Override
    public CompletableFuture<Void> exportAllTicketsToExcel(OutputStream outputStream) {
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("Starting ticket export process using virtual thread");
                List<TicketDetail> allTickets = ticketDetailDomainService.getAllTicketDetails();
                
                // Create workbook with virtual threads for processing chunks of data
                try (Workbook workbook = new XSSFWorkbook()) {
                    Sheet sheet = workbook.createSheet("Tickets");
                    
                    // Create header row
                    Row headerRow = sheet.createRow(0);
                    createHeaderRow(headerRow);
                    
                    // Process ticket data in parallel using virtual threads
                    if (!allTickets.isEmpty()) {
                        int chunkSize = Math.max(1, allTickets.size() / 10); // Split into ~10 chunks
                        
                        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                            List<CompletableFuture<Void>> futures = createDataRows(allTickets, sheet, chunkSize, executor);
                            
                            // Wait for all processing to complete
                            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                        }
                    }
                    
                    // Auto-size columns for better readability
                    for (int i = 0; i < 15; i++) {
                        sheet.autoSizeColumn(i);
                    }
                    
                    // Write the workbook to the output stream
                    workbook.write(outputStream);
                    log.info("Ticket export completed successfully");
                } catch (IOException e) {
                    log.error("Error creating Excel file", e);
                    throw new RuntimeException("Failed to create Excel file", e);
                }
            } catch (Exception e) {
                log.error("Error during ticket export", e);
                throw new RuntimeException("Failed to export tickets", e);
            }
        }, Executors.newVirtualThreadPerTaskExecutor());
    }
    
    private void createHeaderRow(Row headerRow) {
        CellStyle headerStyle = headerRow.getSheet().getWorkbook().createCellStyle();
        Font headerFont = headerRow.getSheet().getWorkbook().createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        String[] headers = {
            "ID", "Name", "Description", "Stock Initial", "Stock Available", 
            "Stock Prepared", "Original Price", "Flash Price", "Sale Start Time", 
            "Sale End Time", "Status", "Activity ID", "Updated At", "Created At"
        };
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }
    
    private List<CompletableFuture<Void>> createDataRows(List<TicketDetail> tickets, Sheet sheet, 
                                                        int chunkSize, ExecutorService executor) {
        return com.google.common.collect.Lists.partition(tickets, chunkSize).stream()
            .map(chunk -> CompletableFuture.runAsync(() -> {
                log.debug("Processing chunk of {} tickets", chunk.size());
                for (int i = 0; i < chunk.size(); i++) {
                    TicketDetail ticket = chunk.get(i);
                    // Calculate the actual row index based on the chunk
                    int rowIndex = tickets.indexOf(ticket) + 1; // +1 because header is at index 0
                    Row row = sheet.createRow(rowIndex);
                    
                    row.createCell(0).setCellValue(ticket.getId());
                    row.createCell(1).setCellValue(ticket.getName());
                    row.createCell(2).setCellValue(ticket.getDescription());
                    row.createCell(3).setCellValue(ticket.getStockInitial());
                    row.createCell(4).setCellValue(ticket.getStockAvailable());
                    row.createCell(5).setCellValue(ticket.isStockPrepared());
                    row.createCell(6).setCellValue(ticket.getPriceOriginal());
                    row.createCell(7).setCellValue(ticket.getPriceFlash());
                    
                    if (ticket.getSaleStartTime() != null) {
                        Cell cell = row.createCell(8);
                        cell.setCellValue(ticket.getSaleStartTime());
                    }
                    
                    if (ticket.getSaleEndTime() != null) {
                        Cell cell = row.createCell(9);
                        cell.setCellValue(ticket.getSaleEndTime());
                    }
                    
                    row.createCell(10).setCellValue(ticket.getStatus());
                    row.createCell(11).setCellValue(ticket.getActivityId());
                    
                    if (ticket.getUpdatedAt() != null) {
                        Cell cell = row.createCell(12);
                        cell.setCellValue(ticket.getUpdatedAt());
                    }
                    
                    if (ticket.getCreatedAt() != null) {
                        Cell cell = row.createCell(13);
                        cell.setCellValue(ticket.getCreatedAt());
                    }
                }
            }, executor))
            .toList();
    }
}