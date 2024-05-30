package com.sds.animalapp.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sds.animalapp.common.Pager;
import com.sds.animalapp.domain.Shelter;
import com.sds.animalapp.domain.ShelterSelectParam;
import com.sds.animalapp.model.shelter.ShelterApiService;
import com.sds.animalapp.model.shelter.ShelterService;
import com.sds.animalapp.model.shelter.SidoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ShelterController {

	@Autowired
	private ShelterApiService shelterApiService;

	@Autowired
	private ShelterService shelterService;
	
	@Autowired
	private SidoService sidoService;
	
	@Autowired
	private ShelterSelectParam shelterSelectParam;

	@GetMapping("/shelter/list")
	public String getShelter(Shelter shelter, Model model,
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "currentSidoCode", defaultValue = "00") String currentSidoCode) throws Exception {
		
		List shelterList = shelterApiService.getShelterList(shelter);
	
		Pager pager = new Pager();
		pager.init(shelterService.selectCount(keyword), currentPage);
		
		List sidoList = sidoService.selectAll();
		
		shelterService.selectAll(shelterSelectParam);
		//Shelter DB 테이블이 시도 테이블과 시군구 테이블을 참조하도록 수정한 후 검색 다시 구현
		ShelterSelectParam shelterSelectParam = new ShelterSelectParam();
		shelterSelectParam.setKeyword(keyword);
		shelterSelectParam.setStartIndex(pager.getStartIndex());
		shelterSelectParam.setRowCount(pager.getPageSize());
		shelterSelectParam.setCurrentSidoCode(currentSidoCode);
		
		shelterService.selectAll(shelterSelectParam);

		model.addAttribute("pager", pager);
		model.addAttribute("shelterList", shelterList);
		model.addAttribute("sidoList", sidoList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentSidoCode", currentSidoCode);

		return "shelter/list";
	}

	// 세부창
	@GetMapping("/shelter/detail")
	public String getDetail(Model model, @RequestParam(value = "id") int shelter_idx) {
		Shelter shelter = shelterService.select(shelter_idx);

		model.addAttribute("detail", shelter);

		return "shelter/detail";
	}

}