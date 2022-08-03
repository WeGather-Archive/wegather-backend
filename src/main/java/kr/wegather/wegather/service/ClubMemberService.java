package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.domain.ClubMember;
import kr.wegather.wegather.domain.enums.DuplicatedStatus;
import kr.wegather.wegather.exception.ClubMemberException;
import kr.wegather.wegather.exception.ClubMemberExceptionType;
import kr.wegather.wegather.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;

    /* ClubMember 생성 */
    // 단건 생성
    public Long createClubMember(ClubMember clubMember) {
        // 중복 체크
        Map<String, Object> result = isDuplicated(clubMember);
        DuplicatedStatus status = (DuplicatedStatus) result.get("status");
        ClubMember clubMemberFound = (ClubMember) result.get("clubMember");

        // 각 상황별 대응 코드
        if (status == DuplicatedStatus.NOT_FOUND) {
            return clubMemberRepository.save(clubMember);
        } else if (status == DuplicatedStatus.DUPLICATED) {
            throw new ClubMemberException(ClubMemberExceptionType.ALREADY_EXIST);
        } else if (status == DuplicatedStatus.DELETED) {
            clubMemberFound.setIsDeleted(false);
            return clubMemberFound.getId();
        }
        throw new ClubMemberException(ClubMemberExceptionType.WRONG_INPUT);
    }

    // 복수 생성
    public JSONObject createClubMembers(Long clubId, ArrayList<Long> users) {
        // 성공 Case, 실패 Case 구분
        JSONArray success = new JSONArray();
        JSONArray duplicated = new JSONArray();

        // ClubMember 공통으로 사용할 동아리
        Club club = new Club();
        club.setId(clubId);

        // 각 user 별 대응
        for (Long userId: users) {
            ClubMember clubMember = new ClubMember();
            clubMember.getUser().setId(userId);
            clubMember.getClub().setId(clubId);

            // 중복 체크
            Map<String, Object> result = isDuplicated(clubMember);
            DuplicatedStatus status = (DuplicatedStatus) result.get("status");
            ClubMember clubMemberFound = (ClubMember) result.get("clubMember");

            // 각 상황별 대응 코드
            if (status == DuplicatedStatus.NOT_FOUND) {
                success.put(clubMemberRepository.save(clubMember));
            } else if (status == DuplicatedStatus.DUPLICATED) {
                duplicated.put(clubMember.getUser().getId());
            } else if (status == DuplicatedStatus.DELETED) {
                clubMemberFound.setIsDeleted(false);
                success.put(clubMemberFound.getId());
            }
        }

        // 응답 객체 생성
        JSONObject res = new JSONObject();
        try {
            res.put("success", success);
            res.put("duplicated", duplicated);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    // 중복 체크
    private Map<String, Object> isDuplicated(ClubMember clubMember) {
        Map<String, Object> hashTableMap = new HashMap<>();
        Long clubId = clubMember.getClub().getId(), userId = clubMember.getUser().getId();
        ClubMember clubMemberFound;
        try {
            clubMemberFound = clubMemberRepository.findByClubAndUser(clubId, userId);
            hashTableMap.put("clubMemberId", clubMemberFound);
            if (clubMemberFound.getIsDeleted()) { hashTableMap.put("status", DuplicatedStatus.DELETED); }
            else { hashTableMap.put("status", DuplicatedStatus.DUPLICATED); }
            return hashTableMap;
        } catch (Exception e) {
            hashTableMap.put("status", DuplicatedStatus.NOT_FOUND);
            hashTableMap.put("clubMember", null);
            return hashTableMap;
        }
    }

    /* ClubMember 조회 */
    // 단건 조회 - Id
    public ClubMember findOne(Long id) {
        return clubMemberRepository.findOne(id);
    }

    // 복수 조회 - Club
    public List<ClubMember> findByClub(Long clubId) {
        return clubMemberRepository.findByClub(clubId);
//        return clubMemberJPARepository.getClubMemberByClub(clubId);
    }

    /* ClubMember 수정 */
    public void updateClubMember() {
        // 현재 해당 사항 없음
    }

    /* ClubMember 삭제 */

    public void deleteClubMember(Long id) {
        ClubMember clubMember = clubMemberRepository.findOne(id);
        clubMember.setIsDeleted(true);
    }
}
