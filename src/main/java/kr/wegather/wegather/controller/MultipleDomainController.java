package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiOperation;
import kr.wegather.wegather.auth.PrincipalDetails;
import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MultipleDomainController {
    private final ClubService clubService;

    @ApiOperation(value = "동아리 전체 목록 조회")
    @GetMapping("/clubs/")
    public ResponseEntity<String> searchClubs(@RequestParam("isMySchool") Boolean isMySchool, @RequestParam("query") String query) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();

        List<Club> clubs = clubService.findAllWithFilter(userId, isMySchool, query);
        JSONArray clubArray = new JSONArray();
        for (Club club: clubs) {
            clubArray.put(club.toJSONObject());
        }
//		현재 해당사항 없음
//		필터 구체화 후 구현

        JSONObject res = new JSONObject();
        try {
            res.put("clubs", clubArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }
}
