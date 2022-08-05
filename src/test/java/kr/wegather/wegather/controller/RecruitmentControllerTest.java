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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(value = false)
public class RecruitmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /* !!테스트 할 때 바꿀 변수!! */
    private String rootPath = "/recruitment";

    @Test
    public void searchRecruitmentTest() throws Exception {
        // given
        Long recruitmentId = 3L;

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(rootPath + "/")
                        .param("id", recruitmentId.toString())
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateRecruitmentTest() throws Exception {
        // given
        Long recruitmentId = 4L;
        JSONObject req = new JSONObject();
        req.put("title", "모집 이름");
        req.put("description", "모집에 대한 설명");
        req.put("status", "ENROLL");
        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put(rootPath + "/" + recruitmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isOk()) // 정상적인 경우
                .andDo(print());
    }

    @Test
    public void deleteRecruitmentTest() throws Exception {
        // given
        Long clubRoleId = 22L;
        JSONObject recruitmentReq = new JSONObject();
        recruitmentReq.put("title", "새로운 모집명");
        recruitmentReq.put("description", "모집에 대한 소개");

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/club/recruitment/" + clubRoleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recruitmentReq.toString())
        )
                .andDo(print())
                .andReturn();
//        System.out.println(result.getResponse().getContentAsString());
        JSONObject recruitment = new JSONObject(result.getResponse().getContentAsString());
        Integer recruitmentId = (Integer) recruitment.get("recruitmentId");
//        Integer recruitmentId = 7;

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(rootPath + "/" + recruitmentId)
        )
                .andExpect(status().isOk()) // 정상적인 경우
                .andDo(print());

    }

    @Test
    public void createSelectionTest() throws Exception {
        // given
        Long recruitmentId = 3L;

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(rootPath + "/" + recruitmentId)
        )
                .andExpect(status().isCreated()) // 정상적인 경우
                .andDo(print());
    }
}
