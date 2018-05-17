package com.fleamarket.core.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleamarket.core.service.LetterService;
import com.fleamarket.core.util.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Log4j2
@RestController
@RequestMapping("letter")
public class LetterController {
    private final LetterService letterService;

    public LetterController(LetterService letterService) {
        this.letterService = letterService;
    }

    @GetMapping("sessions")
    public Object getSessionList(HttpSession session){
        Set<Map<String, Object>> sessionList = letterService.selectSessionList(Utils.getUserSession(session).getId());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(sessionList);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }
}
