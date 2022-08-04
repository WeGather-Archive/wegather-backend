package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.ClubRole;
import kr.wegather.wegather.domain.enums.ClubRoleAuthLevel;
import kr.wegather.wegather.domain.enums.ClubRoleIsDefault;
import kr.wegather.wegather.exception.ClubRoleException;
import kr.wegather.wegather.exception.ClubRoleExceptionType;
import kr.wegather.wegather.repository.ClubMemberRepository;
import kr.wegather.wegather.repository.ClubRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubRoleService {
    private final ClubRoleRepository clubRoleRepository;
    private final ClubMemberRepository clubMemberRepository;

    public Long createClubRole(ClubRole clubRole) {
        return clubRoleRepository.save(clubRole);
    }

    public List<ClubRole> findByClub(Long clubId) {
        return clubRoleRepository.findByClub(clubId);
    }

    public void updateClubRole(Long roleId, String role, ClubRoleAuthLevel authLevel, ClubRoleIsDefault isDefault) {
        ClubRole clubRole = clubRoleRepository.findOne(roleId);
        clubRole.setRole(role);
        clubRole.setAuthLevel(authLevel);
        if (isDefault != ClubRoleIsDefault.CUSTOM) {
            try {
                ClubRole oldDefault = clubRoleRepository.findOneByClubAndIsDefault(clubRole.getClub().getId(), isDefault);
                oldDefault.setIsDefault(clubRole.getIsDefault());
            } catch (Exception e) {}
            clubRole.setIsDefault(isDefault);
        }
    }

    public Integer deleteClubRole(Long roleId, Long newRoleId) {
        Integer resultCount = 0;

        ClubRole clubRole, newClubRole;

        // role, newRole 존재하는지 검사
        clubRole = clubRoleRepository.findOne(roleId);
        newClubRole = clubRoleRepository.findOne(newRoleId);
        if (clubRole == null || newClubRole == null)
            throw new ClubRoleException(ClubRoleExceptionType.NOT_FOUND);
        if (clubRole.getClub().getId() != newClubRole.getClub().getId())
            throw new ClubRoleException(ClubRoleExceptionType.WRONG_INPUT);
        if (clubRole.getIsDefault() != ClubRoleIsDefault.CUSTOM && newClubRole.getIsDefault() != ClubRoleIsDefault.CUSTOM)
            throw new ClubRoleException(ClubRoleExceptionType.WRONG_INPUT);

        // role.isDefault != CUSTOM 이면 newRole에 role의 isDefault를 적용
        if (clubRole.getIsDefault() != ClubRoleIsDefault.CUSTOM) {
            newClubRole.setIsDefault(clubRole.getIsDefault());
        }

        // 기존에 roleId 인 ClubMember의 roleId를 전부 newRoleId로 변경
        resultCount = clubMemberRepository.updateClubMembersByClubRole(roleId, newRoleId);

        // roleId를 삭제
        clubRoleRepository.deleteOne(roleId);
        return resultCount;
    }
}
