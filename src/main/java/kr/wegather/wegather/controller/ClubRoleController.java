package kr.wegather.wegather.controller;

import kr.wegather.wegather.domain.enums.ClubRoleAuthLevel;
import kr.wegather.wegather.domain.enums.ClubRoleIsDefault;
import kr.wegather.wegather.service.ClubRoleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/role")
public class ClubRoleController {
    private final ClubRoleService clubRoleService;

    @PutMapping("/{role_id}")
    public ResponseEntity updateClubRole(@PathVariable("role_id") Long id, @RequestBody updateClubRoleRequest request) {
        String role = request.role;
        ClubRoleAuthLevel authLevel = request.authLevel;
        ClubRoleIsDefault isDefault = request.isDefault;

        clubRoleService.updateClubRole(id, role, authLevel, isDefault);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{role_id}")
    public ResponseEntity<String> deleteClubRole(@PathVariable("role_id") Long id, @RequestBody deleteClubRoleRequest request) {
        Long newRoleId = request.newRoleId;
        Integer resultCount = 0;
        resultCount = clubRoleService.deleteClubRole(id, newRoleId);


        // 응답 객체 생성
        JSONObject res = new JSONObject();
        try {
            res.put("affectedRows", resultCount);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }

    @Data
    static class updateClubRoleRequest {
        private String role;
        private ClubRoleAuthLevel authLevel;
        private ClubRoleIsDefault isDefault;
    }

    @Data
    static class deleteClubRoleRequest {
        private Long newRoleId;
    }
}
