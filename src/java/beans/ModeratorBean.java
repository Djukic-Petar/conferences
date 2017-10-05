package beans;

import db.Conference;
import db.Field;
import db.Moderator;
import db.Openingorclosingtalk;
import db.Program;
import db.Session;
import db.Talk;
import db.User;
import db.Workshop;
import helpers.ConferenceHelper;
import helpers.UserHelper;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Named(value = "moderatorBean")
@SessionScoped
public class ModeratorBean implements Serializable {

    private static final ConferenceHelper CONFERENCE_HELPER = new ConferenceHelper();
    private static final UserHelper USER_HELPER = new UserHelper();
    
    @Inject
    UserBean userBean;
    
    @Inject
    NavBean navBean;
    
    @Inject
    ConferenceDetailsBean conferenceDetailsBean;
    
    private List<Conference> myConferences;
    private Conference currentConference;
    private List<String> fieldStrings;
    private final List<Field> fields = CONFERENCE_HELPER.listFields();
    private String currentFieldString;
    
    private String sessionTitle;
    private Date programStartDate;
    private Date programEndDate;
    private String programHall;
    
    private Session currentSession;
    private String author1;
    private String author2;
    private String author3;
    private String author4;
    private String talkTitle;
    
    public String addTalk() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(programStartDate.after(programEndDate)) {
            userBean.setMessage("Start date must be before end date!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        long ms = programEndDate.getTime() - programStartDate.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        if(diffInMinutes > 30) {
            userBean.setMessage("Opening/closing talks must last less than 30 minutes!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        if(programEndDate.after(currentSession.getProgram().getEndDateTime())) {
            userBean.setMessage("Talk time must be within bounds of the session time");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        if(programStartDate.before(currentSession.getProgram().getStartDateTime())) {
            userBean.setMessage("Talk time must be within bounds of the session time");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        
        Talk newTalk = new Talk(currentSession, talkTitle, programStartDate, programEndDate, 0);
        CONFERENCE_HELPER.addTalk(newTalk);
        
        if(author1 != null && !author1.equals("")) {
            User user1 = USER_HELPER.getUser(author1);
            if(user1 == null) {
                user1 = new User("User not in database", "", "", "", author1, "", 'M', "M", false);
                USER_HELPER.newUser(user1);
            }
            USER_HELPER.addAuthor(user1, newTalk);
        }
        if(author2 != null && !author2.equals("")) {
            User user2 = USER_HELPER.getUser(author2);
            if(user2 == null) {
                user2 = new User("User not in database", "", "", "", author2, "", 'M', "M", false);
                USER_HELPER.newUser(user2);
            }
            USER_HELPER.addAuthor(user2, newTalk);
        }
        if(author3 != null && !author3.equals("")) {
            User user3 = USER_HELPER.getUser(author3);
            if(user3 == null) {
                user3 = new User("User not in database", "", "", "", author3, "", 'M', "M", false);
                USER_HELPER.newUser(user3);
            }
            USER_HELPER.addAuthor(user3, newTalk);
        }
        if(author4 != null && !author4.equals("")) {
            User user4 = USER_HELPER.getUser(author4);
            if(user4 == null) {
                user4 = new User("User not in database", "", "", "", author4, "", 'M', "M", false);
                USER_HELPER.newUser(user4);
            }
            USER_HELPER.addAuthor(user4, newTalk);
        }
        
        return conferenceDetailsBean.viewSession(currentSession);
    }
    
    public String newTalk(Session s) {
        currentSession = s;
        return navBean.navigate("newTalk");
    }
    
    public String addOOCTalkToSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(programStartDate.after(programEndDate)) {
            userBean.setMessage("Start date must be before end date!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        long ms = programEndDate.getTime() - programStartDate.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        if(diffInMinutes > 30) {
            userBean.setMessage("Opening/closing talks must last less than 30 minutes!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        if(programEndDate.after(currentSession.getProgram().getEndDateTime())) {
            userBean.setMessage("Talk time must be within bounds of the session time");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        if(programStartDate.before(currentSession.getProgram().getStartDateTime())) {
            userBean.setMessage("Talk time must be within bounds of the session time");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        
        Openingorclosingtalk ooc = new Openingorclosingtalk(programStartDate, programEndDate);
        ooc.setSession(currentSession);
        CONFERENCE_HELPER.addOOCTalk(ooc);
        
        if(author1 != null && !author1.equals("")) {
            User user1 = USER_HELPER.getUser(author1);
            if(user1 == null) {
                user1 = new User("User not in database", "", "", "", author1, "", 'M', "M", false);
                USER_HELPER.newUser(user1);
            }
            USER_HELPER.addAuthor(user1, ooc);
        }
        if(author2 != null && !author2.equals("")) {
            User user2 = USER_HELPER.getUser(author2);
            if(user2 == null) {
                user2 = new User("User not in database", "", "", "", author2, "", 'M', "M", false);
                USER_HELPER.newUser(user2);
            }
            USER_HELPER.addAuthor(user2, ooc);
        }
        if(author3 != null && !author3.equals("")) {
            User user3 = USER_HELPER.getUser(author3);
            if(user3 == null) {
                user3 = new User("User not in database", "", "", "", author3, "", 'M', "M", false);
                USER_HELPER.newUser(user3);
            }
            USER_HELPER.addAuthor(user3, ooc);
        }
        if(author4 != null && !author4.equals("")) {
            User user4 = USER_HELPER.getUser(author4);
            if(user4 == null) {
                user4 = new User("User not in database", "", "", "", author4, "", 'M', "M", false);
                USER_HELPER.newUser(user4);
            }
            USER_HELPER.addAuthor(user4, ooc);
        }
        
        return conferenceDetailsBean.viewSession(currentSession);
    }
    
    public String newOOCTalk(Session s) {
        currentSession = s;
        return navBean.navigate("newOOCTalk");
    }
    
    public String addWorkshop() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(programStartDate.after(programEndDate)) {
            userBean.setMessage("Start date must be before end date!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        if(programEndDate.after(currentConference.getEndDate())) {
            userBean.setMessage("Program date must be within bounds of the conference date");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        if(programStartDate.before(currentConference.getStartDate())) {
            userBean.setMessage("Program date must be within bounds of the conference date");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        long ms = programEndDate.getTime() - programStartDate.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        if(diffInMinutes > 180) {
            userBean.setMessage("Workshops must last less than three hours!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        
        userBean.setMessage("Workshop added!");
        context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", userBean.getMessage()));
        Program p = new Program(currentConference, programStartDate, programEndDate, programHall);
        Workshop w = new Workshop(p, sessionTitle, null, null);
        CONFERENCE_HELPER.addWorkshop(p, w);
        
        return userBean.conferenceAgenda(currentConference);
    }
    
    public String newWorkshop(Conference c) {
        currentConference = c;
        return navBean.navigate("newWorkshop");
    }
    
    public String addSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(programStartDate.after(programEndDate)) {
            userBean.setMessage("Start date must be before end date!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        if(programEndDate.after(currentConference.getEndDate())) {
            userBean.setMessage("Program date must be within bounds of the conference date");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        if(programStartDate.before(currentConference.getStartDate())) {
            userBean.setMessage("Program date must be within bounds of the conference date");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
            return null;
        }
        
        userBean.setMessage("Session added!");
        context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", userBean.getMessage()));
        Program p = new Program(currentConference, programStartDate, programEndDate, programHall);
        Session s = new Session(p, sessionTitle);
        CONFERENCE_HELPER.addSession(p, s);
        
        return userBean.conferenceAgenda(currentConference);
    }
    
    public String newSession(Conference c) {
        currentConference = c;
        
        return navBean.navigate("newSession");
    }
    
    public boolean isModerator(User user, Conference conference) {
        List<Moderator> mods = new ArrayList<>(user.getModerators());
        return mods.stream().anyMatch((m) -> (Objects.equals(m.getConference().getIdConference(), conference.getIdConference())));
    }
    
    public void saveConference() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        Field selectedField = currentConference.getField();
        for(Field f : fields) {
            if(f.getField().equals(currentFieldString))
                selectedField = f;
        }
        currentConference.setField(selectedField);
        
        if(CONFERENCE_HELPER.updateConference(currentConference)) {
            userBean.setMessage("Conference updated");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", userBean.getMessage()));
        } else {
            userBean.setMessage("Something went wrong!");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
        }
    }
    
    public String editConference(Conference c) {
        currentConference = c;
        fieldStrings = new ArrayList<>();
        for(Field f : fields) {
            fieldStrings.add(f.getField());
        }
        currentFieldString = currentConference.getField().getField();
        
        return navBean.navigate("editConference");
    }
    
    public String moderatorPanel() {
        List<Moderator> mods = new ArrayList<>(userBean.getUser().getModerators());
        myConferences = new ArrayList<>();
        for(Moderator m : mods) {
            myConferences.add(m.getConference());
        }
        
        return navBean.navigate("moderatorPanel");
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

    public List<Conference> getMyConferences() {
        return myConferences;
    }

    public void setMyConferences(List<Conference> myConferences) {
        this.myConferences = myConferences;
    }

    public Conference getCurrentConference() {
        return currentConference;
    }

    public void setCurrentConference(Conference currentConference) {
        this.currentConference = currentConference;
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

    public String getSessionTitle() {
        return sessionTitle;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public Date getProgramStartDate() {
        return programStartDate;
    }

    public void setProgramStartDate(Date programStartDate) {
        this.programStartDate = programStartDate;
    }

    public Date getProgramEndDate() {
        return programEndDate;
    }

    public void setProgramEndDate(Date programEndDate) {
        this.programEndDate = programEndDate;
    }

    public String getProgramHall() {
        return programHall;
    }

    public void setProgramHall(String programHall) {
        this.programHall = programHall;
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public String getAuthor1() {
        return author1;
    }

    public void setAuthor1(String author1) {
        this.author1 = author1;
    }

    public String getAuthor2() {
        return author2;
    }

    public void setAuthor2(String author2) {
        this.author2 = author2;
    }

    public String getAuthor3() {
        return author3;
    }

    public void setAuthor3(String author3) {
        this.author3 = author3;
    }

    public String getAuthor4() {
        return author4;
    }

    public void setAuthor4(String author4) {
        this.author4 = author4;
    }

    public ConferenceDetailsBean getConferenceDetailsBean() {
        return conferenceDetailsBean;
    }

    public void setConferenceDetailsBean(ConferenceDetailsBean conferenceDetailsBean) {
        this.conferenceDetailsBean = conferenceDetailsBean;
    }

    public String getTalkTitle() {
        return talkTitle;
    }

    public void setTalkTitle(String talkTitle) {
        this.talkTitle = talkTitle;
    }
}
