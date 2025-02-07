package com.xxx.ddd.application.model;

import lombok.Data;

import java.util.Date;

@Data
public class TicketDetailDTO {

    private Long id;
    private String name;
    private int stockInitial;
    private int stockAvailable;
    private boolean isStockPrepared;
    private Long priceOriginal;
    private Long priceFlash;
    private Date saleStartTime;
    private Date saleEndTime;
    private int status;
    private Long activityId;
    // add version
    private Long version;
}
