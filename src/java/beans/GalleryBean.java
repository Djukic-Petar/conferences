package beans;

import db.Ceremony;
import db.Conference;
import db.Photo;
import db.Program;
import db.Session;
import db.Workshop;
import helpers.ConferenceHelper;
import helpers.UserHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;

@Named(value = "galleryBean")
@SessionScoped
public class GalleryBean implements Serializable {
    
    private static final ConferenceHelper CONFERENCE_HELPER = new ConferenceHelper();
    private static final UserHelper USER_HELPER = new UserHelper();
    
    private Conference myConference;
    private ProgramList[] days; //ArrayList<Program>[]
    private List<Integer> array;
    
    private int currentDay;
    private List<Session> daySessions;
    private List<Workshop> dayWorkshops;
    private List<Ceremony> dayCeremonies;
    
    private List<Byte[]> photoList;
    private String programTitle;
    private Program currentProgram;
    
    @Inject
    private NavBean navBean;
    
    public String showProgram(Program program, String programTitle) throws FileNotFoundException, IOException {
        List<Photo> programPhotos = new ArrayList<>(program.getPhotos());
        this.programTitle = programTitle;
        currentProgram = program;
        
        photoList = new ArrayList<>();
        
        for(Photo p : programPhotos) {
            byte[] photo = IOUtils.toByteArray(new FileInputStream(new File(p.getLink())));
            Byte[] intoList = new Byte[photo.length];
            for(int i = 0; i < photo.length; i++)
                intoList[i] = photo[i];
            photoList.add(intoList);
        }
        
        return navBean.navigate("programGallery");
    }
    
    public String showDay(int day) {
        currentDay = day;
        List<Program> dayPrograms = days[day];
        daySessions = new ArrayList<>();
        dayWorkshops = new ArrayList<>();
        dayCeremonies = new ArrayList<>();
        
        for(Program p : dayPrograms) {
            if(p.getCeremony() != null)
                dayCeremonies.add(p.getCeremony());
            if(p.getSession() != null)
                daySessions.add(p.getSession());
            if(p.getWorkshop() != null)
                dayWorkshops.add(p.getWorkshop());
        }
        
        return navBean.navigate("dayGallery");
    }

    public String showRootGallery(Conference conference) {
        myConference = conference;
        long dateDiff = conference.getEndDate().getTime() - conference.getStartDate().getTime();
        int numOfDays = (int)TimeUnit.DAYS.convert(dateDiff, TimeUnit.MILLISECONDS);
        days = new ProgramList[numOfDays];
        array = new ArrayList<>();
        List<Program> allPrograms = new ArrayList<>(myConference.getPrograms());
        
        for(int i = 0; i < numOfDays; i++) {
            array.add(i);
            Date dayDate = addDays(conference.getStartDate(), i);
            days[i] = new ProgramList();
            for(Program p : allPrograms) {
                if(sameDay(p.getStartDateTime(), dayDate)) {
                    days[i].add(p);
                }
            }
        }
        
        return navBean.navigate("rootGallery");
    }
    
    public Conference getMyConference() {
        return myConference;
    }

    public void setMyConference(Conference myConference) {
        this.myConference = myConference;
    }

    public ProgramList[] getDays() {
        return days;
    }

    public void setDays(ProgramList[] days) {
        this.days = days;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public List<Session> getDaySessions() {
        return daySessions;
    }

    public void setDaySessions(List<Session> daySessions) {
        this.daySessions = daySessions;
    }

    public List<Workshop> getDayWorkshops() {
        return dayWorkshops;
    }

    public void setDayWorkshops(List<Workshop> dayWorkshops) {
        this.dayWorkshops = dayWorkshops;
    }

    public List<Ceremony> getDayCeremonies() {
        return dayCeremonies;
    }

    public void setDayCeremonies(List<Ceremony> dayCeremonies) {
        this.dayCeremonies = dayCeremonies;
    }

    public NavBean getNavBean() {
        return navBean;
    }

    public void setNavBean(NavBean navBean) {
        this.navBean = navBean;
    }

    public List<Integer> getArray() {
        return array;
    }

    public void setArray(List<Integer> array) {
        this.array = array;
    }

    public List<Byte[]> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Byte[]> photoList) {
        this.photoList = photoList;
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public Program getCurrentProgram() {
        return currentProgram;
    }

    public void setCurrentProgram(Program currentProgram) {
        this.currentProgram = currentProgram;
    }

    public static class ProgramList extends ArrayList<Program> {}
    
    private boolean sameDay(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
    
    public byte[] ByteArrayTobyteArray(Byte array[]) {
        byte[] retVal = new byte[array.length];
        for(int i = 0; i < array.length; i++) {
            retVal[i] = array[i];
        }
        return retVal;
    }
}
