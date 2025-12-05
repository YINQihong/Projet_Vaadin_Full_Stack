package com.covoiturage.dto;

import com.covoiturage.model.Trip;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// ============================================
// 1. TripDTO - 显示完整行程信息
// ============================================
public class TripDTO {
    private Long id;
    private Long driverId;
    private String driverName;  // 司机姓名（从User获取）
    private String departureAddress;
    private String departureCity;
    private String arrivalAddress;
    private String arrivalCity;
    private LocalDateTime departureDatetime;
    private Integer totalSeats;
    private Integer availableSeats;
    private BigDecimal pricePerSeat;
    private String status;
    private String description;
    private LocalDateTime createdAt;
    
    // 构造器
    public TripDTO() {}
    
    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.driverId = trip.getDriverId();
        this.departureAddress = trip.getDepartureAddress();
        this.departureCity = trip.getDepartureCity();
        this.arrivalAddress = trip.getArrivalAddress();
        this.arrivalCity = trip.getArrivalCity();
        this.departureDatetime = trip.getDepartureDatetime();
        this.totalSeats = trip.getTotalSeats();
        this.availableSeats = trip.getAvailableSeats();
        this.pricePerSeat = trip.getPricePerSeat();
        this.status = trip.getStatus();
        this.description = trip.getDescription();
        this.createdAt = trip.getCreatedAt();
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    
    public String getDepartureAddress() { return departureAddress; }
    public void setDepartureAddress(String departureAddress) { this.departureAddress = departureAddress; }
    
    public String getDepartureCity() { return departureCity; }
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }
    
    public String getArrivalAddress() { return arrivalAddress; }
    public void setArrivalAddress(String arrivalAddress) { this.arrivalAddress = arrivalAddress; }
    
    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }
    
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

// ============================================
// 2. CreateTripDTO - 用于发布行程
// ============================================
class CreateTripDTO {
    private String departureAddress;
    private String departureCity;
    private String arrivalAddress;
    private String arrivalCity;
    private LocalDateTime departureDatetime;
    private Integer totalSeats;
    private BigDecimal pricePerSeat;
    private String description;
    
    public CreateTripDTO() {}
    
    // Getters et Setters
    public String getDepartureAddress() { return departureAddress; }
    public void setDepartureAddress(String departureAddress) { this.departureAddress = departureAddress; }
    
    public String getDepartureCity() { return departureCity; }
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }
    
    public String getArrivalAddress() { return arrivalAddress; }
    public void setArrivalAddress(String arrivalAddress) { this.arrivalAddress = arrivalAddress; }
    
    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }
    
    public LocalDateTime getDepartureDatetime() { return departureDatetime; }
    public void setDepartureDatetime(LocalDateTime departureDatetime) { this.departureDatetime = departureDatetime; }
    
    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }
    
    public BigDecimal getPricePerSeat() { return pricePerSeat; }
    public void setPricePerSeat(BigDecimal pricePerSeat) { this.pricePerSeat = pricePerSeat; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}