package com.ar.autocomplete.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ar.autocomplete.entity.Name;

@Repository
public interface NameRepo extends JpaRepository<Name, Long> {

}
