package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiOperation;
import kr.wegather.wegather.domain.School;
import kr.wegather.wegather.domain.SchoolDept;
import kr.wegather.wegather.service.SchoolDeptService;
import kr.wegather.wegather.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/school")
public class SchoolController {
    private final SchoolService schoolService;
    private final SchoolDeptService schoolDeptService;

    @ApiOperation(value = "학교 목록 조회")
    @GetMapping("/")
    public ResponseEntity<String> searchSchools(@RequestParam String name) {
        // 학교 목록 받아오기
        List<School> schools = schoolService.findByName(name);

        // JSON으로 변환
        JSONArray array = new JSONArray();
        for (School school: schools) {
            array.put(school.toJSON());
        }

        // 응답 객체 생성
        JSONObject res = new JSONObject();
        try {
            res.put("schools", array);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }

    @ApiOperation(value = "학과 목록 조회")
    @GetMapping("/{school_id}")
    public ResponseEntity<String> searchSchoolDepts(@PathVariable("school_id") Long id, @RequestParam String name) {
        // 학과 목록 받아오기
        List<SchoolDept> schoolDepts = schoolDeptService.findByIdAndName(id, name);

        // JSON으로 변환
        JSONArray array = new JSONArray();
        for (SchoolDept schoolDept: schoolDepts) {
            array.put(schoolDept.toJSON());
        }

        // 응답 객체 생성
        JSONObject res = new JSONObject();
        try {
            res.put("schools", array);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }
}
