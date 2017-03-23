package ro.upb.researchmgr.web.rest;

import ro.upb.researchmgr.ResearchMgrApp;

import ro.upb.researchmgr.domain.PaperWork;
import ro.upb.researchmgr.domain.User;
import ro.upb.researchmgr.domain.User;
import ro.upb.researchmgr.repository.PaperWorkRepository;
import ro.upb.researchmgr.service.PaperWorkService;
import ro.upb.researchmgr.service.UserService;
import ro.upb.researchmgr.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PaperWorkResource REST controller.
 *
 * @see PaperWorkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResearchMgrApp.class)
public class PaperWorkResourceIntTest {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEADLINE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEADLINE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PaperWorkRepository paperWorkRepository;

    @Autowired
    private PaperWorkService paperWorkService;
    

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;
    
    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager em;

    private MockMvc restPaperWorkMockMvc;

    private PaperWork paperWork;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaperWorkResource paperWorkResource = new PaperWorkResource(paperWorkService, userService);
        this.restPaperWorkMockMvc = MockMvcBuilders.standaloneSetup(paperWorkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaperWork createEntity(EntityManager em) {
        PaperWork paperWork = new PaperWork()
                .subject(DEFAULT_SUBJECT)
                .description(DEFAULT_DESCRIPTION)
                .deadlineDate(DEFAULT_DEADLINE_DATE);
        // Add required entity
        User coordinator = UserResourceIntTest.createEntity(em);
        em.persist(coordinator);
        em.flush();
        paperWork.setCoordinator(coordinator);
        // Add required entity
        User assignee = UserResourceIntTest.createEntity(em);
        em.persist(assignee);
        em.flush();
        paperWork.setAssignee(assignee);
        return paperWork;
    }

    @Before
    public void initTest() {
        paperWork = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaperWork() throws Exception {
        int databaseSizeBeforeCreate = paperWorkRepository.findAll().size();

        // Create the PaperWork

        restPaperWorkMockMvc.perform(post("/api/paper-works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paperWork)))
            .andExpect(status().isCreated());

        // Validate the PaperWork in the database
        List<PaperWork> paperWorkList = paperWorkRepository.findAll();
        assertThat(paperWorkList).hasSize(databaseSizeBeforeCreate + 1);
        PaperWork testPaperWork = paperWorkList.get(paperWorkList.size() - 1);
        assertThat(testPaperWork.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testPaperWork.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPaperWork.getDeadlineDate()).isEqualTo(DEFAULT_DEADLINE_DATE);
    }

    @Test
    @Transactional
    public void createPaperWorkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paperWorkRepository.findAll().size();

        // Create the PaperWork with an existing ID
        PaperWork existingPaperWork = new PaperWork();
        existingPaperWork.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaperWorkMockMvc.perform(post("/api/paper-works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPaperWork)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PaperWork> paperWorkList = paperWorkRepository.findAll();
        assertThat(paperWorkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = paperWorkRepository.findAll().size();
        // set the field null
        paperWork.setSubject(null);

        // Create the PaperWork, which fails.

        restPaperWorkMockMvc.perform(post("/api/paper-works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paperWork)))
            .andExpect(status().isBadRequest());

        List<PaperWork> paperWorkList = paperWorkRepository.findAll();
        assertThat(paperWorkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaperWorks() throws Exception {
        // Initialize the database
        paperWorkRepository.saveAndFlush(paperWork);

        // Get all the paperWorkList
        restPaperWorkMockMvc.perform(get("/api/paper-works?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paperWork.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].deadlineDate").value(hasItem(DEFAULT_DEADLINE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPaperWork() throws Exception {
        // Initialize the database
        paperWorkRepository.saveAndFlush(paperWork);

        // Get the paperWork
        restPaperWorkMockMvc.perform(get("/api/paper-works/{id}", paperWork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paperWork.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.deadlineDate").value(DEFAULT_DEADLINE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaperWork() throws Exception {
        // Get the paperWork
        restPaperWorkMockMvc.perform(get("/api/paper-works/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaperWork() throws Exception {
        // Initialize the database
        paperWorkService.save(paperWork);

        int databaseSizeBeforeUpdate = paperWorkRepository.findAll().size();

        // Update the paperWork
        PaperWork updatedPaperWork = paperWorkRepository.findOne(paperWork.getId());
        updatedPaperWork
                .subject(UPDATED_SUBJECT)
                .description(UPDATED_DESCRIPTION)
                .deadlineDate(UPDATED_DEADLINE_DATE);

        restPaperWorkMockMvc.perform(put("/api/paper-works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaperWork)))
            .andExpect(status().isOk());

        // Validate the PaperWork in the database
        List<PaperWork> paperWorkList = paperWorkRepository.findAll();
        assertThat(paperWorkList).hasSize(databaseSizeBeforeUpdate);
        PaperWork testPaperWork = paperWorkList.get(paperWorkList.size() - 1);
        assertThat(testPaperWork.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testPaperWork.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaperWork.getDeadlineDate()).isEqualTo(UPDATED_DEADLINE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPaperWork() throws Exception {
        int databaseSizeBeforeUpdate = paperWorkRepository.findAll().size();

        // Create the PaperWork

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaperWorkMockMvc.perform(put("/api/paper-works")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paperWork)))
            .andExpect(status().isCreated());

        // Validate the PaperWork in the database
        List<PaperWork> paperWorkList = paperWorkRepository.findAll();
        assertThat(paperWorkList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePaperWork() throws Exception {
        // Initialize the database
        paperWorkService.save(paperWork);

        int databaseSizeBeforeDelete = paperWorkRepository.findAll().size();

        // Get the paperWork
        restPaperWorkMockMvc.perform(delete("/api/paper-works/{id}", paperWork.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PaperWork> paperWorkList = paperWorkRepository.findAll();
        assertThat(paperWorkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaperWork.class);
    }
}
