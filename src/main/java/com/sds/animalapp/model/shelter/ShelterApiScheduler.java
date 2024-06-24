//package com.sds.animalapp.model.shelter;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import com.sds.animalapp.domain.Shelter;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class ShelterApiScheduler {
//
//	private final ShelterApiService shelterApiService;
//
//    private final ShelterService shelterService;
//
//	private static final String LAST_EXECUTION_TIME_FILE = "shelterlastExecutionTime.txt";
//	private LocalDateTime lastExecutionTime;
//
//	@PostConstruct
//	public void init() {
//		// 마지막 실행 시간을 파일에서 읽어옴
//		lastExecutionTime = readLastExecutionTime();
//		if (lastExecutionTime == null || lastExecutionTime.isBefore(LocalDateTime.now().minusDays(1))) {
//			// 마지막 실행 시간이 없거나, 마지막 실행 시간이 하루 이상 지난 경우
//			callShelterApiAndUpdateLastExecutionTime();
//		}
//	}
//
//	@Scheduled(cron = "0 0 0 * * *") // 매일 자정
//	public void fetchData() {
//		callShelterApiAndUpdateLastExecutionTime();
//	}
//
//	private void callShelterApiAndUpdateLastExecutionTime() {
//		try {
//			List shelterAllList = shelterApiService.getShelterList();
//	        shelterService.delete(shelterAllList);
//	        shelterService.insert(shelterAllList);
//	        
//	        List<Shelter> allShelterList = shelterService.getAllRecord();//테이블 모든 레코드 불러오기
//	        shelterService.mapSigngu(allShelterList);//orgCd컬럼에 시군구 코드 update
//	        
//	        lastExecutionTime = LocalDateTime.now();
//	        writeLastExecutionTime(lastExecutionTime);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private LocalDateTime readLastExecutionTime() {
//		File file = new File(LAST_EXECUTION_TIME_FILE);
//		if (!file.exists()) {
//			return null;
//		}
//
//		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//			String lastExecutionTimeStr = reader.readLine();
//			if (lastExecutionTimeStr != null) {
//				return LocalDateTime.parse(lastExecutionTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private void writeLastExecutionTime(LocalDateTime lastExecutionTime) {
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(LAST_EXECUTION_TIME_FILE))) {
//			writer.write(lastExecutionTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
