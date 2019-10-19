/**
 * 
{
"places_id":[
{"place_id":20,"time":"2019-10-1 10:02:23"},
{"place_id":10,"time":"2019-10-1 12:08:23"},
{"place_id":2,"time":"2019-10-1 16:22:23"}
],
"date":"2019-10-1"
},

 */
package team.t404.gotravel.model;
import java.util.List;

/**
 * @Description: 嵌套集合
 *  @date 2019年8月13日 下午4:50:10
 */
public class Myhistory {

	private List<Placeid_Time> places_time;
	private String date;

	public Myhistory(List<Placeid_Time> places_time, String date) {
		super();
		this.places_time = places_time;
		this.date = date;
	}

	public List<Placeid_Time> getPlaces_time() {
		return places_time;
	}

	public String getDate() {
		return date;
	}

	public void setPlaces_time(List<Placeid_Time> places_time) {
		this.places_time = places_time;
	}

	public void setDate(String date) {
		this.date = date;
	}
 

}
