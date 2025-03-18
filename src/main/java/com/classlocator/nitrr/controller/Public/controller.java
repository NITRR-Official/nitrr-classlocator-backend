package com.classlocator.nitrr.controller.Public;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.searchTool;
import com.classlocator.nitrr.interfaces.Pair;
import com.classlocator.nitrr.services.adminService;
import com.classlocator.nitrr.services.jwtService;

@RestController
public class controller {

    @Autowired
    private adminService admins;

    @Autowired
    protected jwtService jwt;

    /**
     * Populates search tools document to searchTool collection and returns the
     * appropriate response.
     * Requires authentication and access to super admin only.
     * @return ResponseEntity<String> - Returns "Generated successfully..." with
     *         HTTP status 201 (CREATED) if successful, otherwise returns
     *         "Map not generated" with HTTP status 501 (NOT_IMPLEMENTED).
     */
    @GetMapping("/generate")
    public ResponseEntity<String> generate() {

        if (admins.searchToolsGenerator()) {
            return new ResponseEntity<>("Generated successfully...", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Map not generated", HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Populates a JSON document to toJSON collection and returns the appropriate
     * response.
     * Requires authentication and access to super admin only.
     * @return ResponseEntity<String> - Returns "JSON File Generated..." with
     *         HTTP status 201 (CREATED) if successful, otherwise returns
     *         "Something went wrong..." with HTTP status 500
     *         (INTERNAL_SERVER_ERROR).
     */
    @GetMapping("/map")
    public ResponseEntity<String> map() {
        if (admins.generateMap(new searchTool())) {
            return new ResponseEntity<>("JSON File Generated...", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles the download request for a specific version of the map.
     * No authentication required.
     * @param version The version number of the map to be downloaded.
     * @return ResponseEntity<?> - Returns the map file as a string with
     *         HTTP status 200 (OK) if successful. If the latest version
     *         is already present, it returns "Already the latest version."
     *         with HTTP status 200. Other failure cases return appropriate
     *         messages with HTTP status 501 (NOT_IMPLEMENTED) or 500
     *         (INTERNAL_SERVER_ERROR).
     */
    @GetMapping("/download/{version}")
    public ResponseEntity<?> map(@PathVariable Integer version) {
        Pair<Integer, String> download = admins.downloadMap(version);
        if (download.getKey() == 1) {
            return new ResponseEntity<>(download.getValue(), HttpStatus.OK);
        } else if (download.getKey() == 0)
            return new ResponseEntity<>("Already the latest version.", HttpStatus.OK);
        else if (download.getKey() == -1)
            return new ResponseEntity<>(download.getValue(), HttpStatus.OK);
        else if (download.getKey() == -2)
            return new ResponseEntity<>("Map Update Error", HttpStatus.NOT_IMPLEMENTED);
        return new ResponseEntity<>("Something went wrong, and we couldn't update.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Endpoint to check if the setup is running.
     * No authentication required.
     * @return ResponseEntity<String> - Returns a confirmation message
     *         "Yeah..., the setup is running..." with HTTP status 200 (OK).
     */
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return new ResponseEntity<>("Yeah..., the setup is running...", HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve all queries for the authenticated user.
     * List all queries if unauthenticated, otherwise list queries for the authenticated user.
     * @return ResponseEntity<?> - Returns a list of queries retrieved
     *         based on the authenticated user's name with HTTP status 200 (OK).
     */
    @GetMapping("/getAllQueries")
    public ResponseEntity<?> getAllQueries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(admins.getAllQueries(authentication.getName()), HttpStatus.OK);
    }

}
