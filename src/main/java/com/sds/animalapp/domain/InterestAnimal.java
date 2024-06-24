package com.sds.animalapp.domain;

import lombok.Data;

@Data
public class InterestAnimal {

	private int interest_animal_idx;
	private int member_idx;
	private int animal_idx;

	// 추가
	private String kindCd;
	private String popfile;

}
