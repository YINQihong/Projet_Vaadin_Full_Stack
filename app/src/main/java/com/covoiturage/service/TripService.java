package com.covoiturage.service;

import com.covoiturage.dto.TripDTO;
import com.covoiturage.model.Trip;
import com.covoiturage.model.User;
import com.covoiturage.repository.TripRepository;
import com.covoiturage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {
    
    @Autowired
    private TripRepository tripRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 发布新行程
     */
    @Transactional
    public TripDTO createTrip(Long driverId, 
                             String departureAddress, 
                             String departureCity,
                             String arrivalAddress, 
                             String arrivalCity,
                             LocalDateTime departureDatetime,
                             Integer totalSeats,
                             BigDecimal pricePerSeat,
                             String description) {
        
        // 1. 验证司机存在
        User driver = userRepository.findById(driverId)
            .orElseThrow(() -> new RuntimeException("Driver not found"));
        
        // 2. 验证必填字段
        if (departureAddress == null || departureAddress.isEmpty()) {
            throw new RuntimeException("Departure address is required");
        }
        if (departureCity == null || departureCity.isEmpty()) {
            throw new RuntimeException("Departure city is required");
        }
        if (arrivalAddress == null || arrivalAddress.isEmpty()) {
            throw new RuntimeException("Arrival address is required");
        }
        if (arrivalCity == null || arrivalCity.isEmpty()) {
            throw new RuntimeException("Arrival city is required");
        }
        if (departureDatetime == null) {
            throw new RuntimeException("Departure date is required");
        }
        if (departureDatetime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Departure date must be in the future");
        }
        if (totalSeats == null || totalSeats < 1 || totalSeats > 8) {
            throw new RuntimeException("Total seats must be between 1 and 8");
        }
        if (pricePerSeat == null || pricePerSeat.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Price must be positive");
        }
        
        // 3. 创建Trip entity
        Trip trip = new Trip();
        trip.setDriverId(driverId);
        trip.setDepartureAddress(departureAddress);
        trip.setDepartureCity(departureCity);
        trip.setArrivalAddress(arrivalAddress);
        trip.setArrivalCity(arrivalCity);
        trip.setDepartureDatetime(departureDatetime);
        trip.setTotalSeats(totalSeats);
        trip.setAvailableSeats(totalSeats);  // 初始可用座位 = 总座位
        trip.setPricePerSeat(pricePerSeat);
        trip.setDescription(description);
        trip.setStatus("active");
        
        // 4. 保存到数据库
        Trip savedTrip = tripRepository.save(trip);
        
        // 5. 更新司机统计
        driver.setTotalTripsDriver(driver.getTotalTripsDriver() + 1);
        userRepository.save(driver);
        
        // 6. 转换成DTO返回
        TripDTO dto = new TripDTO(savedTrip);
        dto.setDriverName(driver.getFirstName() + " " + driver.getLastName());
        
        return dto;
    }
    
    /**
     * 获取所有活跃行程
     */
    public List<TripDTO> getAllActiveTrips() {
        List<Trip> trips = tripRepository.findByStatus("active");
        
        return trips.stream()
            .map(trip -> {
                TripDTO dto = new TripDTO(trip);
                // 添加司机姓名
                userRepository.findById(trip.getDriverId()).ifPresent(driver -> {
                    dto.setDriverName(driver.getFirstName() + " " + driver.getLastName());
                });
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取行程
     */
    public TripDTO getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trip not found"));
        
        TripDTO dto = new TripDTO(trip);
        
        // 添加司机姓名
        userRepository.findById(trip.getDriverId()).ifPresent(driver -> {
            dto.setDriverName(driver.getFirstName() + " " + driver.getLastName());
        });
        
        return dto;
    }
    
    /**
     * 获取某个司机的所有行程
     */
    public List<TripDTO> getTripsByDriverId(Long driverId) {
        List<Trip> trips = tripRepository.findByDriverId(driverId);
        
        return trips.stream()
            .map(trip -> {
                TripDTO dto = new TripDTO(trip);
                userRepository.findById(trip.getDriverId()).ifPresent(driver -> {
                    dto.setDriverName(driver.getFirstName() + " " + driver.getLastName());
                });
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 搜索行程（根据出发和到达城市）
     */
    public List<TripDTO> searchTrips(String departureCity, String arrivalCity) {
        List<Trip> trips = tripRepository.findByDepartureCityAndArrivalCity(departureCity, arrivalCity);
        
        return trips.stream()
            .filter(trip -> "active".equals(trip.getStatus()))  // 只返回活跃的
            .map(trip -> {
                TripDTO dto = new TripDTO(trip);
                userRepository.findById(trip.getDriverId()).ifPresent(driver -> {
                    dto.setDriverName(driver.getFirstName() + " " + driver.getLastName());
                });
                return dto;
            })
            .collect(Collectors.toList());
    }
}