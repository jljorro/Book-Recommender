package es.ucm.gaia.services;

import es.ucm.gaia.cbr.BookCBRApplication;
import es.ucm.gaia.cbr.BookConnector;
import es.ucm.gaia.cbr.BookDescription;
import org.apache.logging.log4j.LogManager;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import ucm.gaia.jcolibri.exception.ExecutionException;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Una clase para probar el funcionamiento de los Servlets con Jersey.
 *
 * @author Jose L. Jorro-Aragoneses
 * @version 1.0
 */
@Path("")
public class RecommenderServices {

    @GET @Path("/recommendation")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<RetrievalResult> getRecommendation(
            @QueryParam("yearOfPublication") int yearOfPublication,
            @QueryParam("author") String author) {

        Collection<RetrievalResult> recommendation = getRecommendationCBR(yearOfPublication, author);

        return recommendation;
    }

    @POST @Path("/addItem")
    @Produces(MediaType.TEXT_PLAIN)
    public String addItem(
            @QueryParam("isbn") long isbn,
            @QueryParam("title") String title,
            @QueryParam("author") String author,
            @QueryParam("yearOfPublication") int yearOfPublication) {

        // Preparamos la query
        BookDescription desc = new BookDescription();
        desc.setIsbn(isbn);
        desc.setTitle(title);
        desc.setYearOfPublication(yearOfPublication);
        desc.setAuthor(author);

        CBRCase c = new CBRCase();
        c.setDescription(desc);

        Collection<CBRCase> insert = new ArrayList<>();
        insert.add(c);

        BookConnector connector = BookConnector.getInstance();
        connector.storeCases(insert);

        return "Item saved correctly";
    }

    @GET @Path("/form")
    @Produces(MediaType.TEXT_HTML)
    public String getForm() {

        String result = "<form action=\"./recommendation/html\">" +
                "<label for=\"author\">author</label>\n" +
                "<input type=\"text\" name=\"author\">\n" +
                "<label for=\"yearOfPublication\">yearOfPublication</label>\n" +
                "<input type=\"number\" name=\"yearOfPublication\">"+
                "<input type=\"submit\" value=\"Submit\">" +
                "</form>";

        return result;
    }

    @GET @Path("/recommendation/html")
    @Produces(MediaType.TEXT_HTML)
    public String getRecommendationHTML(
            @QueryParam("yearOfPublication") int yearOfPublication,
            @QueryParam("author") String author){

        Collection<RetrievalResult> recommendation = getRecommendationCBR(yearOfPublication, author);
        String strResult = "";

        if (recommendation != null) {

            strResult = "<table>";
            strResult += "<tr><th>isbn</th>";
            strResult += "<th>title</th>";
            strResult += "<th>author</th>";
            strResult += "<th>yearOfPublication</th>";
            strResult += "<th>similarity</th></tr>";

            strResult += recommendation.stream()
                            .map(RecommenderServices::getRow)
                            .collect(Collectors.joining());

            strResult += "</table>";

        }

        LogManager.getLogger().info(strResult);

        return strResult;

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {
        return "Book recommender services works correctly!";
    }

    class RecommendationResponse {

        private Collection<RetrievalResult> result;

        public RecommendationResponse(Collection<RetrievalResult> result) {
            this.result = result;
        }

    }

    private Collection<RetrievalResult> getRecommendationCBR(int yearOfPublication, String author) {

        // Preparamos la query
        BookDescription desc = new BookDescription();
        desc.setYearOfPublication(yearOfPublication);
        desc.setAuthor(author);

        CBRQuery query = new CBRQuery();
        query.setDescription(desc);

        // Llamamos al sistema recomendador
        BookCBRApplication cbrApp = BookCBRApplication.getInstance();
        Collection<RetrievalResult> recommendation = null;
        try {

            cbrApp.configure();

            cbrApp.preCycle();

            cbrApp.cycle(query);

            cbrApp.postCycle();

            // Leemos el resultado
            recommendation = cbrApp.getResult();

        } catch (ExecutionException e) {
            LogManager.getLogger().error(e);
        }

        return recommendation;
    }

    private static String getRow(RetrievalResult r) {
        String result = "<tr>";
        BookDescription desc = (BookDescription) r.get_case().getDescription();

        result += "<td>" + desc.getIsbn() + "</td>";
        result += "<td>" + desc.getTitle() + "</td>";
        result += "<td>" + desc.getAuthor() + "</td>";
        result += "<td>" + desc.getYearOfPublication() + "</td>";
        result += "<td>" + r.getEval() + "</td>";
        result += "</tr>";

        return result;
    }

}
