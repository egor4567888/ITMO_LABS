package com.example.service;

import com.example.dto.PointDTO;
import com.example.dto.ResponseDTO;
import com.example.model.Point;
import com.example.repository.PointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class PointService {
    private final PointRepository pointRepository;
    private static final Logger logger = LoggerFactory.getLogger(PointService.class);

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public ResponseDTO<PointDTO> addPoint(PointDTO req, String owner) {
        boolean hit = checkHit(req.getX(), req.getY(), req.getR());
        Point point = new Point(req.getX(), req.getY(), req.getR(), hit, owner);
        Point saved = pointRepository.save(point);
        PointDTO dto = new PointDTO();
        dto.setX(saved.getX());
        dto.setY(saved.getY());
        dto.setR(saved.getR());
        dto.setHit(saved.isHit());
        dto.setCreatedAt(saved.getCreatedAt());
        return new ResponseDTO<>(dto, "Point added");
    }

    public ResponseDTO<List<PointDTO>> getAllPoints(String owner) {
        List<Point> points = pointRepository.findByOwner(owner);
        List<PointDTO> dtoList = points.stream().map(p -> {
            PointDTO dto = new PointDTO();
            dto.setX(p.getX());
            dto.setY(p.getY());
            dto.setR(p.getR());
            dto.setHit(p.isHit());
            dto.setCreatedAt(p.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
        return new ResponseDTO<>(dtoList, "Points retrieved");
    }

    private boolean checkHit(double x, double y, double r) {
        return (x >= 0 && y >= 0 && (x <= r && y<=r)) || (x <= 0 && y <= 0 && (x*x +y*y <= r*r/4)) || (x >= 0 && y <= 0 && (x-y <= r/2 ));
    }
}