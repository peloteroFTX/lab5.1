package com.gtics.lahaces.repository;
import com.gtics.lahaces.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento,Integer> {
}
