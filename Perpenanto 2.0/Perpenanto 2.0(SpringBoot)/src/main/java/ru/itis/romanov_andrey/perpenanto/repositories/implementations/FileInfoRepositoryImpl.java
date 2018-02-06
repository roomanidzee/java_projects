package ru.itis.romanov_andrey.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.FileInfoRepository;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 26.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class FileInfoRepositoryImpl implements FileInfoRepository{

    @Autowired
    private JdbcTemplate template;
    private Map<Long, FileInfo> files = new HashMap<>();

    private static final String FIND_ALL_QUERY = "SELECT * FROM files_of_service";
    private static final String INSERT_QUERY =
            "INSERT INTO files_of_service(original_name, file_size, encoded_name, file_type, file_url) " +
                    "VALUES(?, ?, ?, ?, ?)";
    private static final String FIND_QUERY = "SELECT * FROM files_of_service WHERE files_of_service.id = ?";
    private static final String DELETE_QUERY = "DELETE FROM files_of_service WHERE files_of_service.id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE files_of_service " +
             "SET (original_name, file_size, encoded_name, file_type, file_url) = (?, ?, ?, ?, ?)" +
                    "WHERE files_of_service.id = ?";
    private static final String FIND_BY_STORAGE_NAME_QUERY =
            "SELECT * FROM files_of_service WHERE files_of_service.encoded_name = ?";

    private RowMapper<FileInfo> fileRowMapper = (resultSet, rowNumber) ->{

        Long currentFileId = resultSet.getLong(1);

        if(this.files.get(currentFileId) == null){

            this.files.put(currentFileId, FileInfo.builder()
                                                  .id(currentFileId)
                                                  .originalName(resultSet.getString(2))
                                                  .size(resultSet.getLong(3))
                                                  .encodedName(resultSet.getString(4))
                                                  .type(resultSet.getString(5))
                                                  .url(resultSet.getString(6))
                                                  .build());

        }

        return this.files.get(currentFileId);

    };

    @Override
    public List<FileInfo> findAll() {

        this.template.query(FIND_ALL_QUERY, this.fileRowMapper);

        List<FileInfo> result = Lists.newArrayList(this.files.values());
        this.files.clear();

        return result;

    }

    @Override
    public void save(FileInfo model) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.template.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
            ps.setString(1, model.getOriginalName());
            ps.setLong(2, model.getSize());
            ps.setString(3, model.getEncodedName());
            ps.setString(4, model.getType());
            ps.setString(5, model.getUrl());
            return ps;

        }, keyHolder);

        model.setId(keyHolder.getKey().longValue());

    }

    @Override
    public FileInfo find(Long id) {

        FileInfo file = this.template.queryForObject(FIND_QUERY, this.fileRowMapper, id);
        this.files.clear();

        return file;

    }

    @Override
    public void delete(Long id) {
        this.template.update(DELETE_QUERY, id);
    }

    @Override
    public void update(FileInfo model) {
        this.template.update(UPDATE_QUERY, model.getOriginalName(), model.getSize(), model.getEncodedName(),
                                           model.getType(), model.getUrl(), model.getId());
    }

    @Override
    public FileInfo findByStorageFileName(String storageFileName) {

        FileInfo file = this.template.queryForObject(FIND_BY_STORAGE_NAME_QUERY, this.fileRowMapper, storageFileName);
        this.files.clear();

        return file;

    }
}
