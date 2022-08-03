package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.ClubRole;
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

    public Long createClubRole(ClubRole clubRole) {
        return clubRoleRepository.save(clubRole);
    }

    public List<ClubRole> findByClub(Long clubId) {
        return clubRoleRepository.findByClub(clubId);
    }
}
