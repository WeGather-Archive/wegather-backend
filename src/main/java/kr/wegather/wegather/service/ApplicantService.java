package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Applicant;
import kr.wegather.wegather.domain.Selection;
import kr.wegather.wegather.domain.enums.ApplicantStatus;
import kr.wegather.wegather.repository.ApplicantRepository;
import kr.wegather.wegather.repository.SelectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final SelectionRepository selectionRepository;

    /* Applicant 생성 */
    public Long createApplicant(Applicant applicant) {
        applicantRepository.save(applicant);

        return applicant.getId();
    }

    /* Applicant 조회 */
    // 단건 조회 - Id
    public Applicant findOne(Long id) {
        return applicantRepository.findOne(id);
    }

    // 복수 조회 - Recruitment
    public List<Applicant> findByRecruitment(Long recruitmentId) {
        return applicantRepository.findByRecruitment(recruitmentId);
    }

    // 복수 조회 - Selection
    public List<Applicant> findBySelection(Long selectionId) {
        return applicantRepository.findBySelection(selectionId);
    }

    /* Applicant 수정 */
    public void updateApplicant(Long id, Long selectionId, ApplicantStatus status) {
        Applicant applicant = applicantRepository.findOne(id);
        Selection selection = selectionRepository.findOne(selectionId);
        applicant.setSelection(selection);
        applicant.setStatus(status);
    }

    /* Applicant 삭제 */
    // 상태만 변경?

}
