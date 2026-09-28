package concrete.goonies.bulkbusketbackend.repository.impl;

import concrete.goonies.bulkbusketbackend.domain.Role;
import concrete.goonies.bulkbusketbackend.domain.User;
import concrete.goonies.bulkbusketbackend.dto.UserDTO;
import concrete.goonies.bulkbusketbackend.exception.ApiException;
import concrete.goonies.bulkbusketbackend.repository.RoleRepository;
import concrete.goonies.bulkbusketbackend.repository.UserRepository;
import concrete.goonies.bulkbusketbackend.repository.rowmapper.UserRowMapper;
import concrete.goonies.bulkbusketbackend.services.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

import static concrete.goonies.bulkbusketbackend.domain.Role.RoleType.ROLE_USER;
import static concrete.goonies.bulkbusketbackend.domain.User.VerificationType.ACCOUNT;
import static concrete.goonies.bulkbusketbackend.queries.UserQueries.*;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.time.DateFormatUtils.format;
import static org.apache.commons.lang3.time.DateUtils.addDays;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/26 03:02
 * Author: Scarra Luba
 */

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {

        //Check the email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) {
            throw new ApiException("Email Already Exists");
        }

        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameterSource = getSqlParameterSource(user);
            user.setStatus(User.HouseholdStatus.PENDING.name());
            jdbcTemplate.update(INSERT_USER_QUERY, parameterSource, holder);
            user.setId(requireNonNull(holder.getKey()).longValue());
            //
            //add Role To User
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());

            //Send verification url
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());

            //save url to table
            jdbcTemplate.update(INSERT_VERIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", verificationUrl));

            /*  //Send email with verification url*/
            //emailService.sendVerificationUrl(user.getName(), user.getEmail(), verificationUrl, ACCOUNT);

            //  user.setLocked(false);

            return user;
        } catch (Exception exception) {
            exception.printStackTrace();

            throw new ApiException(exception.getMessage());
        }
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    SELECT_USER_BY_EMAIL_QUERY,
                    Map.of("email", email),
                    new UserRowMapper()
            );

        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No user by email: " + email);

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ApiException(
                    "Failed to find user by email: " + email +
                            " | " + exception.getMessage()
            );
        }
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        Date expirationDate = addDays(new Date(), 1);
        String veriCode = random(6, false, true);

        try {
log.info("VeriCode: {}", veriCode);
            jdbcTemplate.update(DELETE_VERIFICATION_CODE_BY_ID, Map.of("id", user.getId()));
            jdbcTemplate.update(INSERT_VERIFICATION_CODE_BY_ID, Map.of("userId", user.getId(), "code", veriCode, "expirationDate", expirationDate));

            //  sendSms("+27795929406", "BulkBusket verification code: " + veriCode);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ApiException("Failed to send verification code to " + user.getEmail() + " | " + exception.getMessage());
        }

    }

    @Override
    public User verifyCode(String email, String code) {
        if(isVerirficationCodeExpired(code)){
            throw new ApiException("This Code has expired. Please Login again");
        }
        try {
            User userByCode = jdbcTemplate.queryForObject(SELECT_USER_BY_CODE_QUERY, Map.of("code", code), new UserRowMapper());
            User userByEmail = getUserByEmail( email);

            if (userByCode.getEmail().equalsIgnoreCase(userByEmail.getEmail())) {
                jdbcTemplate.update(DELETE_CODE, Map.of("code", code));
                return userByCode;
            } else {
                throw new ApiException("Code is invalid. Please try again.");
            }
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No user by email: " + email);

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ApiException(
                    "Failed to find user by email: " + email +
                            " | " + exception.getMessage()
            );
        }

    }
    private Boolean isVerirficationCodeExpired(String code){

        try {
            return   jdbcTemplate.queryForObject(SELECT_CODE_EXPIRATION_DATE, Map.of("code", code), Boolean.class);

        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("Invalid Code");

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ApiException(
                    "Failed to find user by Code:| " + exception.getMessage()
            );
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByEmail(username);
        //System.out.println(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not Found");
        }
        return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()).getPermission());
    }

    private int getEmailCount(String email) {
        return jdbcTemplate.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private String getVerificationUrl(String key, String accType) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("user/verify/" + accType + "/" + key).toUriString();
    }

    private SqlParameterSource getSqlParameterSource(User user) {

        return new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", passwordEncoder.encode(user.getPassword()));

    }
}
