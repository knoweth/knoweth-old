package com.github.knoweth.client.services;

import com.github.knoweth.common.data.Document;
import org.teavm.flavour.rest.Resource;

import javax.ws.rs.*;
import java.util.List;

/**
 * REST interface for server-side storage of documents.
 *
 * See the server code (StorageController) for method documentation.
 */
@Path("storage")
@Resource
public interface StorageService {

    @GET
    @Path("docs")
    List<Document> getDocuments();

    @POST
    @Path("docs")
    void createDocument(Document doc);

    @GET
    @Path("docs/{id}")
    Document getDocument(@PathParam("id") int id);

    @PUT
    @Path("docs/{id}")
    void setDocument(@PathParam("id") int id, Document newDoc);

    @DELETE
    @Path("docs/{id}")
    void deleteDocument(@PathParam("id") long id);

    @POST
    @Path("docs/{id}/share/{username}")
    void shareDocument(@PathParam("id") long id, @PathParam("username") String username);

    @DELETE
    @Path("docs/{id}/share/{username}")
    void unshareDocument(@PathParam("id") long id, @PathParam("username") String username);
}
