package com.tetticket.ddd.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tetticket.ddd.domain.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "user_profile")
public class UserProfile {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long profile_id;

@NotBlank(message = "Full name is required")
private String full_name;

@NotBlank(message = "Address is required")
private String address;

@Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
private String phone_number;

@Past(message = "Age must be a past date")
private Date age;

@Enumerated(EnumType.STRING)
private Gender gender;

private String profile_picture;

@JsonIgnore
@OneToOne
@JoinColumn(name = "user_id", referencedColumnName = "user_id")
private Users user_id;
}
