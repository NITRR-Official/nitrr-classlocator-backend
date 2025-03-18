package com.classlocator.nitrr.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.config.validate;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.entity.trash;
import com.classlocator.nitrr.interfaces.constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class superAdminService extends comService {

    /**
     * Saves or updates the Super Admin account based on existence.
     * Returns 1 if a new Super Admin is created, 0 if updated,
     * and negative values for authentication or processing errors.
     * 
     * @param user Map<String, String> - A map containing Super Admin details (name,
     *             password, optional new password).
     * @return 1 if a new Super Admin is created, 0 if updated,
     *         -1 for incorrect password, -2 for unauthorized access,
     *         and -3 for other errors.
     */

    private int setSuperAdmin(superAdmin sAdmin, Map<String, String> user) {
        sAdmin.setId(constants.ID);
        sAdmin.setName(user.get(constants.NAME));
        sAdmin.setEmail(user.get(constants.EMAIL));
        sAdmin.setPhone(user.get(constants.PHONE));
        sAdmin.setPassword(passwordEncoder.encode(user.get(constants.PASSWORD)));
        sAdmin.setRoles(Arrays.asList(constants.getRoles()[0]));
        sAdmin.setActive(true);
        if(validate.superAdmin(user)) {
            sadminRe.save(sAdmin); 
            log.info("Super Admin Created successfully");
            return 1;
        } 
        else {
            log.error("Invalid details for super admin");
            throw new BadCredentialsException("Invalid details");
        }
    }

    public int saveUpdateSuperAdmin(Map<String, String> user) {
        try {
            Optional<superAdmin> getSAdmin = sadminRe.findById(constants.ID);
            if (getSAdmin.isPresent()) {
                if (!getSAdmin.get().isActive()) {
                    return setSuperAdmin(getSAdmin.get(), user);
                }
                superAdmin sAdmin = (superAdmin) authorization(constants.ID, user.get(constants.PASSWORD)).get(constants.SMALL_ROLES.get(0));
                sAdmin.setName(user.get(constants.NAME));
                sAdmin.setEmail(user.get(constants.EMAIL));
                sAdmin.setPhone(user.get(constants.PHONE));
                if (user.get(constants.NEW_PASS) != null) {
                    if(!validate.newPass(user.get(constants.NEW_PASS))) {
                        log.error("Invalid new password for super admin");
                        throw new BadCredentialsException("Invalid new password");
                    } 
                    sAdmin.setPassword(passwordEncoder.encode(user.get(constants.NEW_PASS)));
                }
                if(validate.superAdmin(user)) {
                    sadminRe.save(sAdmin); 
                    log.info("Super Admin Updated successfully");
                    return 0;
                } 
                else {
                    log.error("Invalid details for super admin");
                    throw new BadCredentialsException("Invalid details");
                }
            }
            return setSuperAdmin(new superAdmin(), user);
        } catch (BadCredentialsException e) {
            log.error("Invalid password or details missing for super admin", e);
            return -1;
        } catch (NullPointerException e) {
            log.error("Unauthorized access to super admin", e);
            return -2;
        } catch (Exception e) {
            log.error("Internal server error by super admin", e);
            return -3;
        }
    }

    /**
     * Deactivates the Super Admin account.
     * 
     * @return int - Returns 1 if deactivation is successful,
     *         -1 if an error occurs during deactivation.
     */

    public int deleteSuperAdmin() {
        try {
            superAdmin sAdmin = sadminRe.findById(constants.ID).orElse(null);
            if (sAdmin != null) {
                sAdmin.setActive(false);
                sadminRe.save(sAdmin);
            } else {
                log.warn("Super Admin not found for deactivation.");
                throw new NullPointerException();
            }
            log.info("Super Admin deactivated");
            return 1;
        } catch (Exception e) {
            log.error("Super Admin deactivation failed", e);
            return -1;
        }
    }

    /** Future Updates */
    public List<trash> sAdminTrash(Integer rollno) {
        try {
            // Logic to get sAdminTrash is applied here
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }

    /**
     * Approves a query by marking it as reviewed by the Super Admin.
     * If the query is already approved, no action is taken.
     * 
     * @param id String - The ID of the query to be approved.
     * @return 1 if approval is successful,
     *         0 if already approved,
     *         -1 if the query is not found,
     *         -3 for other errors.
     */
    public int approval(String id) {
        try {
            Optional<query> q = queryR.findById(new ObjectId(id));
            if (q.isPresent()) {
                query temp = q.get();
                if (!temp.isSuperAdmin()) {
                    temp.setSuperAdmin(true);
                    int status = Integer.parseInt(temp.getRaisedBy());
                    int appStatus = status == 1 ? updateSearchTool(temp, true) : updateSearchTool(temp, false);
                    if (appStatus != 1)
                        return appStatus;
                } else {
                    log.warn("Query {} already approved.", id);
                    return 0;
                }

                queryR.save(temp);
                log.info("Query {} approved successfully", id);
                return 1;
            } else {
                log.warn("Query {} not found for approval.", id);
                return -1;
            }
        } catch (Exception e) {
            log.error("Internal server error by super admin for query {} :", id, e);
            return -3;
        }
    }

}
