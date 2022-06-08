package de.biofid.spnhc;

import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.unihd.dbs.uima.annotator.heideltime.biofid.HeidelTimeBioFID;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.hucompute.textimager.uima.geonames.gazetteer.GeonamesGazetteer;
import org.hucompute.textimager.uima.gnfinder.GNfinder;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.texttechnologylab.annotation.GeoNamesEntity;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

public class NLP {

    AnalysisEngine pEngine = null;

    public NLP() {

    }

    public static AnalysisEngineDescription getSpacy() throws ResourceInitializationException {

        return createEngineDescription(SpaCyMultiTagger3.class,
                SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://huaxal.hucompute.org:8103",
                SpaCyMultiTagger3.PARAM_MAX_TEXT_WINDOW, 90000);

    }

    public static AnalysisEngineDescription getGNFinder(String sGNPath) throws ResourceInitializationException {

        return createEngineDescription(GNfinder.class,
                GNfinder.PARAM_ONLY_VERIFICATION, true,
                GNfinder.PARAM_BIN_GNFINDER, sGNPath
        );

    }

    public static AnalysisEngineDescription getSegmenter() throws ResourceInitializationException {

        return createEngineDescription(BreakIteratorSegmenter.class,
                BreakIteratorSegmenter.PARAM_WRITE_SENTENCE, true,
                BreakIteratorSegmenter.PARAM_WRITE_TOKEN, true
        );

    }

    public static AnalysisEngineDescription getGeoNames() throws ResourceInitializationException {

        return createEngineDescription(createEngineDescription(GeonamesGazetteer.class,
                        GeonamesGazetteer.PARAM_TAGGING_TYPE_NAME, GeoNamesEntity.class.getName(),
                        GeonamesGazetteer.PARAM_USE_LOWERCASE, false,
                        GeonamesGazetteer.PARAM_USE_STRING_TREE, true,
                        GeonamesGazetteer.PARAM_USE_SENTECE_LEVEL_TAGGING, true,
                        GeonamesGazetteer.PARAM_USE_LEMMATA, true,
                        GeonamesGazetteer.PARAM_NO_SKIPGRAMS, true,
                        GeonamesGazetteer.PARAM_ADD_ABBREVIATED_TAXA, false,
                        GeonamesGazetteer.PARAM_GET_ALL_SKIPS, false,
                        GeonamesGazetteer.PARAM_MIN_LENGTH, 1,
                        GeonamesGazetteer.PARAM_SPLIT_HYPEN, false,
                        GeonamesGazetteer.PARAM_ANNOTATION_COMMENTS, new String[]{ "ttlab_model", "ttlab_geonames_v_1.0.1" })
                );

    }

    public static AnalysisEngineDescription getGNFinder() throws ResourceInitializationException {

        String sGNPath = NLP.class.getClassLoader().getResource("gnfinder").getPath();
        return getGNFinder(sGNPath);

    }

    public static AnalysisEngineDescription getHeidelTimeBioFID() throws ResourceInitializationException {
        return createEngineDescription(HeidelTimeBioFID.class);
    }

    public synchronized JCas process(JCas pCas, AnalysisEngineDescription... engines) throws ResourceInitializationException, AnalysisEngineProcessException {

        AggregateBuilder pipeline = new AggregateBuilder();

        for (AnalysisEngineDescription engine : engines) {
            pipeline.add(engine);
        }

        if(pEngine==null){
            pEngine = pipeline.createAggregate();
        }

        SimplePipeline.runPipeline(pCas.getCas(), pEngine);
        return pCas;

    }

    public synchronized JCas process(String sText, AnalysisEngineDescription... engines) throws UIMAException {

        JCas pCas = JCasFactory.createText(sText, "de");

        return process(pCas, engines);

    }


}
