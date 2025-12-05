package com.covoiturage.views; 

import com.covoiturage.dto.UserDTO;
import com.vaadin.flow.server.VaadinSession;
import com.covoiturage.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
public class RegisterView extends VerticalLayout {
    
    @Autowired
    private UserService userService;
    
    private EmailField emailField;
    private PasswordField passwordField;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField phoneField;
    private Button registerButton;
    
    public RegisterView(UserService userService) {
        this.userService = userService;
        
        // 设置页面样式
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        
        // 创建表单字段
        emailField = new EmailField("Email");
        emailField.setRequired(true);
        emailField.setWidth("300px");
        
        passwordField = new PasswordField("Mot de passe");
        passwordField.setRequired(true);
        passwordField.setWidth("300px");
        
        firstNameField = new TextField("Prénom");
        firstNameField.setRequired(true);
        firstNameField.setWidth("300px");
        
        lastNameField = new TextField("Nom");
        lastNameField.setRequired(true);
        lastNameField.setWidth("300px");
        
        phoneField = new TextField("Téléphone");
        phoneField.setWidth("300px");
        
        registerButton = new Button("S'inscrire");
        registerButton.addClickListener(e -> handleRegister());
        
        // 添加到页面
        add(
            emailField,
            passwordField,
            firstNameField,
            lastNameField,
            phoneField,
            registerButton
        );
    }
    
    private void handleRegister() {
        try {
            String email = emailField.getValue();
            String password = passwordField.getValue();
            String firstName = firstNameField.getValue();
            String lastName = lastNameField.getValue();
            String phone = phoneField.getValue();
            
            UserDTO user = userService.register(email, password, firstName, lastName, phone);
            
            // 保存用户到Session
            VaadinSession.getCurrent().setAttribute("currentUser", user);
            
            Notification.show("Inscription réussie! Bienvenue " + user.getFirstName() + "!", 
                            3000, Notification.Position.MIDDLE);
            
            getUI().ifPresent(ui -> ui.navigate(HomeView.class));
            
        } catch (Exception ex) {
            Notification.show("Erreur: " + ex.getMessage(), 
                            3000, Notification.Position.MIDDLE);
        }
    }
    
    private void clearForm() {
        emailField.clear();
        passwordField.clear();
        firstNameField.clear();
        lastNameField.clear();
        phoneField.clear();
    }
}