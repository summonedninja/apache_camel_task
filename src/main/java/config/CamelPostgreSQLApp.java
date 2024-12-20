package config;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import task.endpoint.PostgreSQLEndpoint;


public class CamelPostgreSQLApp {
    public static void main(String[] args) throws Exception {

        InitDataBase.initializeDatabase();
        InitDataBase.populateDatabase();
        CamelContext camelContext = new DefaultCamelContext();

        PostgreSQLEndpoint endpoint = new PostgreSQLEndpoint();
        endpoint.setUrl("jdbc:postgresql://localhost:5432/apache_camel_example");
        endpoint.setUsername("postgres");
        endpoint.setPassword("CODERJAVA");
        camelContext.getRegistry().bind("customEndpoint", endpoint);


        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("timer://foo?period=5000")
                        .to("customEndpoint")
                        .onException(Exception.class)
                        .handled(true)
                        .log("Error occurred: ${exception.message}")
                        .end()
                        .process(exchange -> {
                            System.out.println("Data from DB: " + exchange.getIn().getBody(String.class));
                        });
            }
        });
        camelContext.start();
        Thread.sleep(30000);
        camelContext.stop();
    }
}
