package com.covoiturage.views;

import com.covoiturage.dto.TripDTO;
import com.covoiturage.service.TripService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.covoiturage.dto.UserDTO;
import com.vaadin.flow.server.VaadinSession;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Route("create-trip")
public class CreateTripView extends VerticalLayout {
    
    @Autowired
    private TripService tripService;
    
    private TextField departureAddressField;
    private TextField departureCityField;
    private TextField arrivalAddressField;
    private TextField arrivalCityField;
    private DateTimePicker departureDatetimePicker;
    private NumberField totalSeatsField;
    private NumberField pricePerSeatField;
    private TextArea descriptionArea;
    private Button publishButton;
    
    public CreateTripView(TripService tripService) {
        this.tripService = tripService;
        
        // 设置页面样式
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        
        // 创建表单字段
        departureAddressField = new TextField("Adresse de départ");
        departureAddressField.setRequired(true);
        departureAddressField.setWidth("400px");
        departureAddressField.setPlaceholder("Ex: 15 rue de la Paix, 75001 Paris");
        
        departureCityField = new TextField("Ville de départ");
        departureCityField.setRequired(true);
        departureCityField.setWidth("400px");
        departureCityField.setPlaceholder("Ex: Paris");
        
        arrivalAddressField = new TextField("Adresse d'arrivée");
        arrivalAddressField.setRequired(true);
        arrivalAddressField.setWidth("400px");
        arrivalAddressField.setPlaceholder("Ex: 10 place Bellecour, 69002 Lyon");
        
        arrivalCityField = new TextField("Ville d'arrivée");
        arrivalCityField.setRequired(true);
        arrivalCityField.setWidth("400px");
        arrivalCityField.setPlaceholder("Ex: Lyon");
        
        departureDatetimePicker = new DateTimePicker("Date et heure de départ");
        departureDatetimePicker.setWidth("400px");
        departureDatetimePicker.setMin(LocalDateTime.now());  // Minimum = maintenant
        
        totalSeatsField = new NumberField("Nombre de places");
        totalSeatsField.setRequired(true);
        totalSeatsField.setWidth("400px");
        totalSeatsField.setMin(1);
        totalSeatsField.setMax(8);
        totalSeatsField.setValue(3.0);
        totalSeatsField.setStep(1);
        totalSeatsField.setHelperText("Entre 1 et 8 places");
        
        pricePerSeatField = new NumberField("Prix par place (€)");
        pricePerSeatField.setRequired(true);
        pricePerSeatField.setWidth("400px");
        pricePerSeatField.setMin(0);
        pricePerSeatField.setValue(10.0);
        pricePerSeatField.setStep(0.5);
        pricePerSeatField.setHelperText("Prix en euros");
        
        descriptionArea = new TextArea("Description (optionnel)");
        descriptionArea.setWidth("400px");
        descriptionArea.setHeight("100px");
        descriptionArea.setPlaceholder("Ex: Départ à l'heure, musique autorisée, pas de fumeur...");
        
        publishButton = new Button("Publier le trajet");
        publishButton.addClickListener(e -> handlePublish());
        
        // Ajouter tous les champs à la page
        add(
            departureAddressField,
            departureCityField,
            arrivalAddressField,
            arrivalCityField,
            departureDatetimePicker,
            totalSeatsField,
            pricePerSeatField,
            descriptionArea,
            publishButton
        );
    }
    
    private void handlePublish() {
        try {
            // Récupérer les données du formulaire
            String departureAddress = departureAddressField.getValue();
            String departureCity = departureCityField.getValue();
            String arrivalAddress = arrivalAddressField.getValue();
            String arrivalCity = arrivalCityField.getValue();
            LocalDateTime departureDatetime = departureDatetimePicker.getValue();
            Integer totalSeats = totalSeatsField.getValue().intValue();
            BigDecimal pricePerSeat = BigDecimal.valueOf(pricePerSeatField.getValue());
            String description = descriptionArea.getValue();
            
            // 获取当前登录用户
            UserDTO currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute("currentUser");
            if (currentUser == null) {
                Notification.show("Vous devez être connecté pour publier un trajet", 
                                3000, Notification.Position.MIDDLE);
                getUI().ifPresent(ui -> ui.navigate(LoginView.class));
                return;
            }
            Long driverId = currentUser.getId();
            
            // Appeler le service pour créer le trajet
            TripDTO trip = tripService.createTrip(
                driverId,
                departureAddress,
                departureCity,
                arrivalAddress,
                arrivalCity,
                departureDatetime,
                totalSeats,
                pricePerSeat,
                description
            );
            
            // Afficher message de succès
            Notification.show("Trajet publié avec succès!", 
                            3000, Notification.Position.MIDDLE);
            
            // Vider le formulaire
            clearForm();
            
            // Rediriger vers la page d'accueil
            getUI().ifPresent(ui -> ui.navigate(HomeView.class));
            
        } catch (Exception ex) {
            // Afficher message d'erreur
            Notification.show("Erreur: " + ex.getMessage(), 
                            3000, Notification.Position.MIDDLE);
        }
    }
    
    private void clearForm() {
        departureAddressField.clear();
        departureCityField.clear();
        arrivalAddressField.clear();
        arrivalCityField.clear();
        departureDatetimePicker.clear();
        totalSeatsField.setValue(3.0);
        pricePerSeatField.setValue(10.0);
        descriptionArea.clear();
    }
}