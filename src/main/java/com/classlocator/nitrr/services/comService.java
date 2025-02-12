package com.classlocator.nitrr.services;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.classlocator.nitrr.interfaces.Pair;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.searchTool;
import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.entity.toJSON;
import com.classlocator.nitrr.repository.adminRepo;
import com.classlocator.nitrr.repository.queryRepo;
import com.classlocator.nitrr.repository.searchToolRepo;
import com.classlocator.nitrr.repository.superAdminRepo;
import com.classlocator.nitrr.repository.toJSONRepo;

import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Service
@Slf4j
public class comService {
    // here isapproved, getAllQueries, saveQuery, authorization

    @Autowired
    private searchToolRepo search;

    @Autowired
    private toJSONRepo searchTool;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    protected adminRepo adminRe;

    @Autowired
    protected queryRepo queryR;

    @Autowired
    protected superAdminRepo sadminRe;

    @Autowired
    protected jwtService jwt;

    protected static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // The rollBack/deleteTrash/recoverQueries/rejectQueries to be applied later

    /**
     * Removes a query from an admin's pending queries and moves it to accepted
     * queries.
     *
     * @param user The roll number of the admin as a string.
     * @param q    The query object to be removed from pending queries.
     * @return 1 if the operation is successful,
     *         -3 if the query is not found or an error occurs.
     */
    private int removePending(String user, query q) {
        boolean removed = false;
        try {
            admin a = adminRe.findByrollno(Integer.parseInt(user));

            removed = a.getPendingQueries().removeIf(x -> x.getId().equals(q.getId()));

            if (removed) {
                a.getAcceptedQueries().add(q);
            } else {
                log.error("Query {} not found in {} pending list :", q.getId(), user);
                return -3;
            }

            adminRe.save(a);
            log.info("Query {} moved to accepted list for {} :", q.getId(), user);
            return 1;
        } catch (Exception e) {
            log.error("Internal server error by {} for query {} :", user, q.getId(), e);
            return -3;
        }
    }

    /**
     * Removes a query from the super admin's pending queries and moves it to accepted queries.
     *
     * @param q The query object to be removed from pending queries.
     * @return 1 if the operation is successful;
     *         -3 if the query is not found, the super admin is not found, or an error occurs.
     */
    private int removePending(query q) {
        boolean removed = false;
        try {
            Optional<superAdmin> suser = sadminRe.findById(1);
            if (suser.isEmpty())
            {
                log.error("Super admin not found for query {} :", q.getId());
                return -3;
            }

            superAdmin user = suser.get();
            removed = user.getPendingQueries().removeIf(x -> x.getId().equals(q.getId()));

            if (removed) {
                user.getAcceptedQueries().add(q);
            } else {
                log.error("Query {} not found in super admin pending list :", q.getId());
                return -3; 
            }

            sadminRe.save(user);
            log.info("Query {} moved to accepted list for super admin :", q.getId());
            return 1;
        } catch (Exception e) {
            log.error("Internal server error for query {} :", q.getId(), e);
            return -3;
        }
    }

    /**
     * Moves a query to the approved list after it has been approved.
     * 
     * @param q The query that has been approved.
     * @param isSuperAdmin A boolean flag indicating whether query is by a super admin.
     * @return An integer indicating the status of the operation.
     */
    private int afterApproval(query q, boolean isSuperAdmin) {
        if (isSuperAdmin) {
            log.info("Query {} to be moved for super admin.", q.getId());
            return removePending(q);
        } else {
            log.info("Query {} to be moved for admin.", q.getId());
            return removePending(q.getRaisedBy(), q);
        }
    }

