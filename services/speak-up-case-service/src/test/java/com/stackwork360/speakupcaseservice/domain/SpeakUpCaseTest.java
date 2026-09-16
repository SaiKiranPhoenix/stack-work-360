package com.stackwork360.speakupcaseservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SpeakUpCaseTest {
    @Test
    void preventsReporterIdentityFromBeingAssignedAsInvestigator() {
        SpeakUpCase speakUpCase = speakUpCase();

        assertThrows(IllegalStateException.class, () -> speakUpCase.assignInvestigator("reporter-hash-1"));
    }

    @Test
    void requiresInvestigatorBeforeInvestigationStarts() {
        SpeakUpCase speakUpCase = speakUpCase();

        assertThrows(IllegalStateException.class, () -> speakUpCase.moveTo(SpeakUpCaseStatus.INVESTIGATING));
    }

    @Test
    void assignsInvestigatorAndMovesThroughStatusWorkflow() {
        SpeakUpCase speakUpCase = speakUpCase();

        speakUpCase.assignInvestigator("investigator-1");
        speakUpCase.moveTo(SpeakUpCaseStatus.INVESTIGATING);
        speakUpCase.moveTo(SpeakUpCaseStatus.RESOLVED);

        assertEquals(SpeakUpCaseStatus.RESOLVED, speakUpCase.status());
        assertEquals("investigator-1", speakUpCase.assignedInvestigatorId());
    }

    private static SpeakUpCase speakUpCase() {
        return new SpeakUpCase(
                null,
                "tenant-1",
                "reporter-hash-1",
                SpeakUpCaseCategory.ETHICS,
                "Conflict of interest",
                "Anonymous report details",
                SpeakUpCaseStatus.SUBMITTED,
                null,
                false,
                null,
                null,
                null
        );
    }
}
