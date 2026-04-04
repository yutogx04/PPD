package com.codequest;

import com.codequest.model.dto.SubmissionResponse;
import com.codequest.model.Badge;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class SubmissionResponseTest {

    @Test
    public void hasBadges_withBadges_returnsTrue() {
        SubmissionResponse response = new SubmissionResponse();
        Badge badge = new Badge();
        badge.setName("Test Badge");
        response.setBadgesUnlocked(Collections.singletonList(badge));

        assertTrue(response.hasBadges());
    }

    @Test
    public void hasBadges_emptyBadges_returnsFalse() {
        SubmissionResponse response = new SubmissionResponse();
        response.setBadgesUnlocked(Collections.emptyList());

        assertFalse(response.hasBadges());
    }

    @Test
    public void hasBadges_nullBadges_returnsFalse() {
        SubmissionResponse response = new SubmissionResponse();
        response.setBadgesUnlocked(null);

        assertFalse(response.hasBadges());
    }

    @Test
    public void isAccepted_AcceptedString_returnsTrue() {
        SubmissionResponse response = new SubmissionResponse();
        response.setStatus("Accepted");
        
        assertTrue(response.isAccepted());
    }

    @Test
    public void isAccepted_WrongString_returnsFalse() {
        SubmissionResponse response = new SubmissionResponse();
        response.setStatus("Failed");
        
        assertFalse(response.isAccepted());
    }

    @Test
    public void properties_gettersAndSetters() {
        SubmissionResponse response = new SubmissionResponse();
        response.setScore(95);
        response.setGrade("S");
        response.setXpGained(30);
        response.setBonusXp(15);
        response.setTestCasesPassed(5);
        response.setTestCasesTotal(5);

        assertEquals(95, response.getScore());
        assertEquals("S", response.getGrade());
        assertEquals(30, response.getXpGained());
        assertEquals(15, response.getBonusXp());
        assertEquals(5, response.getTestCasesPassed());
        assertEquals(5, response.getTestCasesTotal());
    }
}
