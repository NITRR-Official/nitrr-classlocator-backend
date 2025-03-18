package com.classlocator.nitrr.interfaces;

import java.util.Collections;
import java.util.List;

public class constants {

    private constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }


    public static final String ROOM_ID = "Roomid";
    public static final Integer ID = 1;

    public static final String QUERY = "query";

    public static final String ERROR = "error";
    public static final String ROLL_NO = "rollno";
    public static final String DEPT = "department";

    public static final String NAME = "name";
    public static final String RNAME = "name";
    public static final String DESC = "description";
    public static final String DETAIL = "details";
    public static final String EMAIL  = "email";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String NEW_PASS = "new_pass";
    protected static final String[] ROLES = {"SUPER_ADMIN", "ADMIN"};
    public static String[] getRoles() {
        return ROLES.clone(); // Returns a copy to prevent modification
    }
    public static final List<String> SMALL_ROLES = Collections.unmodifiableList(List.of("sadmin", "admin"));
}
