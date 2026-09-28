/* =========================================================
 * SCHEMA
 * ========================================================= */

CREATE SCHEMA IF NOT EXISTS bulkbusket;


/* =========================================================
 * households
 * ========================================================= */
CREATE TABLE IF NOT EXISTS households
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status   VARCHAR(32)  NOT NULL DEFAULT 'PENDING',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* =========================================================
 * ROLES
 * ========================================================= */

CREATE TABLE IF NOT EXISTS roles
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    permission VARCHAR(255) NOT NULL
);

/* =========================================================
 * USER ROLES (MANY-TO-MANY)
 * ========================================================= */

CREATE TABLE IF NOT EXISTS user_roles
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
            REFERENCES households (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT,

    CONSTRAINT uq_user_role
        UNIQUE (user_id, role_id)
);

/* =========================================================
 * EVENTS
 * ========================================================= */

CREATE TABLE IF NOT EXISTS events
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type        VARCHAR(255) NOT NULL
        CHECK (
            type IN (
                     'LOGIN_ATTEMPT',
                     'LOGIN_ATTEMPT_FAILURE',
                     'LOGIN_ATTEMPT_SUCCESS',
                     'PROFILE_UPDATE',
                     'PROFILE_PICTURE_UPDATE',
                     'ROLE_UPDATE',
                     'ACCOUNT_SETTINGS_UPDATE',
                     'PASSWORD_UPDATE',
                     'MFA_UPDATE'
                )
            ),

    description VARCHAR(255) NOT NULL
);
/* =========================================================
 * USER EVENTS (AUDIT LOGS)
 * ========================================================= */

CREATE TABLE IF NOT EXISTS user_events
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    event_id     BIGINT NOT NULL,
    device       VARCHAR(255),
    ip_address   VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_events_user
        FOREIGN KEY (user_id)
            REFERENCES households (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,

    CONSTRAINT fk_user_events_event
        FOREIGN KEY (event_id)
            REFERENCES events (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
);
/* =========================================================
 * ACCOUNT VERIFICATION
 * ========================================================= */

CREATE TABLE IF NOT EXISTS account_verification
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    user_id BIGINT       NOT NULL,
    url     VARCHAR(255) NOT NULL,

    CONSTRAINT fk_user_account_verification
        FOREIGN KEY (user_id)
            REFERENCES households (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,

    CONSTRAINT uq_user_account_verification
        UNIQUE (user_id, url)
);

/* =========================================================
 * RESET VERIFICATION
 * ========================================================= */

CREATE TABLE IF NOT EXISTS reset_verification
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    user_id         BIGINT       NOT NULL,
    url             VARCHAR(255) NOT NULL,
    expiration_date TIMESTAMP    NOT NULL,

    CONSTRAINT fk_user_reset_verification
        FOREIGN KEY (user_id)
            REFERENCES households (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,

    CONSTRAINT uq_user_reset_verification
        UNIQUE (user_id, url)
);

/* =========================================================
 * TWO FACTOR VERIFICATION
 * ========================================================= */

CREATE TABLE IF NOT EXISTS two_factor_verification
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    user_id         BIGINT      NOT NULL,
    code            VARCHAR(10) NOT NULL,
    expiration_date TIMESTAMP   NOT NULL,

    CONSTRAINT fk_user_two_factor_verification
        FOREIGN KEY (user_id)
            REFERENCES households (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,

    CONSTRAINT uq_user_two_factor_verification
        UNIQUE (user_id, code)
);