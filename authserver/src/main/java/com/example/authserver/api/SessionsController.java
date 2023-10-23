package com.example.authserver.api;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SessionsController {

    @Autowired
    SessionRegistry sessionRegistry;

    @RequestMapping("/sessionsx")
    public List<SInfo> getSessions(){

        return sessionRegistry.getAllPrincipals().stream()
                .flatMap(p -> sessionRegistry.getAllSessions(p, true).stream())
                .map(sessionInformation -> new SInfo(sessionInformation.getSessionId(),((User) sessionInformation.getPrincipal()).getUsername(), sessionInformation.isExpired() ))
                .collect(Collectors.toList());

    }

    public record SInfo(String id, String userName, boolean expired){

    }

}
