package com.sds.animalapp.model.volunteer;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sds.animalapp.domain.VolunteerApplication;

@Mapper
public interface VolunteerApplicationDAO {
    int insert(VolunteerApplication application);
    int delete(@Param("id")int id, @Param("member_idx")int member_idx);
    VolunteerApplication findApplication(@Param("title") String title, @Param("vol_date") String vol_date, @Param("noticeId") int noticeId);
    List<VolunteerApplication> findByNoticeId(int noticeId);
    List<VolunteerApplication> findByMemberIdx(int memberIdx);
    List<VolunteerApplication> findAll();
    VolunteerApplication findApplicationById(int id); // 추가된 메서드
    int selectCount(@Param("noticeId")int noticeId, @Param("member_idx")int member_idx);
}

