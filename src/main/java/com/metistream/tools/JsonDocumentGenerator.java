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
        //return rand.nextInt(range);
        return getLinnearRandomNumber(range, 2);
    }

    private static int random(int range, int slope) {
        //return rand.nextInt(range);
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

    private static DemographicsDocument randomDoc() {
        DemographicsDocument doc = new DemographicsDocument();
        doc.setId(UUID.randomUUID().toString());
        doc.setBirthDateTime(formatter.format(randomDateTime(1900)));
        doc.setBirthplace("place " + random(1000));
        doc.setEthnicity("ethnicity " + random(20));
        doc.setGender("gender " + random(5));
        doc.setIngestDate(formatter.format(randomDateTime(2015)));
        doc.setLanguage("language " + random(50));
        doc.setMaritalCode("marital code " + random(5));
        doc.setName(randomString());
        doc.setPhone(randomPhoneNumber());
        doc.setRace("race " + random(10));
        doc.setReligion("religion " + random(20));
        doc.setCondition("condition " + random(100, 5));

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
