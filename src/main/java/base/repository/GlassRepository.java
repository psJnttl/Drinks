package base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import base.domain.Glass;

public interface GlassRepository extends JpaRepository<Glass, Long> {
    Glass findByName(String name);
}
