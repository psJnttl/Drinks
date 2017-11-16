package base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import base.domain.Glass;

public interface GlassRepository extends JpaRepository<Glass, Long> {
    Glass findByName(String name);
    List<Glass> findByNameContainingIgnoreCase(String name);
}
