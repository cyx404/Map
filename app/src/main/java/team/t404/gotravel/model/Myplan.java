package team.t404.gotravel.model;

import java.util.Date;
import java.util.List;

/**
{
"plan_name":"第一个计划",
"places_id":[152, 13, 58, 561],
"time":"2019-1-1 15:50:30"
}
 * **/
public class Myplan {

	private String plan_name;
	private List<Integer> places_id;
	private Date time;

	public Myplan(String plan_name, List<Integer> places_id, Date time) {
		super();
		this.plan_name = plan_name;
		this.places_id = places_id;
		this.time = time;
	}

	public String getPlan_name() {
		return plan_name;
	}

	public List<Integer> getPlaces_id() {
		return places_id;
	}

	public Date getTime() {
		return time;
	}

	public void setPlan_name(String plan_name) {
		this.plan_name = plan_name;
	}

	public void setPlaces_id(List<Integer> places_id) {
		this.places_id = places_id;
	}

	public void setTime(Date time) {
		this.time = time;
	}



}
