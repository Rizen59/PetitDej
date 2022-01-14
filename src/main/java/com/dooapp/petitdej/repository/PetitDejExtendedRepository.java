package com.dooapp.petitdej.repository;

import com.dooapp.petitdej.domain.PetitDej;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface PetitDejExtendedRepository extends PetitDejRepository {
    @Query
    List<PetitDej> findAllByDateEquals(LocalDate localDate);
}
