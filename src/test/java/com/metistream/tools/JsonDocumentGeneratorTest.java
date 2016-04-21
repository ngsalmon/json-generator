package com.metistream.tools;

import org.junit.Test;

/**
 * Created by nathansalmon on 4/20/16.
 */
public class JsonDocumentGeneratorTest {
    @Test
    public void testGeneration() {
        JsonDocumentGenerator.generateDocuments(50000);
    }

}
