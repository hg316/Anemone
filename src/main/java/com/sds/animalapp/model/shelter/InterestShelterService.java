package com.sds.animalapp.model.shelter;

import com.sds.animalapp.domain.InterestShelter;

public interface InterestShelterService {
	
	public void insertInterestShelter(InterestShelter interestShelter);
	
	public void deleteInterestShelter(int interest_shetler_idx);
	
	public boolean duplicatedInterestShelter (InterestShelter interestShelter);
	
}
