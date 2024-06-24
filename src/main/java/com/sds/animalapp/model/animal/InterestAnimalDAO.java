package com.sds.animalapp.model.animal;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sds.animalapp.domain.AdoptAnimal;
import com.sds.animalapp.domain.InterestAnimal;

@Mapper
public interface InterestAnimalDAO {

	// 관심 동물 등록
	public void addInterestAnimal(InterestAnimal interestAnimal);

	// 관심 동물 삭제
	public void deleteInterestAnimal(int interest_animal_idx);

	// 관심 동물 중복 확인
	public int checkDuplicateInterestAnimal(InterestAnimal interestAnimal);

	public int selectCount(@Param("member_idx") int member_idx, @Param("animal_idx") int animal_idx);

	InterestAnimal findInterest(@Param("kindCd") String kindCd, @Param("popfile") String popfile);

	List<InterestAnimal> findByMemberIdx(int member_idx);

	List<InterestAnimal> findAll();

	InterestAnimal findInterestById(int interest_animal_idx);

	InterestAnimal findInterestByAnimalIdxAndMemberIdx(@Param("animal_idx") int animal_idx,
			@Param("member_idx") int member_idx);
}
