/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.mchange.v2.io.FileUtils;
import db.Author;
import db.Ceremony;
import db.Comment;
import db.Openingorclosingtalk;
import db.Program;
import db.Rating;
import helpers.ConferenceHelper;
import db.Session;
import db.Talk;
import db.User;
import db.Welcomingspeech;
import db.Workshop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author John
 */
@Named(value = "conferenceDetailsBean")
@SessionScoped
public class ConferenceDetailsBean implements Serializable {

    private static final ConferenceHelper CONFERENCE_HELPER = new ConferenceHelper();
    
    private Program currentProgram;
    private Session currentSession;
    private List<Talk> sessionTalks;
    private List<Openingorclosingtalk> sessonOOCTalks;
    private List<Comment> programComments;
    private int sessionLikes;
    private String comment;
    private Boolean liked;
    
    private Talk currentTalk;
    private UploadedFile ppt = null;
    private UploadedFile pdf = null;
    private double talkRating;
    private int numOfRatings;
    private int rating;
    
    private UploadedFile newPhoto = null;
    
    private Ceremony currentCeremony;
    private List<Openingorclosingtalk> ceremonyOOCTalks;
    private List<Welcomingspeech> ceremonyWelcomingSpeeches;
    
    @Inject
    NavBean navBean;
    
    @Inject
    UserBean userBean;
    
    @Inject
    GalleryBean galleryBean;
    
    public String amIPresenting(Session s) {
        if(s == null || s.getTalks() == null || s.getTalks().size() == 0)
            return "";
        List<Talk> talks = new ArrayList<>(s.getTalks());
        String retVal = "";
        for(Talk t : talks) {
            List<Author> authors = new ArrayList<>(t.getAuthors());
            for(Author a : authors) {
                if(Objects.equals(a.getUser().getIdUser(), userBean.getUser().getIdUser())) {
                    if(a.isPresenting())
                        return "color: blue;";
                }
            }
        }
        
        return retVal;
    }
    
    public String viewCeremony(Ceremony ceremony) {
        currentCeremony = ceremony;
        currentProgram = ceremony.getProgram();
        ceremonyOOCTalks = new ArrayList<>(ceremony.getOpeningorclosingtalks());
        ceremonyWelcomingSpeeches = new ArrayList<>(ceremony.getWelcomingspeeches());
        programComments = new ArrayList<>(ceremony.getProgram().getComments());
        sessionLikes = 0;
        for(Comment c : programComments) {
            if(c.getLiked())
                sessionLikes++;
        }
        
        return navBean.navigate("viewCeremony");
    }
    
