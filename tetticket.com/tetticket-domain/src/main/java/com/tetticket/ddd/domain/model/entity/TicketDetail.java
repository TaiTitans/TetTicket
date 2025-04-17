package com.tetticket.ddd.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket_item")
public class TicketDetail {
    @Id
    private Long id;

    private String name;
    private String description;
    private int stockInitial;
    private int stockAvailable;
    private boolean isStockPrepared;
    private Long priceOriginal;
    private Long priceFlash;
    private Date saleStartTime;
    private Date saleEndTime;
    private int status;
    private Long activityId;
    private Date updatedAt;
    private Date createdAt;

}
