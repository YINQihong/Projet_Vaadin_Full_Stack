package com.covoiturage.repository;

import com.covoiturage.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    // 查找某个司机的所有行程
    List<Trip> findByDriverId(Long driverId);
    
    // 查找活跃的行程
    List<Trip> findByStatus(String status);
    
    // 根据出发和到达城市查找
    List<Trip> findByDepartureCityAndArrivalCity(String departureCity, String arrivalCity);
}