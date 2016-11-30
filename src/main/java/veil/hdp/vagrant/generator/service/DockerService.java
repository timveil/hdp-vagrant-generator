package veil.hdp.vagrant.generator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import veil.hdp.vagrant.generator.model.Arguments;

@Service
public class DockerService implements FileService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void buildFile(Arguments arguments) {


    }


}
