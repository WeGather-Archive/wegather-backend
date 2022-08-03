package kr.wegather.wegather.controller;

import kr.wegather.wegather.service.ClubMemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clubmember")
public class ClubMemberController {

    private final ClubMemberService clubMemberService;

    @PostMapping("/{club_id}")
    public ResponseEntity<String> createManyClubMembers(@PathVariable("club_id") Long clubId, @RequestBody createClubMemberRequest request) {
        ArrayList<Long> clubMembers = request.clubMembers;

        JSONObject res = clubMemberService.createClubMembers(clubId, clubMembers);

        return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
    }

    @Data
    static class createClubMemberRequest {
        ArrayList<Long> clubMembers;
    }
}
