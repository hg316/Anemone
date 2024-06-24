package com.sds.animalapp.model.animal;

import java.util.List;

import com.sds.animalapp.domain.AdoptAnimal;
import com.sds.animalapp.domain.InterestAnimal;

public interface InterestAnimalService {

	// 관심 동물 등록
	public void addInterestAnimal(InterestAnimal interestAnimal);

	// 관심 동물 삭제
	public void deleteInterestAnimal(int interest_animal_idx);

	// 관심 동물 중복 확인
	public boolean checkDuplicateInterestAnimal(InterestAnimal interestAnimal);

	public int getInterestRecordNum(int member_idx, int animal_idx);

	List<InterestAnimal> getAllInterests();

	List<InterestAnimal> getInterestByMemberIdx(int member_idx);

	InterestAnimal findById(int interest_animal_idx);

	InterestAnimal findInterestByAnimalIdxAndMemberIdx(int animal_idx, int member_idx);
}
