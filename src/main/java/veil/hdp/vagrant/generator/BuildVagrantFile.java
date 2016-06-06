package veil.hdp.vagrant.generator;

import veil.hdp.vagrant.generator.model.Arguments;
import veil.hdp.vagrant.generator.service.VagrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class BuildVagrantFile implements CommandLineRunner {

    @Autowired
    private Environment environment;

    @Autowired
    private VagrantService vagrantService;

    @Override
    public void run(String... args) throws Exception {
        Arguments arguments = new Arguments(environment);
        vagrantService.buildFile(arguments);
    }
}
