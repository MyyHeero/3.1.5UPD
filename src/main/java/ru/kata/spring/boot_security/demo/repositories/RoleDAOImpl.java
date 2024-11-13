package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class RoleDAOImpl implements RoleDAO {


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<Role> findByName(String name) {
        TypedQuery<Role> query = entityManager.createQuery("from Role r where r.name = :name", Role.class);
        query.setParameter("name", name);
        return query.getResultStream().findFirst();
    }

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }

    @Override
    public Set<Role> findByNameIn(Set<String> names) {
        if (names == null || names.isEmpty()) {
            return Collections.emptySet();
        }

        TypedQuery<Role> query = entityManager.createQuery("from Role where name in (:names)", Role.class);
        query.setParameter("names", names);
        return new HashSet<Role>(query.getResultList());
    }

    @Override
    public Optional<Role> findById(int id) {
       return Optional.ofNullable(entityManager.find(Role.class, id));
    }
}
