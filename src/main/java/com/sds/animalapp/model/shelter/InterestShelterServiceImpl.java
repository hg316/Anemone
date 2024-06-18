package com.sds.animalapp.model.shelter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sds.animalapp.domain.InterestShelter;

@Service
public class InterestShelterServiceImpl implements InterestShelterService{
	
	@Autowired
	private InterestShelterDAO interestShelterDAO;

	public void insertInterestShelter(InterestShelter interestShelter) {
		if(!duplicatedInterestShelter(interestShelter)) {
			interestShelterDAO.insertInterestShelter(interestShelter);			
		}else {
			throw new IllegalArgumentException("이미 등록된 관심 동물입니다.");
		}
	}

	public void deleteInterestShelter(int interest_shetler_idx) {
		interestShelterDAO.deleteInterestShelter(interest_shetler_idx);
	}

	@Override
	public boolean duplicatedInterestShelter(InterestShelter interestShelter) {
		int count = interestShelterDAO.duplicatedInterestShelter(interestShelter);
		return count > 0;
	}




}
