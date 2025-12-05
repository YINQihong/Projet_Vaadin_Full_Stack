package com.covoiturage.views;

import com.covoiturage.dto.BookingDTO;
import com.covoiturage.dto.UserDTO;
import com.covoiturage.service.BookingService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("my-bookings")
public class MyBookingsView extends VerticalLayout {
    
    private BookingService bookingService;
    private UserDTO currentUser;
    
    private Grid<BookingDTO> bookingsGrid;
    
    public MyBookingsView(BookingService bookingService) {
        this.bookingService = bookingService;
        
        // 检查登录
        currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute("currentUser");
        if (currentUser == null) {
            Notification.show("Vous devez être connecté", 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
            return;
        }
        
        setSizeFull();
        setPadding(true);
        
        H2 title = new H2("Mes réservations");
        
        // Grille des réservations
        bookingsGrid = new Grid<>(BookingDTO.class, false);
        
        bookingsGrid.addColumn(booking -> 
            booking.getDepartureCity() + " → " + booking.getArrivalCity()
        ).setHeader("Trajet").setWidth("200px");
        
        bookingsGrid.addColumn(booking -> 
            booking.getDepartureDatetime() != null ? 
            booking.getDepartureDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : 
            "N/A"
        ).setHeader("Date").setWidth("150px");
        
        bookingsGrid.addColumn(BookingDTO::getSeatsBooked).setHeader("Places").setWidth("80px");
        
        bookingsGrid.addColumn(booking -> {
            String status = booking.getStatus();
            switch(status) {
                case "pending": return "⏳ En attente";
                case "accepted": return "✅ Accepté";
                case "rejected": return "❌ Refusé";
                case "cancelled": return "🚫 Annulé";
                default: return status;
            }
        }).setHeader("Status").setWidth("150px");
        
        bookingsGrid.addColumn(booking -> 
            booking.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        ).setHeader("Réservé le").setWidth("150px");
        
        bookingsGrid.addColumn(booking -> 
            booking.getMessageToDriver() != null && !booking.getMessageToDriver().isEmpty() ? 
            "Oui" : "Non"
        ).setHeader("Message").setWidth("100px");
        
        // Charger les réservations
        loadMyBookings();
        
        add(title, bookingsGrid);
    }
    
    private void loadMyBookings() {
        try {
            List<BookingDTO> bookings = bookingService.getBookingsByPassengerId(currentUser.getId());
            bookingsGrid.setItems(bookings);
            
            if (bookings.isEmpty()) {
                Notification.show("Vous n'avez pas encore de réservation", 
                                3000, Notification.Position.MIDDLE);
            }
            
            // 点击行跳转到行程详情
            bookingsGrid.addItemClickListener(event -> {
                BookingDTO booking = event.getItem();
                if (booking.getTripId() != null) {
                    getUI().ifPresent(ui -> ui.navigate(TripDetailsView.class, booking.getTripId()));
                }
            });
            
        } catch (Exception ex) {
            Notification.show("Erreur: " + ex.getMessage(), 
                            3000, Notification.Position.MIDDLE);
        }
    }
}