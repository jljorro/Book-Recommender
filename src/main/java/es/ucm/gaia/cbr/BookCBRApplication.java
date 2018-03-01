package es.ucm.gaia.cbr;

import ucm.gaia.jcolibri.casebase.LinealCaseBase;
import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;
import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import ucm.gaia.jcolibri.cbrcore.Connector;
import ucm.gaia.jcolibri.exception.ExecutionException;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;
import ucm.gaia.jcolibri.method.retrieve.selection.SelectCases;

import java.util.Collection;

/**
 * Implementation of a CBR Recommender system of books.
 *
 * @author Jose L. Jorro-Aragoneses
 * @version 1.0
 */
public class BookCBRApplication implements StandardCBRApplication {

    private static BookCBRApplication instance = null;

    private BookCBRApplication() {}

    public static BookCBRApplication getInstance() {
        if (instance == null)
            instance = new BookCBRApplication();

        return instance;
    }

    private Connector connector;
    private CBRCaseBase caseBase;
    private Collection<RetrievalResult> result;

    @Override
    public void configure() throws ExecutionException {
        connector = BookConnector.getInstance();
        caseBase = new LinealCaseBase();
    }

    @Override
    public CBRCaseBase preCycle() throws ExecutionException {
        caseBase.init(connector);
        return caseBase;
    }


    @Override
    public void cycle(CBRQuery cbrQuery) throws ExecutionException {

        NNConfig config = new NNConfig();

        config.setDescriptionSimFunction(new Average());

        config.addMapping(new Attribute("yearOfPublication", BookDescription.class), new Interval(2012));
        config.addMapping(new Attribute("author", BookDescription.class), new Equal());

        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), cbrQuery, config);

        result = SelectCases.selectTopKRR(eval, 5);
    }

    @Override
    public void postCycle() throws ExecutionException {
        caseBase.close();
    }

    public Collection<RetrievalResult> getResult() {
        return result;
    }
}
