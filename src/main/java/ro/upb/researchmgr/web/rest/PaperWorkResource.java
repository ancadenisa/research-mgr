package ro.upb.researchmgr.web.rest;

import com.codahale.metrics.annotation.Timed;
import ro.upb.researchmgr.domain.PaperWork;
import ro.upb.researchmgr.domain.User;
import ro.upb.researchmgr.security.AuthoritiesConstants;
import ro.upb.researchmgr.security.SecurityUtils;
import ro.upb.researchmgr.service.PaperWorkService;
import ro.upb.researchmgr.service.UserService;
import ro.upb.researchmgr.web.rest.util.HeaderUtil;
import ro.upb.researchmgr.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PaperWork.
 */
@RestController
@RequestMapping("/api")
public class PaperWorkResource {

    private final Logger log = LoggerFactory.getLogger(PaperWorkResource.class);

    private static final String ENTITY_NAME = "paperWork";
        
    private final PaperWorkService paperWorkService;
    
    private final UserService userService;

    public PaperWorkResource(PaperWorkService paperWorkService, UserService userService) {
        this.paperWorkService = paperWorkService;
        this.userService =  userService;
    }

    /**
     * POST  /paper-works : Create a new paperWork.
     *
     * @param paperWork the paperWork to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paperWork, or with status 400 (Bad Request) if the paperWork has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/paper-works")
    @Timed
    public ResponseEntity<PaperWork> createPaperWork(@RequestBody PaperWork paperWork) throws URISyntaxException {
        log.debug("REST request to save PaperWork : {}", paperWork);
        if (paperWork.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new paperWork cannot already have an ID")).body(null);
        }
        PaperWork result = paperWorkService.save(paperWork);
        return ResponseEntity.created(new URI("/api/paper-works/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /paper-works : Updates an existing paperWork.
     *
     * @param paperWork the paperWork to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paperWork,
     * or with status 400 (Bad Request) if the paperWork is not valid,
     * or with status 500 (Internal Server Error) if the paperWork couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/paper-works")
    @Timed
    public ResponseEntity<PaperWork> updatePaperWork(@Valid @RequestBody PaperWork paperWork) throws URISyntaxException {
        log.debug("REST request to update PaperWork : {}", paperWork);
        if (paperWork.getId() == null) {
            return createPaperWork(paperWork);
        }
        PaperWork result = paperWorkService.save(paperWork);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paperWork.getId().toString()))
            .body(result);
    }

    /**
     * GET  /paper-works : get all the paperWorks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paperWorks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/paper-works")
    @Timed
    public ResponseEntity<List<PaperWork>> getAllPaperWorks(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PaperWorks");
        
        Page<PaperWork> page = null;
        
        // page has different content depending on user authority
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.COORD)) {
        	page = paperWorkService.findByCoordinatorIsCurrentUser(pageable);
        } else if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SEC)) {
        	page = paperWorkService.findAll(pageable);
        } else if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PHD)) {
        	page = paperWorkService.findByAssigneeIsCurrentUser(pageable);
        } else {
        	page = paperWorkService.findAll(pageable);
        }
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/paper-works");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /paper-works/:id : get the "id" paperWork.
     *
     * @param id the id of the paperWork to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paperWork, or with status 404 (Not Found)
     */
    @GetMapping("/paper-works/{id}")
    @Timed
    public ResponseEntity<PaperWork> getPaperWork(@PathVariable Long id) {
        log.debug("REST request to get PaperWork : {}", id);
        PaperWork paperWork = paperWorkService.getWithPaperAttachments(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paperWork));
    }

    /**
     * DELETE  /paper-works/:id : delete the "id" paperWork.
     *
     * @param id the id of the paperWork to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/paper-works/{id}")
    @Timed
    public ResponseEntity<Void> deletePaperWork(@PathVariable Long id) {
        log.debug("REST request to delete PaperWork : {}", id);
        paperWorkService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * PUT  /uploadPaperAttachments/:id Updates the "id" paperWork. by adding paperAttacments to it
     *
     * @param paperWork the paperWork to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paperWork,
     * or with status 400 (Bad Request) if the paperWork is not valid,
     * or with status 500 (Internal Server Error) if the paperWork couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/uploadPaperAttachments/{id}")
    @Timed
    public ResponseEntity<Void>  updloadPaperWorkAttachments(@PathVariable Long id, @RequestParam("file")List<MultipartFile> file) throws URISyntaxException {
        log.debug("REST request to upload a paper attachment to paper work with id: {}", id);
        paperWorkService.uploadAttachments(id, file);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, id.toString())).build();
    }

}
