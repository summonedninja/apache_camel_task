package task.endpoint;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.support.DefaultEndpoint;
import producer.PostgreSQLProducer;

@UriEndpoint(scheme = "postgresql",
        syntax = "postgresql://username:password@host:port/database",
        title = "PostgreSQL",
        producerOnly = true)
public class PostgreSQLEndpoint extends DefaultEndpoint {

    @UriParam
    private String username;
    @UriParam
    private String password;
    @UriParam
    private String url;

    public PostgreSQLEndpoint() {
        super();
    }

    @Override
    public Producer createProducer() throws Exception {
        return new PostgreSQLProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Consumer is not supported");
    }

    @Override
    public String getEndpointUri() {
        return String.format("postgresql://%s:%s@%s", username, password, url);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