    public void uploadPhoto(Program program) {
        
        String path = "C:/Temp/Gallery/" + program.getIdProgram();
        Path temp;
        
        try(InputStream istream = newPhoto.getInputstream()) {
            Path folder = Paths.get(path);
            if(!folder.toFile().exists()) {
                folder.toFile().mkdir();
            }
            String extensionSplit[] = newPhoto.getFileName().split("\\.");
            String extension = "." + extensionSplit[extensionSplit.length - 1];
            String filename = "" + program.getIdProgram();
            temp = Files.createTempFile(folder, filename, extension, new FileAttribute<?>[]{});
            
            Files.copy(istream, temp, StandardCopyOption.REPLACE_EXISTING);
            
            CONFERENCE_HELPER.addPhoto(program, temp.toString());
            
            byte[] photo = IOUtils.toByteArray(new FileInputStream(new File(temp.toString())));
            Byte[] backToList = new Byte[photo.length];
            for(int i = 0; i < photo.length; i++)
                backToList[i] = photo[i];
            galleryBean.getPhotoList().add(backToList);
            
        } catch (IOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean canUploadPhoto(Program program) {
        return CONFERENCE_HELPER.canUploadPhoto(userBean.getUser(), program);
    }
    
    //Poslati poruke svim "favoritima"
    public void presentTalk() {
        CONFERENCE_HELPER.presentTalk(userBean.getUser(), currentTalk);
    }
    
    public void downloadPpt() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        ec.setResponseContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + currentTalk.getTitle() + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

        OutputStream output = ec.getResponseOutputStream();
        
        String path = CONFERENCE_HELPER.getTalkPdfLink(currentTalk);
//        String folder = path.substring(0, path.lastIndexOf('/'));
//        File directory = new File(folder);
//        File[] filesInDir = directory.listFiles((FilenameFilter)new RegexFileFilter("*.ppt?x"));
//        File myPdf = filesInDir[0];
//        Path pdfPath = myPdf.toPath();
        Files.copy(Paths.get(path), output);
        output.flush();
        
        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }
    
    public void downloadPdf() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        ec.setResponseContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + currentTalk.getTitle() + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

        OutputStream output = ec.getResponseOutputStream();
        
        String path = CONFERENCE_HELPER.getTalkPdfLink(currentTalk);
//        String folder = path.substring(0, path.lastIndexOf('/'));
//        File directory = new File(folder);
//        File[] filesInDir = directory.listFiles((FilenameFilter)new RegexFileFilter("*.pdf"));
//        File myPdf = filesInDir[0];
//        Path pdfPath = myPdf.toPath();
        Files.copy(Paths.get(path), output);
        output.flush();
        
        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }
    
    
    public void uploadPpt() {
        String path = "C:/Temp/Pres/" + currentTalk.getIdTalk();
        Path temp;
        
        try(InputStream istream = ppt.getInputstream()) {
            Path folder = Paths.get(path);
            if(!folder.toFile().exists()) {
                folder.toFile().mkdir();
            }
            String extensionSplit[] = ppt.getFileName().split("\\.");
            String extension = "." + extensionSplit[extensionSplit.length - 1];
            String filename = "" + currentTalk.getIdTalk();
            temp = Files.createTempFile(folder, filename, extension, new FileAttribute<?>[]{});
            
            Files.copy(istream, temp, StandardCopyOption.REPLACE_EXISTING);
            
            CONFERENCE_HELPER.addFile(currentTalk, temp.toString());
            
        } catch (IOException ex) {
            Logger.getLogger(ConferenceDetailsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void uploadPdf() {
        String path = "C:/Temp/Pres/" + currentTalk.getIdTalk();
        Path temp;
        
        try(InputStream istream = pdf.getInputstream()) {
            Path folder = Paths.get(path);
            if(!folder.toFile().exists()) {
                folder.toFile().mkdir();
            }
            String extensionSplit[] = pdf.getFileName().split("\\.");
            String extension = "." + extensionSplit[extensionSplit.length - 1];
            String filename = "" + currentTalk.getIdTalk();
            temp = Files.createTempFile(folder, filename, extension, new FileAttribute<?>[]{});
            
            Files.copy(istream, temp, StandardCopyOption.REPLACE_EXISTING);
            
            CONFERENCE_HELPER.addFile(currentTalk, temp.toString());
            
        } catch (IOException ex) {
            Logger.getLogger(ConferenceDetailsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean canDownloadPpt() {
        return CONFERENCE_HELPER.isPptUploaded(currentTalk);
    }
    
    public boolean canDownloadPdf(){
        return CONFERENCE_HELPER.isPdfUploaded(currentTalk);
    }
    
    public void rateTalk() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(CONFERENCE_HELPER.addRating(currentTalk, userBean.getUser(), rating)) {
            userBean.setMessage("Rating submitted");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", userBean.getMessage()));
            talkRating *= numOfRatings;
            talkRating += rating;
            numOfRatings++;
            talkRating /= numOfRatings;
        } else {
            userBean.setMessage("You have already rated this talk");
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", userBean.getMessage()));
        }
    }
    
    public boolean canPresent() {
        List<Author> autors = new ArrayList<>(currentTalk.getAuthors());
        for(Author a : autors) {
            if(a.getUser().getIdUser().intValue() == userBean.getUser().getIdUser().intValue()) {
                return !a.isPresenting();
            } 
        }
        return false;
    }
    
    public boolean isAuthor() {
        List<Author> autors = new ArrayList<>(currentTalk.getAuthors());
        if (autors.stream().anyMatch((a) -> (a.getUser().getIdUser().intValue() == userBean.getUser().getIdUser().intValue()))) {
            return true;
        }
        return false;
    }
    
    public String viewTalk(Talk talk) {
        currentTalk = talk;
        talkRating = 0;
        numOfRatings = 0;
        List<Rating> ratings = new ArrayList<>(talk.getRatings());
        for(Rating r : ratings)
            talkRating += r.getRating();
        numOfRatings = ratings.size();
        talkRating = numOfRatings == 0 ? 0 : talkRating / numOfRatings;
        return navBean.navigate("viewTalk");
    }
    
    public boolean renderComments() {
        return CONFERENCE_HELPER.isInAgenda(currentProgram, userBean.getUser());
    }
    
    public String viewSession(Session s) {
        currentSession = s;
        currentProgram = s.getProgram();
        sessionTalks = CONFERENCE_HELPER.getSessionTalks(s);
        sessonOOCTalks = CONFERENCE_HELPER.getSessionOOCTalks(s);
        programComments = CONFERENCE_HELPER.getComments(s.getProgram());
        sessionLikes = 0;
        if(programComments != null && programComments.size() > 0) {
            for(Comment c : programComments) {
                if(c.getLiked())
                    sessionLikes++;
            }
        }
        return navBean.navigate("viewSession");
    }
    
    public void addComment() {
        Comment newComment = CONFERENCE_HELPER.addComment(userBean.getUser(), currentSession.getProgram(),comment, liked);
        programComments.add(newComment);
        if(newComment.getLiked())
            sessionLikes++;
    }
    
    public String authorsToCSV(Collection<Author> authors){
        return CONFERENCE_HELPER.authorsToCSV(authors);
    }
    
    private String listToCSV(Collection<String> list) {
        return list.stream().collect(Collectors.joining(", "));
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public List<Talk> getSessionTalks() {
        return sessionTalks;
    }

    public void setSessionTalks(List<Talk> sessionTalks) {
        this.sessionTalks = sessionTalks;
    }

    public List<Openingorclosingtalk> getSessonOOCTalks() {
        return sessonOOCTalks;
    }

    public void setSessonOOCTalks(List<Openingorclosingtalk> sessonOOCTalks) {
        this.sessonOOCTalks = sessonOOCTalks;
    }

    public List<Comment> getProgramComments() {
        return programComments;
    }

    public void setProgramComments(List<Comment> programComments) {
        this.programComments = programComments;
    }

    public int getSessionLikes() {
        return sessionLikes;
    }

    public void setSessionLikes(int sessionLikes) {
        this.sessionLikes = sessionLikes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Program getCurrentProgram() {
        return currentProgram;
    }

    public void setCurrentProgram(Program currentProgram) {
        this.currentProgram = currentProgram;
    }

    public Talk getCurrentTalk() {
        return currentTalk;
    }

    public void setCurrentTalk(Talk currentTalk) {
        this.currentTalk = currentTalk;
    }

    public UploadedFile getPpt() {
        return ppt;
    }

    public void setPpt(UploadedFile ppt) {
        this.ppt = ppt;
    }

    public UploadedFile getPdf() {
        return pdf;
    }

    public void setPdf(UploadedFile pdf) {
        this.pdf = pdf;
    }

    public double getTalkRating() {
        return talkRating;
    }

    public void setTalkRating(double talkRating) {
        this.talkRating = talkRating;
    }

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public void setNumOfRatings(int numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public UploadedFile getNewPhoto() {
        return newPhoto;
    }

    public void setNewPhoto(UploadedFile newPhoto) {
        this.newPhoto = newPhoto;
    }

    public NavBean getNavBean() {
        return navBean;
    }

    public void setNavBean(NavBean navBean) {
        this.navBean = navBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public Ceremony getCurrentCeremony() {
        return currentCeremony;
    }

    public void setCurrentCeremony(Ceremony currentCeremony) {
        this.currentCeremony = currentCeremony;
    }

    public List<Openingorclosingtalk> getCeremonyOOCTalks() {
        return ceremonyOOCTalks;
    }

    public void setCeremonyOOCTalks(List<Openingorclosingtalk> ceremonyOOCTalks) {
        this.ceremonyOOCTalks = ceremonyOOCTalks;
    }

    public List<Welcomingspeech> getCeremonyWelcomingSpeeches() {
        return ceremonyWelcomingSpeeches;
    }

    public void setCeremonyWelcomingSpeeches(List<Welcomingspeech> ceremonyWelcomingSpeeches) {
        this.ceremonyWelcomingSpeeches = ceremonyWelcomingSpeeches;
    }

    public GalleryBean getGalleryBean() {
        return galleryBean;
    }

    public void setGalleryBean(GalleryBean galleryBean) {
        this.galleryBean = galleryBean;
    }
    
    public String mapOccasion(char o){
        if(o == 'o')
            return "Opening ceremony";
        else
            return "Closing ceremony";
    }
}
