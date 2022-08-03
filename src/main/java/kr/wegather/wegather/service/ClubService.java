package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubService {

	private final ClubRepository clubRepository;

	/* club 생성 */
	public Long createClub(Club club) {

		clubRepository.save(club);

		return club.getId();
	}


	/* club 조회 */
	// 단건 조회 - Id
	public Club findOne(Long id) {
		return clubRepository.findOne(id);
	}

	// 복수 조회 - 필터
	public List<Club> findAll() {
		return clubRepository.findAll();
	}

	public List<Club> findByName(String clubName) {
		return clubRepository.findByName(clubName);
	}


	/* club 수정 */
	public void update(Long id, String newClubName) {
		Club club = clubRepository.findOne(id);
		club.setName(newClubName);
	}


	/* club 삭제 */




	/* club 이름으로 조회 */


	



}
