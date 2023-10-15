package com.example.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Api(value = "StatsServiceController", description = "REST APIs related to Stats Service")
@RestController
@RequestMapping("/stats")
public class StatsServiceController {
    @Autowired
    RestTemplate restTemplate;

    @ApiOperation(value = "Get the number of matches won by a team", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Count of matches won retrieved successfully"),
            @ApiResponse(code = 404, message = "Team not found"),
    })
    @GetMapping("/team-stats/{teamId}")
    public int getTeamStats(@PathVariable int teamId) {
        ResponseEntity<Integer> response = restTemplate.exchange(
                "http://matchService/matches/team/" + teamId,
                HttpMethod.GET,
                null,
                Integer.class
        );
        return response.getBody();
    }

    @ApiOperation(value = "Get the number of goals scored by a player", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Number of goals scored retrieved successfully"),
            @ApiResponse(code = 404, message = "Player not found"),
    })
    @GetMapping("/player-stats/{playerId}")
    public int getPlayerStats(@PathVariable int playerId) {
        ResponseEntity<Integer> response = restTemplate.exchange(
                "http://matchService/matches/player/" + playerId,
                HttpMethod.GET,
                null,
                Integer.class
        );
        return response.getBody();
    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
