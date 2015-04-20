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
       final CommandLine commandLine = buildCommandLine(args);

        SpringApplication app = new SpringApplication(GeneratorApplication.class);

        app.setWebEnvironment(false);

        app.addListeners(new ApplicationListener<ApplicationEnvironmentPreparedEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
                event.getEnvironment().getPropertySources().addLast(new CommonsCLIPropertySource(commandLine));
            }
        });

        app.run(args);
    }

    private static CommandLine buildCommandLine(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption(buildOption("h", true, "Hostname of generated image.  Required!", true));
        options.addOption(buildOption("n", true, "Name of cluster.  Required!", true));
        options.addOption(buildOption("ip", true, "IP address of generated image", false));
        options.addOption(buildOption("ram", true, "RAM allocated to generated image (in megabytes)", false));
        options.addOption(buildOption("rs", true, "Reserved system memory (in megabytes)", false));
        options.addOption(buildOption("rh", true, "Reserved HBase memory (in megabytes)", false));
        options.addOption(buildOption("min", true, "Minimum container size memory (in megabytes)", false));
        options.addOption(buildOption("cpu", true, "# of cpus of generated image", false));
        options.addOption(buildOption("update", false, "Update YUM libraries while building image", false));
        options.addOption(buildOption("b", true, "Name of blueprint used to build cluster", false));
        options.addOption(buildOption("d", true, "# of disks in generated image", false));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <HDP Vagrant Generator>", options);

        CommandLineParser parser = new BasicParser();

        return parser.parse(options, args, true);

    }

    private static Option buildOption(String option, boolean hasArg, String description, boolean required) {
        Option opt = new Option(option, hasArg, description);
        opt.setRequired(required);
        return opt;
    }
}
