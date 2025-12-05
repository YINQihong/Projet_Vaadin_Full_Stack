package com.covoiturage.views;

import com.covoiturage.dto.UserDTO;
import com.covoiturage.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("profile")
public class ProfileView extends VerticalLayout implements HasUrlParameter<Long> {
    
    private UserService userService;
    private Long userId;
    private UserDTO user;
    
    public ProfileView(UserService userService) {
        this.userService = userService;
        
        setSizeFull();
        setPadding(true);
        setAlignItems(Alignment.CENTER);
    }
    
    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        this.userId = parameter;
        loadUserProfile();
    }
    
    private void loadUserProfile() {
        try {
            user = userService.getUserById(userId);
            buildUI();
        } catch (Exception ex) {
            Notification.show("Erreur: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(HomeView.class));
        }
    }
    
    private void buildUI() {
        removeAll();
        
        // Photo et nom
        H1 name = new H1(user.getFirstName() + " " + user.getLastName());
        
        // Informations principales
        VerticalLayout mainInfo = new VerticalLayout();
        mainInfo.setWidth("600px");
        mainInfo.getStyle()
            .set("border", "1px solid #ddd")
            .set("border-radius", "8px")
            .set("padding", "20px")
            .set("background-color", "#f9f9f9");
        
        H2 infoTitle = new H2("Informations");
        
        Paragraph email = new Paragraph("📧 Email: " + user.getEmail());
        mainInfo.add(infoTitle, email);
        
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            Paragraph phone = new Paragraph("📞 Téléphone: " + user.getPhone());
            mainInfo.add(phone);
        }
        
        if (user.getBio() != null && !user.getBio().isEmpty()) {
            Paragraph bio = new Paragraph("💬 Bio: " + user.getBio());
            mainInfo.add(bio);
        }
        
        Paragraph verified = new Paragraph(user.getIsVerified() ? 
            "✅ Compte vérifié" : "⚠️ Compte non vérifié");
        mainInfo.add(verified);
        
        // Statistiques conducteur
        if (user.getTotalTripsDriver() != null && user.getTotalTripsDriver() > 0) {
            VerticalLayout driverStats = new VerticalLayout();
            driverStats.setWidth("600px");
            driverStats.getStyle()
                .set("border", "1px solid #ddd")
                .set("border-radius", "8px")
                .set("padding", "20px")
                .set("background-color", "#e8f5e9");
            
            H2 driverTitle = new H2("🚗 En tant que conducteur");
            Paragraph tripsDriver = new Paragraph("Trajets publiés: " + user.getTotalTripsDriver());
            
            driverStats.add(driverTitle, tripsDriver);
            
            if (user.getAverageRatingDriver() != null && user.getAverageRatingDriver().doubleValue() > 0) {
                Paragraph ratingDriver = new Paragraph("⭐ Note moyenne: " + 
                    user.getAverageRatingDriver() + "/5");
                driverStats.add(ratingDriver);
            }
            
            add(name, mainInfo, driverStats);
        } else {
            add(name, mainInfo);
        }
        
        // Boutons
        Button backButton = new Button("← Retour", e -> {
            getUI().ifPresent(ui -> ui.getPage().getHistory().back());
        });
        
        // Vérifier si c'est mon propre profil
        UserDTO currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute("currentUser");
        if (currentUser != null && currentUser.getId().equals(userId)) {
        	Button editButton = new Button("✏️ Modifier mon profil", e -> {
        	    getUI().ifPresent(ui -> ui.navigate(EditProfileView.class));
        	});
        	editButton.getStyle().set("background-color", "#2196F3").set("color", "white");
        	add(backButton, editButton);
        } else {
            add(backButton);
        }
    }
}