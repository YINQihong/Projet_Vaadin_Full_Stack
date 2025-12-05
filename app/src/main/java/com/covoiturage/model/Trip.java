package com.covoiturage.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "driver_id", nullable = false)
    private Long driverId;
    
    @Column(name = "departure_address", nullable = false)
    private String departureAddress;
    
    @Column(name = "departure_city", nullable = false)
    private String departureCity;
    
    @Column(name = "departure_lat")
    private BigDecimal departureLat;
    
    @Column(name = "departure_lng")
    private BigDecimal departureLng;
    
    @Column(name = "arrival_address", nullable = false)
    private String arrivalAddress;
    
    @Column(name = "arrival_city", nullable = false)
    private String arrivalCity;
    
    @Column(name = "arrival_lat")
    private BigDecimal arrivalLat;
    
    @Column(name = "arrival_lng")
    private BigDecimal arrivalLng;
    
    @Column(name = "departure_datetime", nullable = false)
    private LocalDateTime departureDatetime;
    
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;
    
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;
    
    @Column(name = "price_per_seat", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerSeat;
    
    @Column(length = 20)
    private String status = "active";
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructeur
    public Trip() {}
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    
    public String getDepartureAddress() { return departureAddress; }
    public void setDepartureAddress(String departureAddress) { this.departureAddress = departureAddress; }
    
    public String getDepartureCity() { return departureCity; }
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }
    
    public BigDecimal getDepartureLat() { return departureLat; }
    public void setDepartureLat(BigDecimal departureLat) { this.departureLat = departureLat; }
    
    public BigDecimal getDepartureLng() { return departureLng; }
    public void setDepartureLng(BigDecimal departureLng) { this.departureLng = departureLng; }
    
    public String getArrivalAddress() { return arrivalAddress; }
    public void setArrivalAddress(String arrivalAddress) { this.arrivalAddress = arrivalAddress; }
    
    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }
    
    public BigDecimal getArrivalLat() { return arrivalLat; }
    public void setArrivalLat(BigDecimal arrivalLat) { this.arrivalLat = arrivalLat; }
    
    public BigDecimal getArrivalLng() { return arrivalLng; }
    public void setArrivalLng(BigDecimal arrivalLng) { this.arrivalLng = arrivalLng; }
    
    public LocalDateTime getDepartureDatetime() { return departureDatetime; }
    public void setDepartureDatetime(LocalDateTime departureDatetime) { this.departureDatetime = departureDatetime; }
    
    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }
    
    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
    
    public BigDecimal getPricePerSeat() { return pricePerSeat; }
    public void setPricePerSeat(BigDecimal pricePerSeat) { this.pricePerSeat = pricePerSeat; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}