package com.ats.prompt_service.validation;

import java.util.regex.Pattern;

public final class PromptContentSafety {

    private static final Pattern UNSAFE_INSTRUCTION_PATTERN = Pattern.compile(
            "(?i)\\b(ignore|disregard)\\s+(all\\s+)?(previous|prior|above)\\s+instructions\\b"
                    + "|\\breveal\\s+(the\\s+)?(system|developer)\\s+prompt\\b"
                    + "|\\bbypass\\s+(safety|security|guardrails)\\b"
                    + "|\\bjailbreak\\b"
                    + "|\\bdeveloper\\s+mode\\b"
                    + "|\\bDAN\\b"
    );

    private PromptContentSafety() {
    }

    public static boolean isPotentiallyVulnerable(String content) {
        return content != null && UNSAFE_INSTRUCTION_PATTERN.matcher(content).find();
    }
}
