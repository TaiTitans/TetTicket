package com.tetticket.ddd.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Ticket {
    @Id
    private Long id;

    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private int status;
    private Date updatedAt;
    private Date createdAt;

}
