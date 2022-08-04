package kr.wegather.wegather.controller;

import org.json.JSONArray;
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
public class ClubRoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /* !!테스트 할 때 바꿀 변수!! */
    private String rootPath = "/role";

    @Test
    public void updateClubRoleTest() throws Exception {
        // given
        Long roleId = 15L;
        JSONObject req = new JSONObject();
        req.put("role", "무슨 역할일까요~?");
        req.put("authLevel", "OPERATOR");
        req.put("isDefault", "DEFAULT_OPERATOR");

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put(rootPath + "/" + roleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteClubRoleTest() throws Exception {
        // given
        JSONObject clubReq = new JSONObject();
        clubReq.put("name", "Test 동아리");
        clubReq.put("introduction", "deleteClubRoleTest를 위한 동아리");
        MvcResult clubResult = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/club/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(clubReq.toString())
                )
                .andReturn();
        JSONObject clubRes = new JSONObject(clubResult.getResponse().getContentAsString());
        Integer newClubId = (Integer) clubRes.get("club_id");

        JSONObject roleReq = new JSONObject();
        roleReq.put("role", "Test 역할");
        roleReq.put("authLevel", 0);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/club/role/" + newClubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleReq.toString())
        );

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/club/role/" + newClubId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleReq.toString())
        );

        MvcResult rolesResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/club/role")
                        .param("id", newClubId.toString())
        )
                .andReturn();
        JSONObject roles = new JSONObject(rolesResult.getResponse().getContentAsString());
        JSONArray array = (JSONArray) roles.get("club_roles");


        // when
        Integer DEFAULT_OPERATOR = (Integer) array.getJSONObject(0).get("id");
        Integer DEFAULT_MEMBER = (Integer) array.getJSONObject(1).get("id");
        Integer CUSTOM1 = (Integer) array.getJSONObject(2).get("id");
        Integer CUSTOM2 = (Integer) array.getJSONObject(3).get("id");
        System.out.println(DEFAULT_OPERATOR);
        System.out.println(DEFAULT_MEMBER);
        System.out.println(CUSTOM1);
        System.out.println(CUSTOM2);

        // then
        JSONObject deteleRoleReq = new JSONObject();

        // 존재하지 않는 역할에 대한 삭제 요청
        deteleRoleReq.put("newRoleId", CUSTOM1 + 1);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(rootPath + "/" + CUSTOM1 + 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deteleRoleReq.toString())
        )
                .andExpect(status().isNotFound());

        // 다른 동아리의 역할에 대한 삭제 요청
        deteleRoleReq.put("newRoleId", 15L);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(rootPath + "/" + CUSTOM1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deteleRoleReq.toString())
        )
                .andExpect(status().isBadRequest());

        // 같은 동아리지만 두 역할이 모두 Default 역할일 경우
        deteleRoleReq.put("newRoleId", DEFAULT_MEMBER);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(rootPath + "/" + DEFAULT_OPERATOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deteleRoleReq.toString())
        )
                .andExpect(status().isBadRequest());

        // 정상적인 경우 - CUSTOM 역할 삭제 -> 관련 ClubMember를 전부 DEFAULT 역할로 변경
        deteleRoleReq.put("newRoleId", CUSTOM1);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(rootPath + "/" + DEFAULT_MEMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deteleRoleReq.toString())
        )
                .andExpect(status().isOk());

        // 정상적인 경우 - DEFAULT 역할 삭제 -> 관련 ClubMember를 전부 CUSTOM 역할로 변경
        deteleRoleReq.put("newRoldId", DEFAULT_MEMBER);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(rootPath + "/" + CUSTOM2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deteleRoleReq.toString())
        )
                .andExpect(status().isOk());
    }

}
