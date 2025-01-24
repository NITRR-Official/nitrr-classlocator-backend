package com.classlocator.nitrr.services;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.classlocator.nitrr.interfaces.Pair;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Service
public class comService {
    // here isapproved, getAllQueries, saveQuery, authorization

    @Autowired
    protected adminRepo adminRe;

    @Autowired
    protected queryRepo queryR;

    @Autowired
    protected superAdminRepo sadminRe;

    @Autowired
    private searchToolRepo search;

    @Autowired
    protected jwtService jwt;

    @Autowired
    private toJSONRepo searchTool;

    @Autowired
    private AuthenticationManager authManager;

    protected static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // The rollBack/deleteTrash/recoverQueries/rejectQueries to be applied later

    public boolean rollBack(query q, boolean isAdmin, int rollno) {
        // Logic applied to rollback a query if that query is latest to that particular
        // room then reject that query
        // to trash
        return false;
    }

    public boolean deleteTrash(boolean isAdmin, int rollno) {
        // Logic is applied here to delete the already present trash by admin or
        // superadmin
        return false;
    }

    public boolean recoverQueries(boolean isAdmin, int rollno) {
        // Logic is applied here to recover the deleted queries
        return false;
    }

    public boolean rejectQueries(boolean isAdmin, int rollno) {
        // Logic to move the queries to trash is applied here
        return false;
    }

