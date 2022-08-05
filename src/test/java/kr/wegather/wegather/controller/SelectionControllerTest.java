package kr.wegather.wegather.controller;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(value = false)
public class SelectionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /* !!테스트 할 때 바꿀 변수!! */
    private String rootPath = "/selection";

    @Test
    public void searchSelectionTest() throws Exception {
        // given
        Long selectionId = 3L;

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(rootPath + "/")
                        .param("id", selectionId.toString())
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateSelectionTest() throws Exception {
        // given
        Long selectionId = 3L;
        JSONObject req = new JSONObject();
        req.put("order", 2);
        req.put("name", "면접 전형");
        req.put("start", LocalDateTime.now());
//        req.put("end", null);
        req.put("location", "숭실대학교 문화관 407호");
//        req.put("onlineLink", null);
        req.put("isOnline", false);

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put(rootPath + "/" + selectionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateSelectionsTest() throws Exception {
        // given
        Long selectionId = 6L;
        List<Long> applicants = new ArrayList<>();
        applicants.add(11L);
        applicants.add(12L);
        JSONObject req = new JSONObject();
        req.put("status", "APPROVED");
        req.put("applicants", applicants);

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch(rootPath + "/" + selectionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isOk()) // 정상적인 경우
                .andDo(print());
    }

    @Test
    public void deleteSelectionTest() throws Exception {
        // given
        Long recruitmentId = 3L;
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post( "/recruitment/" + recruitmentId)
                )
                .andReturn();
        JSONObject selection = new JSONObject(result.getResponse().getContentAsString());
        Integer selectionId = (Integer) selection.get("id");
        JSONObject req = new JSONObject();
        req.put("recruitmentId", recruitmentId);
        req.put("order", 1);

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(rootPath + "/" + selectionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isOk())
                .andDo(print());

    }
}
