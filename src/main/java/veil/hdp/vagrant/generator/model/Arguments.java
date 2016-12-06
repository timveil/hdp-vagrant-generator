package veil.hdp.vagrant.generator.model;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import veil.hdp.vagrant.generator.Constants;

import java.util.Formatter;
import java.util.List;
import java.util.Set;

public class Arguments {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String fqdn;

    private String hostname;

    private final String ip;

    private final Integer memory;

    private final Integer cores;

    private final Integer disks;

    private final boolean updateYum;

    private final String stackVersion;

    private final String ambariVersion;

    private final boolean kerberosEnabled;

    private final String kerberosRealm;

    private final Set<Component> components;



    public Arguments(Environment environment) {
        this.fqdn =  environment.getProperty(Constants.VM_FQDN, String.class);
        this.hostname =  environment.getProperty(Constants.VM_HOSTNAME, String.class);

        if (StringUtils.isBlank(hostname)) {
            this.hostname = fqdn;
        }

        this.ip =  environment.getProperty(Constants.VM_IP, String.class);
        this.memory =  environment.getProperty(Constants.VM_MEMORY, Integer.class);
        this.cores =  environment.getProperty(Constants.VM_CORES, Integer.class);
        this.updateYum =   Boolean.parseBoolean(environment.getProperty(Constants.VM_UPDATE_YUM, String.class));
        this.disks =  environment.getProperty(Constants.VM_DISKS, Integer.class);
        this.stackVersion =  environment.getProperty(Constants.HDP_STACK_VERSION, String.class);
        this.ambariVersion =  environment.getProperty(Constants.HDP_AMBARI_VERSION, String.class);
        this.kerberosEnabled = environment.getProperty(Constants.HDP_KERBEROS_ENABLED, Boolean.class);
        this.kerberosRealm = environment.getProperty(Constants.HDP_KERBEROS_REALM, String.class);

        final String componentString = environment.getProperty(Constants.HDP_COMPONENTS);
        final List<String> componentStrings = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(componentString);
        final Set<Component> components = Sets.newHashSet();

        if (!componentStrings.isEmpty()) {
            for (String component : componentStrings) {
                components.add(Component.valueOf(component));
            }
        }

        this.components = components;

        this.prettyPrint();
    }

    public String getFqdn() {
        return fqdn;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public Integer getMemory() {
        return memory;
    }

    public Integer getCores() {
        return cores;
    }

    public Integer getDisks() {
        return disks;
    }

    public boolean isUpdateYum() {
        return updateYum;
    }

    public String getStackVersion() {
        return stackVersion;
    }

    public String getAmbariVersion() {
        return ambariVersion;
    }

    public boolean isKerberosEnabled() {
        return kerberosEnabled;
    }

    public String getKerberosRealm() {
        return kerberosRealm;
    }

    public Set<Component> getComponents() {
        return components;
    }

    private void prettyPrint() {
        StringBuilder builder = new StringBuilder();
        Formatter formatter = new Formatter(builder);

        formatter.format(Constants.FORMAT_NEW_LINE, " ");
        formatter.format(Constants.FORMAT_NEW_LINE, "***********************************************************************");
        formatter.format(Constants.FORMAT_NEW_LINE, "*** Arguments");
        formatter.format(Constants.FORMAT_NEW_LINE, "***********************************************************************");
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_FQDN, fqdn);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_HOSTNAME, hostname);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_IP, ip);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_MEMORY, memory);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_CORES, cores);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_DISKS, disks);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_UPDATE_YUM, updateYum);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_STACK_VERSION, stackVersion);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_AMBARI_VERSION, ambariVersion);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_KERBEROS_ENABLED, kerberosEnabled);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_KERBEROS_REALM, kerberosRealm);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_COMPONENTS, components);
        formatter.format(Constants.FORMAT_NEW_LINE, "***********************************************************************");
        formatter.format(Constants.FORMAT_NEW_LINE, " ");

        log.info(builder.toString());
    }

}
