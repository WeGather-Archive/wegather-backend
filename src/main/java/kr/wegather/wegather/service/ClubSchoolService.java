package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.ClubSchool;
import kr.wegather.wegather.repository.ClubSchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubSchoolService {
    private final ClubSchoolRepository clubSchoolRepository;

    /* 동아리 학교 생성 */
    public Long createClubSchool(ClubSchool clubSchool) {
        return clubSchoolRepository.save(clubSchool);
    }

    /* 동아리 학교 조회 */
    // 복수 조회 - By Club
    public List<ClubSchool> findByClub(Long clubId) {
        return clubSchoolRepository.findByClub(clubId);
    }

    /* 동아리 학교 수정 */
    /* 동아리 학교 삭제 */
}
