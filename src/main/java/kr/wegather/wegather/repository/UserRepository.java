package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserRepository {

    private final EntityManager em;

    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    public User saveUser(User user) {
        em.persist(user);
        return user;
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
        return em.createQuery("SELECT u FROM User u WHERE u.name = :name AND u.isDeleted = :isDeleted", User.class)
                .setParameter("name", name)
                .setParameter("isDeleted", false)
                .getSingleResult();
    }

    public User findOneByGoogleProviderId(String provider_id) {
        return em.createQuery("SELECT u FROM User u WHERE u.providerId = :provider_id AND u.isDeleted = :isDeleted AND u.provider = :provider", User.class)
                .setParameter("provider_id", provider_id)
                .setParameter("provider", "google")
                .setParameter("isDeleted", false)
                .getSingleResult();
    }
}
