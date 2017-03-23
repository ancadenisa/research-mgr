package ro.upb.researchmgr.repository;

import ro.upb.researchmgr.domain.PaperWork;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PaperWork entity.
 */
@SuppressWarnings("unused")
public interface PaperWorkRepository extends JpaRepository<PaperWork,Long> {

    @Query("select paperWork from PaperWork paperWork where paperWork.coordinator.login = ?#{principal.username}")
    Page<PaperWork> findByCoordinatorIsCurrentUser(Pageable pageable);

    @Query("select paperWork from PaperWork paperWork where paperWork.assignee.login = ?#{principal.username}")
    Page<PaperWork> findByAssigneeIsCurrentUser(Pageable pageable);

}
