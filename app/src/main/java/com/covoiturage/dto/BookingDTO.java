package com.covoiturage.dto;

import com.covoiturage.model.Booking;
import java.time.LocalDateTime;

// ============================================
// 1. BookingDTO - 显示完整预订信息
// ============================================
public class BookingDTO {
    private Long id;
    private Long tripId;
    private Long passengerId;
    private String passengerName;  // 从User获取
    private Integer seatsBooked;
    private String status;
    private String messageToDriver;
    private LocalDateTime createdAt;
    
    // 行程信息（可选，用于显示预订详情）
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureDatetime;
    
    // 构造器
    public BookingDTO() {}
    
    public BookingDTO(Booking booking) {
        this.id = booking.getId();
        this.tripId = booking.getTripId();
        this.passengerId = booking.getPassengerId();
        this.seatsBooked = booking.getSeatsBooked();
        this.status = booking.getStatus();
        this.messageToDriver = booking.getMessageToDriver();
        this.createdAt = booking.getCreatedAt();
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    
    public Integer getSeatsBooked() { return seatsBooked; }
    public void setSeatsBooked(Integer seatsBooked) { this.seatsBooked = seatsBooked; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessageToDriver() { return messageToDriver; }
    public void setMessageToDriver(String messageToDriver) { this.messageToDriver = messageToDriver; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public String getDepartureCity() { return departureCity; }
    public void setDepartureCity(String departureCity) { this.departureCity = departureCity; }
    
    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }
    
    public LocalDateTime getDepartureDatetime() { return departureDatetime; }
    public void setDepartureDatetime(LocalDateTime departureDatetime) { this.departureDatetime = departureDatetime; }
}

// ============================================
// 2. CreateBookingDTO - 用于创建预订
// ============================================
class CreateBookingDTO {
    private Long tripId;
    private Integer seatsBooked;
    private String messageToDriver;
    
    public CreateBookingDTO() {}
    
    // Getters et Setters
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    
    public Integer getSeatsBooked() { return seatsBooked; }
    public void setSeatsBooked(Integer seatsBooked) { this.seatsBooked = seatsBooked; }
    
    public String getMessageToDriver() { return messageToDriver; }
    public void setMessageToDriver(String messageToDriver) { this.messageToDriver = messageToDriver; }
}