    /**
     * Validates the input string to ensure it is not null or empty.
     *
     * @param input The input string to check.
     * @return The trimmed input string if valid.
     * @throws NullPointerException if the input is null or contains only whitespace.
     */
    private String infoCheck(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new NullPointerException("Input cannot be empty or only whitespace.");
        }
        return input;
    }

    /**
     * Processes a query request and either updates an existing query or creates a new one.
     *
     * @param q A map containing query details (Roomid, name, description).
     * @param rollno The roll number of the user raising the query.
     * @return A map containing either the saved query object or an error code.
     */
    private Map<String, ?> processQuery(Map<String, String> q, Integer rollno) {
        Map<String, query> status = new HashMap<>();
        Map<String, Integer> error = new HashMap<>();
        try {
            Integer room = Integer.parseInt(q.get("Roomid"));
            String roll = rollno.toString();

            Optional<query> exists = queryR.findFirstByRoomidAndRaisedBy(room, roll);
            query raiser = exists.orElse(new query());

            raiser.setRaisedBy(roll);
            raiser.setRoomid(room);
            raiser.setName(infoCheck(q.get("name")));
            raiser.setDescription(infoCheck(q.get("description")));

            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            raiser.setDate(currentDate.format(formatter));

            queryR.save(raiser);
            status.put("query", raiser);
            log.info("Query for room {} saved into roll number {} successfully.", q.get("Roomid"), roll);
            return status;
        } catch (NumberFormatException e) {
            error.put("error", -1);
            log.error("Invalid room id {} by {} :", q.get("Roomid") ,rollno, e);
            return error;
        } catch (NullPointerException e) {
            error.put("error", -2);
            log.error("Missing required fields for room id {} by {} :", q.get("Roomid") ,rollno, e);
            return error;
        } catch (Exception e) {
            error.put("error", -3);
            log.error("Internal server error by {} for room id {} :", rollno, q.get("Roomid"), e);
            return error;
        }
    }

    /**
     * Updates the search tool in MongoDB after a query is approved and generates a new map.
     *
     * @param q The query object containing the room ID, name, and description.
     * @param isSuperAdmin Boolean indicating if the user is a super admin.
     * @return Integer status code:
     *         1, Update successful;
     *         -2, Failed to generate the search tool or map;
     *         -3, Error occurred (rollback needed).
     */

    protected int updateSearchTool(query q, boolean isSuperAdmin) {
        try {

            /*
             * Step 1: Check if the search tool exists for all 301 rooms.
             * If not, generate the search tool using searchToolsGenerator().
             */

            if (!(search.count() > 0)) {
                boolean status = searchToolsGenerator();
                if (!status) {
                    log.error("Failed to generate search tool collection.");
                    return -2;
                }
                log.info("Search tool collection generated successfully.");
            }
            else log.info("Search tool collection already exists.");

            /* Step 2: Retrieve or create a new searchTool instance for the given room ID. */
            Optional<searchTool> room = search.findById(q.getRoomid());
            searchTool temp = room.isPresent() ? room.get() : new searchTool();

            /* Step 3: Add the new query information to the searchTool's data list. */
            temp.getData().add(new Pair<ObjectId, Pair<String, String>>(q.getId(),
                    new Pair<String, String>(q.getName(), q.getDescription())));
            temp.setId(q.getRoomid());

            /*
             * Step 4: Generate a new map and update the toJSON entity.
             * If successful, move the query to the approved list.
             */
            if (generateMap(temp)) {

                /*
                 * 
                 * Now after successful generation of new map, the query will move to approved
                 * list
                 * 
                 * Here moving to pending will take place, as if something goes wrong then no
                 * further updates
                 * allowed so that it will act as transactional system
                 * 
                 */

                int approvalDB = afterApproval(q, isSuperAdmin);
                if (approvalDB != 1) {
                    // rollback the generateMap() and return -3
                    log.error("Failed to move query {} to approved list.", q.getId());
                    return -3;
                }
                search.save(temp);
                log.info("Query {} moved to approved list successfully.", q.getId());
                return 1;
            } else
            {
                log.error("Failed to generate toJSON map for query {}.", q.getId());
                return -2;
            }
        } catch (Exception e) {
            log.error("Internal server error for query {} :", q.getId(), e);
            return -3;
        }
    }

    /** In future, the below functions will be implemented */
    public boolean rollBack(query q, boolean isAdmin, int rollno) {
        // Logic applied to rollback a query if that query is latest to that particular
        // room then reject that query
        // to trash
        return false;
    }

    /** In future, the below functions will be implemented */
    public boolean deleteTrash(boolean isAdmin, int rollno) {
        // Logic is applied here to delete the already present trash by admin or
        // superadmin
        return false;
    }

    /** In future, the below functions will be implemented */
    public boolean recoverQueries(boolean isAdmin, int rollno) {
        // Logic is applied here to recover the deleted queries
        return false;
    }

    /** In future, the below functions will be implemented */
    public boolean rejectQueries(boolean isAdmin, int rollno) {
        // Logic to move the queries to trash is applied here
        return false;
    }

    /**
     * This method retrieves all existing searchTool documents, updates the data
     * with the latest query, and stores the updated version as a JSON object.
     * 
     * @param croom The searchTool object containing the updated query data.
     * @return true if the map generation is successful, false otherwise.
     */
    @SuppressWarnings("unchecked")
    public boolean generateMap(searchTool croom) {
        try {
            JSONObject jsonOutput = new JSONObject();
            List<searchTool> rooms = search.findAll();

            List<toJSON> mapp = searchTool.findAll();

            int versions = 0;
            if (!mapp.isEmpty()) {
                versions = mapp.get(0).getMapVersion() + 1;
                log.info("Map version updated to {}.", versions);
            }
            else log.info("Map version initialized to 0.");

            jsonOutput.put("version", versions);

            for (searchTool room : rooms) {
                List<Pair<ObjectId, Pair<String, String>>> dataArray = croom.getId() == room.getId() ? croom.getData()
                        : room.getData();

                if (dataArray == null || dataArray.isEmpty()) {
                    continue;
                }

                String name = dataArray.get(dataArray.size() - 1).getValue().getKey();
                String details = dataArray.get(dataArray.size() - 1).getValue().getValue();

                JSONObject entry = new JSONObject();
                entry.put("name", name);
                entry.put("details", details);

                jsonOutput.put(room.getId().toString(), entry);
            }

            toJSON json = mapp.isEmpty() ? new toJSON() : mapp.get(0);

            json.setMapVersion(versions);
            json.setSearchTool(jsonOutput.toJSONString());

            searchTool.save(json);
            log.info("Map generated successfully.");
            return true;
        } catch (Exception e) {
            log.error("Failed to generate map.", e);
            return false;
        }
    }

    /**
     * Initializes the search tools data in MongoDB from a predefined JSON file.
     * 
     * This method reads `searchTool.json`, extracts room IDs and corresponding
     * details, and inserts them into the MongoDB collection.
     * 
     * @return true if the process completes successfully, false if an error occurs.
     */
    public boolean searchToolsGenerator() {
        String relativePath = "src/main/resources/templates/searchTool.json";
        File file = Paths.get(relativePath).toFile();
        query q = new query();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));

            for (Object key : jsonObject.keySet()) {
                try {
                    String id = (String) key;
                    JSONObject valueObj = (JSONObject) jsonObject.get(id);
                    searchTool s = new searchTool();
                    s.setId(Integer.parseInt(id));

                    s.getData()
                            .add(new Pair<ObjectId, Pair<String, String>>(q.getId(),
                                    new Pair<String, String>(valueObj.get("name").toString(),
                                            valueObj.get("details").toString())));

                    search.save(s);
                } catch (Exception e) {
                    log.error("Failed to insert data for room {} :", key, e.getMessage());
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Internal server error while generating search tools.", e);
            return false;
        }
    }

    /**
     * This method fetches both pending and accepted queries for:
     * 1. The user (identified by `rollno`).
     * 2. Other users (excluding the given `rollno`).
     * 
     * @param rollno The roll number of the user whose queries need to be fetched.
     * @return A map containing both the user's and other users' queries, categorized by status.
     */
    public Map<String, Map<String, List<query>>> getAllQueries(String rollno) {
        Map<String, Map<String, List<query>>> all = new HashMap<>();

        Map<String, List<query>> pendingMe = new HashMap<>();
        pendingMe.put("Pending", queryR.findAllByRollno(rollno, false));
        pendingMe.put("Accepted", queryR.findAllByRollno(rollno, true));

        Map<String, List<query>> pendingOthers = new HashMap<>();
        pendingOthers.put("Pending", queryR.findAllByNotRollno(rollno, false));
        pendingOthers.put("Accepted", queryR.findAllByNotRollno(rollno, true));

        all.put("Me", pendingMe);
        all.put("Others", pendingOthers);

        return all;
    }

    /**
     * This function will authorize the user and return the user object if the user is legit
     * 
     * @param rollno   integer
     * @param password string
     * @return Map<String, Object>
     */
    public Map<String, Object> authorization(Integer rollno, String password) {
        Authentication auth = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(rollno.toString(), password));
        boolean authStatus = auth.isAuthenticated();

        Map<String, Object> attributes = new HashMap<String, Object>();
        if (authStatus) {
            if (auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_SUPER_ADMIN"))) {
                superAdmin suser = sadminRe.findById(rollno).get();
                attributes.put("sadmin", suser);
                log.info("Super admin {} authorized successfully.", rollno);
                return attributes;
            }

            if (auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN"))) {
                admin auser = adminRe.findByrollno(rollno);
                attributes.put("admin", auser);
                log.info("Admin {} authorized successfully.", rollno);
                return attributes;
            }
        }

        log.error("Authorization failed for {}.", rollno);
        return null;
    }

    /**
     * Saves a new query to the pending queries list of the super admin.
     * 
     * @param q A map containing query details such as name, description, and room ID.
     * @return An integer status code:
     *         1, if the query was successfully saved;
     *         -3, if any other unexpected error occurs or if the super admin
     *         record is missing.
     */
    public int saveQuery(Map<String, String> q) {
        Map<String, ?> activity = processQuery(q, 1);
        try {
            superAdmin suser = sadminRe.findById(1).get();
            query temp = (query) activity.get("query");
            if (temp == null) {
                log.warn("Query for {} not processed successfully.", q.get("Roomid"));
                return (int) activity.get("error");
            }
            suser.getPendingQueries().add(temp);
            sadminRe.save(suser);
            log.info("Query for {} saved successfully.", q.get("Roomid"));
            return 1;
        } catch (Exception e) {
            log.error("Internal server error for {} :", q.get("Roomid"), e); 
            return -3;
        }
    }

    /**
     * Saves a new query to the pending queries list of the admin.
     * 
     * @param q      A map containing query details such as name, description, and
     *               room ID.
     * @param rollno The roll number of the user whose query need to be saved.
     * @return An integer status code:
     *         1, if the query was successfully saved;
     *         -3, if any other unexpected error occurs or if the super admin
     *         record is missing.
     */
    public int saveQuery(Map<String, String> q, Integer rollno) {
        Map<String, ?> activity = processQuery(q, rollno);

        try {
            admin user = adminRe.findByrollno(rollno);
            if (user == null)
            {
                log.error("Admin {} not found.", rollno);
                return -3;
            }

            query temp = (query) activity.get("query");
            if (temp == null) {
                log.warn("Query for {} not processed successfully.", q.get("Roomid"));
                return (int) activity.get("error");
            }

            user.getPendingQueries().add(temp);
            rejectQueries(false, 0); // This will be applied later

            adminRe.save(user);
            log.info("Query for {} room raised by {} saved successfully.", q.get("Roomid"), rollno);
            return 1;
        } catch (Exception e) {
            log.error("Internal server error for room {} by {} :", q.get("Roomid"), rollno, e);
            return -3;
        }
    }

    /**
     * Downloads the search tool map based on the provided version number.
     * 
     * @param version The version number of the map currently held by the client.
     * @return A Pair<Integer, String> where:
     *         1, if an updated map is available;
     *         0, if the provided version matches the latest version (no update
     *         needed);
     *         -2, if no map is found in the database;
     *         -3, if an unexpected error occurs.
     */
    public Pair<Integer, String> downloadMap(int version) {
        try {
            List<toJSON> map = searchTool.findAll();
            int mapVer = -1;
            if (map != null) {
                mapVer = map.get(0).getMapVersion();
                log.info("Map version {} downloaded successfully.", mapVer);
                if (mapVer == version) {
                    return new Pair<Integer, String>(0, null);
                } else {
                    return new Pair<Integer, String>(1, map.get(0).getSearchTool());
                }
            } else {
                log.warn("No map found in the database, please contact super admin immediately.", mapVer);
                return new Pair<Integer, String>(-2, null);
            }
        } catch (Exception e) {
            log.error("Internal server error while updating the map version {}.", version, e);
            return new Pair<Integer, String>(-3, null);
        }
    }

}