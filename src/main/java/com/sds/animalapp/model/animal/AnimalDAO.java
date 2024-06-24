package com.sds.animalapp.model.animal;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sds.animalapp.domain.Animal;
import com.sds.animalapp.domain.AnimalSelectParam;

@Mapper
public interface AnimalDAO {
	public void insertAll(List<Animal> animalList); // 보호동물 API 데이터 insert 문

	void insert(Animal animal); // 새로운 데이터 삽입

	void update(Animal animal); // 기존 데이터 업데이트

	public void delete();

	public int selectCount(AnimalSelectParam animalSelectParam); // 필터링에 따른 총 레코드 수

	public List selectAll(AnimalSelectParam animalSelectParam); // 필터링에 매칭되는 모든 필터링 된 동물 조회

	public Animal select(int animal_idx); // 동물 상세 조회

	public List selectPreview(); // 미리보기

	// 한 동물에 대한 입양신청 인원 조회
	public int countRegistMember(int animal_idx);

	Animal selectByDesertionNo(String desertionNo); // 유기번호로 동물 조회

	Integer findShelterIdxByCareNm(@Param("careNm") String careNm, @Param("careAddr") String careAddr,
			@Param("orgNm") String orgNm);
}