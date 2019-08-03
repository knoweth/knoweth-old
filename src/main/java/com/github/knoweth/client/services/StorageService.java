package com.github.knoweth.client.services;

import com.github.knoweth.common.data.Document;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("storage")
public interface StorageService {

    @GET
    @Path("docs")
    List<Document> getDocuments(@QueryParam("a") int a);
}
