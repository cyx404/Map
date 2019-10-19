package team.t404.gotravel.model;

import java.util.List;

/**
 * 个性表
 * **/

public class Label {

	private int chi_id;
	private List<String> hobby;
	private List<String> customization;
	private List<String> place_type;

	public int getChi_id() {
		return chi_id;
	}

	public List<String> getHobby() {
		return hobby;
	}

	public List<String> getCustomization() {
		return customization;
	}

	public List<String> getPlace_type() {
		return place_type;
	}

	public void setChi_id(int chi_id) {
		this.chi_id = chi_id;
	}

	public void setHobby(List<String> hobby) {
		this.hobby = hobby;
	}

	public void setCustomization(List<String> customization) {
		this.customization = customization;
	}

	public void setPlace_type(List<String> place_type) {
		this.place_type = place_type;
	}
	


}
