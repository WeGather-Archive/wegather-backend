package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.domain.Recruitment;
import kr.wegather.wegather.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitmentService {

	private final RecruitmentRepository recruitmentRepository;

	/* recruitment 생성 */
	public Long createRecruitment(Recruitment rec) {

		recruitmentRepository.save(rec);

		return rec.getId();
	}

	/* 내 recruitment 목록 전체 조회 */
	public List<Recruitment> findAllRecruitmentsByClub(Long clubId) {
		return recruitmentRepository.findAllByClub(clubId);
	}

	// recruitment id 로 하나 조회
	public Recruitment findOne(Long id) {
		return recruitmentRepository.findOne(id);
	}

}
