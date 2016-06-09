package veil.hdp.vagrant.generator.model;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import veil.hdp.vagrant.generator.Constants;

import java.util.Formatter;
import java.util.List;
import java.util.Set;

public class Arguments {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String hostname;

    private final String ip;

    private final Integer memoryInMegabytes;

    private final Integer cores;

    private final Integer disks;

    private final Integer minContainerSizeInMegabytes;

    private final Integer reservedSystemMemoryInMegabytes;

    private final Integer reservedHbaseMemoryInMegabytes;

    private final String clusterName;

    private final String blueprintName;

    private final boolean updateLibraries;

    private final String ambariRepoUrl;

    private final Set<Component> components;

    private final String mySqlConnectorVersion;

    private final String stackVersion;

    private final boolean kerberosEnabled;

    private final String kerberosRealm;

    public Arguments(Environment environment) {
        this.hostname =  environment.getProperty(Constants.VM_HOSTNAME, String.class);
        this.ip =  environment.getProperty(Constants.VM_IP, String.class);
        this.memoryInMegabytes =  environment.getProperty(Constants.VM_RAM, Integer.class);
        this.reservedSystemMemoryInMegabytes =  environment.getProperty(Constants.HDP_MEMORY_RESERVED_SYSTEM, Integer.class);
        this.reservedHbaseMemoryInMegabytes =  environment.getProperty(Constants.HDP_MEMORY_RESERVED_HBASE, Integer.class);
        this.minContainerSizeInMegabytes =  environment.getProperty(Constants.HDP_MIN_CONTAINER_SIZE, Integer.class);
        this.cores =  environment.getProperty(Constants.VM_CORES, Integer.class);
        this.updateLibraries =   Boolean.parseBoolean(environment.getProperty(Constants.VM_UPDATE_YUM, String.class));
        this.blueprintName =  environment.getProperty(Constants.HDP_BLUEPRINT_NAME, String.class);
        this.clusterName =  environment.getProperty(Constants.HDP_CLUSTER_NAME, String.class);
        this.disks =  environment.getProperty(Constants.VM_DISKS, Integer.class);
        this.mySqlConnectorVersion =  environment.getProperty(Constants.MYSQL_CONNECTOR_VERSION, String.class);
        this.stackVersion =  environment.getProperty(Constants.HDP_STACK_VERSION, String.class);

        String componentString = environment.getProperty(Constants.HDP_COMPONENTS);
        List<String> componentStrings = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(componentString);
        Set<Component> components = Sets.newHashSet();

        if (!componentStrings.isEmpty()) {
            for (String component : componentStrings) {
                components.add(Component.valueOf(component));
            }
        }

        this.components = components;


        this.ambariRepoUrl = environment.getProperty(Constants.HDP_AMBARI_REPO, String.class);

        this.kerberosEnabled = environment.getProperty(Constants.HDP_KERBEROS_ENABLED, Boolean.class);
        this.kerberosRealm = environment.getProperty(Constants.HDP_KERBEROS_REALM, String.class);


        this.prettyPrint();
    }

    public String getHostname() {
        return hostname;
    }

    public String getIp() {
        return ip;
    }

    public Integer getMemoryInMegabytes() {
        return memoryInMegabytes;
    }

    public Integer getCores() {
        return cores;
    }

    public Integer getDisks() {
        return disks;
    }

    public Integer getMinContainerSizeInMegabytes() {
        return minContainerSizeInMegabytes;
    }

    public Integer getReservedSystemMemoryInMegabytes() {
        return reservedSystemMemoryInMegabytes;
    }

    public Integer getReservedHbaseMemoryInMegabytes() {
        return reservedHbaseMemoryInMegabytes;
    }

    public String getClusterName() {
        return clusterName;
    }

    public String getBlueprintName() {
        return blueprintName;
    }

    public String getAmbariRepoUrl() {
        return ambariRepoUrl;
    }

    public boolean isUpdateLibraries() {
        return updateLibraries;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public String getBlueprintUrl() {
        return "http://" + hostname + ":8080/api/v1/blueprints/" + blueprintName;
    }

    public String getClusterUrl() {
        return "http://" + hostname + ":8080/api/v1/clusters/" + clusterName;
    }

    public String getCheckStatusUrl() {
        return "http://" + hostname + ":8080/api/v1/clusters/" + clusterName + "/requests/1";
    }

    public boolean containsHiveComponent() {
        return components != null && components.contains(Component.hive);
    }

    public boolean containsKnoxComponent() {
        return components != null && components.contains(Component.knox);
    }

    public boolean containsSqoopComponent() {
        return components != null && components.contains(Component.sqoop);
    }

    public boolean containsSparkComponent() {
        return components != null && components.contains(Component.spark);
    }

    public boolean containsRangerComponent() {
        return components != null && components.contains(Component.ranger);
    }

    public String getMySqlConnectorVersion() {
        return mySqlConnectorVersion;
    }

    public String getStackVersion() {
        return stackVersion;
    }

    public boolean isKerberosEnabled() {
        return kerberosEnabled;
    }

    public String getKerberosRealm() {
        return kerberosRealm;
    }

    private void prettyPrint() {
        StringBuilder builder = new StringBuilder();
        Formatter formatter = new Formatter(builder);

        formatter.format(Constants.FORMAT_NEW_LINE, " ");
        formatter.format(Constants.FORMAT_NEW_LINE, "***********************************************************************");
        formatter.format(Constants.FORMAT_NEW_LINE, "*** Arguments");
        formatter.format(Constants.FORMAT_NEW_LINE, "***********************************************************************");
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_HOSTNAME, hostname);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_IP, ip);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_RAM, memoryInMegabytes);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_CORES, cores);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_DISKS, disks);
        formatter.format(Constants.FORMAT_SPACER, Constants.VM_UPDATE_YUM, updateLibraries);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_CLUSTER_NAME, clusterName);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_BLUEPRINT_NAME, blueprintName);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_STACK_VERSION, stackVersion);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_MIN_CONTAINER_SIZE, minContainerSizeInMegabytes);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_MEMORY_RESERVED_SYSTEM, reservedSystemMemoryInMegabytes);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_MEMORY_RESERVED_HBASE, reservedHbaseMemoryInMegabytes);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_AMBARI_REPO, ambariRepoUrl);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_COMPONENTS, components);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_KERBEROS_ENABLED, kerberosEnabled);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_KERBEROS_REALM, kerberosRealm);
        formatter.format(Constants.FORMAT_NEW_LINE, "***********************************************************************");
        formatter.format(Constants.FORMAT_NEW_LINE, " ");

        log.info(builder.toString());
    }

}
