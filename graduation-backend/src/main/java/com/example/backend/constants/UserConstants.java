package com.example.backend.constants;

public interface UserConstants {
    String PHONE_FORMAT = "^1[3-9]\\d{9}$";
    String USERNAME_FORMAT = "^[a-zA-Z0-9,\\.\\-_]{4,17}$";
    String PASSWORD_FORMAT = "^[a-zA-Z0-9,\\.\\-_]{4,17}$";
    String ID_NUMBER_FORMAT = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$";

    Long ADMIN_ROLE_ID = 1L;
    Long YOUNGSTER_ROLE_ID = 2L;
    Long ELDER_ROLE_ID = 3L;

    Integer ELDER_AGE_BOUNDARY = 50;

    Integer RELATION_STATUS_NORMAL = 0;
    Integer RELATION_STATUS_WAITING = 1;

    Integer USER_STATUS_NORMAL = 0;
    Integer USER_STATUS_BLOCK = 1;
    Integer USER_STATUS_MONITORING = 2;
}
