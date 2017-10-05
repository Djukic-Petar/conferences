/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Conference;
import db.Field;
import helpers.ConferenceHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.DateHelper;

/**
 *
 * @author John
 */
@Named(value = "searchBean")
@ViewScoped
public class SearchBean implements Serializable {
    private static final int DAY_IN_MS = 86400000;
    private static final ConferenceHelper CONFERENCE_HELPER = new ConferenceHelper();
    private final List<Conference> conferenceList = CONFERENCE_HELPER.listConferences();
    private List<Conference> selectedName;
    private final List<Field> fieldList = CONFERENCE_HELPER.listFields();
    private List<String> fieldStrings = new ArrayList<>();
    private Date startDate;
    private Date endDate;
    private final Date threeDaysFromNow = new Date(System.currentTimeMillis() + (3 * DAY_IN_MS));
    private String[] selectedFields;
    private List<Conference> searchResults;
    
    @Inject
    UserBean userBean;

    public SearchBean() {
        for(Field f : fieldList){
            fieldStrings.add(f.getField());
        }
    }
    
    public List<Conference> completeNames(String query) {
        List<Conference> retVal = new ArrayList<>();
        for(Conference con : conferenceList) {
            if(con.getName().toLowerCase().startsWith(query.toLowerCase()))
                retVal.add(con);
        }
        return retVal;
    }
    
//    private List<Field> listSelectedFields() {
//        List<Field> retVal = null;
//        if("".equals(selectedFields) || selectedFields == null)
//            return retVal;
//        else {
//            retVal = new ArrayList<>();
//            String fields[] = selectedFields.split(",");
//            for(String SelectedField : fields) {
//                for(Field f : fieldList)
//                    if(f.getField().equalsIgnoreCase(SelectedField))
//                        retVal.add(f);
//            }
//        }
//        return retVal;
//    }
            
    public void search(){
        List<Conference> tempList = new ArrayList<>(conferenceList);
        if(selectedName != null && selectedName.size() > 0)
            tempList = new ArrayList<>(selectedName);
        
        Iterator it = tempList.iterator();
        while(it.hasNext()) {
            Conference c = (Conference)it.next();
            if(startDate != null) {
                if(c.getStartDate().before(startDate)){
                    it.remove();
                    continue;
                }
            }
            if(endDate != null) {
                if(c.getEndDate().after(endDate)) {
                    it.remove();
                    continue;
                }
            }
            if(selectedFields != null && selectedFields.length > 0) {
                boolean found = false;
                for(String field : selectedFields) {
                    if(field.equals(c.getField().getField())) {
                        found = true;
                    }
                }
                if(!found) {
                    it.remove();
                    continue;
                }
            }
            if(c.getEndDate().before(new Date())) {
                it.remove();
            }
        }
        searchResults = tempList;
    }

    public void today() {
        List<Conference> tempList = new ArrayList<>(conferenceList);
        Date todayDate = new Date();
        
        Iterator it = tempList.iterator();
        while(it.hasNext()) {
            Conference c = (Conference)it.next();
            if(!c.getStartDate().before(todayDate) || !c.getEndDate().after(todayDate)) {
                it.remove();
            }
        }
        searchResults = tempList;
    }
    
    public void thisMonth() {
        List<Conference> tempList = new ArrayList<>(conferenceList);
        Date monthStart = DateHelper.getMonthStartDate();
        Date monthEnd = DateHelper.getMonthEndDate();
        
        Iterator it = tempList.iterator();
        while(it.hasNext()) {
            Conference c = (Conference)it.next();
            if(c.getStartDate().after(monthEnd) || c.getEndDate().before(new Date())) {
                it.remove();
            }
        }
        searchResults = tempList;
    }
    
    public boolean renderSignUp(Conference conference){
        boolean cond1 = conference.getStartDate().after(threeDaysFromNow);
        boolean cond2 = !CONFERENCE_HELPER.isSubscribed(userBean.getUser(), conference);
        return  cond1 && cond2;
    }
    
    public List<Conference> getSearchResults() {
        return searchResults;
    }

    public String[] getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(String[] selectedFields) {
        this.selectedFields = selectedFields;
    }

    public List<Conference> getConferenceList() {
        return conferenceList;
    }

    public List<Conference> getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(List<Conference> selectedName) {
        this.selectedName = selectedName;
    }

    public List<String> getFieldStrings() {
        return fieldStrings;
    }

    public void setFieldStrings(List<String> fieldStrings) {
        this.fieldStrings = fieldStrings;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
}
