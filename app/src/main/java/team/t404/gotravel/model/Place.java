package team.t404.gotravel.model;

//import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

//@Document(collection = "place")
public class Place implements Serializable {

    private int place_id;
    private String name;
    private String introduce;
    private List<String> picture;
    private int praise;
    private String address;
    private String longitude_latitude;
    private List<String> place_type;
    private List<String> hobby;
    private List<String> customization;

    public int getPlace_id() {
        return place_id;
    }

    public String getName() {
        return name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public List<String> getPicture() {
        return picture;
    }

    public int getPraise() {
        return praise;
    }

    public String getAddress() {
        return address;
    }

    public String getLongitude_latitude() {
        return longitude_latitude;
    }

    public List<String> getPlace_type() {
        return place_type;
    }

    public List<String> getHobby() {
        return hobby;
    }

    public List<String> getCustomization() {
        return customization;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLongitude_latitude(String longitude_latitude) {
        this.longitude_latitude = longitude_latitude;
    }

    public void setPlace_type(List<String> place_type) {
        this.place_type = place_type;
    }

    public void setHobby(List<String> hobby) {
        this.hobby = hobby;
    }

    public void setCustomization(List<String> customization) {
        this.customization = customization;
    }


   /**针对List集合中的对象重写hashcode()和equals()方法；retainAll()和removeAll()会利用对象的这两个方法来比较对象是否是同一个对象**/
    @Override
    public int hashCode() {
        return this.place_id;
    }

    //重写
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Place) {
            Place place = (Place) obj;
            return this.place_id == place.place_id;
        }
        return true;
    }


}