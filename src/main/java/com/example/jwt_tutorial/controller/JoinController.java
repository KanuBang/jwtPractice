package com.example.jwt_tutorial.controller;

import com.example.jwt_tutorial.dto.JoinDTO;
import com.example.jwt_tutorial.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String join(@RequestBody  JoinDTO joinDTO) {
        System.out.println(joinDTO.getUsername());
        joinService.joinProcess(joinDTO);
        return "ok";
    }
}
