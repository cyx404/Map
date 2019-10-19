package team.t404.gotravel.model;

import java.util.Date;

/**
 * {"places_id":12, "time":"2019-10-1 12:02:23"}
 **/

public class Placeid_Time {
    private int place_id;
    private Date time;

    public Placeid_Time(int place_id, Date time) {
        super();
        this.place_id = place_id;
        this.time = time;
    }

    public int getPlaces_id() {
        return place_id;
    }

    public Date getTime() {
        return time;
    }

    public void setPlaces_id(int places_id) {
        this.place_id = places_id;
    }

    public void setTime(Date time) {
        this.time = time;
    }


}
