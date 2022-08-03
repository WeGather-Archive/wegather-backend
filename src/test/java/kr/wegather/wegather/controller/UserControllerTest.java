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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(value = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /* !!테스트 할 때 바꿀 변수!! */
    private Long userId = 6L;
    private String rootPath = "/user";

    @Test
    public void signUpTest() throws Exception {
        // given
        JSONObject req = new JSONObject();
        req.put("schoolDept", 6478L); // 6478 숭실대학교 AI융합학부
        req.put("name", "test"); // 본명
        req.put("email", "test@test.com"); // 이메일 주소
        req.put("password", "pwd"); // 비밀번호

        // when
        req.put("nickName", "testNickname"); // 닉네임 (작성할지 여부는 선택사항)

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(rootPath + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isCreated()) // 정상적인 경우
//                .andExpect(status().isConflict()) // 이미 생성된 상태인 경우
                .andDo(print());
    }

    @Test
    public void loginTest() throws Exception {
        // given
        JSONObject req = new JSONObject();

        // when
        req.put("email", "test@test.com");
        req.put("password", "pwd");

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(rootPath + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isOk()) // 정상적인 경우
                .andDo(print());

        req = new JSONObject();
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(rootPath + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
        )
                .andExpect(status().isBadRequest()) // email이나 password가 비어있을 경우
                .andDo(print());
    }

    @Test
    public void searchUserTest() throws Exception {
        // given
        Long userId = this.userId;

        // when

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(rootPath + "/" + userId.toString())
                )
                .andExpect(status().isOk()) // 정상적인 경우
//                .andExpect(status().isBadRequest()) // 없는 유저일 경우 || 삭제된 유저일 경우
                .andDo(print());
    }

    @Test
    public void updateUserTest() throws Exception {
        // given
        JSONObject user = new JSONObject();
        Long userId = this.userId;

        // when

        // 선택 사항
        user.put("nickname", "mathpaul3");
        user.put("avatar", "https://w.namu.la/s/43a3472858577498e23c3701af9afad33de29d4a6235e3a9e8442af0c61ea63a6a688e30777396471edc221e21671196cd0f9d8d1ea0ca3c970d7cbc45dae1ba2a4d273434f0f37abf67ad738410fef9fc1c2f727555e50843ce73375330e737");
        user.put("profile", "여우는 귀여워~!");
        user.put("phone", "01012345678");

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put(rootPath + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user.toString())
        )
                .andExpect(status().isOk()) // 정상적인 경우
//                .andExpect(status().isBadRequest()) // 없는 유저일 경우 || 삭제된 유저일 경우
                .andDo(print());
    }

    @Test
    public void updatePasswordTest() throws Exception {
        // given
        JSONObject password = new JSONObject();
        Long userId = this.userId;

        // when
        password.put("password", "newpassword!");

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(rootPath + "/pwd/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(password.toString())
                )
                .andExpect(status().isOk()) // 정상적인 경우
//                .andExpect(status().isBadRequest()) // 없는 유저일 경우 || 삭제된 유저일 경우
                .andDo(print());
    }

    @Test
    public void updateEmailTest() throws Exception {
        // given
        JSONObject email = new JSONObject();
        Long userId = this.userId;

        // when
        email.put("email", "aaaa@test.com");

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch(rootPath + "/email/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(email.toString())
                )
//                .andExpect(status().isOk()) // 정상적인 경우
                .andExpect(status().isBadRequest()) // 없는 유저일 경우 || 삭제된 유저일 경우
                .andDo(print());
    }

    @Test
    public void deleteUserTest() throws Exception {
        // given
        Long userId = this.userId;

        // when

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(rootPath + "/" + userId)
                )
                .andExpect(status().isOk()) // 정상적인 경우
//                .andExpect(status().isBadRequest()) // 없는 유저일 경우 || 삭제된 유저일 경우
                .andDo(print());
    }
}
