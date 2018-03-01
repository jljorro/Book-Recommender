package es.ucm.gaia.cbr;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CaseBaseFilter;
import ucm.gaia.jcolibri.cbrcore.Connector;
import ucm.gaia.jcolibri.exception.InitializingException;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of a Connector of CSB Books file.
 *
 * @author Jose L. Jorro-Aragoneses
 * @version 1.0
 */
public class BookConnector implements Connector{

    private static BookConnector instance = null;

    private BookConnector() {}

    public static BookConnector getInstance() {
        if (instance == null)
            instance = new BookConnector();

        return instance;
    }

    private Collection<CBRCase> cases;

    @Override
    public void initFromXMLfile(URL url) throws InitializingException {

    }

    @Override
    public void close() {

    }

    @Override
    public void storeCases(Collection<CBRCase> collection) {

    }

    @Override
    public void deleteCases(Collection<CBRCase> collection) {

    }

    @Override
    public Collection<CBRCase> retrieveAllCases() {
        List<String[]> data = getCSVRows("BX-Books.csv");

        cases = new ArrayList<>();

        data.stream()
            .forEach(d -> {
                BookDescription desc = new BookDescription(d);
                CBRCase c = new CBRCase();
                c.setDescription(desc);
                cases.add(c);
            });

        return cases;
    }

    @Override
    public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter caseBaseFilter) {
        return null;
    }

    private List<String[]> getCSVRows(String path) {
        InputStream f = getClass().getClassLoader().getResourceAsStream(path);

        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.getFormat().setDelimiter(';');

        CsvParser parser = new CsvParser(settings);

        List<String[]> result = parser.parseAll(f);

        return result;
    }

}
