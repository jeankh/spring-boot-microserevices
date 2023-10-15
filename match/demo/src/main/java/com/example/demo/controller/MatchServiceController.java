package com.example.demo.controller;

import com.example.demo.beans.Match;
import com.example.demo.beans.Player;
import com.example.demo.beans.Team;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Api(value = "MatchController", description = "REST APIs related to Match Entity")
@RestController
@Service
@RequestMapping("/matches")
public class MatchServiceController {
    @Autowired
    RestTemplate restTemplate;
    private static final Map<Integer, Match> matchData = new HashMap<Integer, Match>() {
        {
            // Create teams with players
            Team team1 = new Team(1, "Team A", Arrays.asList(new Player(1, "Player 1"), new Player(2, "Player 2")));
            Team team2 = new Team(2, "Team B", Arrays.asList(new Player(3, "Player 3"), new Player(4, "Player 4")));

            // Create and add matches to the map
            put(1, new Match(1, "MatchA", team1, team2));
            put(2, new Match(2, "MatchB", team1, team2));
            put(3, new Match(3, "MatchC", team1, team2));
        }
    };

    @ApiOperation(value = "Get a match by ID", response = Match.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Match retrieved successfully"),
            @ApiResponse(code = 404, message = "Match not found")
    })
    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable int id) {
        try {
            Match match = matchData.get(id);
            if (match == null) {
                match = new Match(0, "N/A", null, null);
            }
            return match;
        }catch (Exception e){
            System.out.println("Exception in getMatchById "+e);
            return new Match(0, "N/A", null, null);
        }

    }

    @ApiOperation(value = "Add a new match", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Match added successfully"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 409, message = "Match with the same ID already exists")
    })
    @HystrixCommand(fallbackMethod = "postMatchFallback")
    @PostMapping
    public String addMatch(@RequestBody Match match) {
        if (match.getId() == null || match.getName() == null || match.getTeam1() == null || match.getTeam2() == null) {
            return "Fail\nYour request should include an ID, name, team1, and team2.";
        } else {
            // Check if team1 and team2 exist
            boolean team1Exists = checkTeamExist(match.getTeam1());
            boolean team2Exists = checkTeamExist(match.getTeam2());

            if (!team1Exists || !team2Exists) {
                return "One or both of the teams do not exist.";
            }

            if (matchData.containsKey(match.getId())) {
                return "Match with the same ID already exists.";
            } else {
                //craate a new instance of match and put it in the map to run the constructor and randomly choose the winner
                Match newMatch = new Match(match.getId(), match.getName(), match.getTeam1(), match.getTeam2());
                matchData.put(match.getId(), newMatch);
                return "Success";
            }
        }
    }
    @SuppressWarnings("unused")
    public String postMatchFallback(Match match) {
        return "Circuit is open or a fallback situation occurred. The match creation failed.";
    }
    private boolean checkTeamExist(Team team) {
        ResponseEntity<Team> response = restTemplate.exchange(
                "http://teamService/teams/" + team.getId(),
                HttpMethod.GET,
                null,
                Team.class
        );
        Team team1 = response.getBody();
        if (team1 == null || team1.getId() == 0) {
            return false;
        }
        return true;
    }


    @ApiOperation(value = "Update a match", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Match updated successfully"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "Match not found")
    })
    @PutMapping("/{id}")
    public String  updateMatch(@PathVariable int id, @RequestBody Match updatedMatch) {
        if (matchData.get(id) != null) {
            if (updatedMatch.getName()==null || updatedMatch.getTeam1()==null || updatedMatch.getTeam2()==null){
                return "Fail\nYour request should include an ID, name, team1, and team2.";
            }
            Match matchToUpdate = new Match(id, updatedMatch.getName(), updatedMatch.getTeam1(), updatedMatch.getTeam2());
            matchData.remove(id);
            matchData.put(id, matchToUpdate);
            return "Success!";
        } else {
            return "Match not found.";
        }
    }

    @ApiOperation(value = "Delete a match", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Match deleted successfully"),
            @ApiResponse(code = 404, message = "Match not found")
    })
    @DeleteMapping("/{id}")
    public String deleteMatch(@PathVariable Integer id) {
        if (matchData.get(id) == null) {
            return "Match not found.";
        }
        matchData.remove(id);
        return "Success!";
    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}