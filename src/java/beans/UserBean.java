/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Agenda;
import db.Ceremony;
import db.Conference;
import db.Program;
import db.Session;
import db.Subsctiption;
import db.User;
import db.Workshop;
import helpers.ConferenceHelper;
import helpers.UserHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;
import validation.Password;
import validation.Username;

/**
 *
 * @author John
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    private static final UserHelper USER_HELPER =  new UserHelper();
    private static final ConferenceHelper CONFERENCE_HELPER =  new ConferenceHelper();
    private String name;
    private String surname;
    private String email;
    private String institution;
    @Username
    private String newUsername;
    private String username;
    @Password
    private String password;
    @Password
    private String newPassword;
    private char gender;
    private String size;
    private String linkedIn;
    private boolean isAdmin;
    private String message = "";
    private User user = null;
    private UploadedFile photo = null;
    private String conferencePassword;
    private Conference toSubscribe;
    private List<Conference> myConferences;
    
    private List<Workshop> conferenceWorkshops;
    private List<Session> conferenceSessions;
    private List<Ceremony> conferenceCeremonies;
    
    private List<Agenda> myAgenda;
    private List<Workshop> agendaWorkshops;
    private List<Session> agendaSessions;
    private List<Ceremony> agendaCeremonies;
    private Conference currentConference;
    
    private User toView;
    private byte[] photoToView;
    
    private List<User> conferenceSubs;
    
    private List<Conference> allConferences;
    
    @Inject
    private NavBean navBean;
    
    @Inject
    private AdminBean adminBean;
    
    public boolean isUnregistered() {
        return user == null;
    }
    
    public boolean isRegularUser() {
        return (user != null && !user.isIsAdmin());
    }
    
    public boolean isAdmin() {
        return (user != null && user.isIsAdmin());
    }
    
    public String conferenceSubscribers(Conference conference) {
        conferenceSubs = CONFERENCE_HELPER.getConferenceSubs(conference);
        return navBean.navigate("conferenceSubscribers");
    }
    
    public void favoriteUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(USER_HELPER.addFavourite(user, toView)) {
            setMessage("User successfully added to favourites!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", getMessage()));
        }
        else {
            setMessage("OOps!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", getMessage()));
        }
        
    }
    
    public boolean inFavs() {
        return USER_HELPER.isInFavs(user, toView);
    }
    
    public String viewProfile(User u) throws FileNotFoundException, IOException {
        toView = u;
        String imgFolder = "C:/Temp/Img/" + u.getIdUser() + "/";
        File directory = new File(imgFolder);
        File imageFile = directory.listFiles()[0];
        photoToView = IOUtils.toByteArray(new FileInputStream(imageFile));
        
        return navBean.navigate("viewProfile");
    }
    
    public String myAgenda(Conference c) {
        myAgenda = CONFERENCE_HELPER.getUsersAgenda(user, c);
        List<Program> agendaPrograms = CONFERENCE_HELPER.getAgendaPrograms(myAgenda);
        
        agendaWorkshops = CONFERENCE_HELPER.getWorkshops(agendaPrograms);
        agendaSessions = CONFERENCE_HELPER.getSessions(agendaPrograms);
        agendaCeremonies = CONFERENCE_HELPER.getCeremonies(agendaPrograms);
        
        return navBean.navigate("myAgenda");
    }
    
    public void addCeremonyToAgenda(Ceremony c) {
        FacesContext context = FacesContext.getCurrentInstance();
        if(CONFERENCE_HELPER.addProgramToAgenda(c.getProgram(), user)) {
            setMessage("Program successfully added to agenda!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", getMessage()));
        }
        else {
            setMessage("You are already subscribed to that program!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", getMessage()));
        }
    }
    
    public void addWorkshopToAgenda(Workshop w) {
        FacesContext context = FacesContext.getCurrentInstance();
        if(CONFERENCE_HELPER.addProgramToAgenda(w.getProgram(), user)) {
            setMessage("Program successfully added to agenda!");
        context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", getMessage()));
        }
        else {
            setMessage("You are already subscribed to that program!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", getMessage()));
        }
    }
    
    public void addSessionToAgenda(db.Session s) {
        FacesContext context = FacesContext.getCurrentInstance();
        if(CONFERENCE_HELPER.addProgramToAgenda(s.getProgram(), user)) {
            setMessage("Program successfully added to agenda!");
        context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", getMessage()));
        }
        else {
            setMessage("You are already subscribed to that program!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", getMessage()));
        }
    }

    public String conferenceAgenda(Conference c) {
        List<Program> conferencePrograms = CONFERENCE_HELPER.getPrograms(c);
        
        currentConference = c;
        conferenceWorkshops = CONFERENCE_HELPER.getWorkshops(conferencePrograms);
        conferenceSessions = CONFERENCE_HELPER.getSessions(conferencePrograms);
        conferenceCeremonies = CONFERENCE_HELPER.getCeremonies(conferencePrograms);
        
        return navBean.navigate("conferenceAgenda");
    }
    
    public String fetchMyConferences() {
        myConferences = CONFERENCE_HELPER.getSubscribedConferences(user);
        return navBean.navigate("myConferences");
    }
    
    public String subscribe() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(!toSubscribe.getPassword().equals(conferencePassword)) {
            setMessage("Invalid password!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", getMessage()));
            return navBean.getCurrentPage();
        }
        CONFERENCE_HELPER.subscribe(getUser(), toSubscribe);
        setMessage("You subscribed to " + toSubscribe.getName() + "!");
        context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", getMessage()));
            
        return navBean.home();
    }
    
    public String showSubscribe(Conference c) {
        toSubscribe = c;
        if(user == null) {
            navBean.setNextPage("subscribe");
            return navBean.navigate("register");
        }
        return navBean.navigate("subscribe");
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConferencePassword() {
        return conferencePassword;
    }

    public void setConferencePassword(String conferencePassword) {
        this.conferencePassword = conferencePassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public NavBean getNavBean() {
        return navBean;
    }

    public void setNavBean(NavBean navBean) {
        this.navBean = navBean;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UploadedFile getPhoto() {
        return photo;
    }

    public void setPhoto(UploadedFile photo) {
        this.photo = photo;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Conference getToSubscribe() {
        return toSubscribe;
    }

    public void setToSubscribe(Conference toSubscribe) {
        this.toSubscribe = toSubscribe;
    }

    public List<Conference> getMyConferences() {
        return myConferences;
    }

    public void setMyConferences(List<Conference> myConferences) {
        this.myConferences = myConferences;
    }

    public List<Workshop> getConferenceWorkshops() {
        return conferenceWorkshops;
    }

    public void setConferenceWorkshops(List<Workshop> conferenceWorkshops) {
        this.conferenceWorkshops = conferenceWorkshops;
    }

    public List<Session> getConferenceSessions() {
        return conferenceSessions;
    }

    public void setConferenceSessions(List<Session> conferenceSessions) {
        this.conferenceSessions = conferenceSessions;
    }

    public List<Ceremony> getConferenceCeremonies() {
        return conferenceCeremonies;
    }

    public void setConferenceCeremonies(List<Ceremony> conferenceCeremonies) {
        this.conferenceCeremonies = conferenceCeremonies;
    }

    public List<Agenda> getMyAgenda() {
        return myAgenda;
    }

    public void setMyAgenda(List<Agenda> myAgenda) {
        this.myAgenda = myAgenda;
    }

    public List<Workshop> getAgendaWorkshops() {
        return agendaWorkshops;
    }

    public void setAgendaWorkshops(List<Workshop> agendaWorkshops) {
        this.agendaWorkshops = agendaWorkshops;
    }

    public List<Session> getAgendaSessions() {
        return agendaSessions;
    }

    public void setAgendaSessions(List<Session> agendaSessions) {
        this.agendaSessions = agendaSessions;
    }

    public List<Ceremony> getAgendaCeremonies() {
        return agendaCeremonies;
    }

    public void setAgendaCeremonies(List<Ceremony> agendaCeremonies) {
        this.agendaCeremonies = agendaCeremonies;
    }

    public User getToView() {
        return toView;
    }

    public void setToView(User toView) {
        this.toView = toView;
    }

    public byte[] getPhotoToView() {
        return photoToView;
    }

    public void setPhotoToView(byte[] photoToView) {
        this.photoToView = photoToView;
    }

    public List<User> getConferenceSubs() {
        return conferenceSubs;
    }

    public void setConferenceSubs(List<User> conferenceSubs) {
        this.conferenceSubs = conferenceSubs;
    }

    public List<Conference> getAllConferences() {
        return allConferences;
    }

    public void setAllConferences(List<Conference> allConferences) {
        this.allConferences = allConferences;
    }
    
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        user = USER_HELPER.getUser(username, password);
        if(user == null) {
            message = "Invalid credentials!";
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", message));
            navBean.setCurrentPage("login");
            return "/login";
        } else {
            message = "Welcome, " + user.getName();
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", message));
            navBean.setCurrentPage("index");
            if(user.isIsAdmin()) {
                adminBean.setAllConferences(CONFERENCE_HELPER.listConferences());
                navBean.navigate("adminPanel");
            }
            return "/index";
        }
    }

    public Conference getCurrentConference() {
        return currentConference;
    }

    public void setCurrentConference(Conference currentConference) {
        this.currentConference = currentConference;
    }
    
    public void logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        navBean.setCurrentPage("index");
        ec.redirect("/PIA_JUL/");
    }

    public String register() {
        user = new User(name, surname, email, institution, newUsername, password, gender, size, false);
        int userId = USER_HELPER.newUser(user);
        
        String path = "C:/Temp/Img/" + userId;
        Path temp;
        
        try(InputStream istream = photo.getInputstream()) {
            Path folder = Paths.get(path);
            if(!folder.toFile().exists()) {
                folder.toFile().mkdir();
            }
            String extensionSplit[] = photo.getFileName().split("\\.");
            String extension = "." + extensionSplit[extensionSplit.length - 1];
            String filename = "" + userId;
            temp = Files.createTempFile(folder, filename, extension, new FileAttribute<?>[]{});
            
            Files.copy(istream, temp, StandardCopyOption.REPLACE_EXISTING);
            
        } catch (IOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(navBean.getNextPage() != null && !navBean.getNextPage().equals("")) {
            return navBean.goToNext();
        }
        navBean.setCurrentPage("index");
        return "/index";
    }
    
    public String changePassword() {
        FacesContext context = FacesContext.getCurrentInstance();
        user = USER_HELPER.changePassword(username, password, newPassword);
        
        if(user == null) {
            message = "Invalid credentials!";
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", message));
            navBean.setCurrentPage("resetPass");
            return "/resetPass";
        } else {
            message = "Welcome, " + user.getName();
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", message));
            navBean.setCurrentPage("index");
            return "/index";
        }
    }
}
