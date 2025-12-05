package com.covoiturage.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    private String phone;
    
    @Column(name = "photo_url")
    private String photoUrl;
    
    private String bio;
    
    @Column(name = "is_verified")
    private Boolean isVerified = false;
    
    @Column(name = "average_rating_driver", precision = 3, scale = 2)
    private BigDecimal averageRatingDriver = BigDecimal.ZERO;
    
    @Column(name = "average_rating_passenger", precision = 3, scale = 2)
    private BigDecimal averageRatingPassenger = BigDecimal.ZERO;
    
    @Column(name = "total_trips_driver")
    private Integer totalTripsDriver = 0;
    
    @Column(name = "total_trips_passenger")
    private Integer totalTripsPassenger = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructeurs
    public User() {}
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    
    public BigDecimal getAverageRatingDriver() { return averageRatingDriver; }
    public void setAverageRatingDriver(BigDecimal averageRatingDriver) { 
        this.averageRatingDriver = averageRatingDriver; 
    }
    
    public BigDecimal getAverageRatingPassenger() { return averageRatingPassenger; }
    public void setAverageRatingPassenger(BigDecimal averageRatingPassenger) { 
        this.averageRatingPassenger = averageRatingPassenger; 
    }
    
    public Integer getTotalTripsDriver() { return totalTripsDriver; }
    public void setTotalTripsDriver(Integer totalTripsDriver) { 
        this.totalTripsDriver = totalTripsDriver; 
    }
    
    public Integer getTotalTripsPassenger() { return totalTripsPassenger; }
    public void setTotalTripsPassenger(Integer totalTripsPassenger) { 
        this.totalTripsPassenger = totalTripsPassenger; 
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}