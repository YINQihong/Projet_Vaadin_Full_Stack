package com.covoiturage.views;

import com.covoiturage.dto.TripDTO;
import com.vaadin.flow.component.datepicker.DatePicker;
import java.time.LocalDate;
import com.covoiturage.service.TripService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.covoiturage.dto.UserDTO;
import com.covoiturage.service.BookingService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("search-trips")
public class SearchTripsView extends VerticalLayout {
    
    private TripService tripService;
    private BookingService bookingService;
    
    private TextField departureCityField;
    private TextField arrivalCityField;
    private Button searchButton;
    private Grid<TripDTO> resultsGrid;
    private DatePicker datePicker;
    
    
    public SearchTripsView(TripService tripService, BookingService bookingService) {
        this.tripService = tripService;
        this.bookingService = bookingService;
        
        setSizeFull();
        setPadding(true);
        
        // Formulaire de recherche
        departureCityField = new TextField("Ville de départ");
        departureCityField.setPlaceholder("Ex: Paris");
        departureCityField.setWidth("200px");
        
        arrivalCityField = new TextField("Ville d'arrivée");
        arrivalCityField.setPlaceholder("Ex: Lyon");
        arrivalCityField.setWidth("200px");
        
        datePicker = new DatePicker("Date de départ");
        datePicker.setPlaceholder("Optionnel");
        datePicker.setWidth("200px");
        datePicker.setMin(LocalDate.now());  // Minimum = aujourd'hui
        
        searchButton = new Button("Rechercher");
        searchButton.addClickListener(e -> handleSearch());
        
        HorizontalLayout searchLayout = new HorizontalLayout(
            departureCityField, 
            arrivalCityField, 
            datePicker,
            searchButton
        );
        searchLayout.setAlignItems(Alignment.END);
        
        // Grille de résultats
        resultsGrid = new Grid<>(TripDTO.class, false);
        resultsGrid.addColumn(TripDTO::getDepartureCity).setHeader("Départ");
        resultsGrid.addColumn(TripDTO::getArrivalCity).setHeader("Arrivée");
        resultsGrid.addColumn(trip -> 
            trip.getDepartureDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        ).setHeader("Date");
        resultsGrid.addColumn(TripDTO::getAvailableSeats).setHeader("Places dispo");
        resultsGrid.addColumn(trip -> trip.getPricePerSeat() + "€").setHeader("Prix/place");
        resultsGrid.addColumn(TripDTO::getDriverName).setHeader("Conducteur");
        resultsGrid.addColumn(TripDTO::getStatus).setHeader("Status");
        
        resultsGrid.addComponentColumn(trip -> {
            Button bookButton = new Button("Réserver");
            bookButton.addClickListener(e -> handleBooking(trip));
            return bookButton;
        }).setHeader("Action");
        
        resultsGrid.addItemClickListener(event -> {
            TripDTO selectedTrip = event.getItem();
            getUI().ifPresent(ui -> ui.navigate(TripDetailsView.class, selectedTrip.getId()));
        });
        
        // Charger tous les trajets actifs au démarrage
        loadAllActiveTrips();
        
        add(searchLayout, resultsGrid);
    }
    
    private void handleSearch() {
        try {
            String departureCity = departureCityField.getValue();
            String arrivalCity = arrivalCityField.getValue();
            
            if (departureCity == null || departureCity.isEmpty()) {
                Notification.show("Veuillez entrer une ville de départ", 
                                3000, Notification.Position.MIDDLE);
                return;
            }
            if (arrivalCity == null || arrivalCity.isEmpty()) {
                Notification.show("Veuillez entrer une ville d'arrivée", 
                                3000, Notification.Position.MIDDLE);
                return;
            }
            
            // Rechercher les trajets
            List<TripDTO> trips = tripService.searchTrips(departureCity, arrivalCity);
            
            // Filtrer par date si une date est sélectionnée
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                trips = trips.stream()
                    .filter(trip -> trip.getDepartureDatetime().toLocalDate().equals(selectedDate))
                    .collect(java.util.stream.Collectors.toList());
            }

            
            if (trips.isEmpty()) {
                Notification.show("Aucun trajet trouvé pour " + departureCity + " → " + arrivalCity, 
                                3000, Notification.Position.MIDDLE);
            } else {
                Notification.show(trips.size() + " trajet(s) trouvé(s)", 
                                3000, Notification.Position.MIDDLE);
            }
            
            resultsGrid.setItems(trips);
            
        } catch (Exception ex) {
            Notification.show("Erreur: " + ex.getMessage(), 
                            3000, Notification.Position.MIDDLE);
        }
    }
    
    private void handleBooking(TripDTO trip) {
        // 检查用户是否登录
        UserDTO currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute("currentUser");
        if (currentUser == null) {
            Notification.show("Vous devez être connecté pour réserver", 
                            3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
            return;
        }
        
        // 检查是否是自己的行程
        if (trip.getDriverId().equals(currentUser.getId())) {
            Notification.show("Vous ne pouvez pas réserver votre propre trajet", 
                            3000, Notification.Position.MIDDLE);
            return;
        }
        
        // 创建预订对话框
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Réserver ce trajet");
        
        VerticalLayout dialogLayout = new VerticalLayout();
        
        // 显示行程信息
        dialogLayout.add(new Paragraph("Trajet: " + trip.getDepartureCity() + " → " + trip.getArrivalCity()));
        dialogLayout.add(new Paragraph("Date: " + trip.getDepartureDatetime().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        dialogLayout.add(new Paragraph("Prix: " + trip.getPricePerSeat() + "€ / place"));
        dialogLayout.add(new Paragraph("Places disponibles: " + trip.getAvailableSeats()));
        
        // 选择座位数
        NumberField seatsField = new NumberField("Nombre de places");
        seatsField.setValue(1.0);
        seatsField.setMin(1);
        seatsField.setMax(trip.getAvailableSeats());
        seatsField.setStep(1);
        seatsField.setWidth("200px");
        
        // 留言给司机
        TextArea messageArea = new TextArea("Message au conducteur (optionnel)");
        messageArea.setWidth("300px");
        messageArea.setPlaceholder("Ex: J'arriverai 5 minutes en avance...");
        
        dialogLayout.add(seatsField, messageArea);
        
        // 按钮
        Button confirmButton = new Button("Confirmer la réservation", e -> {
            try {
                Integer seats = seatsField.getValue().intValue();
                String message = messageArea.getValue();
                
                bookingService.createBooking(currentUser.getId(), trip.getId(), seats, message);
                
                Notification.show("Réservation envoyée! En attente de confirmation du conducteur.", 
                                3000, Notification.Position.MIDDLE);
                dialog.close();
                
                // Recharger la liste pour mettre à jour les places disponibles
                handleSearch();
                
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
    private void loadAllActiveTrips() {
        try {
            List<TripDTO> allTrips = tripService.getAllActiveTrips();
            resultsGrid.setItems(allTrips);
        } catch (Exception ex) {
            Notification.show("Erreur lors du chargement des trajets: " + ex.getMessage(), 
                            3000, Notification.Position.MIDDLE);
        }
    }
}