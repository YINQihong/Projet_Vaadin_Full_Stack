package com.covoiturage.views;

import com.covoiturage.dto.UserDTO;
import com.covoiturage.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
public class LoginView extends VerticalLayout {
    
    @Autowired
    private UserService userService;
    
    private EmailField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    
    public LoginView(UserService userService) {
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
        
        loginButton = new Button("Se connecter");
        loginButton.addClickListener(e -> handleLogin());
        
        // 注册链接
        RouterLink registerLink = new RouterLink("Pas encore de compte? S'inscrire", RegisterView.class);
        
        // 添加到页面
        add(
            emailField,
            passwordField,
            loginButton,
            registerLink
        );
    }
    
    private void handleLogin() {
        try {
            String email = emailField.getValue();
            String password = passwordField.getValue();
            
            UserDTO user = userService.login(email, password);
            
            // 保存用户到Session
            VaadinSession.getCurrent().setAttribute("currentUser", user);
            
            Notification.show("Bienvenue " + user.getFirstName() + "!", 
                            3000, Notification.Position.MIDDLE);
            
            getUI().ifPresent(ui -> ui.navigate(HomeView.class));
            
        } catch (Exception ex) {
            Notification.show("Erreur: " + ex.getMessage(), 
                            3000, Notification.Position.MIDDLE);
        }
    }
}