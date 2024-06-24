package com.sds.animalapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sds.animalapp.domain.AdoptAnimal;
import com.sds.animalapp.domain.Animal;
import com.sds.animalapp.domain.Member;
import com.sds.animalapp.model.animal.AdoptAnimalService;
import com.sds.animalapp.model.animal.AnimalService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AdoptAnimalController {

	@Autowired
	private AdoptAnimalService adoptAnimalService;

	@Autowired
	private AnimalService animalService;

	@PostMapping("/animal/registerAdopt")
	@ResponseBody
	public ResponseEntity<String> registerAdopt(@RequestParam("animal_idx") int animal_idx,
			@RequestParam("kindCd") String kindCd, @RequestParam("popfile") String popfile, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}
		int member_idx = member.getMember_idx();
		// 로그 추가
		log.debug("받은 animal_idx: " + animal_idx);
		log.debug("받은 member_idx: " + member_idx);

		Animal animal = animalService.select(animal_idx);
		if (animal == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("동물을 찾을 수 없습니다.");
		}

		AdoptAnimal adoptAnimal = new AdoptAnimal();
		adoptAnimal.setAnimal_idx(animal.getAnimal_idx());
		adoptAnimal.setMember_idx(member.getMember_idx());
		adoptAnimal.setKindCd(kindCd);
		adoptAnimal.setPopfile(popfile);

		try {
			adoptAnimalService.addAdoptAnimal(adoptAnimal);
		} catch (Exception e) {
			log.error("입양 신청 실패: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok("신청완료");
	}

	@PostMapping("/animal/cancelAdopt")
	@ResponseBody
	public ResponseEntity<String> cancelAdopt(@RequestParam("animal_idx") int animal_idx, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}
		int member_idx = member.getMember_idx();

		try {
			AdoptAnimal adoptAnimal = adoptAnimalService.findAdoptByAnimalIdxAndMemberIdx(animal_idx, member_idx);
			if (adoptAnimal == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("입양 신청 내역을 찾을 수 없습니다.");
			}
			adoptAnimalService.deleteAdoptAnimal(adoptAnimal.getAdopt_animal_idx());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("입양 신청 취소 실패: " + e.getMessage());
		}
		return ResponseEntity.ok("신청 취소 완료");
	}

	@PostMapping("/animal/checkAdopt")
	@ResponseBody
	public ResponseEntity<Boolean> checkAdopt(@RequestParam("animal_idx") int animal_idx,
			@RequestParam("member_idx") int member_idx, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		}

		AdoptAnimal adoptAnimal = adoptAnimalService.findAdoptByAnimalIdxAndMemberIdx(animal_idx, member_idx);
		return ResponseEntity.ok(adoptAnimal != null);
	}

}
