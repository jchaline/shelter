package fr.jchaline.shelter.json;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Dweller list for team up before mission
 * @author jeremy
 */
public class TeamUp {
	
	@NotNull
	@NotEmpty
	private List<Long> dwellersId;
	
	public TeamUp() {
		
	}

	public List<Long> getDwellersId() {
		return dwellersId;
	}

	public void setDwellersId(List<Long> dwellersId) {
		this.dwellersId = dwellersId;
	}

}
