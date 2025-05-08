package com.tetticket.ddd.application.service.export;

import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

/**
 * Service for exporting tickets to sheet files
 */
public interface TicketExportService {
    
    /**
     * Export all tickets to an Excel file asynchronously using virtual threads
     * @param outputStream The output stream to write the Excel file to
     * @return A CompletableFuture that completes when the export is done
     */
    CompletableFuture<Void> exportAllTicketsToExcel(OutputStream outputStream);
}