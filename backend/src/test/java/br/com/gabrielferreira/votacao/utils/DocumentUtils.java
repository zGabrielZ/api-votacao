package br.com.gabrielferreira.votacao.utils;

import java.util.concurrent.ThreadLocalRandom;

public final class DocumentUtils {

    private DocumentUtils() {}

    public static String generateCpf() {
        String baseDigits;

        do {
            baseDigits = "%09d".formatted(ThreadLocalRandom.current().nextInt(1_000_000_000));
        } while (hasRepeatedDigits(baseDigits));

        int firstCheckDigit = calculateCheckDigit(baseDigits, 10);
        String digitsWithFirstCheckDigit = baseDigits + firstCheckDigit;
        int secondCheckDigit = calculateCheckDigit(digitsWithFirstCheckDigit, 11);

        return digitsWithFirstCheckDigit + secondCheckDigit;
    }

    private static boolean hasRepeatedDigits(String digits) {
        return digits.chars().allMatch(digit -> digit == digits.charAt(0));
    }

    private static int calculateCheckDigit(String digits, int initialWeight) {
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * (initialWeight - i);
        }

        int remainder = (sum * 10) % 11;
        return remainder == 10 ? 0 : remainder;
    }
}