    private admin removePending(admin user, query q) {
        boolean removed = false;
        try {
            removed = user.getPendingQueries().removeIf(x -> x.getId().equals(q.getId()));
            if (removed) {
                return user;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    private superAdmin removePending(superAdmin user, query q) {
        boolean removed = false;
        try {
            removed = user.getPendingQueries().removeIf(x -> x.getId().equals(q.getId()));
            if (removed) {
                return user;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    // The below functionality is to be applied as soon as possible

    @SuppressWarnings("unchecked")
    private boolean generateMap() {
        try {
            // Initialize JSON Object
            JSONObject jsonOutput = new JSONObject();
            List<searchTool> rooms = search.findAll();

            // Getting whether map exist or not
            List<toJSON> mapp = searchTool.findAll();

            // Inserting the current version here into the json file
            int versions = 0;
            if (mapp != null) {
                versions = mapp.get(0).getMapVersion() + 1;
            }

            jsonOutput.put("version", versions);

            for (searchTool room : rooms) {
                List<Pair<String, Pair<String, String>>> dataArray = room.getData();

                if (dataArray == null || dataArray.isEmpty()) {
                    continue; // Skip if data array is missing or empty
                }

                String name = dataArray.get(dataArray.size() - 1).getValue().getKey();
                String details = dataArray.get(dataArray.size() - 1).getValue().getValue();

                // Add to JSON output
                JSONObject entry = new JSONObject();
                entry.put("name", name);
                entry.put("details", details);

                jsonOutput.put(room.getId().toString(), entry);
            }

            toJSON json = new toJSON();

            json.setMapVersion(versions);
            json.setSearchTool(jsonOutput.toJSONString());

            searchTool.save(json);
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    private query processQuery(query q, Integer s) {
        try {
            q.setRaisedBy(s.toString());

            // Date system
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = currentDate.format(formatter);
            q.setDate(formattedDate);
            return queryR.save(q);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
            return null;
        }
    }

    protected int updateSearchTool(query q, boolean isSuperAdmin) {
        // After approval this function will update the searchTool in mongodb and will
        // update the maps
        try {

            /*
             * 
             * First it will always check whether the searchTool exists or not for all 301
             * rooms,
             * if it exist then it will create new map, otherwise it will first fill all the
             * details
             * in mongodb using version 1 searchTool.json file created in 2022.
             * 
             */

            if (!(search.count() > 0)) {
                boolean status = searchTools();
                if (!status) {
                    return -1;
                }
            }

            Optional<searchTool> room = search.findById(q.getRoomid());
            searchTool temp;
            if (room.isPresent())
                temp = room.get();
            else
                temp = new searchTool();

            if (temp.getData() == null)
                temp.setData(new ArrayList<Pair<String, Pair<String, String>>>());
            Pair<String, String> t = new Pair<String, String>(q.getName(), q.getDescription());
            temp.getData().add(new Pair<String, Pair<String, String>>(q.getId().toString(), t));
            temp.setId(q.getRoomid());

            // Generate new map here and will save the new map to toJSON entity
            if (generateMap()) {

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

                int approvalDB = isSuperAdmin ? saveQuery(q, 1) : saveQuery(q, Integer.parseInt(q.getRaisedBy()), 1);
                if (approvalDB == -1) {
                    // rollback the generateMap() and return -1
                    return -1;
                }
                search.save(temp);
                return 1;
            } else
                return -1;
        } catch (Exception e) {
            System.out.println("Exception raised from update search tool: " + e.toString());
            return -1;
        }

    }

    public boolean searchTools() {
        // File path to your JSON file

        String relativePath = "src/main/resources/templates/searchTool.json";
        File file = Paths.get(relativePath).toFile();
        // String filePath = "D:\\Learn
        // Backend\\Classlocator-backend\\src\\main\\java\\com\\classlocator\\nitrr\\services\\template\\searchTool.json";

        try {
            // Parse the JSON file
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));

            // Loop through JSON and prepare MongoDB documents
            for (Object key : jsonObject.keySet()) {
                try {
                    String id = (String) key;
                    JSONObject valueObj = (JSONObject) jsonObject.get(id);
                    searchTool s = new searchTool();
                    s.setId(Integer.parseInt(id));

                    if (s.getData() == null)
                        s.setData(new ArrayList<Pair<String, Pair<String, String>>>());

                    Pair<String, String> p = new Pair<String, String>(valueObj.get("name").toString(),
                            valueObj.get("details").toString());
                    s.getData().add(new Pair<String, Pair<String, String>>("1", p));
                    search.save(s);
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e.toString());
                }
            }
            System.out.println("Data successfully inserted into MongoDB!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return false;
        }
    }

    public List<query> getAllQueries() {
        return queryR.findAll();
    }

    public Map<String, Object> authorization(Integer rollno, String password) {
        // This function will authorize whether the user is legit or not by comparing
        // passwords, authentication etc.

        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(rollno.toString(), password));
        
        System.out.println("Role: " + auth.getAuthorities().toString() + " "+ auth.isAuthenticated());

        Map<String, Object> attributes = new HashMap<String, Object>();
        if (auth.isAuthenticated()) {
            
            if (auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMIN"))) {
                admin auser = adminRe.findByrollno(rollno);
                attributes.put("admin", auser);
                return attributes;
            }  

            if (auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_SUPER_ADMIN"))) {
                superAdmin suser = sadminRe.findById(rollno).get();
                attributes.put("sadmin", suser);
                return attributes;
            }
        }

        if(auth.isAuthenticated()) attributes.put("Forbidden", HttpStatus.FORBIDDEN);
        else attributes.put("Unauthorized", HttpStatus.UNAUTHORIZED);
        return attributes;
    }

    public List<query> Queries(Integer rollno, Integer type) {
        try {
            admin a = adminRe.findByrollno(rollno);
            if (type == 1)
                return a.getPendingQueries();
            else
                return a.getAcceptedQueries();
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<query>();
        }
    }

    public List<query> Queries(Integer type) {
        try {
            // Logic required to get all sadmin queries
            return new ArrayList<>();
            // if(type == 1)
            // return a.getPendingQueries();
            // else
            // return a.getAcceptedQueries();
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<query>();
        }
    }

    public int saveQuery(query q, Integer status) {
        try {
            // admin user = adminRe.findByrollno(s);
            System.out.println(q.toString());
            List<superAdmin> suser = sadminRe.findAll();
            if (suser.isEmpty())
                return 0;

            // Getting the admin data and setting the arraylist and updating it...

            query temp = processQuery(q, 1);
            if (temp != null) {
                for (superAdmin ele : suser) {
                    if (temp != null && status == 0) {
                        ele.getPendingQueries().add(temp);
                    } else if (temp != null && status == 1) {
                        ele = removePending(ele, q);
                        if (ele != null)
                            ele.getAcceptedQueries().add(temp);
                        else
                            return -1;
                    }
                    sadminRe.save(ele);
                }
            } else
                return -1;
            return 1;
        } catch (Exception e) {
            System.out.print(e.toString());
            return -1;
        }
    }

    public int saveQuery(query q, Integer s, Integer status) {
        try {
            admin user = adminRe.findByrollno(s);

            if (user == null)
                return 0;

            // Getting the admin data and setting the arraylist and updating it...
            query temp = processQuery(q, s);
            if (temp != null && status == 0)
                user.getPendingQueries().add(temp);
            else if (temp != null && status == 1) {
                // First we need to move the query from pending queries to accepted queries
                user = removePending(user, q);
                if (user != null)
                    user.getAcceptedQueries().add(temp);
                else
                    return -1;
            } else if (temp != null && status == -1) {
                rejectQueries(false, 0);
            } else
                return -1;
            adminRe.save(user);
            return 1;
        } catch (Exception e) {
            System.out.print(e.toString());
            return -1;
        }
    }

    public Pair<Integer, String> downloadMap(int version) {
        try {
            List<toJSON> map = searchTool.findAll();
            int mapVer = -1;
            if (map != null) {
                mapVer = map.get(0).getMapVersion();
                if (mapVer > version) {
                    return new Pair<Integer, String>(1, map.get(0).getSearchTool());
                }
                return new Pair<Integer, String>(0, null);
            } else
                return new Pair<Integer, String>(-1, null);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
            return new Pair<Integer, String>(-1, null);
        }
        // if(version > )
    }

}
