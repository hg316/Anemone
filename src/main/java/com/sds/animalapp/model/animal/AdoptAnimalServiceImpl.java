package com.sds.animalapp.model.animal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sds.animalapp.domain.AdoptAnimal;

@Service
public class AdoptAnimalServiceImpl implements AdoptAnimalService {

	@Autowired
	private AdoptAnimalDAO adoptAnimalDAO;

	@Override
	public void addAdoptAnimal(AdoptAnimal adopt) {
		// 중복 체크
		if (!checkDuplicateAdoptAnimal(adopt)) {
			adoptAnimalDAO.addAdoptAnimal(adopt);
		} else {
			throw new IllegalArgumentException("이미 입양 신청된 관심 동물입니다.");
		}
	}

	@Override
	public void deleteAdoptAnimal(int adopt_animal_idx) {
		adoptAnimalDAO.deleteAdoptAnimal(adopt_animal_idx);
	}

	@Override
	public boolean checkDuplicateAdoptAnimal(AdoptAnimal adoptAnimal) {
		int count = adoptAnimalDAO.checkDuplicateAdoptAnimal(adoptAnimal);
		return count > 0;
	}

	@Override
	public List<AdoptAnimal> getAllAdopts() {
		return adoptAnimalDAO.findAll();
	}

	@Override
	public List<AdoptAnimal> getAdoptByMemberIdx(int member_idx) {
		return adoptAnimalDAO.findByMemberIdx(member_idx);
	}

	@Override
	public AdoptAnimal findById(int adopt_animal_idx) {
		return adoptAnimalDAO.findAdoptById(adopt_animal_idx);
	}

	@Override
	public AdoptAnimal findAdoptByAnimalIdxAndMemberIdx(int animal_idx, int member_idx) {
		return adoptAnimalDAO.findAdoptByAnimalIdxAndMemberIdx(animal_idx, member_idx);
	}

	@Override
	public int getAdopteRecordNum(int member_idx, int animal_idx) {
		return adoptAnimalDAO.selectCount(member_idx, animal_idx);
	}
}
