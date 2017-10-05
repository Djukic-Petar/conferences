package db;
// Generated Jun 26, 2017 5:09:54 PM by Hibernate Tools 4.3.1



/**
 * Author generated by hbm2java
 */
public class Author  implements java.io.Serializable {


     private Integer idAutor;
     private Openingorclosingtalk openingorclosingtalk;
     private Talk talk;
     private User user;
     private Welcomingspeech welcomingspeech;
     private Workshop workshop;
     private boolean presenting;

    public Author() {
    }

	
    public Author(User user, boolean presenting) {
        this.user = user;
        this.presenting = presenting;
    }
    public Author(Openingorclosingtalk openingorclosingtalk, Talk talk, User user, Welcomingspeech welcomingspeech, Workshop workshop, boolean presenting) {
       this.openingorclosingtalk = openingorclosingtalk;
       this.talk = talk;
       this.user = user;
       this.welcomingspeech = welcomingspeech;
       this.workshop = workshop;
       this.presenting = presenting;
    }
   
    public Integer getIdAutor() {
        return this.idAutor;
    }
    
    public void setIdAutor(Integer idAutor) {
        this.idAutor = idAutor;
    }
    public Openingorclosingtalk getOpeningorclosingtalk() {
        return this.openingorclosingtalk;
    }
    
    public void setOpeningorclosingtalk(Openingorclosingtalk openingorclosingtalk) {
        this.openingorclosingtalk = openingorclosingtalk;
    }
    public Talk getTalk() {
        return this.talk;
    }
    
    public void setTalk(Talk talk) {
        this.talk = talk;
    }
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    public Welcomingspeech getWelcomingspeech() {
        return this.welcomingspeech;
    }
    
    public void setWelcomingspeech(Welcomingspeech welcomingspeech) {
        this.welcomingspeech = welcomingspeech;
    }
    public Workshop getWorkshop() {
        return this.workshop;
    }
    
    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }
    public boolean isPresenting() {
        return this.presenting;
    }
    
    public void setPresenting(boolean presenting) {
        this.presenting = presenting;
    }




}

