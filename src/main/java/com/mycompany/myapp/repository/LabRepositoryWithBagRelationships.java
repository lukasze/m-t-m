package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lab;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface LabRepositoryWithBagRelationships {
    Optional<Lab> fetchBagRelationships(Optional<Lab> lab);

    List<Lab> fetchBagRelationships(List<Lab> labs);

    Page<Lab> fetchBagRelationships(Page<Lab> labs);
}
