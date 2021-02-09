package com.acq.test.views.helloworld;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.jboss.logging.Logger;

import com.acq.test.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "login", layout = MainView.class)
@PageTitle("Login Page")
@CssImport("./styles/views/login/login-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class LoginView extends HorizontalLayout {

    private TextField userField;
    private PasswordField passwordField = new PasswordField("Password");
    private Button loginButton;

    public LoginView() {
        addClassName("login");
        
        
        userField = new TextField("Username:");
        userField.setRequired(true);

        passwordField = new PasswordField("Password");
        passwordField.setRequired(true);

        loginButton = new Button("Login");

        add(userField, passwordField, loginButton);
        setVerticalComponentAlignment(Alignment.END, userField, passwordField, loginButton);
        loginButton.addClickListener(e -> {
        	login(userField.getValue(), passwordField.getValue());
            //Notification.show("Hello " + username.getValue());
        });
    }
    

    private void login(String userName, String pwd) {
        Subject currentUser = SecurityUtils.getSubject();
        
        if (!currentUser.isAuthenticated()) {
        	System.out.println(userField.getValue());
            UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd);
            token.setRememberMe(true);
            try {
                currentUser.login(token);
                if (currentUser.hasRole("admin")) {       
                	Notification.show("Hello Admin " + userField.getValue());             
                } else if(currentUser.hasRole("editor")) {
                	Notification.show("Hello Editor " + userField.getValue());            
                } else if(currentUser.hasRole("author")) {
                	Notification.show("Hello Author " + userField.getValue()); ;            
                } else {                                  
                	Notification.show("Hello Guest " + userField.getValue()); ;               
                }
                loginButton.setText("Logout");
            } catch (UnknownAccountException uae) {
            	Notification.show("User unknown");
                Logger.getLogger(getClass().getName()).warn("User unknown", uae);
            } catch (IncorrectCredentialsException ice) {
            	Notification.show("Incorrect credentials");
                Logger.getLogger(getClass().getName()).warn("Incorrect credentials", ice);
            } catch (LockedAccountException lae) {
            	Notification.show("Account locked");
                Logger.getLogger(getClass().getName()).warn("Account locked", lae);
            } catch (AuthenticationException ae) {
            	Notification.show("Invalid login");
                Logger.getLogger(getClass().getName()).warn("Invalid login", ae);
            }
        } else {        	
        	currentUser.logout();
        	loginButton.setText("Login");
        }
    }

}
