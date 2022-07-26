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
}
