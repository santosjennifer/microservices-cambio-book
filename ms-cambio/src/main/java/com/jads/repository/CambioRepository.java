package com.jads.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jads.model.Cambio;

public interface CambioRepository extends JpaRepository<Cambio, Long> {

	Cambio findByFromAndTo(String from, String to);

}
