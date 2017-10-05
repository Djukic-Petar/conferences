package beans;

import db.Conference;
import db.Field;
import db.User;
import helpers.ConferenceHelper;
import helpers.UserHelper;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Named(value = "adminBean")
@SessionScoped
public class AdminBean implements Serializable {

    private static final ConferenceHelper CONFERENCE_HELPER = new ConferenceHelper();
    private static final UserHelper USER_HELPER = new UserHelper();
    
    @Inject
    UserBean userBean;
    
    @Inject
    NavBean navBean;
    
    @Inject
    ConferenceDetailsBean conferenceDetailsBean;
    
    private List<Conference> allConferences;
    
    private Conference currentConference;
    private String modUsername;
    
    private List<String> fieldStrings;
    private final List<Field> fields = CONFERENCE_HELPER.listFields();
    private String currentFieldString;
    
    public String saveConference() {
        FacesContext context = FacesContext.getCurrentInstance();

        for(Field f : fields) {
            if(f.getField().equals(currentFieldString)) {
                currentConference.setField(f);
                break;
            }
        }
        CONFERENCE_HELPER.addConference(currentConference);
        
        userBean.setMessage("Conference created!");
        context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", userBean.getMessage()));
        
        allConferences = (CONFERENCE_HELPER.listConferences());
        return navBean.navigate("adminPanel");
    }
    
    public String newConference() {
        currentConference = new Conference();
        fieldStrings = new ArrayList<>();
        for(Field f : fields) {
            fieldStrings.add(f.getField());
        }
        return navBean.navigate("newConference");
    }
    
    public String addModerator() {
        FacesContext context = FacesContext.getCurrentInstance();
        User newMod = USER_HELPER.getUser(modUsername);
        if(newMod == null) {
            userBean.setMessage("Invalid username!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        USER_HELPER.addMod(newMod, currentConference);
        userBean.setMessage("Moderator added!");
        context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", userBean.getMessage()));
        return navBean.navigate("adminPanel");
    }
    
    public String newModerator(Conference conf) {
        currentConference = conf;
        return navBean.navigate("newModerator");
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public NavBean getNavBean() {
        return navBean;
    }

    public void setNavBean(NavBean navBean) {
        this.navBean = navBean;
    }

    public ConferenceDetailsBean getConferenceDetailsBean() {
        return conferenceDetailsBean;
    }

    public void setConferenceDetailsBean(ConferenceDetailsBean conferenceDetailsBean) {
        this.conferenceDetailsBean = conferenceDetailsBean;
    }

    public Conference getCurrentConference() {
        return currentConference;
    }

    public void setCurrentConference(Conference currentConference) {
        this.currentConference = currentConference;
    }

    public String getModUsername() {
        return modUsername;
    }

    public void setModUsername(String modUsername) {
        this.modUsername = modUsername;
    }

    public List<String> getFieldStrings() {
        return fieldStrings;
    }

    public void setFieldStrings(List<String> fieldStrings) {
        this.fieldStrings = fieldStrings;
    }

    public String getCurrentFieldString() {
        return currentFieldString;
    }

    public void setCurrentFieldString(String currentFieldString) {
        this.currentFieldString = currentFieldString;
    }

    public List<Conference> getAllConferences() {
        return allConferences;
    }

    public void setAllConferences(List<Conference> allConferences) {
        this.allConferences = allConferences;
    }
}
