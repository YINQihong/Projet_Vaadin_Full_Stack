package com.covoiturage.views;

import com.covoiturage.dto.BookingDTO;
import com.covoiturage.dto.TripDTO;
import com.covoiturage.dto.UserDTO;
import com.covoiturage.service.BookingService;
import com.covoiturage.service.TripService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("my-trips")
public class MyTripsView extends VerticalLayout {
    
    private TripService tripService;
    private BookingService bookingService;
    private UserDTO currentUser;
    
    private Grid<TripDTO> tripsGrid;
    
    public MyTripsView(TripService tripService, BookingService bookingService) {
        this.tripService = tripService;
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
        
        H2 title = new H2("Mes trajets publiés");
        
        // Grille des trajets
        tripsGrid = new Grid<>(TripDTO.class, false);
        tripsGrid.addColumn(trip -> 
            trip.getDepartureCity() + " → " + trip.getArrivalCity()
        ).setHeader("Trajet");
        tripsGrid.addColumn(trip -> 
            trip.getDepartureDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        ).setHeader("Date");
        tripsGrid.addColumn(TripDTO::getAvailableSeats).setHeader("Places dispo");
        tripsGrid.addColumn(TripDTO::getTotalSeats).setHeader("Places total");
        tripsGrid.addColumn(trip -> trip.getPricePerSeat() + "€").setHeader("Prix");
        tripsGrid.addColumn(TripDTO::getStatus).setHeader("Status");
        
        // Bouton pour voir les réservations
        tripsGrid.addComponentColumn(trip -> {
            Button viewBookingsButton = new Button("Voir réservations");
            viewBookingsButton.addClickListener(e -> showBookings(trip));
            return viewBookingsButton;
        }).setHeader("Actions");
        
        // Charger les trajets du conducteur
        loadMyTrips();
        
        add(title, tripsGrid);
    }
    
    private void loadMyTrips() {
        try {
            List<TripDTO> trips = tripService.getTripsByDriverId(currentUser.getId());
            tripsGrid.setItems(trips);
            
            if (trips.isEmpty()) {
                Notification.show("Vous n'avez pas encore publié de trajet", 
                                3000, Notification.Position.MIDDLE);
            }
        } catch (Exception ex) {
            Notification.show("Erreur: " + ex.getMessage(), 
                            3000, Notification.Position.MIDDLE);
        }
    }
    
    private void showBookings(TripDTO trip) {
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeaderTitle("Réservations pour " + trip.getDepartureCity() + " → " + trip.getArrivalCity());
        
        VerticalLayout dialogLayout = new VerticalLayout();
        
        try {
            List<BookingDTO> bookings = bookingService.getBookingsByTripId(trip.getId());
            
            if (bookings.isEmpty()) {
                dialogLayout.add(new Paragraph("Aucune réservation pour ce trajet"));
            } else {
                for (BookingDTO booking : bookings) {
                    VerticalLayout bookingCard = createBookingCard(booking, trip);
                    dialogLayout.add(bookingCard);
                }
            }
            
        } catch (Exception ex) {
            dialogLayout.add(new Paragraph("Erreur: " + ex.getMessage()));
        }
        
        Button closeButton = new Button("Fermer", e -> dialog.close());
        dialogLayout.add(closeButton);
        
        dialog.add(dialogLayout);
        dialog.open();
    }
    
    private VerticalLayout createBookingCard(BookingDTO booking, TripDTO trip) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
            .set("border", "1px solid #ddd")
            .set("border-radius", "8px")
            .set("padding", "15px")
            .set("margin-bottom", "10px");
        
        H3 passengerName = new H3("Passager: " + booking.getPassengerName());
        Paragraph seats = new Paragraph("Places réservées: " + booking.getSeatsBooked());
        Paragraph status = new Paragraph("Status: " + booking.getStatus());
        
        if (booking.getMessageToDriver() != null && !booking.getMessageToDriver().isEmpty()) {
            Paragraph message = new Paragraph("Message: " + booking.getMessageToDriver());
            card.add(passengerName, seats, status, message);
        } else {
            card.add(passengerName, seats, status);
        }
        
        // Boutons d'action (seulement si status = pending)
        if ("pending".equals(booking.getStatus())) {
            Button acceptButton = new Button("Accepter", e -> {
                try {
                    bookingService.acceptBooking(booking.getId(), currentUser.getId());
                    Notification.show("Réservation acceptée!", 3000, Notification.Position.MIDDLE);
                    // Recharger
                    loadMyTrips();
                    // Fermer le dialogue
                    card.getParent().ifPresent(parent -> {
                        Dialog dialog = (Dialog) parent.getParent().orElse(null);
                        if (dialog != null) dialog.close();
                    });
                } catch (Exception ex) {
                    Notification.show("Erreur: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
                }
            });
            acceptButton.getStyle().set("background-color", "#4CAF50").set("color", "white");
            
            Button rejectButton = new Button("Refuser", e -> {
                try {
                    bookingService.rejectBooking(booking.getId(), currentUser.getId());
                    Notification.show("Réservation refusée", 3000, Notification.Position.MIDDLE);
                    // Recharger
                    loadMyTrips();
                    // Fermer le dialogue
                    card.getParent().ifPresent(parent -> {
                        Dialog dialog = (Dialog) parent.getParent().orElse(null);
                        if (dialog != null) dialog.close();
                    });
                } catch (Exception ex) {
                    Notification.show("Erreur: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
                }
            });
            rejectButton.getStyle().set("background-color", "#f44336").set("color", "white");
            
            HorizontalLayout buttons = new HorizontalLayout(acceptButton, rejectButton);
            card.add(buttons);
        }
        
        return card;
    }
}