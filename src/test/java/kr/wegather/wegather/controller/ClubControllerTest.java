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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(value = false)
public class ClubControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /* !!테스트 할 때 바꿀 변수!! */
    private Long clubId = 21L;
    private String rootPath = "/club";

    @Test
    public void searchClubsTest() throws Exception {
        // given

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(rootPath)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void createClubTest() throws Exception {
        // given
        JSONObject req = new JSONObject();

        // when
        req.put("name", "Club Name");
        req.put("introduction", "Introduce your club to everyone!");
        req.put("avatar", "https://media.edutopia.org/styles/responsive_2880px_16x9/s3/sites/default/files/masters/2021-09/35693941723_b69de07291_k-crop.jpg");
        req.put("phone", "01011112222");

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(rootPath + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isCreated()) // 정상적인 경우
                .andDo(print());
    }

    @Test
    public void createImageTest() throws Exception {
        // given

        // when

        // then

    }

    @Test
    public void searchClubTest() throws Exception {
        // given
        Long clubId = this.clubId;

        // when

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(rootPath + "/")
                                .param("id", clubId.toString())
                )
                .andExpect(status().isOk()) // 정상적인 경우
                .andDo(print());
    }

    @Test
    public void updateClubTest() throws Exception {
        // given
        Long clubId = this.clubId;
        JSONObject req = new JSONObject();

        // when
        req.put("name", "emaN bulC");
        req.put("introduction", "!enoyreve ot bulc rouy ecudortnI");
//        req.put("avatar", "");
        req.put("phone", "01011112222");

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put(rootPath + "/" + clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isOk()) // 정상적인 경우
                .andDo(print());
    }

    @Test
    public void searchClubMembersTest() throws Exception {
        // given
        Long cludId = this.clubId;

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(rootPath + "/member")
                        .param("id", cludId.toString())
        )
                .andExpect(status().isOk()) // 정상적인 경우
                .andDo(print());
    }

    @Test
    public void searchClubRolesTest() throws Exception {
        // given
        Long clubId = this.clubId;

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(rootPath + "/role")
                        .param("id", clubId.toString())
        )
                .andExpect(status().isOk()) // 정상적인 경우
                .andDo(print());
    }

    @Test
    public void createClubRoleTest() throws Exception {
        // given
        Long clubId = this.clubId;
        JSONObject req = new JSONObject();

        // when
        req.put("role", "역할1");
        req.put("authLevel", 1);

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(rootPath + "/role/" + clubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isCreated()) // 정상적인 경우
                .andDo(print());
    }

    @Test
    public void createEmailTest() throws Exception {
        // given

        // when

        // then

    }

    @Test
    public void searchQuestionnairesTest() throws Exception {
        // given
        Long clubId = this.clubId;

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(rootPath + "/form")
                        .param("id", clubId.toString())
        )
                .andExpect(status().isOk()) // 정상적인 경우
                .andDo(print());
    }

    @Test
    public void createQuestionnaireTest() throws Exception {
        // given
        JSONObject req = new JSONObject();
        Long selectionId = 1L;

        // when
        req.put("selectionId", selectionId);
        req.put("title", "질문지 제목");

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(rootPath + "/form")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
//                .andExpect(status().isCreated()) // 정상적인 경우
                .andExpect(status().isConflict()) // 해당 전형에 이미 폼이 존재하는 경우
                .andDo(print());
    }

    @Test
    public void searchRecruitmentsTest() throws Exception {
        // given
        Long clubId = 22L;

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(rootPath + "/recruitment")
                        .param("id", clubId.toString())
        )
                .andExpect(status().isOk()) // 정상적인 경우
                .andDo(print());
    }

    @Test
    public void createRecruitmentTest() throws Exception {
        // given
        Long clubRoleId = 38L;
        JSONObject req = new JSONObject();
        req.put("title", "새로운 모집명");
        req.put("description", "모집에 대한 소개");

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(rootPath + "/recruitment/" + clubRoleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
