package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lab;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class LabRepositoryWithBagRelationshipsImpl implements LabRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Lab> fetchBagRelationships(Optional<Lab> lab) {
        return lab.map(this::fetchStudents);
    }

    @Override
    public Page<Lab> fetchBagRelationships(Page<Lab> labs) {
        return new PageImpl<>(fetchBagRelationships(labs.getContent()), labs.getPageable(), labs.getTotalElements());
    }

    @Override
    public List<Lab> fetchBagRelationships(List<Lab> labs) {
        return Optional.of(labs).map(this::fetchStudents).orElse(Collections.emptyList());
    }

    Lab fetchStudents(Lab result) {
        return entityManager
            .createQuery("select lab from Lab lab left join fetch lab.students where lab is :lab", Lab.class)
            .setParameter("lab", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Lab> fetchStudents(List<Lab> labs) {
        return entityManager
            .createQuery("select distinct lab from Lab lab left join fetch lab.students where lab in :labs", Lab.class)
            .setParameter("labs", labs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
