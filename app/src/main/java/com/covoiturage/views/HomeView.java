package com.covoiturage.views;

import com.covoiturage.dto.UserDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

@Route("")
public class HomeView extends VerticalLayout {
    
    public HomeView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        
        H1 title = new H1("Bienvenue sur Co-Voiturage");
        Paragraph description = new Paragraph("Partagez vos trajets, économisez et voyagez ensemble!");
        
        // 检查用户是否登录
        UserDTO currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute("currentUser");
        
        if (currentUser != null) {
            // 用户已登录
            H3 welcome = new H3("Bonjour " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");
            
            RouterLink createTripLink = new RouterLink("Publier un trajet", CreateTripView.class);
            RouterLink searchLink = new RouterLink("Rechercher un trajet", SearchTripsView.class);
            RouterLink myTripsLink = new RouterLink("Mes trajets", MyTripsView.class);
            RouterLink myBookingsLink = new RouterLink("Mes réservations", MyBookingsView.class);
            // TODO: Ajouter d'autres liens (rechercher, mes trajets, etc.)
            
            Button logoutButton = new Button("Se déconnecter", e -> handleLogout());
            
            add(title, welcome, description, createTripLink, searchLink, myTripsLink, myBookingsLink, logoutButton);        } else {
            // 用户未登录
            RouterLink loginLink = new RouterLink("Se connecter", LoginView.class);
            RouterLink registerLink = new RouterLink("S'inscrire", RegisterView.class);
            
            add(title, description, loginLink, registerLink);
        }
    }
    
    private void handleLogout() {
        VaadinSession.getCurrent().setAttribute("currentUser", null);
        getUI().ifPresent(ui -> ui.getPage().reload());
    }
}