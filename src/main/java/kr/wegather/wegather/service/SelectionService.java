package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Applicant;
import kr.wegather.wegather.domain.Recruitment;
import kr.wegather.wegather.domain.Selection;
import kr.wegather.wegather.domain.enums.ApplicantStatus;
import kr.wegather.wegather.repository.ApplicantRepository;
import kr.wegather.wegather.repository.SelectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SelectionService {
    private final SelectionRepository selectionRepository;
    private final ApplicantRepository applicantRepository;

    /* Selection 생성 */
    public Long createSelection(Long recruitmentId) {
        Selection selection = new Selection();
        Recruitment recruitment = new Recruitment();
        recruitment.setId(recruitmentId);
        List<Selection> selections = selectionRepository.findByRecruitment(recruitmentId);
        Integer order = selections.size() + 1;

        selection.setRecruitment(recruitment);
        selection.setOrder(order);

        selectionRepository.save(selection);

        return selection.getId();
    }

    /* Selection 조회 */
    // 단건 조회 - Id
    public Selection findOne(Long id) {
        return selectionRepository.findOne(id);
    }

    // 단건 조회 - Id
    public Selection findOneWithUser(Long id) {
        return selectionRepository.findOneWithUser(id);
    }

    // 복수 조회 - Recruitment
    public List<Selection> findByRecruitment(Long recruitmentId) {
        return selectionRepository.findByRecruitment(recruitmentId);
    }
    /* Selection 수정 */

    public void updateSelection(Long id, Integer order, String name, Timestamp start, Timestamp end, String location, String onlineLink, Boolean isOnline) {
        Selection selection = selectionRepository.findOne(id);
        if (selection.getOrder() != order) {
            Selection anotherSelection = selectionRepository.findByRecruitmentAndOrder(selection.getRecruitment().getId(), order);
            anotherSelection.setOrder(selection.getOrder());
            selection.setOrder(order);
        }
        selection.setName(name);
        selection.setStartTime(start);
        selection.setEndTime(end);
        selection.setLocation(location);
        selection.setOnlineLink(onlineLink);
        selection.setIsOnline(isOnline);
    }

    /* Selection 삭제 */
    public void deleteSelection(Long selectionId, Long recruitmentId, Integer order) {
        selectionRepository.deleteOne(selectionId);
        Integer resultCount = selectionRepository.updateSelectionByRecruitmentAndOrder(recruitmentId, order);
    }
}
