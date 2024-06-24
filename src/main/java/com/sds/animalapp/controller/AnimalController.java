package com.sds.animalapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sds.animalapp.common.Pager;
import com.sds.animalapp.domain.Animal;
import com.sds.animalapp.domain.AnimalSelectParam;
import com.sds.animalapp.domain.Member;
import com.sds.animalapp.model.animal.AdoptAnimalService;
import com.sds.animalapp.model.animal.AnimalApiService;
import com.sds.animalapp.model.animal.AnimalService;
import com.sds.animalapp.model.animal.InterestAnimalService;
import com.sds.animalapp.model.member.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AnimalController {

	private final AnimalService animalService;

	private final AnimalApiService animalApiService;

	private final MemberService memberService; // MemberService

	private final InterestAnimalService interestAnimalService;

	private final AdoptAnimalService adoptAnimalService;

	@GetMapping("/animal/list")
	public String getAnimal(Animal animal, Model model,
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "kind", defaultValue = "") String kind,
			@RequestParam(value = "age", defaultValue = "") String age,
			@RequestParam(value = "region", defaultValue = "") String region,
			@RequestParam(value = "care", defaultValue = "") String care,
			@RequestParam(value = "sex", defaultValue = "") String sex,
			@RequestParam(value = "status", defaultValue = "") String status) throws Exception {
		/*---------------------------------------------------------
		 		API 호출 코드
		//		animalService.delete();
		//		List<Animal> animalAPIList = animalApiService.call();
		//		animalService.insertAll(animalAPIList);
		----------------------------------------------------------*/
		Pager pager = new Pager();

		AnimalSelectParam animalSelectParam = new AnimalSelectParam();
		animalSelectParam.setKeyword(kind);
		animalSelectParam.setStartIndex(pager.getStartIndex());
		animalSelectParam.setRowCount(pager.getPageSize());
		animalSelectParam.setAge(age);
		animalSelectParam.setRegion(region);
		animalSelectParam.setCare(care);
		animalSelectParam.setSex(sex);
		animalSelectParam.setStatus(status);

		pager.init(animalService.selectCount(animalSelectParam), currentPage);

		// 페이지에 해당하는 동물 리스트를 가져올 때 페이징 정보를 고려하여 가져와야 함
		animalSelectParam.setStartIndex(pager.getStartIndex());
		animalSelectParam.setRowCount(pager.getPageSize());

		List<Animal> animalList = animalService.selectAll(animalSelectParam);

		model.addAttribute("pager", pager);
		model.addAttribute("animalList", animalList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("kind", kind);
		model.addAttribute("age", age);
		model.addAttribute("region", region);
		model.addAttribute("care", care);
		model.addAttribute("sex", sex);
		model.addAttribute("status", status);

		return "animal/list";
	}

	@GetMapping("/animal/detail")
	public String getDetail(Model model, HttpSession session, @RequestParam(value = "id") int animal_idx) {
		Member member = (Member) session.getAttribute("member");
		Animal animal = animalService.select(animal_idx);
		int applicantsCount = animalService.countRegistMember(animal_idx); // 수정

		Integer shelter_idx = animalService.findShelterIdxByCareNm(animal.getCareNm(), animal.getCareAddr(),
				animal.getOrgNm());
		if (shelter_idx != null) {
			model.addAttribute("shelter_idx", shelter_idx);
		}
		if (member != null) {
			int interestRecordNum = interestAnimalService.getInterestRecordNum(member.getMember_idx(), animal_idx);
			int adoptRecordNum = adoptAnimalService.getAdopteRecordNum(member.getMember_idx(), animal_idx);
			model.addAttribute("interestRecordNum", interestRecordNum);
			model.addAttribute("adoptRecordNum", adoptRecordNum);
		}

		model.addAttribute("detail", animal);
		model.addAttribute("applicantsCount", applicantsCount); // 추가

		return "animal/detail";
	}

}