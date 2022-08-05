package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.ClubRole;
import kr.wegather.wegather.domain.Recruitment;
import kr.wegather.wegather.domain.enums.RecruitmentStatus;
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

    public Long createRecruitment(Long clubRoleId, String title, String description) {
        Recruitment recruitment = new Recruitment();
        ClubRole clubRole = new ClubRole();
        clubRole.setId(clubRoleId);

        recruitment.setClubRole(clubRole);
        recruitment.setTitle(title);
        recruitment.setDescription(description);
        recruitment.setStatus(RecruitmentStatus.CREATED);

        return recruitmentRepository.save(recruitment);
    }

    public List<Recruitment> findByClub(Long clubId) {
        return recruitmentRepository.findByClub(clubId);
    }

    public Recruitment findOneWithApplicant(Long id) {
        return recruitmentRepository.findOneWithApplicant(id);
    }

    public void updateRecruitment(Long recruitmentId, String title, String description, RecruitmentStatus status) {
        Recruitment recruitment = recruitmentRepository.findOne(recruitmentId);
        recruitment.setTitle(title);
        recruitment.setDescription(description);
        recruitment.setStatus(status);
    }

    public void deleteRecruitment(Long recruitmentId) {
        recruitmentRepository.deleteOne(recruitmentId);
    }
}
