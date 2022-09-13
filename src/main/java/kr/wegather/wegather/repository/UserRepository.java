package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    public User findOne(Long id) {
        User user = em.find(User.class, id);
        if (user.getIsDeleted())
            return null;
        else
            return user;
    }

    public User findOneByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.isDeleted = :isDeleted", User.class)
                .setParameter("email", email)
                .setParameter("isDeleted", false)
                .getSingleResult();
    }

    public User findOneByName(String name) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.name = :name AND u.isDeleted = :isDeleted", User.class)
                .setParameter("name", name)
                .setParameter("isDeleted", false)
                .getSingleResult();
        } catch (NoResultException nre){
            return null;
        }


    }
}
