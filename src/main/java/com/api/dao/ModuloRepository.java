package com.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.entity.Modulo;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Integer> {

}
