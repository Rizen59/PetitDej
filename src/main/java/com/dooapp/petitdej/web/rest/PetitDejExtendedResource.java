package com.dooapp.petitdej.web.rest;

import com.dooapp.petitdej.domain.PetitDej;
import com.dooapp.petitdej.domain.User;
import com.dooapp.petitdej.repository.PetitDejRepository;
import com.dooapp.petitdej.service.UserService;
import com.dooapp.petitdej.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

/**
 * Extended REST controller for managing {@link com.dooapp.petitdej.domain.PetitDej}.
 */
@RestController
@RequestMapping("/api/v1")
@Transactional
public class PetitDejExtendedResource extends PetitDejResource {

    private final Logger log = LoggerFactory.getLogger(PetitDejResource.class);

    private static final String ENTITY_NAME = "petitDej";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PetitDejRepository petitDejRepository;

    private final UserService userService;

    public PetitDejExtendedResource(PetitDejRepository petitDejRepository, UserService userService) {
        super(petitDejRepository);
        this.petitDejRepository = petitDejRepository;
        this.userService = userService;
    }

    // TODO Write documentation here
    @GetMapping("/petit-dejs/participate/{id}")
    public ResponseEntity<PetitDej> switchParticipationInPetitDej(@PathVariable(value = "id", required = false) final Long id) {
        final User user = userService.getUserWithAuthorities().get();

        // TODO Write code here
        PetitDej result = petitDejRepository.getById(id);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
