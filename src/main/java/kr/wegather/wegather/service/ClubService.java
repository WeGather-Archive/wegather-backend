package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

	private final ClubRepository clubRepository;

	/* club 생성 */
	public Long createClub(Club club) {

		clubRepository.save(club);

		return club.getId();
	}

	/* club 목록 전체 조회 */
	public List<Club> findAllClubs() {
		return clubRepository.findAll();
	}

	public Club findOne(Long id) {
		return clubRepository.findOne(id);
	}

	/* club 이름으로 조회 */
	public List<Club> findClubsByName(String clubName) {
		return clubRepository.findByName(clubName);
	}

	/* club 이름 수정 */
	public void update(Long id, String newClubName) {
		Club club = clubRepository.findOne(id);
		club.setName(newClubName);
	}
	



}
