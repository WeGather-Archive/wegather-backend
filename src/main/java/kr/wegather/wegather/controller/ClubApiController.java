package kr.wegather.wegather.controller;

import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.service.ClubService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClubApiController {

	private final ClubService clubService;

	// club 생성
	@PostMapping("/api/v1/club")
	public createClubResponse createClub(@RequestBody @Validated createClubRequest request){
		Club club = new Club();
		club.setName(request.getName());

		Long id = clubService.createClub(club);
		return new createClubResponse(id);
	}

	// club 이름 수정
	@PutMapping("/api/v1/club/{id}")
	public updateClubResponse updateClub(@PathVariable("id") Long id, @RequestBody @Validated updateClubRequest request) {
		clubService.update(id, request.getName());
		Club findClub = clubService.findOne(id);
		return new updateClubResponse(findClub.getId(), findClub.getName());
	}

	// club 조회
	@GetMapping("/api/v1/club")
	public List<Club> readClub() {
		return clubService.findAllClubs();
	}


	@Data
	static class updateClubRequest {
		private String name;
	}

	@Data
	@AllArgsConstructor
	static class updateClubResponse {
		private Long id;
		private String name;
	}

	@Data
	static class createClubRequest {
		private String name;
	}

	@Data
	static class createClubResponse {
		private Long id;

		public createClubResponse(Long id) {
			this.id = id;
		}
	}
}
