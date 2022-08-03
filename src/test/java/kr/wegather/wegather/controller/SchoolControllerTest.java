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
public class SchoolControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void searchSchoolsTest() throws Exception {
        // given
        String schoolName;

        // when
        schoolName = "숭";

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/school/")
                        .param("name", schoolName)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void searchSchoolDeptsTest() throws Exception {
        // given
        Long schoolId;
        String deptName;

        // when
        schoolId = 136L;
        deptName = "AI";

        // then
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/school/" + schoolId)
                        .param("name", deptName)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
