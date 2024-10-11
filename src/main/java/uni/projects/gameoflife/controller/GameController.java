package uni.projects.gameoflife.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uni.projects.gameoflife.model.Grid;
import uni.projects.gameoflife.service.GameService;

import java.io.IOException;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Grid grid = gameService.processJsonFile(file);
        return grid.toJson();
    }

    @PostMapping("/next-generation")
    public String getNextGeneration(@RequestBody String gridJson) throws IOException {
        Grid currentGrid = Grid.fromJson(gridJson);
        Grid nextGrid = gameService.computeNextGeneration(currentGrid);
        return nextGrid.toJson();
    }

}
