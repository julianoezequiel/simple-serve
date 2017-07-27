package com.api.dao;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.entity.Usuario;

/**
 * 
 * @author juliano.ezequiel
 *
 */
@Repository
@Transactional(value = TxType.NEVER)
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	@Query(value = "SELECT u from Usuario u where u.email = :email")
	Usuario buscarPorEmail(@Param("email") String email);

}
