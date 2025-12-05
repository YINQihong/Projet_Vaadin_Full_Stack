package com.covoiturage.views;

import com.covoiturage.dto.TripDTO;
import com.covoiturage.dto.UserDTO;
import com.covoiturage.service.BookingService;
import com.covoiturage.service.TripService;
import com.covoiturage.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.time.format.DateTimeFormatter;

@Route("trip")
public class TripDetailsView extends VerticalLayout implements HasUrlParameter<Long> {
    
    private TripService tripService;
    private UserService userService;
    private BookingService bookingService;
    
    private Long tripId;
    private TripDTO trip;
    private UserDTO driver;
    
    public TripDetailsView(TripService tripService, UserService userService, BookingService bookingService) {
        this.tripService = tripService;
        this.userService = userService;
        this.bookingService = bookingService;
        
        setSizeFull();
        setPadding(true);
        setAlignItems(Alignment.CENTER);
    }
    
    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        this.tripId = parameter;
        loadTripDetails();
    }
    
    private void loadTripDetails() {
        try {
            // Charger le trajet
            trip = tripService.getTripById(tripId);
            
            // Charger le conducteur
            driver = userService.getUserById(trip.getDriverId());
            
            // Construire l'UI
            buildUI();
            
        } catch (Exception ex) {
            Notification.show("Erreur: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(SearchTripsView.class));
        }
    }
    
    private void buildUI() {
        removeAll();
        
        // Titre
        H1 title = new H1(trip.getDepartureCity() + " → " + trip.getArrivalCity());
        
        // Informations du trajet
        VerticalLayout tripInfo = new VerticalLayout();
        tripInfo.setWidth("600px");
        tripInfo.getStyle()
            .set("border", "1px solid #ddd")
            .set("border-radius", "8px")
            .set("padding", "20px")
            .set("background-color", "#f9f9f9");
        
        H2 tripInfoTitle = new H2("Informations du trajet");
        
        Paragraph departureInfo = new Paragraph("📍 Départ: " + trip.getDepartureAddress() + ", " + trip.getDepartureCity());
        Paragraph arrivalInfo = new Paragraph("📍 Arrivée: " + trip.getArrivalAddress() + ", " + trip.getArrivalCity());
        Paragraph dateInfo = new Paragraph("📅 Date: " + trip.getDepartureDatetime().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")));
        Paragraph seatsInfo = new Paragraph("💺 Places disponibles: " + trip.getAvailableSeats() + "/" + trip.getTotalSeats());
        Paragraph priceInfo = new Paragraph("💰 Prix: " + trip.getPricePerSeat() + "€ par place");
        Paragraph statusInfo = new Paragraph("🔄 Status: " + trip.getStatus());
        
        tripInfo.add(tripInfoTitle, departureInfo, arrivalInfo, dateInfo, seatsInfo, priceInfo, statusInfo);
        
        // Description (si existe)
        if (trip.getDescription() != null && !trip.getDescription().isEmpty()) {
            Paragraph description = new Paragraph("📝 Description: " + trip.getDescription());
            tripInfo.add(description);
        }
        
        // Informations du conducteur
        VerticalLayout driverInfo = new VerticalLayout();
        driverInfo.setWidth("600px");
        driverInfo.getStyle()
            .set("border", "1px solid #ddd")
            .set("border-radius", "8px")
            .set("padding", "20px")
            .set("background-color", "#f0f8ff");
        
        H2 driverInfoTitle = new H2("Conducteur");
        
        Button driverNameButton = new Button(driver.getFirstName() + " " + driver.getLastName(), e -> {
            getUI().ifPresent(ui -> ui.navigate(ProfileView.class, driver.getId()));
        });
        driverNameButton.getStyle()
            .set("font-size", "20px")
            .set("font-weight", "bold")
            .set("background", "none")
            .set("border", "none")
            .set("color", "#2196F3")
            .set("cursor", "pointer")
            .set("text-decoration", "underline");
        Paragraph driverEmail = new Paragraph("📧 Email: " + driver.getEmail());
        
        if (driver.getPhone() != null && !driver.getPhone().isEmpty()) {
            Paragraph driverPhone = new Paragraph("📞 Téléphone: " + driver.getPhone());
            driverInfo.add(driverInfoTitle, driverNameButton, driverEmail, driverPhone);
        } else {
        	driverInfo.add(driverInfoTitle, driverNameButton, driverEmail);
        }
        
        // Statistiques du conducteur
        if (driver.getTotalTripsDriver() != null && driver.getTotalTripsDriver() > 0) {
            Paragraph tripsCount = new Paragraph("🚗 Trajets publiés: " + driver.getTotalTripsDriver());
            driverInfo.add(tripsCount);
        }
        
        if (driver.getAverageRatingDriver() != null && driver.getAverageRatingDriver().doubleValue() > 0) {
            Paragraph rating = new Paragraph("⭐ Note moyenne: " + driver.getAverageRatingDriver() + "/5");
            driverInfo.add(rating);
        }
        
        if (driver.getBio() != null && !driver.getBio().isEmpty()) {
            Paragraph bio = new Paragraph("💬 Bio: " + driver.getBio());
            driverInfo.add(bio);
        }
        
        // Boutons d'action
        HorizontalLayout buttons = new HorizontalLayout();
        
        Button backButton = new Button("← Retour", e -> {
            getUI().ifPresent(ui -> ui.getPage().getHistory().back());
        });
        
        // Bouton de réservation (seulement si pas le conducteur)
        UserDTO currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute("currentUser");
        if (currentUser != null && !currentUser.getId().equals(trip.getDriverId())) {
            Button bookButton = new Button("Réserver ce trajet", e -> handleBooking());
            bookButton.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "white");
            buttons.add(backButton, bookButton);
        } else {
            buttons.add(backButton);
        }
        
        add(title, tripInfo, driverInfo, buttons);
    }
    
    private void handleBooking() {
        // Vérifier connexion
        UserDTO currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute("currentUser");
        if (currentUser == null) {
            Notification.show("Vous devez être connecté pour réserver", 
                            3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
            return;
        }
        
        // Créer dialogue de réservation
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Réserver ce trajet");
        
        VerticalLayout dialogLayout = new VerticalLayout();
        
        dialogLayout.add(new Paragraph("Trajet: " + trip.getDepartureCity() + " → " + trip.getArrivalCity()));
        dialogLayout.add(new Paragraph("Date: " + trip.getDepartureDatetime().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        dialogLayout.add(new Paragraph("Prix: " + trip.getPricePerSeat() + "€ / place"));
        dialogLayout.add(new Paragraph("Places disponibles: " + trip.getAvailableSeats()));
        
        NumberField seatsField = new NumberField("Nombre de places");
        seatsField.setValue(1.0);
        seatsField.setMin(1);
        seatsField.setMax(trip.getAvailableSeats());
        seatsField.setStep(1);
        seatsField.setWidth("200px");
        
        TextArea messageArea = new TextArea("Message au conducteur (optionnel)");
        messageArea.setWidth("300px");
        messageArea.setPlaceholder("Ex: J'arriverai 5 minutes en avance...");
        
        dialogLayout.add(seatsField, messageArea);
        
        Button confirmButton = new Button("Confirmer la réservation", e -> {
            try {
                Integer seats = seatsField.getValue().intValue();
                String message = messageArea.getValue();
                
                bookingService.createBooking(currentUser.getId(), trip.getId(), seats, message);
                
                Notification.show("Réservation envoyée! En attente de confirmation du conducteur.", 
                                3000, Notification.Position.MIDDLE);
                dialog.close();
                
                // Recharger les détails
                loadTripDetails();
                
            } catch (Exception ex) {
                Notification.show("Erreur: " + ex.getMessage(), 
                                3000, Notification.Position.MIDDLE);
            }
        });
        
        Button cancelButton = new Button("Annuler", e -> dialog.close());
        
        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);
        dialogLayout.add(buttonsLayout);
        
        dialog.add(dialogLayout);
        dialog.open();
    }
}