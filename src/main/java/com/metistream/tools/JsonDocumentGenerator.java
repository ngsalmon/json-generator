package com.metistream.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by nathansalmon on 3/7/16.
 */
public class JsonDocumentGenerator {
    public static Random rand = new Random();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    static ObjectMapper mapper = new ObjectMapper();
    static Index index;

    public static void main(String[] args) {
        JsonDocumentGenerator generator = new JsonDocumentGenerator();
        //System.out.println(generator.generateDocuments(5));
        try {
            index.add(generator.generateDocuments(100000));
        } catch (Exception e) {
            System.out.println("Error posting generated documents to Solr:");
            e.printStackTrace();
        }
        System.exit(0);
    }

    JsonDocumentGenerator() {
        index = new Index("localhost:9983", "hcp");
    }

    public static List<String> generateDocuments(int numDocuments) {
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
        return getLinearRandomNumber(range, 2);
    }

    private static int random(int range, int slope) {
        return getLinearRandomNumber(range, slope);
    }

    private static int randBetween(int start, int end) {
        return start + rand.nextInt(end - start + 1);
    }

    private static LocalDateTime randomDateTime(int startYear) {
        return LocalDateTime.now().minus(rand.nextInt(365 * (2016-startYear)), ChronoUnit.DAYS);
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

    private static String randomFlintICD10Code(int zipcode, LocalDateTime date) {
        // flint zip code range: 48501 - 48550
        // Z77.011 - lead exposure
        // T56.0X1A Toxic effect of lead and its compounds, accidental (unintentional), initial encounter
        // T56.0X1D Toxic effect of lead and its compounds, accidental (unintentional), subsequent encounter
        // T56.0X1S Toxic effect of lead and its compounds, accidental (unintentional), sequela
        int distance = Math.abs(48525-zipcode) - 25;  // max distance ~1000
        distance = distance <= 0 ? 1 : distance;
        int daysInPast = Math.abs((int) ChronoUnit.DAYS.between(LocalDateTime.now(), date));

        if (rand.nextInt(distance) < 5 && rand.nextInt(daysInPast <= 0 ? 1 : daysInPast) < 50) {
            int option = random(5, random(5)) - 1;
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
        }
        return randomICD10Code();
    }

    private static String randomGender() {
        String[] genders = new String[] {"M", "F", "M", "F", "M", "F", "M", "F", "M", "F", "M", "F", "M", "F",
                "M", "F","M", "F","M", "F","M", "F","M", "F","M", "F","M", "F","M", "F","M", "F","M", "F", "U"};
        return genders[rand.nextInt(genders.length)];
    }

    private static String randomEthnicity() {
        String[] ethnicities = new String[] {
                "White (Caucasian)",
                "African American",
                "Native American",
                "Asian American",
                "Pacific Islander",
                "Middle Eastern",
                "Other race",
                "Two or more races"
        };
        return ethnicities[random(ethnicities.length, randBetween(2, 10)) - 1];
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
        return languages[random(languages.length, randBetween(2, 50)) - 1];
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
        return maritalCodes[random(maritalCodes.length, randBetween(2, 3)) - 1];
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
        return religions[random(religions.length, randBetween(2, 20)) - 1];
    }

    private static DemographicsDocument randomDoc() {
        int zipcode = getGaussian(48200, 1000);
        LocalDateTime ingestDate = randomDateTime(2014);
        DemographicsDocument doc = new DemographicsDocument();
        doc.setId(UUID.randomUUID().toString());
        doc.setBirthDateTime(formatter.format(randomDateTime(1900)));
        doc.setBirthplace(Integer.toString(randBetween(48000, 49999)));
        doc.setEthnicity(randomEthnicity());
        doc.setGender(randomGender());
        doc.setIngestDate(formatter.format(ingestDate));
        doc.setLanguage(randomLanguage());
        doc.setMaritalCode(randomMaritalCode());
        doc.setName(randomString());
        doc.setPhone(randomPhoneNumber());
        doc.setReligion(randomReligion());
        doc.setCondition(randomFlintICD10Code(zipcode, ingestDate));
        doc.setPostalCode(zipcode);

        return doc;
    }

    public static int getLinearRandomNumber(int maxSize, int slope) {
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
        double gaussian = rand.nextGaussian();
        return (int) (Math.pow(gaussian, 2) * stdDev + mean);
    }
}
