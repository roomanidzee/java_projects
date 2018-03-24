package ru.itis.romanov_andrey.perpenanto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;

/**
 * 01.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface FileInfoRepository extends JpaRepository<FileInfo, Long>{

    FileInfo findByStorageFileName(String storageFileName);

}
