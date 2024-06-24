package com.sds.animalapp.model.animal;

import java.util.List;

import com.sds.animalapp.domain.AdoptAnimal;
import com.sds.animalapp.domain.VolunteerApplication;

public interface AdoptAnimalService {

	// 입양신청 동물 등록
	public void addAdoptAnimal(AdoptAnimal adopt);

	// 입양신청 동물 삭제
	public void deleteAdoptAnimal(int adopt_animal_idx);

	// 입양신청 동물 중복 확인
	public boolean checkDuplicateAdoptAnimal(AdoptAnimal adoptAnimal);

	public int getAdopteRecordNum(int member_idx, int animal_idx);

	List<AdoptAnimal> getAllAdopts();

	List<AdoptAnimal> getAdoptByMemberIdx(int member_idx);

	AdoptAnimal findById(int adopt_animal_idx);

	AdoptAnimal findAdoptByAnimalIdxAndMemberIdx(int animal_idx, int member_idx);

}
