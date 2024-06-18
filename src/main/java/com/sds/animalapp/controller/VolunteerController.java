package com.sds.animalapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sds.animalapp.common.Pager;
import com.sds.animalapp.domain.Member;
import com.sds.animalapp.domain.VolunteerApplication;
import com.sds.animalapp.domain.VolunteerNotice;
import com.sds.animalapp.domain.VolunteerSelectParam;
import com.sds.animalapp.model.volunteer.VolunteerApplicationService;
import com.sds.animalapp.model.volunteer.VolunteerService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class VolunteerController {

	@Autowired
	private VolunteerService volunteerService;

	@Autowired
	private VolunteerApplicationService volunteerApplicationService;

	@GetMapping("/volunteer/list")
	public String volunteerList(Model model, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "keyword", defaultValue = "") String keyword) {
		Pager pager = new Pager();
		pager.init(volunteerService.selectCount(keyword), currentPage);

		VolunteerSelectParam volunteerSelectParam = new VolunteerSelectParam();
		volunteerSelectParam.setKeyword(keyword);
		volunteerSelectParam.setStartIndex(pager.getStartIndex());
		volunteerSelectParam.setRowCount(pager.getPageSize());

		// 봉사 목록 가져오기
		List<VolunteerNotice> volunteerList = volunteerService.selectAll(volunteerSelectParam);

		// 모델에 데이터 추가
		model.addAttribute("volunteerList", volunteerList);
		model.addAttribute("pager", pager);
		model.addAttribute("keyword", keyword);

		// 뷰 반환
		return "volunteer/list";
	}

	@GetMapping("/volunteer/writeform")
	public String getWriteForm(HttpSession session) {
		Member member = (Member) session.getAttribute("member");
        if (member == null) return "redirect:/member/login";
        
		return "volunteer/regist";
	}

	@PostMapping("/volunteer/regist") 
	public String regist(VolunteerNotice volunteer, HttpSession session) { 
		Member member = (Member) session.getAttribute("member");
        if (member == null) return "redirect:/member/login";
        volunteer.setMember_idx(member.getMember_idx());
		volunteerService.insert(volunteer);
		
		return "redirect:/volunteer/list"; 
	}
	
	// 세부창
	@GetMapping("/volunteer/detail")
	public String getDetail(Model model, HttpSession session, @RequestParam(value = "id") int noticeId) {
		Member member = (Member) session.getAttribute("member");
        if (member == null) return "redirect:/member/login";
        
		log.info("detail에서 Id 요청 확인용: " + noticeId); // noticeId 값 로그 추가

		VolunteerNotice volunteerNotice = volunteerService.select(noticeId);
		if (volunteerNotice == null) {
			log.error("noticeId 값이 없음 : " + noticeId);
			return "redirect:/error/404";
		}

		// 해당 봉사를 신청한 사람 수 불러오기
		// id -> noticeId로 변경
		int registNum = volunteerService.selectRegistCount(noticeId);
		
		int member_idx = member.getMember_idx();
		log.info(String.valueOf("detail page 요청 member_idx: ")+ String.valueOf(member.getMember_idx()));
		//voluntear_apply 테이블에서 내가 신청한 봉사 기록 확인
		int recordNum = volunteerApplicationService.getRecordNum(noticeId, member_idx);
		log.info("내 신청 수: "+String.valueOf(recordNum));
		
		model.addAttribute("detail", volunteerNotice);
		model.addAttribute("registCount", registNum);
		model.addAttribute("recordNum", recordNum);

		return "volunteer/detail";
	}

	

	@PostMapping("/volunteer/apply")
	@ResponseBody
	public ResponseEntity<String> apply(@RequestParam("id") int noticeId, HttpSession session) {
		log.info("Received noticeId: " + noticeId); // 로그 추가하여 noticeId 값 확인
		Member member = (Member) session.getAttribute("member");
		if (member == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}

		VolunteerNotice volunteerNotice = volunteerService.select(noticeId);
		if (volunteerNotice == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("봉사활동을 찾을 수 없습니다.");
		}

		VolunteerApplication volunteerApplication = new VolunteerApplication();
		volunteerApplication.setTitle(volunteerNotice.getTitle());
		volunteerApplication.setVol_date(volunteerNotice.getVol_date());
		volunteerApplication.setNoticeId(volunteerNotice.getVol_event_post_idx()); // 여기에 noticeId 설정
		volunteerApplication.setMemberIdx(member.getMember_idx());

		volunteerApplicationService.apply(volunteerApplication);
		return ResponseEntity.ok("신청 완료");
	}

	@PostMapping("/volunteer/cancel")
	@ResponseBody
	public ResponseEntity<String> cancel(@RequestParam("applicationId") int noticeId, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		log.info("CANCEL METHOD Received noticeId: " + noticeId); // 로그 추가하여 noticeId 값 확인
		volunteerApplicationService.cancel(noticeId, member.getMember_idx());
		return ResponseEntity.ok("신청 취소 완료");
	}

}
