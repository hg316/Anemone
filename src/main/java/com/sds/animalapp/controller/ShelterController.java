package com.sds.animalapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sds.animalapp.common.Pager;
import com.sds.animalapp.domain.Shelter;
import com.sds.animalapp.domain.ShelterSelectParam;
import com.sds.animalapp.domain.Sido;
import com.sds.animalapp.domain.Signgu;
import com.sds.animalapp.model.shelter.ShelterApiService;
import com.sds.animalapp.model.shelter.ShelterService;
import com.sds.animalapp.model.shelter.SidoService;
import com.sds.animalapp.model.shelter.SignguService;

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
    private SignguService signguService;
    

    @GetMapping("/shelter/list")
    public String getShelter(Model model,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "currentSidoCode", defaultValue = "00") String currentSidoCode,
            @RequestParam(value = "currentSignguCode", defaultValue = "00") String currentSignguCode) throws Exception {

    	ShelterSelectParam shelterSelectParam = new ShelterSelectParam();
    	shelterSelectParam.setKeyword(keyword);
    	shelterSelectParam.setCurrentSidoCode(currentSidoCode);
    	shelterSelectParam.setCurrentSignguCode(currentSignguCode);
    	
        Pager pager = new Pager();
        pager.init(shelterService.selectCount(shelterSelectParam), currentPage);
        
        List<Sido> sidoList = sidoService.selectAll();
        List<Signgu> signguList = signguService.selectAll(currentSidoCode);
        
        //Shelter DB 테이블이 시도 테이블과 시군구 테이블을 참조하도록 수정한 후 검색 다시 구현
        shelterSelectParam.setStartIndex(pager.getStartIndex());
        shelterSelectParam.setRowCount(pager.getPageSize());
        
        List shelterList = shelterService.selectAll(shelterSelectParam);
        Shelter shelter = new Shelter();
        
        
        
        model.addAttribute("pager", pager);
        model.addAttribute("shelterList", shelterList);
        model.addAttribute("sidoList", sidoList);
        model.addAttribute("signguList", signguList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentSidoCode", currentSidoCode);
        model.addAttribute("currentSignguCode", currentSignguCode);
        model.addAttribute("shelter", shelter);

        return "shelter/list";
    }

 // 세부창
    @GetMapping("/shelter/detail")
    public String getDetail(Model model, @RequestParam(value = "id", required = false) Integer shelter_idx,
                            @RequestParam(value = "careNm", required = false) String careNm) {
        // shelter_idx가 제공되지 않은 경우 careNm으로 shelter_idx를 찾음
        if (shelter_idx == null && careNm != null && !careNm.isEmpty()) {
            shelter_idx = shelterService.findShelterIdxByCareNm(careNm);
        }

        // shelter_idx가 여전히 null인 경우 에러 페이지로 리디렉션
        if (shelter_idx == null) {
            return "redirect:/error";
        }
        

        // shelter_idx가 존재하면 해당 shelter 정보를 조회하여 모델에 추가
        Shelter shelter = shelterService.select(shelter_idx);
        model.addAttribute("detail", shelter);

        return "shelter/detail";
    }
    
    @GetMapping("/shelter/select/signgu")
    @ResponseBody
    public List<Signgu> getSigngu(@RequestParam(value = "currentSidoCode", defaultValue = "00") String currentSidoCode) {
        
        List<Signgu> signguList = signguService.selectAll(currentSidoCode);
        
        return signguList;
    }

    
}