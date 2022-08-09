package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.*;
import kr.wegather.wegather.domain.enums.ApplicantStatus;
import kr.wegather.wegather.exception.ApplicantException;
import kr.wegather.wegather.exception.ApplicantExceptionType;
import kr.wegather.wegather.exception.SelectionException;
import kr.wegather.wegather.exception.SelectionExceptionType;
import kr.wegather.wegather.repository.ApplicantRepository;
import kr.wegather.wegather.repository.ClubMemberRepository;
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
    private final ClubMemberRepository clubMemberRepository;

    /* Applicant 생성 */
    public Long createApplicant(Long recruitmentId, Long userId) {
        // 이미 동아리원이거나 이미 해당 모집에 지원했는지 확인
        Applicant duplicatedApplicant;
        ClubMember duplicatedClubMember;
        try {
            duplicatedApplicant = applicantRepository.findByRecruitmentAndUser(recruitmentId, userId);
            if (duplicatedApplicant != null)
                throw new ApplicantException(ApplicantExceptionType.ALREADY_EXIST);
        } catch (Exception e) {
            if (e.getClass() == ApplicantException.class)
                throw new ApplicantException(ApplicantExceptionType.ALREADY_EXIST);
        }

        try {
            duplicatedClubMember = clubMemberRepository.findByRecruitmentAndUser(recruitmentId, userId);
            if (duplicatedClubMember != null)
                throw new ApplicantException(ApplicantExceptionType.ALREADY_EXIST);
        } catch (Exception e) {
            if (e.getClass() == ApplicantException.class)
                throw new ApplicantException(ApplicantExceptionType.ALREADY_EXIST);
        }

        // 아니라면 지원자 생성
        Applicant applicant = new Applicant();
        Recruitment recruitment = new Recruitment();
        recruitment.setId(recruitmentId);
        User user = new User();
        user.setId(userId);
        Selection selection;
        try {
            selection = selectionRepository.findByRecruitmentAndOrder(recruitmentId, 1);
        } catch (Exception e) {
            throw new SelectionException(SelectionExceptionType.NOT_FOUND);
        }

        applicant.setRecruitment(recruitment);
        applicant.setSelection(selection);
        applicant.setUser(user);
        applicantRepository.save(applicant);

        return applicant.getId();
    }

    /* Applicant 조회 */
    // 단건 조회 - By Id
    public Applicant findOne(Long id) {
        return applicantRepository.findOne(id);
    }

    // 복수 조회 - By Recruitment
    public List<Applicant> findByRecruitment(Long recruitmentId) {
        return applicantRepository.findByRecruitment(recruitmentId);
    }

    /* Applicant 수정 */
    public void updateApplicant(Long id, Long selectionId, ApplicantStatus status) {
        Applicant applicant = applicantRepository.findOne(id);
        Selection selection = selectionRepository.findOne(selectionId);
        applicant.setSelection(selection);
        applicant.setStatus(status);
    }

    public Integer updateApplicants(Long selectionId, List<Long> applicants, ApplicantStatus status) {
        Integer resultCount = 0;
        for (Long applicantId: applicants) {
            resultCount += applicantRepository.updateApplicantBySelectionAndStatus(selectionId, applicantId, status);
        }
        return resultCount;
    }

    /* Applicant 삭제 */
    // 상태만 변경?

}
