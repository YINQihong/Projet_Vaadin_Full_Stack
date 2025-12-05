package com.covoiturage.views;

import com.covoiturage.dto.UserDTO;
import com.covoiturage.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("edit-profile")
public class EditProfileView extends VerticalLayout {
    
    private UserService userService;
    private UserDTO currentUser;
    
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField phoneField;
    private TextArea bioArea;
    private Button saveButton;
    private Button cancelButton;
    
    public EditProfileView(UserService userService) {
        this.userService = userService;
        
        // 检查登录
        currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute("currentUser");
        if (currentUser == null) {
            Notification.show("Vous devez être connecté", 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
            return;
        }
        
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        
        H1 title = new H1("Modifier mon profil");
        
        // Champs du formulaire
        firstNameField = new TextField("Prénom");
        firstNameField.setValue(currentUser.getFirstName());
        firstNameField.setRequired(true);
        firstNameField.setWidth("400px");
        
        lastNameField = new TextField("Nom");
        lastNameField.setValue(currentUser.getLastName());
        lastNameField.setRequired(true);
        lastNameField.setWidth("400px");
        
        phoneField = new TextField("Téléphone");
        if (currentUser.getPhone() != null) {
            phoneField.setValue(currentUser.getPhone());
        }
        phoneField.setWidth("400px");
        phoneField.setPlaceholder("Ex: 0612345678");
        
        bioArea = new TextArea("Bio");
        if (currentUser.getBio() != null) {
            bioArea.setValue(currentUser.getBio());
        }
        bioArea.setWidth("400px");
        bioArea.setHeight("150px");
        bioArea.setPlaceholder("Parlez un peu de vous...");
        bioArea.setMaxLength(500);
        bioArea.setHelperText("Maximum 500 caractères");
        
        // Info non modifiable
        TextField emailField = new TextField("Email");
        emailField.setValue(currentUser.getEmail());
        emailField.setReadOnly(true);
        emailField.setWidth("400px");
        emailField.setHelperText("L'email ne peut pas être modifié");
        
        // Boutons
        saveButton = new Button("💾 Enregistrer", e -> handleSave());
        saveButton.getStyle().set("background-color", "#4CAF50").set("color", "white");
        
        cancelButton = new Button("Annuler", e -> {
            getUI().ifPresent(ui -> ui.navigate(ProfileView.class, currentUser.getId()));
        });
        
        add(
            title,
            emailField,
            firstNameField,
            lastNameField,
            phoneField,
            bioArea,
            saveButton,
            cancelButton
        );
    }
    
    private void handleSave() {
        try {
            String firstName = firstNameField.getValue();
            String lastName = lastNameField.getValue();
            String phone = phoneField.getValue();
            String bio = bioArea.getValue();
            
            // Appeler le service
            UserDTO updatedUser = userService.updateProfile(
                currentUser.getId(), 
                firstName, 
                lastName, 
                phone, 
                bio
            );
            
            // Mettre à jour la session
            VaadinSession.getCurrent().setAttribute("currentUser", updatedUser);
            
            Notification.show("Profil mis à jour avec succès!", 
                            3000, Notification.Position.MIDDLE);
            
            // Rediriger vers le profil
            getUI().ifPresent(ui -> ui.navigate(ProfileView.class, updatedUser.getId()));
            
        } catch (Exception ex) {
            Notification.show("Erreur: " + ex.getMessage(), 
                            3000, Notification.Position.MIDDLE);
        }
    }
}