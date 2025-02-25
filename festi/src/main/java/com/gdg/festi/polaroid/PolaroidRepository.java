package com.gdg.festi.polaroid;

import com.gdg.festi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolaroidRepository extends JpaRepository<Polaroid, Long> {
    @Query(value = "SELECT * FROM polaroid order by RAND() limit 20",nativeQuery = true)
    List<Polaroid> findRandomPolaroids();

    List<Polaroid> findByUser(User user);
}
