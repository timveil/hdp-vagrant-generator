package veil.hdp.vagrant.generator.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import veil.hdp.vagrant.generator.model.Arguments;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class DockerService extends AbstractFileService {
    private static final String DOCKERFILE = "Dockerfile";

    @Override
    protected void buildFile(Map<String, Object> model, Arguments arguments) {

        String dockerFile = convertTemplateToString(resolver, "templates/docker/dockerfile.mustache", model);
        String krb5 = convertTemplateToString(resolver, "templates/common/kerberos/krb5.mustache", model);
        String kdc = convertTemplateToString(resolver, "templates/common/kerberos/kdc.mustache", model);
        String kadm5 = convertTemplateToString(resolver, "templates/common/kerberos/kadm5.mustache", model);


        final String parentDirectoryName = "out/" + arguments.getFqdn();

        try {
            FileUtils.writeStringToFile(new File(parentDirectoryName, DOCKERFILE), dockerFile, StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(new File(parentDirectoryName, "krb5.conf"), krb5, StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(new File(parentDirectoryName, "kdc.conf"), kdc, StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(new File(parentDirectoryName, "kadm5.acl"), kadm5, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }
}
