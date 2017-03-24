package ro.upb.researchmgr.service.impl;

import ro.upb.researchmgr.service.PaperWorkService;
import ro.upb.researchmgr.service.UserService;
import ro.upb.researchmgr.config.Constants;
import ro.upb.researchmgr.domain.PaperAttachment;
import ro.upb.researchmgr.domain.PaperWork;
import ro.upb.researchmgr.repository.PaperAttachmentRepository;
import ro.upb.researchmgr.repository.PaperWorkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 * Service Implementation for managing PaperWork.
 */
@Service
@Transactional
public class PaperWorkServiceImpl implements PaperWorkService{

    private final Logger log = LoggerFactory.getLogger(PaperWorkServiceImpl.class);
    
    private final PaperWorkRepository paperWorkRepository;
    
    private final PaperAttachmentRepository paperAttachmentRepository;
    
    private final UserService userService;

    public PaperWorkServiceImpl(PaperWorkRepository paperWorkRepository,  UserService userService, PaperAttachmentRepository paperAttachmentRepository) {
        this.paperWorkRepository = paperWorkRepository;
        this.userService = userService;
        this.paperAttachmentRepository = paperAttachmentRepository;
    }

    /**
     * Save a paperWork.
     *
     * @param paperWork the entity to save
     * @return the persisted entity
     */
    @Override
    public PaperWork save(PaperWork paperWork) {
        log.debug("Request to save PaperWork : {}", paperWork);
        PaperWork result = paperWorkRepository.save(paperWork);
        return result;
    }

    /**
     *  Get all the paperWorks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PaperWork> findAll(Pageable pageable) {
        log.debug("Request to get all PaperWorks");
        Page<PaperWork> result = paperWorkRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one paperWork by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PaperWork findOne(Long id) {
        log.debug("Request to get PaperWork : {}", id);
        PaperWork paperWork = paperWorkRepository.findOne(id);
        return paperWork;
    }

    /**
     *  Delete the  paperWork by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaperWork : {}", id);
        paperWorkRepository.delete(id);
    }
    
    @Override
    public Page<PaperWork> findByAssigneeIsCurrentUser(Pageable pageable) {
    	Page<PaperWork> result = paperWorkRepository.findByAssigneeIsCurrentUser(pageable);
    	return result;
    }
    
    @Override
    public Page<PaperWork> findByCoordinatorIsCurrentUser(Pageable pageable) {
    	Page<PaperWork> result = paperWorkRepository.findByCoordinatorIsCurrentUser(pageable);
    	return result;
    }
    
    @Override
    public void uploadAttachments(Long id, List<MultipartFile> file) {
    	PaperWork paperWork = paperWorkRepository.findOne(id);
    	for (MultipartFile mf : file) {
    		PaperAttachment paperAttachment = new PaperAttachment();
    		paperAttachment.setDate(new Date());
    		paperAttachment.setPaperWork(paperWork);
    		paperAttachment.setPath("");
    		paperAttachmentRepository.save(paperAttachment);
    		//TODO ANCA - noticed that it would be ok if paperWork has a name clumn
    		String nameOfFile = userService.getUserWithAuthorities().getLogin() + "_" + paperWork.getSubject() + "_" + paperAttachment.getId();
    		paperAttachment.setPath(Constants.UPLOADED_PATH + nameOfFile);
    		paperAttachmentRepository.saveAndFlush(paperAttachment);
    		
    		try {
				mf.transferTo(new File("src/main/webapp/" + paperAttachment.getPath()));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
	
