package com.sds.animalapp.model.animal;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sds.animalapp.domain.Animal;
import com.sds.animalapp.domain.AnimalSelectParam;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

	private final AnimalDAO animalDAO;

	public void insertAll(List<Animal> animalList) {
		animalDAO.insertAll(animalList);
	}

	@Override
	public void insert(Animal animal) {
		animalDAO.insert(animal);
	}

	public void update(Animal animal) {
		animalDAO.update(animal);
	} // 기존 데이터 업데이트

	public void delete() {
		animalDAO.delete();
	}

	public int selectCount(AnimalSelectParam animalSelectParam) {
		return animalDAO.selectCount(animalSelectParam);
	}

	public List selectAll(AnimalSelectParam animalSelectParam) {
		return animalDAO.selectAll(animalSelectParam);
	}

	public Animal select(int animal_idx) {
		return animalDAO.select(animal_idx);
	}

	public List selectPreview() {

		return animalDAO.selectPreview();
	}

	@Override
	public int countRegistMember(int animal_idx) {
		return animalDAO.countRegistMember(animal_idx);
	}

	@Override
	public Animal selectByDesertionNo(String desertionNo) {
		return animalDAO.selectByDesertionNo(desertionNo);
	}

	@Override
	public Integer findShelterIdxByCareNm(String careNm, String careAddr, String orgNm) {
		return animalDAO.findShelterIdxByCareNm(careNm, careAddr, orgNm);
	}
}