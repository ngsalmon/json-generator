package com.metistream.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by nathansalmon on 3/7/16.
 */
public class Index {
    // Commit within 5 minutes (means it can take up to 5 minutes or a little
    // more for changes to be visible to searches via the UI).
    private static final int COMMIT_WITHIN_MS = 1000;

    private final CloudSolrClient solrClient;

    public Index(String zkServer, String collection) {
        solrClient = new CloudSolrClient(zkServer);
        solrClient.setDefaultCollection(collection);
    }

    public void add(List<String> documents) throws Exception {
        try {
            solrClient.add(getSolrDocuments(documents), COMMIT_WITHIN_MS);
        } catch (SolrServerException | IOException e) {
            System.out.println("Unable to add message:");
            e.printStackTrace();
        }
    }

    public static Collection<SolrInputDocument> getSolrDocuments(List<String> jsonDocuments) throws Exception {
        List<SolrInputDocument> docs = new ArrayList<>();
        for (String msg : jsonDocuments) {
            SolrInputDocument document = new SolrInputDocument();
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON string to Map
            Map<String, Object> map = mapper.readValue(msg, new TypeReference<Map<String, String>>(){});

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();
                document.addField(field, value);
            }
            docs.add(document);
        }
        return docs;
    }
}
