package veil.hdp.vagrant.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import veil.hdp.vagrant.generator.model.Arguments;
import veil.hdp.vagrant.generator.service.DockerService;

@Component
@Profile("!test")
public class BuildDockerFile implements CommandLineRunner {

    @Autowired
    private Environment environment;

    @Autowired
    private DockerService dockerService;

    @Override
    public void run(String... args) throws Exception {
        Arguments arguments = new Arguments(environment);
        dockerService.buildFile(arguments);
    }
}
