package com.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.entity.Permissao;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Integer> {

}
