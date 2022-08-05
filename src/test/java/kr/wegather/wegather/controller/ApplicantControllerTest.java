package kr.wegather.wegather.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class ApplicantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /* !!테스트 할 때 바꿀 변수!! */
    private String rootPath = "/applicant";

    @Test
    public void createApplicantTest() throws Exception {
        // given
        Long recruitmentId = 3L;

        // when

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(rootPath + "/" + recruitmentId)
        )
//                .andExpect(status().isCreated()) // 정상적인 경우
                .andExpect(status().isConflict()) // 이미 지원한 상태의 동아리거나 이미 동아리원인 경우
                .andDo(print());
    }
}
