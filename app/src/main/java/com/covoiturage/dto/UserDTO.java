package com.covoiturage.dto;

import com.covoiturage.model.User;
import java.math.BigDecimal;

// ============================================
// 1. UserDTO - 显示用户信息（不含密码）
// ============================================
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String photoUrl;
    private String bio;
    private Boolean isVerified;
    private BigDecimal averageRatingDriver;
    private BigDecimal averageRatingPassenger;
    private Integer totalTripsDriver;
    private Integer totalTripsPassenger;
    
    // 构造器
    public UserDTO() {}
    
    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.photoUrl = user.getPhotoUrl();
        this.bio = user.getBio();
        this.isVerified = user.getIsVerified();
        this.averageRatingDriver = user.getAverageRatingDriver();
        this.averageRatingPassenger = user.getAverageRatingPassenger();
        this.totalTripsDriver = user.getTotalTripsDriver();
        this.totalTripsPassenger = user.getTotalTripsPassenger();
    }
    
    // Getters和Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
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
}

// ============================================
// 2. CreateUserDTO - 用于注册
// ============================================
class CreateUserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    
    public CreateUserDTO() {}
    
    // Getters和Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}