package com.example.demo.controller;
import java.util.*;

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
@RestController
@RequestMapping(value = "/teams")
@Service
@Api(value = "Team Service", tags = "Team Service")
public class TeamServiceController {

    @Autowired
    RestTemplate restTemplate;
    private static final Map<Integer, Team> teamData = new HashMap<Integer, Team>() {


        {
            put(1,  new Team(1, "Team A", Arrays.asList(new Player(1, "Player 1"), new Player(2, "Player 2"))));
            put(2,  new Team(2, "Team B", Arrays.asList(new Player(1, "Player 1"), new Player(2, "Player 2"))));

        }

    };
    @ApiOperation(value = "Get a team by ID", response = Team.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved team by ID"),
            @ApiResponse(code = 404, message = "Team not found")
    })
    @GetMapping(value = "/{teamId}")
    public Team getTeamById(@PathVariable int teamId) {
        System.out.println("Getting Team by id " + teamId);
        Team team = teamData.get(teamId);
        if (team == null) {
            team = new Team(0, "N/A", Collections.emptyList());
        }
        return team;
    }
    @ApiOperation(value = "Create a new team", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Team created successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Player not found")
    })
    @HystrixCommand(fallbackMethod = "postTeamFallback")
    @PostMapping
    public String postTeam(@RequestBody Team team) {
        System.out.println("Posting Team " + team);
        if(team.getId() == null || team.getName() == null || team.getPlayers() == null  ){
            return  "Fail\nYour body should be like\n{id = 1, name = name, players:[ id = 1, name = name ]}";
        }else {
            boolean allPlayersExist = true;
            int playerNotExistID = 0;
            String msg = "Success";
            for (Player player : team.getPlayers()) {
                ResponseEntity<Player> response = restTemplate.exchange("http://playerService/players/" + player.getId(),
                        HttpMethod.GET, null, Player.class);
                Player player1 = response.getBody();
                if (player1 == null || player1.getId() == 0 ) {
                    allPlayersExist = false;
                    playerNotExistID = player.getId();
                }
                assert player1 != null;
                System.out.println("response Player " + player1.getName());
            }
            System.out.println("allPlayersExist:  " + allPlayersExist);
            if (allPlayersExist){
                Team t = teamData.get(team.getId());
                if (t != null) {
                    msg = "ID already exist";
                    return msg;
                } else {
                    teamData.put(team.getId(), team);
                    return msg;
                }
            }else{
                msg = "The Player with Id "+ playerNotExistID+ " Doesn't exist";
                return msg;
            }


        }

    }
    @SuppressWarnings("unused")
    public String postTeamFallback(Team team) {
        return "Circuit is open or a fallback situation occurred. The team creation failed.";
    }




    @ApiOperation(value = "Update a team by ID", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Team updated successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Team not found")
    })
    @PutMapping("/{id}")
    public String updateTeam(@PathVariable int id, @RequestBody Team updateTeam) {
        System.out.println("Updating by id " + id);

        String msg = "Success";
        Team team = teamData.get(id);
        if(team.getId() == null || team.getName() == null || team.getPlayers() == null ){
            return  "Fail! \n Your body should be like\n  {name = name, players:[]}";
        }else{
            teamData.remove(id);
            teamData.put(id, new Team(id, updateTeam.getName(),updateTeam.getPlayers()));
            return msg;
        }
    }

    @ApiOperation(value = "Delete a team by ID", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Team deleted successfully"),
            @ApiResponse(code = 404, message = "Team not found")
    })
    @DeleteMapping("/{id}")
    public String deleteTeam(@PathVariable Integer id){
        if (teamData.get(id) == null) {
            return "Team not found.";
        }else{
            teamData.remove(id);
            return "Success";
        }
    }





    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}