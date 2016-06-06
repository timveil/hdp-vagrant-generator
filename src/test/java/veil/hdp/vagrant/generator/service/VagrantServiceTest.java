package veil.hdp.vagrant.generator.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import veil.hdp.vagrant.generator.GeneratorApplication;
import veil.hdp.vagrant.generator.model.Arguments;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GeneratorApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class VagrantServiceTest {

    @Autowired
    private Environment environment;

    @Autowired
    private VagrantService vagrantService;

    private Arguments arguments;

    @Before
    public void setUp() throws Exception {

        arguments = new Arguments(environment);

    }

    @After
    public void tearDown() throws Exception {
        arguments = null;
    }

    @Test
    public void buildFile() throws Exception {

        vagrantService.buildFile(arguments);


    }

}