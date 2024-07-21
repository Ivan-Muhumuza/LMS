package org.example.lms.util;

import java.util.Random;

public class PatronIDGenerator {
    private static final Random random = new Random();

    public static long generateUniqueID() {
        return 1_000_000_000L + random.nextLong(9_000_000_000L); // Generates a unique 14-digit number
    }
}


