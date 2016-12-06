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

    private final Integer memoryInMegabytes;

    private final Integer cores;

    private final Integer disks;

    private final Integer minContainerSizeInMegabytes;

    private final Integer reservedSystemMemoryInMegabytes;

    private final Integer reservedHbaseMemoryInMegabytes;

    private final String clusterName;

    private final String blueprintName;

    private final boolean updateLibraries;

    private final String ambariRepoFileUrl;

    private final Set<Component> components;

    private final String stackVersion;

    private final boolean kerberosEnabled;

    private final String kerberosRealm;

    private final String hdpRepoBaseUrl;

    private final String hdpRepoUtilsBaseUrl;

    private final String ambariApiBlueprintsUrl;

    private final String ambariApiClustersUrl;

    private final String ambariApiClustersRequestsUrl;

    private final String ambariApiRepositoriesHdpUrl;

    private final String ambariApiRepositoriesHdpUtilsUrl;

    private final String hdpRepoIp;

    private final String hdpRepoFqdn;

    public Arguments(Environment environment) {
        this.fqdn =  environment.getProperty(Constants.VM_FQDN, String.class);
        this.hostname =  environment.getProperty(Constants.VM_HOSTNAME, String.class);

        if (StringUtils.isBlank(hostname)) {
            this.hostname = fqdn;
        }

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
        this.stackVersion =  environment.getProperty(Constants.HDP_STACK_VERSION, String.class);
        this.ambariRepoFileUrl = environment.getProperty(Constants.HDP_REPO_AMBARI_FILE, String.class);
        this.ambariApiBlueprintsUrl = environment.getProperty(Constants.HDP_AMBARI_API_BLUEPRINTS_URL, String.class);
        this.ambariApiClustersUrl = environment.getProperty(Constants.HDP_AMBARI_API_CLUSTERS_URL, String.class);
        this.ambariApiClustersRequestsUrl = environment.getProperty(Constants.HDP_AMBARI_API_CLUSTERS_REQUESTS_URL, String.class);
        this.hdpRepoBaseUrl = environment.getProperty(Constants.HDP_REPO_BASE, String.class);
        this.hdpRepoUtilsBaseUrl = environment.getProperty(Constants.HDP_REPO_UTILS_BASE, String.class);
        this.kerberosEnabled = environment.getProperty(Constants.HDP_KERBEROS_ENABLED, Boolean.class);
        this.kerberosRealm = environment.getProperty(Constants.HDP_KERBEROS_REALM, String.class);
        this.ambariApiRepositoriesHdpUrl = environment.getProperty(Constants.HDP_AMBARI_API_REPOSITORIES_HDP_URL, String.class);
        this.ambariApiRepositoriesHdpUtilsUrl = environment.getProperty(Constants.HDP_AMBARI_API_REPOSITORIES_HDPUTILS_URL, String.class);
        this.hdpRepoIp = environment.getProperty(Constants.HDP_REPO_IP, String.class);
        this.hdpRepoFqdn = environment.getProperty(Constants.HDP_REPO_FQDN, String.class);

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

    public String getAmbariRepoFileUrl() {
        return ambariRepoFileUrl;
    }

    public boolean isUpdateLibraries() {
        return updateLibraries;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public String getAmbariApiBlueprintsUrl() {
        return ambariApiBlueprintsUrl;
    }

    public String getAmbariApiClustersUrl() {
        return ambariApiClustersUrl;
    }

    public String getAmbariApiClustersRequestsUrl() {
        return ambariApiClustersRequestsUrl;
    }

    public String getHdpRepoBaseUrl() {
        return hdpRepoBaseUrl;
    }

    public String getHdpRepoUtilsBaseUrl() {
        return hdpRepoUtilsBaseUrl;
    }

    public boolean containsHiveComponent() {
        return components != null && components.contains(Component.hive);
    }

    public boolean containsSparkComponent() {
        return components != null && components.contains(Component.spark);
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

    public String getAmbariApiRepositoriesHdpUrl() {
        return ambariApiRepositoriesHdpUrl;
    }

    public String getAmbariApiRepositoriesHdpUtilsUrl() {
        return ambariApiRepositoriesHdpUtilsUrl;
    }

    public boolean hasCustomHDPUrl() {
        return StringUtils.isNotBlank(hdpRepoBaseUrl) && StringUtils.isNotBlank(ambariApiRepositoriesHdpUrl);
    }

    public boolean hasCustomHDPUtilsUrl() {
        return StringUtils.isNotBlank(hdpRepoUtilsBaseUrl) && StringUtils.isNotBlank(ambariApiRepositoriesHdpUtilsUrl);
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
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_AMBARI_API_BLUEPRINTS_URL, ambariApiBlueprintsUrl);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_AMBARI_API_CLUSTERS_URL, ambariApiClustersUrl);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_AMBARI_API_CLUSTERS_REQUESTS_URL, ambariApiClustersRequestsUrl);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_AMBARI_API_REPOSITORIES_HDP_URL, ambariApiRepositoriesHdpUrl);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_AMBARI_API_REPOSITORIES_HDPUTILS_URL, ambariApiRepositoriesHdpUtilsUrl);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_REPO_AMBARI_FILE, ambariRepoFileUrl);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_REPO_BASE, hdpRepoBaseUrl);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_REPO_UTILS_BASE, hdpRepoUtilsBaseUrl);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_REPO_IP, hdpRepoIp);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_REPO_FQDN, hdpRepoFqdn);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_COMPONENTS, components);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_KERBEROS_ENABLED, kerberosEnabled);
        formatter.format(Constants.FORMAT_SPACER, Constants.HDP_KERBEROS_REALM, kerberosRealm);
        formatter.format(Constants.FORMAT_NEW_LINE, "***********************************************************************");
        formatter.format(Constants.FORMAT_NEW_LINE, " ");

        log.info(builder.toString());
    }

}
