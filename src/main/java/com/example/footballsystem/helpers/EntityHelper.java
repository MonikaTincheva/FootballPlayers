package com.example.footballsystem.helpers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityHelper {

    public static boolean validateName(String name) {
        return name.trim().length() > 3 && name.trim().length() <= 30;
    }

    public static boolean validatePosition(String position) {
        return !position.trim().isEmpty() && position.trim().length() <= 2;
    }

    public static boolean validateTeamNumber(int number) {
        return number >= 1 && number <= 99;
    }

    public static boolean validateGroup(String group) {
        return group.trim().length() == 1 && Character.isLetter(group.trim().charAt(0));
    }

    public static boolean validateMinutes(int minutes) {
        return minutes >= 0 && minutes <= 90;
    }


    public static boolean isValidScore(String result) {
        String regex = "^\\d{1,2}-\\d{1,2}$";

        List<String> regexList = List.of(

                "^\\d{1,2}\\(\\d{1,2}\\)-\\d{1,2}\\(\\d{1,2}\\)$",
                "^\\d{1,2}\\(\\d{1,2}\\)-\\d{1,2}$",
                "^\\d{1,2}-\\d{1,2}\\(\\d{1,2}\\)$"
        );
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);

        if (matcher.matches()) {
            return true;
        } else {
            for (String reg : regexList) {
                pattern = Pattern.compile(reg);
                matcher = pattern.matcher(result);
                if ((matcher.matches())) {
                    String[] parts = result.split("-");
                    String leftPart = parts[0];
                    String rightPart = parts[1];
                    int leftScore = Integer.parseInt(leftPart.replaceAll("\\(\\d+\\)", "").trim());
                    int rightScore = Integer.parseInt(rightPart.replaceAll("\\(\\d+\\)", "").trim());
                    return leftScore == rightScore;
                }

            }
            return false;
        }
    }
}