package com.sds.animalapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sds.animalapp.domain.InterestShelter;
import com.sds.animalapp.model.shelter.InterestShelterService;

@Controller
public class InterestShelterController {
	
	@Autowired
	private InterestShelterService interestShelterService;
	
	@PostMapping("/shelter/registInterestShelter")
	@ResponseBody
	public String registInterestShelter(
			@RequestParam("shelter_idx") int shelter_idx,
			@RequestParam("member_idx") int member_idx) {
		
		
		InterestShelter interestShelter = new InterestShelter();
        interestShelter.setShelter_idx(shelter_idx);
        interestShelter.setMember_idx(member_idx);
		
			
		try {
			interestShelterService.insertInterestShelter(interestShelter);
			return "보호소 관심등록이 완료되었습니다.";
		}catch (IllegalArgumentException e) {
			return "이미 등록된 관심동물 입니다.";
		}
	}
	

}
