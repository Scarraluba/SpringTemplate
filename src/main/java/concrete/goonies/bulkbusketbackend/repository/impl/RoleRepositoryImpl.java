package concrete.goonies.bulkbusketbackend.repository.impl;

import concrete.goonies.bulkbusketbackend.domain.Role;
import concrete.goonies.bulkbusketbackend.exception.ApiException;
import concrete.goonies.bulkbusketbackend.repository.RoleRepository;
import concrete.goonies.bulkbusketbackend.repository.rowmapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static concrete.goonies.bulkbusketbackend.domain.Role.RoleType.ROLE_USER;
import static concrete.goonies.bulkbusketbackend.queries.UserQueries.*;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/26 04:20
 * Author: Scarra Luba
 */

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Role create(Role role) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        try {
            Role role = jdbcTemplate.queryForObject( SELECT_ROLE_BY_NAME_QUERY, Map.of("name", roleName), new RoleRowMapper());
            jdbcTemplate.update( INSERT_ROLE_TO_USER_QUERY, Map.of("userId", userId, "roleId", role.getId()));

        } catch (
                EmptyResultDataAccessException exception) {
            throw new ApiException("No role found by name " + ROLE_USER.name());
        } catch (Exception exception) {
            exception.printStackTrace();  throw new ApiException("Something went wrong. Try again"+ exception.getMessage());
        }
    }

    @Override
    public Role getRoleByUserId(Long userId) {

        log.info("gettingRold");
        try {
            return jdbcTemplate.queryForObject(SELECT_USER_BY_ID_QUERY,Map.of("id",userId), new RoleRowMapper());
        } catch (
                EmptyResultDataAccessException exception) {
            throw new ApiException("No role found by name " + ROLE_USER.name());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ApiException("Something went wrong. Try again"+ exception.getMessage());
        }
        //return null;
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {

    }
}

