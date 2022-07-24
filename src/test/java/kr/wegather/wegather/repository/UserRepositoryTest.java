package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.SchoolDept;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testUser() throws Exception {
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
        Long savedId = userRepository.save(user);
        User findUser = userRepository.findOne(savedId);

        // then
        Assertions.assertThat(findUser.getId()).isEqualTo(user.getId());
        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(findUser).isEqualTo(user);
        System.out.println("findUser == user" + (findUser == user));
    }
}