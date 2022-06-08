package de.biofid.spnhc;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.hucompute.textimager.uima.util.XmlFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.texttechnologylab.annotation.AnnotationComment;
import org.texttechnologylab.annotation.AnnotatorMetaData;
import org.texttechnologylab.annotation.type.Taxon;
import org.texttechnologylab.utilities.helper.StringUtils;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;

public class Methods {
    public static Configuration cf = Configuration.getDefaultConfiguration();

    public static void init() throws IOException {

        enableCORS("*", "GET, POST, PUT, DELETE", "Access-Control-Request-Headers,Access-Control-Allow-Headers");

        NLP processNLP = new NLP();

        cf.setDirectoryForTemplateLoading(new File("./src/main/resources"));

        get("/", "text/html", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>(0);
            try {

                attributes.put("result", "");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return new ModelAndView(attributes, "workshop.ftl");
            }

        }, new FreeMarkerEngine(cf));

        post("/", "multipart/form-data", (request, response) -> {

            JCas pCas = null;

            Map<String, Object> attributes = new HashMap<>(0);
            try {

                request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp/"));
                Part filePart = request.raw().getPart("uploadedFile");

                try (InputStream inputStream = filePart.getInputStream()) {
                    OutputStream outputStream = new FileOutputStream("/tmp/" + filePart.getSubmittedFileName());
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                }

                String sContent = StringUtils.getContent(new File("/tmp/" + filePart.getSubmittedFileName()));


                pCas = processNLP.process(sContent, NLP.getSpacy(), NLP.getHeidelTimeBioFID(), NLP.getGeoNames(), NLP.getGNFinder());

                Set<AnnotatorMetaData> amSet = new HashSet<>();
                JCasUtil.select(pCas, AnnotatorMetaData.class).stream().forEach(am -> {
                    amSet.add(am);
                });

                amSet.forEach(am -> {
                    am.removeFromIndexes();
                });

                JSONArray taxonArray = new JSONArray();

                JCas finalPCas = pCas;
                JCasUtil.select(finalPCas, Taxon.class).forEach(t->{

                    JSONObject taxonObject = new JSONObject();
                    taxonObject.put("begin", t.getBegin());
                    taxonObject.put("end", t.getEnd());
                    taxonObject.put("identifier", t.getIdentifier());

                    JCasUtil.select(finalPCas, AnnotationComment.class).stream().filter(ac->{
                        return ac.getReference().equals(t);
                    })
                    .filter(ac->{
                        return ac.getKey().equalsIgnoreCase("gnfinder_verification");
                    })
                    .forEach(ac->{
                        taxonObject.put("comment", ac.getValue());
                    });

                    JCasUtil.selectCovering(finalPCas, Sentence.class, t).forEach(s->{
                        taxonObject.put("sentence", s.getCoveredText());
                    });

                    taxonArray.put(taxonObject);

                });

                attributes.put("result", XmlFormatter.getPrettyString(pCas));
                attributes.put("taxon", taxonArray);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return new ModelAndView(attributes, "workshop.ftl");
            }

        }, new FreeMarkerEngine(cf));


    }


    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            //response.type("application/json");
        });
    }

}
