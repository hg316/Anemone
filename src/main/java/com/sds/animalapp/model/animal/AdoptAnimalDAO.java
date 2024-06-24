package com.sds.animalapp.model.animal;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sds.animalapp.domain.AdoptAnimal;

@Mapper
public interface AdoptAnimalDAO {
	// 입양신청 동물 등록
	public void addAdoptAnimal(AdoptAnimal adoptAnimal);

	// 입양신청 동물 삭제
	public void deleteAdoptAnimal(int adopt_animal_idx);

	// 입양신청 동물 중복 확인
	public int checkDuplicateAdoptAnimal(AdoptAnimal adoptAnimal);

	public int selectCount(@Param("member_idx") int member_idx, @Param("animal_idx") int animal_idx);

	AdoptAnimal findAdopt(@Param("kindCd") String kindCd, @Param("popfile") String popfile);

	List<AdoptAnimal> findByMemberIdx(int member_idx);

	List<AdoptAnimal> findAll();

	AdoptAnimal findAdoptById(int adopt_animal_id);

	AdoptAnimal findAdoptByAnimalIdxAndMemberIdx(@Param("animal_idx") int animal_idx,
			@Param("member_idx") int member_idx);

}