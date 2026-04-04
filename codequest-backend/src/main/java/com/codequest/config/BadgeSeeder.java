package com.codequest.config;

import com.codequest.entity.Badge;
import com.codequest.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @RequiredArgsConstructor @Slf4j
public class BadgeSeeder {

    private final BadgeRepository badgeRepository;

    public void seed() {
        List<Badge> badges = List.of(
            Badge.builder().name("Premier Pas").nameEn("First Step").description("Compléter votre première leçon").descriptionEn("Complete your first lesson").icon("badge_first_lesson").conditionType(Badge.ConditionType.TOTAL_LESSONS).conditionValue(1).build(),
            Badge.builder().name("Challenger").nameEn("Challenger").description("Résoudre votre premier défi").descriptionEn("Solve your first challenge").icon("badge_first_challenge").conditionType(Badge.ConditionType.TOTAL_CHALLENGES).conditionValue(1).build(),
            Badge.builder().name("Perfectionniste").nameEn("Perfectionist").description("Réussir un défi du premier coup").descriptionEn("Pass a challenge on the first try").icon("badge_first_attempt").conditionType(Badge.ConditionType.FIRST_ATTEMPT_SUCCESS).conditionValue(1).build(),
            Badge.builder().name("En Série").nameEn("On a Roll").description("Maintenir un streak de 7 jours").descriptionEn("Maintain a 7-day streak").icon("badge_streak_7").conditionType(Badge.ConditionType.STREAK_DAYS).conditionValue(7).build(),
            Badge.builder().name("Inarrêtable").nameEn("Unstoppable").description("Maintenir un streak de 30 jours").descriptionEn("Maintain a 30-day streak").icon("badge_streak_30").conditionType(Badge.ConditionType.STREAK_DAYS).conditionValue(30).build(),
            Badge.builder().name("Décathlète").nameEn("Decathlete").description("Résoudre 10 défis").descriptionEn("Solve 10 challenges").icon("badge_10_challenges").conditionType(Badge.ConditionType.TOTAL_CHALLENGES).conditionValue(10).build(),
            Badge.builder().name("Polyglotte").nameEn("Polyglot").description("Compléter 2 parcours différents").descriptionEn("Complete 2 different tracks").icon("badge_2_tracks").conditionType(Badge.ConditionType.TOTAL_TRACKS).conditionValue(2).build(),
            Badge.builder().name("Noctambule").nameEn("Night Owl").description("Soumettre un défi entre minuit et 5h").descriptionEn("Submit a challenge between midnight and 5am").icon("badge_night").conditionType(Badge.ConditionType.NIGHT_SUBMIT).conditionValue(1).build(),
            Badge.builder().name("Pythoniste").nameEn("Pythonista").description("Compléter tout le parcours Python").descriptionEn("Complete the entire Python track").icon("badge_python_complete").conditionType(Badge.ConditionType.TRACK_COMPLETE).conditionValue(1).build(),
            Badge.builder().name("Maître du Code").nameEn("Code Master").description("Atteindre le niveau 6").descriptionEn("Reach level 6").icon("badge_level_6").conditionType(Badge.ConditionType.ACHIEVE_LEVEL).conditionValue(6).build()
        );
        badgeRepository.saveAll(badges);
        log.info("Seeded {} badges", badges.size());
    }
}
