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


    @Test
    public void registration() throws Exception {
        // given
        Random random = new Random();
        SchoolDept schoolDept = new SchoolDept();
        schoolDept.setId(6478L);
        User user = new User();
        user.setSchoolDept(schoolDept);
        user.setName("엄호용");
        user.setEmail("mathpaul" + Integer.toString(random.nextInt()) + "@gmail.com");
        user.setNickname("mathpaul3");
        user.setPassword("password");

        // when
        Long savedId = userService.register(user);

        // then
        assertEquals(user, userRepository.findOne(savedId));
    }

    @Test
    // 중복된 유저를 생성하는 경우 Test가 fail 해야 한다!
    // java.lang.IllegalStateException: User Already Exists가 뜬다면 정상
    public void registerDuplicated() throws Exception {
        // given
        SchoolDept schoolDept = new SchoolDept();
        schoolDept.setId(6478L);
        User user = new User();
        user.setSchoolDept(schoolDept);
        user.setName("엄호용");
        user.setEmail("mathpaul3@gmail.com");
        user.setNickname("mathpaul3");
        user.setPassword("password");

        // when
        userService.register(user);

        // then
        fail("Exception Not DETECTED!!");
    }
}