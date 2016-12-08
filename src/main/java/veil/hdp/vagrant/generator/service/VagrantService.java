package veil.hdp.vagrant.generator.service;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;
import veil.hdp.vagrant.generator.model.Arguments;

import java.util.Map;

@Service
public class VagrantService extends AbstractFileService {

    private static final String VAGRANTFILE = "Vagrantfile";
    private static final String INSTALL_SH = "install.sh";
    private static final String CLEAN_LOGS_SH = "clean-logs.sh";
    private static final String HOSTS = "hosts";
    private static final String BLUEPRINT_JSON = "blueprint.json";
    private static final String CREATE_CLUSTER_JSON = "create-cluster.json";
    private static final String CREATE_HDP_REPO_JSON = "create-hdp-repo.json";
    private static final String CREATE_HDP_UTILS_REPO_JSON = "create-hdp-utils-repo.json";
    private static final String KRB5_CONF = "krb5.conf";
    private static final String KDC_CONF = "kdc.conf";
    private static final String KADM5_ACL = "kadm5.acl";

    @Override
    protected void buildFile(Map<String, Object> model, Arguments arguments) {

        final String parentDirectoryName = "out/" + arguments.getFqdn();

        writeContentsToFile(parentDirectoryName, INSTALL_SH, convertTemplateToString(resolver, "templates/common/shell/install.mustache", model));
        writeContentsToFile(parentDirectoryName, CLEAN_LOGS_SH, convertTemplateToString(resolver, "templates/common/shell/clean-logs.mustache", model));
        writeContentsToFile(parentDirectoryName, HOSTS, convertTemplateToString(resolver, "templates/common/network/hosts.mustache", model));
        writeContentsToFile(parentDirectoryName, BLUEPRINT_JSON, convertTemplateToString(resolver, "templates/common/json/blueprint.mustache", model));
        writeContentsToFile(parentDirectoryName, CREATE_CLUSTER_JSON, convertTemplateToString(resolver, "templates/common/json/create-cluster.mustache", model));

        if (arguments.isCustomRepoEnabled()) {
            writeContentsToFile(parentDirectoryName, CREATE_HDP_REPO_JSON, convertTemplateToString(resolver, "templates/common/json/create-repository.mustache", ImmutableMap.of("url", arguments.getCustomRepoHdpUrl())));
            writeContentsToFile(parentDirectoryName, CREATE_HDP_UTILS_REPO_JSON, convertTemplateToString(resolver, "templates/common/json/create-repository.mustache", ImmutableMap.of("url", arguments.getCustomRepoHdpUtilsUrl())));
        }

        if (arguments.isKerberosEnabled()) {
            writeContentsToFile(parentDirectoryName, KRB5_CONF, convertTemplateToString(resolver, "templates/common/kerberos/krb5.mustache", model));
            writeContentsToFile(parentDirectoryName, KDC_CONF, convertTemplateToString(resolver, "templates/common/kerberos/kdc.mustache", model));
            writeContentsToFile(parentDirectoryName, KADM5_ACL, convertTemplateToString(resolver, "templates/common/kerberos/kadm5.mustache", model));
        }

        writeContentsToFile(parentDirectoryName, VAGRANTFILE, convertTemplateToString(resolver, "templates/vagrant/vagrantfile.mustache", model));


    }

}
