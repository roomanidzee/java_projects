package ru.itis.romanov_andrey.perpenanto.repositories.implementations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.itis.romanov_andrey.perpenanto.dto.implementations.FilesToProfileDTO;
import ru.itis.romanov_andrey.perpenanto.dto.interfaces.EntityDTOInterface;
import ru.itis.romanov_andrey.perpenanto.models.transfermodels.FilesToProfileTransfer;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FilesToProfile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.FilesToProfileRepository;
import ru.itis.romanov_andrey.perpenanto.security.role.Role;
import ru.itis.romanov_andrey.perpenanto.security.states.UserState;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * 28.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class FilesToProfileRepositoryImpl implements FilesToProfileRepository{

    @Autowired
    private JdbcTemplate template;
    private Map<Long, FilesToProfile> filesToProfiles = new HashMap<>();
    private List<FilesToProfileTransfer> filesToProfileTransfers = new ArrayList<>();

    private static final String FIND_ALL_QUERY =
            "SELECT * FROM files_to_profile " +
                    "LEFT JOIN files_of_service ON files_of_service.id = files_to_profile.file_id " +
                    "LEFT JOIN profile ON profile.id = files_to_profile.profile_id " +
                    "LEFT JOIN \"user\" ON \"user\".id = profile.user_id";
    private static final String INSERT_QUERY =
            "INSERT INTO files_to_profile(profile_id, file_id) VALUES(?, ?)";
    private static final String FIND_QUERY =
            "SELECT * FROM files_to_profile " +
                    "LEFT JOIN files_of_service ON files_of_service.id = files_to_profile.file_id " +
                    "LEFT JOIN profile ON profile.id = files_to_profile.profile_id " +
                    "LEFT JOIN \"user\" ON \"user\".id = profile.user_id " +
                    "WHERE files_to_profile.profile_id = ?";
    private static final String DELETE_QUERY =
            "DELETE FROM files_to_profile WHERE files_to_profile.profile_id = ?";
    private static final String DELETE_QUERY_2 =
            "DELETE FROM files_to_profile WHERE files_to_profile.profile_id = ? " +
                                                "AND files_to_profile.file_id = ?";

    private RowMapper<FilesToProfile> filesToProfileRowMapper = (resultSet, rowNumber) -> {

       Long currentProfileId = resultSet.getLong(1);

       if(this.filesToProfiles.get(currentProfileId) == null){

           this.filesToProfiles.put(currentProfileId, FilesToProfile.builder()
                                                                    .profile(new Profile())
                                                                    .files(Sets.newHashSet())
                                                                    .build());

           String roleString = resultSet.getString(19);
           String userStateString = resultSet.getString(20);

           Role[] roles = Role.values();
           UserState[] userStates = UserState.values();

           Optional<Role> role = Arrays.stream(roles)
                                       .filter(role1 -> role1.toString().equals(roleString))
                                       .findAny();

           Optional<UserState> userState = Arrays.stream(userStates)
                                                 .filter(userState1 -> userState1.toString().equals(userStateString))
                                                 .findAny();

           if(!role.isPresent()){
               throw new NullPointerException("role not found!");
           }

           if(!userState.isPresent()){
               throw new NullPointerException("user state not found!");
           }

           User user = User.builder()
                           .id(resultSet.getLong(15))
                           .login(resultSet.getString(16))
                           .password(resultSet.getString(17))
                           .tempPassword(resultSet.getString(18))
                           .role(role.get())
                           .userState(userState.get())
                           .confirmHash(resultSet.getString(21))
                           .build();

           Profile profile = Profile.builder()
                                    .id(resultSet.getLong(9))
                                    .user(user)
                                    .email(resultSet.getString(11))
                                    .name(resultSet.getString(12))
                                    .surname(resultSet.getString(13))
                                    .phone(resultSet.getString(14))
                                    .build();

           this.filesToProfiles.get(currentProfileId).setProfile(profile);

           FileInfo fileInfo = FileInfo.builder()
                                       .id(resultSet.getLong(3))
                                       .originalName(resultSet.getString(4))
                                       .size(resultSet.getLong(5))
                                       .encodedName(resultSet.getString(6))
                                       .type(resultSet.getString(7))
                                       .url(resultSet.getString(8))
                                       .build();

           this.filesToProfiles.get(currentProfileId).getFiles().add(fileInfo);

       }else{
           FileInfo fileInfo = FileInfo.builder()
                                       .id(resultSet.getLong(3))
                                       .originalName(resultSet.getString(4))
                                       .size(resultSet.getLong(5))
                                       .encodedName(resultSet.getString(6))
                                       .type(resultSet.getString(7))
                                       .url(resultSet.getString(8))
                                       .build();

           this.filesToProfiles.get(currentProfileId).getFiles().add(fileInfo);
       }

        return this.filesToProfiles.get(currentProfileId);

    };

    private BatchPreparedStatementSetter sqlBatchHelper = new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {

            FilesToProfileTransfer filesToProfileTransfer = filesToProfileTransfers.get(i);
            ps.setLong(1, filesToProfileTransfer.getProfileId());
            ps.setLong(2, filesToProfileTransfer.getFileId());

        }

        @Override
        public int getBatchSize() {
            return filesToProfileTransfers.size();
        }
    };

    @Override
    public List<FilesToProfile> findAll() {

        this.template.query(FIND_ALL_QUERY, this.filesToProfileRowMapper);

        List<FilesToProfile> result = Lists.newArrayList(this.filesToProfiles.values());
        this.filesToProfiles.clear();

        return result;

    }

    @Override
    public void save(FilesToProfile model) {

        filesToProfileTransfers.clear();
        EntityDTOInterface<FilesToProfileTransfer, FilesToProfile> entityDTO = new FilesToProfileDTO();
        filesToProfileTransfers.addAll(entityDTO.convert(Lists.newArrayList(model)));

        this.template.batchUpdate(INSERT_QUERY, this.sqlBatchHelper);

    }

    @Override
    public FilesToProfile find(Long id) {

        FilesToProfile filesToProfile = this.template.query(FIND_QUERY, this.filesToProfileRowMapper, id).get(0);
        this.filesToProfiles.clear();

        return filesToProfile;

    }

    @Override
    public void delete(Long id) {

        this.template.update(DELETE_QUERY, id);

    }

    @Override
    public void update(FilesToProfile model) {

        FilesToProfile newModel;

        try{

            newModel = DataAccessUtils.objectResult(Collections.singletonList(find(model.getProfile().getId())),
                                                    FilesToProfile.class);

            model.getFiles()
                 .stream()
                 .filter(file -> !newModel.getFiles().contains(file))
                 .forEach(newModel.getFiles()::add);

            delete(model.getProfile().getId());
            save(newModel);

        }catch(IncorrectResultSizeDataAccessException | IndexOutOfBoundsException e){

            FilesToProfile filesToProfile = FilesToProfile.builder()
                                                          .profile(model.getProfile())
                                                          .files(model.getFiles())
                                                          .build();
            save(filesToProfile);

        }

    }

    @Override
    public void delete(Long id1, Long id2) {
        this.template.update(DELETE_QUERY_2, id1, id2);
    }
}
