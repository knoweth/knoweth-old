package com.github.knoweth.client.services;

import com.github.knoweth.common.data.Document;
import org.teavm.flavour.rest.Resource;

import javax.ws.rs.*;
import java.util.List;

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
}
