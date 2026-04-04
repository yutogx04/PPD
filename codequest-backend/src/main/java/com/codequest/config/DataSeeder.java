package com.codequest.config;

import com.codequest.entity.AppSettings;
import com.codequest.entity.User;
import com.codequest.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private final TrackRepository trackRepository;
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final PythonTrackSeeder pythonTrackSeeder;
    private final JavaScriptTrackSeeder javaScriptTrackSeeder;
    private final JavaTrackSeeder javaTrackSeeder;
    private final BadgeSeeder badgeSeeder;
    private final AppSettingsRepository appSettingsRepository;

    @Override
    @Transactional
    public void run(String... args) {
        
        seedAdminUser();

        if (badgeRepository.count() == 0) {
            badgeSeeder.seed();
            log.info("Badges seeded.");
        }

        if (appSettingsRepository.count() == 0) {
            appSettingsRepository.save(AppSettings.builder().build());
            log.info("Default app settings seeded.");
        }

        if (trackRepository.count() > 0) {
            log.info("Tracks already seeded, skipping track seeding.");
            return;
        }

        log.info("Seeding tracks...");
        pythonTrackSeeder.seed();
        javaScriptTrackSeeder.seed();
        javaTrackSeeder.seed();
        log.info("Database seeding complete!");
    }

    private void seedAdminUser() {
        if (userRepository.findByEmail("admin@codequest.com").isPresent()) {
            log.info("Admin user already exists.");
            return;
        }
        userRepository.save(User.builder()
                .email("admin@codequest.com")
                .pseudo("Admin")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMIN)
                .emailVerified(true)
                .enabled(true)
                .xp(0)
                .level(1)
                .streak(0)
                .build());
        log.info("Seeded admin user: admin@codequest.com / admin123");
    }
}
