package com.sds.animalapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sds.animalapp.domain.Animal;
import com.sds.animalapp.domain.InterestAnimal;
import com.sds.animalapp.domain.Member;
import com.sds.animalapp.model.animal.AnimalService;
import com.sds.animalapp.model.animal.InterestAnimalService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class InterestAnimalController {

	@Autowired
	private InterestAnimalService interestAnimalService;

	@Autowired
	private AnimalService animalService;

	@PostMapping("/animal/registerInterest")
	@ResponseBody
	public ResponseEntity<String> registerInterest(@RequestParam("animal_idx") int animal_idx,
			@RequestParam("kindCd") String kindCd, @RequestParam("popfile") String popfile, HttpSession session) {

		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}

		int member_idx = member.getMember_idx();

		log.debug(" 받은animal_idx 값: " + animal_idx);
		log.debug("받은 member_idx 값: " + member_idx);

		Animal animal = animalService.select(animal_idx);
		if (animal == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("동물을 찾을 수 없습니다.");
		}

		InterestAnimal interestAnimal = new InterestAnimal();
		interestAnimal.setAnimal_idx(animal_idx); // 동물의 인덱스 설정
		interestAnimal.setMember_idx(member_idx); // 사용자의 인덱스 설정
		interestAnimal.setKindCd(kindCd);
		interestAnimal.setPopfile(popfile);

		try {
			interestAnimalService.addInterestAnimal(interestAnimal);
		} catch (Exception e) {
			log.error("관심 동물 등록 실패: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok("신청완료");
	}

	@PostMapping("/animal/cancelInterest")
	@ResponseBody
	public ResponseEntity<String> cancelInterest(@RequestParam("animal_idx") int animal_idx, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}

		try {
			InterestAnimal interestAnimal = interestAnimalService.findInterestByAnimalIdxAndMemberIdx(animal_idx,
					member.getMember_idx());
			if (interestAnimal == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("관심 동물 신청 내역을 찾을 수 없습니다.");
			}
			interestAnimalService.deleteInterestAnimal(interestAnimal.getInterest_animal_idx());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관심 동물 신청 취소 실패: " + e.getMessage());
		}
		return ResponseEntity.ok("신청 취소 완료");
	}

	@PostMapping("/animal/checkInterest")
	@ResponseBody
	public ResponseEntity<Boolean> checkInterest(@RequestParam("animal_idx") int animal_idx,
			@RequestParam("member_idx") int member_idx, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		}

		InterestAnimal interestAnimal = interestAnimalService.findInterestByAnimalIdxAndMemberIdx(animal_idx,
				member_idx);
		return ResponseEntity.ok(interestAnimal != null);
	}
}
