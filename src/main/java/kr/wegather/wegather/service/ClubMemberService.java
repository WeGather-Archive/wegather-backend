package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.domain.ClubMember;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.repository.ClubMemberRepository;
import kr.wegather.wegather.repository.ClubRepository;
import kr.wegather.wegather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;

    /* ClubMember 생성 */
    public JSONObject createClubMembers(Long clubId, ArrayList<Long> users) {
        JSONObject res = new JSONObject();
        JSONArray success = new JSONArray();
        JSONArray duplicated = new JSONArray();

        Club club = new Club();
        club.setId(clubId);

        for (Long userId: users) {
            ClubMember clubMemberFound;
            User user = new User();
            user.setId(userId);
            try {
                clubMemberFound = clubMemberRepository.findByClubAndUser(clubId, userId);
                if (clubMemberFound.getIsDeleted()) {
                    clubMemberFound.setIsDeleted(false);
                    success.put(userId);
                }
                else { duplicated.put(userId); }
            } catch (Exception e) {
                ClubMember clubMember = new ClubMember();
                user.setId(userId);
                clubMember.setClub(club);
                clubMember.setUser(user);
                clubMemberRepository.save(clubMember);
                success.put(userId);
            }
        }
        try {
            res.put("success", success);
            res.put("duplicated", duplicated);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /* ClubMember 조회 */
    // 단건 조회 - Id
    public ClubMember findOne(Long id) {
        return clubMemberRepository.findOne(id);
    }

    // 복수 조회 - Club
    public List<ClubMember> findByClub(Long clubId) {
        return clubMemberRepository.findByClub(clubId);
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
