package ro.upb.researchmgr.service;

import ro.upb.researchmgr.domain.PaperWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service Interface for managing PaperWork.
 */
public interface PaperWorkService {

    /**
     * Save a paperWork.
     *
     * @param paperWork the entity to save
     * @return the persisted entity
     */
    PaperWork save(PaperWork paperWork);

    /**
     *  Get all the paperWorks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PaperWork> findAll(Pageable pageable);

    /**
     *  Get the "id" paperWork.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PaperWork findOne(Long id);

    /**
     *  Delete the "id" paperWork.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    
    Page<PaperWork> findByAssigneeIsCurrentUser(Pageable pageable);
    
    Page<PaperWork> findByCoordinatorIsCurrentUser(Pageable pageable);

	void uploadAttachments(Long id, List<MultipartFile> file);
    
}
