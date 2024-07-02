package uk.betacraft.jaranalyzer;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

public class JarAnalyzer {
    // assuming UTC by default
    private static final String tzID = System.getProperty("tz", "UTC");
    private static TimeZone tz = TimeZone.getTimeZone(tzID);

    public static void main(String[] args) {
        try {
            File f = new File(args.length == 0 ? "" : args[0]);
            if (args.length != 0 && !f.exists())
                System.exit(0);

            analyze(f);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static boolean isJarZip(File f) {
        return !f.isDirectory() && (f.getName().toLowerCase().endsWith(".jar") || f.getName().toLowerCase().endsWith(".zip"));
    }

    public static void analyze(File folder) {
        if (isJarZip(folder)) {
            printInfoJar(folder);
        } else if (folder.isDirectory()) {
            System.out.println(" ========= FOLDER: " + folder.getName());

            File[] files = folder.listFiles();

            for (File file : files) {
                if (file.isDirectory()) {
                    analyze(file);
                } else {
                    printInfoJar(file);
                }
            }

            System.out.println(" ========= END: " + folder.getName());
        }
    }

    public static final DateTimeFormatter sourceDateFormat = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of(tzID));
    public static final DateTimeFormatter targetDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'").withZone(ZoneId.of("UTC"));

    public static void printInfoJar(File f) {
        try {
            Instant latestTime = null;
            JarFile jf = new JarFile(f);
            Enumeration<JarEntry> entries = jf.entries();

            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                FileTime jarEntryLastModTime = jarEntry.getLastModifiedTime();

                Instant instant = jarEntryLastModTime.toInstant();

                // we need to adjust the time for the user's timezone... Java is really bad.
                int offset_user = TimeZone.getDefault().getOffset(jarEntryLastModTime.toMillis());
                instant = instant.plusMillis(offset_user);

                // adjust the time for the timezone the jar was built in
                int offset = tz.getOffset(jarEntryLastModTime.toMillis());
                instant = instant.minusMillis(offset);

                if (latestTime == null) {
                    latestTime = instant;
                } else if (instant.isAfter(latestTime)) {
                    latestTime = instant;
                }
            }

            jf.close();
            String relativePath = URLDecoder.decode((new File("")).toURI().relativize(f.toURI()).toString(), "UTF-8");

            String formattedDateTime = " | " + Long.toString(latestTime.toEpochMilli()) + " | " + targetDateFormat.format(LocalDateTime.ofInstant(latestTime, ZoneId.of("UTC")));

            System.out.printf("%-60.60s  %-60.60s%n", relativePath, formattedDateTime);

        } catch (ZipException zipException) {
            // happens on 1.9+ jars - avoid it as there's nothing that can be done
            if ("zip END header not found".equals(zipException.getMessage()))
                return;

            zipException.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
