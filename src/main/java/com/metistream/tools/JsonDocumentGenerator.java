package com.metistream.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by nathansalmon on 3/7/16.
 */
public class JsonDocumentGenerator {
    public static Random rand;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    static ObjectMapper mapper;
    static Index index;

    public static void main(String[] args) {
        JsonDocumentGenerator generator = new JsonDocumentGenerator();
        //System.out.println(generator.generateDocuments(5));
        try {
            index.add(generator.generateDocuments(10000));
        } catch (Exception e) {
            System.out.println("Error posting generated documents to Solr:");
            e.printStackTrace();
        }
        System.exit(0);
    }

    JsonDocumentGenerator() {
        rand = new Random();
        mapper = new ObjectMapper();
        index = new Index("localhost:9983", "hcp");
    }

    private List<String> generateDocuments(int numDocuments) {
        List<String> documents = new ArrayList<>();
        for (int i = 0; i < numDocuments; i++) {
            try {
                documents.add(mapper.writeValueAsString(randomDoc()));
            } catch (Exception e) {
                System.out.println("Error generating JSON:");
                e.printStackTrace();
            }
        }

        //return documents.size() > 1 ? "[" + String.join(",\n", documents) + "]" : documents.get(0);
        return documents;
    }

    private static int random(int range) {
        return getLinnearRandomNumber(range, 2);
    }

    private static int random(int range, int slope) {
        return getLinnearRandomNumber(range, slope);
    }

    private static int randBetween(int start, int end) {
        return start + rand.nextInt(end - start + 1);
    }

    private static LocalDateTime randomDateTime(int startYear) {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(startYear, 2015);
        gc.set(GregorianCalendar.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(GregorianCalendar.DAY_OF_YEAR, dayOfYear);
        gc.set(GregorianCalendar.HOUR_OF_DAY, randBetween(0, 23));
        gc.set(GregorianCalendar.MINUTE, randBetween(0, 59));
        gc.set(GregorianCalendar.SECOND, randBetween(0, 59));
        return LocalDateTime.from(gc.toZonedDateTime());
    }

    private static String randomPhoneNumber() {
        return "(" + randBetween(200, 999) + ") " + randBetween(100, 999) + "-" + randBetween(1000, 9999);
    }

    private static String randomString() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private static String randomICD10Code() {
        return RandomStringUtils.randomAlphabetic(1)
                + RandomStringUtils.randomNumeric(2)
                + "."
                + RandomStringUtils.randomNumeric(3);
    }

    private static String randomFlintICD10Code() {
        // Z77.011 - lead exposure
        // T56.0X1A Toxic effect of lead and its compounds, accidental (unintentional), initial encounter
        // T56.0X1D Toxic effect of lead and its compounds, accidental (unintentional), subsequent encounter
        // T56.0X1S Toxic effect of lead and its compounds, accidental (unintentional), sequela
        int option = random(5, random(5));
        switch (option) {
        case 0:
            return "Z77.011";
        case 1:
            return "T56.0X1A";
        case 2:
            return "T56.0X1D";
        case 3:
            return "T56.0X1S";
        case 4:
            return randomICD10Code();
        }
        return randomICD10Code();
    }

    private static String randomGender() {
        String[] genders = new String[] {"M", "F", "M", "F", "M", "F", "M", "F", "M", "F", "M", "F", "M", "F", "U"};
        return genders[rand.nextInt(genders.length)];
    }

    private static String randomEthnicity() {
        String[] ethnicities = new String[] {
                "White (Caucaian)",
                "African American",
                "Native American",
                "Asian American",
                "Pacific Islander,",
                "Middle Eastern",
                "Other race",
                "Two or more races"
        };
        return ethnicities[random(ethnicities.length, random(10))];
    }

    private static String randomLanguage() {
        String[] languages = new String[] {
                "English",
                "Spanish",
                "Chinese",
                "Tagalog",
                "German",
                "Korean",
                "French",
                "Vietnamese",
                "Italian",
                "Russian",
                "Arabic"
        };
        return languages[random(languages.length, randBetween(10, 50))];
    }

    private static String randomMaritalCode() {
        String[] maritalCodes = new String[] {
                "M",
                "S",
                "D",
                "W",
                "A",
                "U",
                "P",
                "X"
        };
        return maritalCodes[random(maritalCodes.length, randBetween(0, 2))];
    }

    private static String randomReligion() {
        String[] religions = new String[] {
                "Christianity", // (59.9% to 70.6%)
                "Unaffiliated", // (15.0% to 37.3%)
                "Judaism", // (1.2% to 2.2%)
                "Islam", // (0.6% to 0.9%)
                "Buddhism", // (0.5% to 0.9%)
                "Hinduism", // (0.4% to 0.7%)
                "Unitarian Universalism", // (0.3%)
                "Wicca/Paganism/Druidry" // (0.1%)
        };
        return religions[random(religions.length, randBetween(10, 50))];
    }

    private static DemographicsDocument randomDoc() {
        DemographicsDocument doc = new DemographicsDocument();
        doc.setId(UUID.randomUUID().toString());
        doc.setBirthDateTime(formatter.format(randomDateTime(1900)));
        doc.setBirthplace(Integer.toString(getGaussian(48550, 3)));
        doc.setEthnicity(randomEthnicity());
        doc.setGender(randomGender());
        doc.setIngestDate(formatter.format(randomDateTime(2015)));
        doc.setLanguage(randomLanguage());
        doc.setMaritalCode(randomMaritalCode());
        doc.setName(randomString());
        doc.setPhone(randomPhoneNumber());
        doc.setReligion(randomReligion());
        // doc.setRace("race" + random);
        //doc.setCondition("condition " + getGaussian(50, 5));
        doc.setCondition(randomFlintICD10Code());
        doc.setPostalCode(Integer.toString(getGaussian(48550, 2)));

        return doc;
    }

    public static int getLinnearRandomNumber(int maxSize, int slope) {
        //Get a linearly multiplied random number
        int randomMultiplier = maxSize * (maxSize + 1) / slope;
        int randomInt = rand.nextInt(randomMultiplier);

        //Linearly iterate through the possible values to find the correct one
        int linearRandomNumber = 0;
        for(int i=maxSize; randomInt >= 0; i--){
            randomInt -= i;
            linearRandomNumber++;
        }

        return linearRandomNumber;
    }

    public static int getGaussian(int mean, int stdDev) {
        return (int) rand.nextGaussian() * stdDev + mean;
    }
}
