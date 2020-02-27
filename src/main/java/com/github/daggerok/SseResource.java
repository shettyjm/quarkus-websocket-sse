package com.github.daggerok;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * TODO...
 *
 * see: https://github.com/volkaert/events-sse-quarkus/blob/master/broker/src/main/java/fr/volkaert/events/sse/quarkus/broker/BrokerController.java
 * see: https://github.com/volkaert/events-sse-quarkus/blob/master/subscriber/src/main/java/fr/volkaert/events/sse/quarkus/subscriber/SubscriberController.java
 */
@Path("/sse")
public class SseResource {

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }
}
