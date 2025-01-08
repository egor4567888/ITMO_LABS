package com.example.controller;

import com.example.dto.PointDTO;
import com.example.dto.ResponseDTO;
import com.example.service.PointService;
import org.springframework.http.ResponseEntity; 
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/points")
public class PointController {
    private final PointService pointService;
    private static final Logger logger = LoggerFactory.getLogger(PointController.class);

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<PointDTO>> addPoint(@RequestBody PointDTO req) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user == null) {
            return ResponseEntity.badRequest().body(new ResponseDTO<>(null, "Unauthorized"));
        }
        ResponseDTO<PointDTO> added = pointService.addPoint(req, user);
        return ResponseEntity.ok(added);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<PointDTO>>> getAllPoints() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user == null) {
            return ResponseEntity.ok(new ResponseDTO<>(List.of(), "No user"));
        }
        ResponseDTO<List<PointDTO>> result = pointService.getAllPoints(user);
        return ResponseEntity.ok(result);
    }
}