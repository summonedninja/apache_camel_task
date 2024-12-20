package producer;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;
import task.endpoint.PostgreSQLEndpoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgreSQLProducer extends DefaultProducer {
    private final PostgreSQLEndpoint endpoint;

    public PostgreSQLProducer(PostgreSQLEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String url = endpoint.getUrl();
        String username = endpoint.getUsername();
        String password = endpoint.getPassword();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM employees ")) {
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                result.append("ID:").append(resultSet.getInt("id"))
                        .append(", Name:").append(resultSet.getString("name"))
                        .append(", Age: ").append(resultSet.getString("email"))
                        .append(".\n");
            }
            exchange.getIn().setBody(result.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error while processing exchange", e);
        }
    }
}
