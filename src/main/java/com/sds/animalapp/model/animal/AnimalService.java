package com.sds.animalapp.model.animal;

import java.util.List;

import com.sds.animalapp.domain.Animal;
import com.sds.animalapp.domain.AnimalSelectParam;

public interface AnimalService {
	void insertAll(List<Animal> animalList); // 보호동물 API 데이터 insert

	void insert(Animal animal); // 새로운 데이터 삽입

	void update(Animal animal); // 기존 데이터 업데이트

	void delete();

	int selectCount(AnimalSelectParam animalSelectParam); // 필터링에 따른 총 레코드 수

	List<Animal> selectAll(AnimalSelectParam animalSelectParam); // 필터링에 매칭되는 모든 동물 조회

	Animal select(int animal_idx); // 동물 상세 조회

	List<Animal> selectPreview(); // 미리보기

	int countRegistMember(int animal_idx); // 한 동물에 대한 입양신청 인원 조회

	Animal selectByDesertionNo(String desertionNo); // 유기번호로 동물 조회

	Integer findShelterIdxByCareNm(String careNm, String careAddr, String orgNm);
}
