/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Conference;
import db.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.inject.Inject;

/**
 *
 * @author John
 */
@Named(value = "navBean")
@SessionScoped
public class NavBean implements Serializable {

    private String currentPage = "unregisteredHome";
    private String nextPage;
    
    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }
    
    public String navigate(String address) {
        currentPage = address;
        return currentPage;
    }
    
    public String goToNext() {
        currentPage = nextPage;
        nextPage = null;
        return currentPage;
    }
    
    public String adminPanel() {
        return navigate("adminPanel");
    }
    
    public String login() {
        currentPage = "login";
        return currentPage;
    }
    
    public String home() {
        currentPage = "index";
        return currentPage;
    }
    
    public String register() {
        currentPage = "register";
        return currentPage;
    }
    
    public String resetPass() {
        currentPage = "resetPass";
        return currentPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}
