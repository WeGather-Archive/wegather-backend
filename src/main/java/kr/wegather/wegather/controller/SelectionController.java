package kr.wegather.wegather.controller;

import kr.wegather.wegather.domain.Selection;
import kr.wegather.wegather.domain.enums.ApplicantStatus;
import kr.wegather.wegather.service.ApplicantService;
import kr.wegather.wegather.service.SelectionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/selection")
public class SelectionController {
    private final SelectionService selectionService;
    private final ApplicantService applicantService;

    @GetMapping("/")
    public ResponseEntity searchSelection(@RequestParam Long id) {
        Selection selection = selectionService.findOneWithUser(id);
        JSONObject res = new JSONObject();
        try {
            res.put("selection", selection.toJSONObjectForRecruitment());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }

    @PutMapping("/{selection_id}")
    public ResponseEntity updateSelection(@PathVariable("selection_id") Long id, @RequestBody updateSelectionRequest request) {
        String name = request.name, location = request.location, onlineLink = request.onlineLink;
        Integer order = request.order;
        Boolean isOnline = request.isOnline;
        Timestamp start = request.start, end = request.end;
        selectionService.updateSelection(id, order, name, start, end, location, onlineLink, isOnline);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/{selection_id}")
    public ResponseEntity updateSelections(@PathVariable("selection_id") Long id, @RequestBody updateSelectionsRequest request) {
        Long selectionId = id;
        List<Long> applicants = request.applicants;
        ApplicantStatus status = request.status;
        Integer affectedRowsCount = applicantService.updateApplicants(selectionId, applicants, status);

        JSONObject res = new JSONObject();
        try {
            res.put("affectedRows", affectedRowsCount);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }

    @DeleteMapping("/{selection_id}")
    public ResponseEntity deleteSelection(@PathVariable("selection_id") Long selectionId, @RequestBody deleteSelectionRequest request) {
        Long recruitmentId = request.recruitmentId;
        Integer order = request.order;

        selectionService.deleteSelection(selectionId, recruitmentId, order);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    static class updateSelectionRequest {
        private Integer order;
        private String name;
        private Timestamp start;
        private Timestamp end;
        private String location;
        private String onlineLink;
        private Boolean isOnline;
    }

    @Data
    static class deleteSelectionRequest {
        private Long recruitmentId;
        private Integer order;
    }

    @Data
    static class updateSelectionsRequest {
        private ApplicantStatus status;
        private List<Long> applicants;
    }
}
