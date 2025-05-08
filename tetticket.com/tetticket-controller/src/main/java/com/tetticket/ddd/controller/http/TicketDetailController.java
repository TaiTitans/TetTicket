package com.tetticket.ddd.controller.http;

import com.tetticket.ddd.application.model.TicketDetailDTO;
import com.tetticket.ddd.application.service.export.TicketExportService;
import com.tetticket.ddd.application.service.ticket.TicketDetailAppService;
import com.tetticket.ddd.controller.model.enums.ResultUtil;
import com.tetticket.ddd.controller.model.vo.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketDetailController {

    // CALL Service Application
    @Autowired
    private TicketDetailAppService ticketDetailAppService;

    @Autowired
    private TicketExportService ticketExportService;

    @GetMapping("/ping/java")
    public ResponseEntity<Object> ping() throws InterruptedException {
        // Giả lập tác vụ mất thời gian
        Thread.sleep(1000);  // Giống như time.Sleep(1 * time.Second)

        // Trả về response với status OK
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response("OK"));
    }

    // Lớp Response để trả về JSON response
    public static class Response {
        private String status;

        public Response(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    /**
     * Get ticket detail
     * @param ticketId
     * @param detailId
     * @return ResultUtil
     */
    @GetMapping("/{ticketId}/detail/{detailId}")
    public ResultMessage<TicketDetailDTO> getTicketDetail(
            @PathVariable("ticketId") Long ticketId,
            @PathVariable("detailId") Long detailId,
            @RequestParam(name = "version", required = false) Long version
    ) {
        return ResultUtil.data(ticketDetailAppService.getTicketDetailById(detailId, version));
    }

    /**
     * order by User
     * @param ticketId
     * @param detailId
     * @return ResultUtil
     */
    @GetMapping("/{ticketId}/detail/{detailId}/order")
    public boolean orderTicketByUser(
            @PathVariable("ticketId") Long ticketId,
            @PathVariable("detailId") Long detailId
    ) {
        return ticketDetailAppService.orderTicketByUser(detailId);
    }

    /**
     * Export all tickets to Excel file
     * Uses virtual threads for improved performance and concurrency
     * @return Excel file as a streaming response
     */
    @GetMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> exportAllTicketsToExcel() {
        log.info("Received request to export all tickets to Excel");

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "tickets_export_" + timestamp + ".xlsx";

        StreamingResponseBody responseBody = outputStream -> {
            try {
                // Use virtual threads via the export service
                ticketExportService.exportAllTicketsToExcel(outputStream).join();
            } catch (Exception e) {
                log.error("Error during ticket export streaming", e);
                throw new RuntimeException("Failed to export tickets", e);
            }
        };

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(responseBody);
    }
}
