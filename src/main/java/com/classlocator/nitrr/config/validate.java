package com.classlocator.nitrr.config;

import com.classlocator.nitrr.interfaces.constants;
import com.classlocator.nitrr.services.comService;

import java.util.Map;

public class validate extends comService {

    private static final String EREGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PREGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final String RREGEX = "^\\d{8,10}$";
    private static final String PHREGEX = "^\\d{10}$";

    private validate() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean newPass(String newpass) {
        return newpass.matches(PREGEX);
    }

    public static boolean admin (Map<String, String> user) {
        return user.get(constants.ROLL_NO).matches(RREGEX) && user.get(constants.PASSWORD).matches(PREGEX);
    }

    public static boolean superAdmin (Map<String, String> user) {
        return user.get(constants.EMAIL).matches(EREGEX) && user.get(constants.PASSWORD).matches(PREGEX) && user.get(constants.PHONE).matches(PHREGEX);
    }
}
