package com.example.demo.controller;

import com.example.demo.beans.PlayerStats;
import com.example.demo.beans.TeamStats;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Api(value = "StatsServiceController", description = "REST APIs related to Stats Service")
@RestController
@RequestMapping("/stats")
public class StatsServiceController {
    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    @ApiOperation(value = "Get the number of matches won by a team", response = TeamStats.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Team stats retrieved successfully"),
            @ApiResponse(code = 404, message = "Team not found")
    })
    @GetMapping("/team-stats/{teamId}")
    @HystrixCommand(fallbackMethod = "getTeamStatsFallback")
    public TeamStats getTeamStats(@PathVariable int teamId) {
        ResponseEntity<TeamStats> response = restTemplate.exchange(
                "http://matchService/matches/team/" + teamId,
                HttpMethod.GET,
                null,
                TeamStats.class
        );

        TeamStats teamStats = response.getBody();
        System.out.println("Response body: " + teamStats);

        if (teamStats == null) {
            teamStats = new TeamStats(0, -1);
        }

        return teamStats;
    }

    public TeamStats getTeamStatsFallback(int teamId) {
        // This is the fallback method to handle cases where the request fails or there is no response.
        return new TeamStats(teamId, -2);
    }





    @ApiOperation(value = "Get the number of goals scored by a player", response = PlayerStats.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Player stats retrieved successfully"),
            @ApiResponse(code = 404, message = "Player not found"),
    })
    @GetMapping("/player-stats/{playerId}")
    @HystrixCommand(fallbackMethod = "getPlayerStatsFallback")
    public PlayerStats getPlayerStats(@PathVariable int playerId) {
        ResponseEntity<PlayerStats> response = restTemplate.exchange(
                "http://matchService/matches/player/" + playerId,
                HttpMethod.GET,
                null,
                PlayerStats.class
        );
        PlayerStats playerStats = response.getBody();

        if (playerStats == null) {
            playerStats = new PlayerStats(playerId, -1);
        }

        return playerStats;
    }

    public PlayerStats getPlayerStatsFallback(int playerId) {
        // This is the fallback method to handle cases where the request fails or there is no response.
        return new PlayerStats(playerId, -2);
    }


    @RequestMapping("/ribbon")
    public String hi() {
        String randomString = this.restTemplate.getForObject("http://matchService/matches/backend", String.class);
        return "Server Response :: " + randomString;
    }


}
