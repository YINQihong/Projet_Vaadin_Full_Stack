package com.covoiturage.repository;

import com.covoiturage.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // 查找某个行程的所有预订
    List<Booking> findByTripId(Long tripId);
    
    // 查找某个乘客的所有预订
    List<Booking> findByPassengerId(Long passengerId);
    
    // 查找某个行程某个状态的预订
    List<Booking> findByTripIdAndStatus(Long tripId, String status);
}