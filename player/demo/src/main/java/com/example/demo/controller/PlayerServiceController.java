package com.example.demo.controller;

import com.example.demo.beans.Player;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.Map;

@Api(value = "PlayerServiceController", description = "REST APIs for Player Entity")
@RestController
@RequestMapping("/players")
public class PlayerServiceController {
    private static final Map<Integer, Player> playerData = new HashMap<Integer, Player>() {
        {
            put(1, new Player(1, "Player1"));
            put(2, new Player(2, "Player2"));
        }
    };

    @ApiOperation(value = "Get a player by ID", response = Player.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved player"),
            @ApiResponse(code = 404, message = "Player not found")
    })

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable int id) {
        Player player = playerData.get(id);
        if (player == null) {
            player = new Player(0, "N/A");
        }
        return player;
    }


    @ApiOperation(value = "Add a new player", response = Player.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Player successfully added")
    })
    @PostMapping
    public Player addPlayer(@RequestBody Player player) {
        playerData.put(player.getId(), player);
        return player;
    }

    @ApiOperation(value = "Update a player by ID", response = Player.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Player successfully updated"),
            @ApiResponse(code = 404, message = "Player not found")
    })
    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable int id, @RequestBody Player updatedPlayer) {
        Player existingPlayer = playerData.get(id);
        if (existingPlayer != null) {
            existingPlayer.setName(updatedPlayer.getName());
            return existingPlayer;
        } else {
            return new Player(0, "N/A");
        }
    }

    @ApiOperation(value = "Delete a player by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Player successfully deleted"),
            @ApiResponse(code = 404, message = "Player not found")
    })
    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable int id) {
        playerData.remove(id);
    }
}
