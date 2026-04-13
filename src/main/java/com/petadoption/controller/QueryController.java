package com.petadoption.controller;

import com.petadoption.dto.QueryDTO;
import com.petadoption.model.Query;
import com.petadoption.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queries")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @PostMapping("/ask")
    public ResponseEntity<Query> ask(@RequestBody QueryDTO queryDTO) {
        return new ResponseEntity<>(queryService.askQuery(queryDTO), HttpStatus.CREATED);
    }

    @PostMapping("/respond")
    public ResponseEntity<Query> respond(@RequestParam Long questionId, @RequestParam String answer) {
        return ResponseEntity.ok(queryService.respondQuery(questionId, answer));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(queryService.getAllQueries());
    }
}
