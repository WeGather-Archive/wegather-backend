package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Selection;
import kr.wegather.wegather.repository.SelectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SelectionService {
    private final SelectionRepository selectionRepository;

    /* Selection 생성 */
    public Long createSelection(Selection selection) {
        selectionRepository.save(selection);

        return selection.getId();
    }

    /* Selection 조회 */
    // 단건 조회 - Id
    public Selection findOne(Long id) {
        return selectionRepository.findOne(id);
    }

    // 복수 조회 - Recruitment
    public List<Selection> findByRecruitment(Long recruitmentId) {
        return selectionRepository.findByRecruitment(recruitmentId);
    }

    /* Selection 수정 */
    public void updateSelection(Long id, Integer order, String name, LocalDateTime start, LocalDateTime end, String location, String onlineLink, Boolean isOnline) {
        Selection selection = selectionRepository.findOne(id);
        selection.setOrder(order);
        selection.setName(name);
        selection.setStartTime(start);
        selection.setEndTime(end);
        selection.setLocation(location);
        selection.setOnlineLink(onlineLink);
        selection.setIsOnline(isOnline);
    }

    /* Selection 삭제 */
    public void deteleSelection(Long id) {
        selectionRepository.deleteOne(id);
    }
}
