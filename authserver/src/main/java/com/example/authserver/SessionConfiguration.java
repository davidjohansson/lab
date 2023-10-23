package com.example.authserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.AbstractSessionEvent;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableScheduling
public class SessionConfiguration {

    @Bean
    public SessionRegistry sessionRegistry(){
        return new CustomSessionRegistry();

    }


    public static class CustomSessionRegistry extends SessionRegistryImpl{

        @Scheduled(fixedDelay =10000)
        public void sch(){
            printAllSessions();


        }
        @Override
        public void onApplicationEvent(AbstractSessionEvent event) {
            System.out.println(event.toString());
            super.onApplicationEvent(event);
        }

        @Override
        public void removeSessionInformation(String sessionId) {
            System.out.println("REMOVING SESSION " + sessionId);

            printAllSessions();

            super.removeSessionInformation(sessionId);

            printAllSessions();

        }

        @Override
        public void registerNewSession(String sessionId, Object principal) {
            System.out.println("REG NEW SESSION");
            super.registerNewSession(sessionId, principal);

            printAllSessions();
        }

        private void printAllSessions() {
            System.out.println("SESSIONS: ");

            this.getAllPrincipals().stream()
                    .flatMap(p -> this.getAllSessions(p, true).stream())
                    .forEach(sessionInformation -> {
                        System.out.println("\t Id:\t %s".formatted(sessionInformation.getSessionId()));
                        System.out.println("\t Principal:\t %s".formatted(((User) sessionInformation.getPrincipal() ).getUsername()));
                        System.out.println("\t Expired:\t %s".formatted(sessionInformation.isExpired()));
                    });
        }
    }
}
