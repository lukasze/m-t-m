package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Lab;
import com.mycompany.myapp.repository.LabRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LabResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LabResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRITPION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRITPION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/labs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LabRepository labRepository;

    @Mock
    private LabRepository labRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLabMockMvc;

    private Lab lab;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lab createEntity(EntityManager em) {
        Lab lab = new Lab().title(DEFAULT_TITLE).descritpion(DEFAULT_DESCRITPION);
        return lab;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lab createUpdatedEntity(EntityManager em) {
        Lab lab = new Lab().title(UPDATED_TITLE).descritpion(UPDATED_DESCRITPION);
        return lab;
    }

    @BeforeEach
    public void initTest() {
        lab = createEntity(em);
    }

    @Test
    @Transactional
    void createLab() throws Exception {
        int databaseSizeBeforeCreate = labRepository.findAll().size();
        // Create the Lab
        restLabMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lab))
            )
            .andExpect(status().isCreated());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeCreate + 1);
        Lab testLab = labList.get(labList.size() - 1);
        assertThat(testLab.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLab.getDescritpion()).isEqualTo(DEFAULT_DESCRITPION);
    }

    @Test
    @Transactional
    void createLabWithExistingId() throws Exception {
        // Create the Lab with an existing ID
        lab.setId(1L);

        int databaseSizeBeforeCreate = labRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLabMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lab))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLabs() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);

        // Get all the labList
        restLabMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lab.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].descritpion").value(hasItem(DEFAULT_DESCRITPION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLabsWithEagerRelationshipsIsEnabled() throws Exception {
        when(labRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLabMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(labRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLabsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(labRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLabMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(labRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLab() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);

        // Get the lab
        restLabMockMvc
            .perform(get(ENTITY_API_URL_ID, lab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lab.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.descritpion").value(DEFAULT_DESCRITPION));
    }

    @Test
    @Transactional
    void getNonExistingLab() throws Exception {
        // Get the lab
        restLabMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLab() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);

        int databaseSizeBeforeUpdate = labRepository.findAll().size();

        // Update the lab
        Lab updatedLab = labRepository.findById(lab.getId()).get();
        // Disconnect from session so that the updates on updatedLab are not directly saved in db
        em.detach(updatedLab);
        updatedLab.title(UPDATED_TITLE).descritpion(UPDATED_DESCRITPION);

        restLabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLab.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLab))
            )
            .andExpect(status().isOk());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate);
        Lab testLab = labList.get(labList.size() - 1);
        assertThat(testLab.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLab.getDescritpion()).isEqualTo(UPDATED_DESCRITPION);
    }

    @Test
    @Transactional
    void putNonExistingLab() throws Exception {
        int databaseSizeBeforeUpdate = labRepository.findAll().size();
        lab.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lab.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lab))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLab() throws Exception {
        int databaseSizeBeforeUpdate = labRepository.findAll().size();
        lab.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lab))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLab() throws Exception {
        int databaseSizeBeforeUpdate = labRepository.findAll().size();
        lab.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lab))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLabWithPatch() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);

        int databaseSizeBeforeUpdate = labRepository.findAll().size();

        // Update the lab using partial update
        Lab partialUpdatedLab = new Lab();
        partialUpdatedLab.setId(lab.getId());

        partialUpdatedLab.descritpion(UPDATED_DESCRITPION);

        restLabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLab.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLab))
            )
            .andExpect(status().isOk());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate);
        Lab testLab = labList.get(labList.size() - 1);
        assertThat(testLab.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLab.getDescritpion()).isEqualTo(UPDATED_DESCRITPION);
    }

    @Test
    @Transactional
    void fullUpdateLabWithPatch() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);

        int databaseSizeBeforeUpdate = labRepository.findAll().size();

        // Update the lab using partial update
        Lab partialUpdatedLab = new Lab();
        partialUpdatedLab.setId(lab.getId());

        partialUpdatedLab.title(UPDATED_TITLE).descritpion(UPDATED_DESCRITPION);

        restLabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLab.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLab))
            )
            .andExpect(status().isOk());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate);
        Lab testLab = labList.get(labList.size() - 1);
        assertThat(testLab.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLab.getDescritpion()).isEqualTo(UPDATED_DESCRITPION);
    }

    @Test
    @Transactional
    void patchNonExistingLab() throws Exception {
        int databaseSizeBeforeUpdate = labRepository.findAll().size();
        lab.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lab.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lab))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLab() throws Exception {
        int databaseSizeBeforeUpdate = labRepository.findAll().size();
        lab.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lab))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLab() throws Exception {
        int databaseSizeBeforeUpdate = labRepository.findAll().size();
        lab.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lab))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLab() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);

        int databaseSizeBeforeDelete = labRepository.findAll().size();

        // Delete the lab
        restLabMockMvc
            .perform(delete(ENTITY_API_URL_ID, lab.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
