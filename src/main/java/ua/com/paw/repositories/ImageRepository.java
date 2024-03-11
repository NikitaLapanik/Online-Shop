package ua.com.paw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.paw.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
