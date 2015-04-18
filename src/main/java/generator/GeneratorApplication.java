package generator;

import generator.config.CommonsCLIPropertySource;
import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class GeneratorApplication {

    public static void main(String[] args) throws ParseException {
       // final CommandLine commandLine = buildCommandLine(args);

        SpringApplication app = new SpringApplication(GeneratorApplication.class);

        app.setWebEnvironment(false);

       /* app.addListeners(new ApplicationListener<ApplicationEnvironmentPreparedEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
                event.getEnvironment().getPropertySources().addLast(new CommonsCLIPropertySource(commandLine));
            }
        });*/

        app.run(args);
    }

    private static CommandLine buildCommandLine(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption("b", "build", false, "build database, tables and load data");
        options.addOption("q", "query", false, "execute queries");
        options.addOption("c", "count", false, "when executing queries, count the records returned from the query");
        options.addOption("i", "iterations", true, "number of times the query will be executed");

        options.addOption(OptionBuilder.withLongOpt("test.name").withDescription("short name or description of test being run").hasArg().withArgName("NAME").create());
        options.addOption(OptionBuilder.withLongOpt("hdfs.data.path").withDescription("path to where the data should be stored on HDFS").hasArg().withArgName("PATH").create());
        options.addOption(OptionBuilder.withLongOpt("local.data.path").withDescription("path to local data to be uploaded to HDFS and used by build scripts ").hasArg().withArgName("PATH").create());
        options.addOption(OptionBuilder.withLongOpt("hive.db.name").withDescription("name of database to create in Hive ").hasArg().withArgName("NAME").create());

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <Hive Test Harness Jar>", options);

        CommandLineParser parser = new BasicParser();

        return parser.parse(options, args);

    }
}
