package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.SchoolDept;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;


//    @Test
//    public void registerTest() throws Exception {
//        // given
//        User user = new User();
//        Random random = new Random();
//        SchoolDept schoolDept = new SchoolDept();
//        schoolDept.setId(6478L);
//
//        // when
//        user.setSchoolDept(schoolDept);
//        user.setName("test");
//        user.setEmail("test" + Integer.toString(random.nextInt()) + "@test.com");
////        user.setNickname("testNickname");
//        user.setPassword("pwd");
//        user.setIsDeleted(false);
//
//        // then
//        Long savedId = userService.register(user);
//        assertEquals(user, userRepository.findOne(savedId));
//    }
//
//    @Test
//    // 중복된 유저를 생성하는 경우 Test가 fail 해야 한다!
//    // org.springframework.transaction.UnexpectedRollbackException 이 뜬다면 정상
//    // java.lang.AssertionError: Exception Not DETECTED!! 가 뜬다면 중복되지 않은 것으로 처리된 것
//    public void validateDuplicatedUserTest() throws Exception {
//        // given
//        SchoolDept schoolDept = new SchoolDept();
//        schoolDept.setId(6478L);
//        User user = new User();
//        user.setSchoolDept(schoolDept);
//        user.setName("test");
//        user.setPassword("password");
//
//        // when
//        user.setEmail("test1234@test.com");
//
//        // then
//        try {
//            userService.register(user);
//            fail("Exception Not DETECTED!!");
//        } catch (Exception e) {
//        }
//    }
}