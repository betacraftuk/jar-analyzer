package uk.betacraft.jaranalyzer;

import java.io.File;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.regex.Pattern;
/**
 * Taken from https://stackoverflow.com/a/48946762
 */
public final class FilenameComparator implements Comparator<File> {
    private static final Pattern NUMBERS = 
            Pattern.compile("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

    @Override public final int compare(File o1, File o2) {
        // Optional "NULLS LAST" semantics:
        if (o1 == null || o2 == null)
            return o1 == null ? o2 == null ? 0 : -1 : 1;

        String name1 = o1.getName();
        String name2 = o2.getName();

        // Fix Notch's inconsistent versioning breaking order... TODO: find a better way of doing this if possible
        if ((name1.equals("b1.0.jar") && name2.contains("b1.0_01.jar"))
                || (name1.equals("b1.7.jar") && name2.contains("b1.7_01.jar")))
            return -1;
        else if ((name2.equals("b1.0.jar") && name1.contains("b1.0_01.jar"))
                || (name2.equals("b1.7.jar") && name1.contains("b1.7_01.jar")))
            return 1;
        else if ((name1.equals("b1.0_01.jar") && name2.contains("b1.0.2.jar"))
                || (name1.equals("b1.7_01.jar") && name2.contains("b1.7.2.jar"))
                || (name1.equals("b1.7_01.jar") && name2.contains("b1.7.3.jar")))
            return -1;
        else if ((name2.equals("b1.0_01.jar") && name1.contains("b1.0.2.jar"))
                || (name2.equals("b1.7_01.jar") && name1.contains("b1.7.2.jar"))
                || (name2.equals("b1.7_01.jar") && name1.contains("b1.7.3.jar")))
            return 1;

        // Splitting both input strings by the above patterns
        String[] split1 = NUMBERS.split(name1);
        String[] split2 = NUMBERS.split(name2);

        for (int i = 0; i < Math.min(split1.length, split2.length); i++) {
            char c1 = split1[i].charAt(0);
            char c2 = split2[i].charAt(0);
            int cmp = 0;

            // If both segments start with a digit, sort them numerically using 
            // BigInteger to stay safe
            if (c1 >= '0' && c1 <= '9' && c2 >= '0' && c2 <= '9')
                cmp = new BigInteger(split1[i]).compareTo(new BigInteger(split2[i]));

            // Place 1.x.jar before 1.x.y.jar
            if (c1 == '.' && (split2[i].equals(".jar") || split2[i].equals(".exe") || split2[i].equals(".zip")))
                cmp = 1;
            else if (c2 == '.' && (split1[i].equals(".jar") || split1[i].equals(".exe") || split1[i].equals(".zip")))
                cmp = -1;

            // If we haven't sorted numerically before, or if numeric sorting yielded 
            // equality (e.g 007 and 7) then sort lexicographically
            else if (cmp == 0)
                cmp = split1[i].compareTo(split2[i]);

            // Abort once some prefix has unequal ordering
            if (cmp != 0)
                return cmp;
        }

        // If we reach this, then both strings have equally ordered prefixes, but 
        // maybe one string is longer than the other (i.e. has more segments)
        return split1.length - split2.length;
    }
}
