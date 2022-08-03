package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.repository.ClubRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ClubServiceTest {

	@Autowired ClubService clubService;
	@Autowired ClubRepository clubRepository;
	@Autowired EntityManager em;

	@Test
	public void 클럽생성() throws Exception {
		Club club = new Club();
		club.setName("클럽1");

		Long savedId = clubService.createClub(club);

		Assertions.assertEquals(club, clubRepository.findOne(savedId));
	}

	@Test
	public void 클럽이름으로_조회() throws Exception {
		List<Club> clubs = new ArrayList<>();

		Club club = new Club();
		club.setName("클럽1");
		clubs.add(club);

		Long savedId = clubService.createClub(club);
		List<Club> findClub = clubService.findByName("클럽1");

		Assertions.assertEquals(clubs, findClub);
	}

	@Test
	public void 클럽이름_수정() throws Exception {
		Club club = new Club();
		club.setName("클럽1");

		Long savedId = clubService.createClub(club);

		clubService.update(savedId, "클럽2");

		Assertions.assertEquals(club.getName(), "클럽2");

	}

}
