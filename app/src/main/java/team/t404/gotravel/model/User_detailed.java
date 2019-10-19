package team.t404.gotravel.model;

//import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 用户的详细信息表(user_detailed)
 **/
//@Document(collection = "user_detailed")
public class User_detailed {
    private String phone;
    private List<String> hobby;
    private List<String> customization;
    private List<Placeid_Time> mycollections;
    private List<Myhistory> myhistories;
    private List<Myplan> myplans;

    public String getPhone() {
        return phone;
    }

    public List<String> getHobby() {
        return hobby;
    }

    public List<String> getCustomization() {
        return customization;
    }

    public List<Placeid_Time> getMycollections() {
        return mycollections;
    }

    public List<Myhistory> getMyhistories() {
        return myhistories;
    }

    public List<Myplan> getMyplans() {
        return myplans;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setHobby(List<String> hobby) {
        this.hobby = hobby;
    }

    public void setCustomization(List<String> customization) {
        this.customization = customization;
    }

    public void setMycollections(List<Placeid_Time> mycollections) {
        this.mycollections = mycollections;
    }

    public void setMyhistories(List<Myhistory> myhistories) {
        this.myhistories = myhistories;
    }

    public void setMyplans(List<Myplan> myplans) {
        this.myplans = myplans;
    }

}
