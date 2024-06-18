package com.sds.animalapp.model.shelter;

import org.apache.ibatis.annotations.Mapper;

import com.sds.animalapp.domain.InterestShelter;


@Mapper
public interface InterestShelterDAO {
	
	public void insertInterestShelter(InterestShelter interestShelter);
	
	public void deleteInterestShelter(int interest_shetler_idx);
	
	public int duplicatedInterestShelter (InterestShelter interestShelter);
	
}
