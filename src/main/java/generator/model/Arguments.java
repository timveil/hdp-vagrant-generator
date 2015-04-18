package generator.model;

import org.springframework.core.env.Environment;

import java.util.Set;

public class Arguments {

    private static final String HDP_REPO_NAME="HDP-2.2";
    private static final String HDP_UTILS_REPO_NAME="HDP-UTILS-1.1.0.20";


    private String hostname;

    private String ip;

    private Integer memoryInMegabytes;

    private Integer cpus;

    private String clusterName;

    private String blueprintName;

    private boolean updateLibraries;

    private String mysqlPassword;

    private Set<View> views;

    private Set<Component> components;

    public String getBlueprintName() {
        return blueprintName;
    }

    public void setBlueprintName(String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public Arguments(Environment environment) {


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

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getMemoryInMegabytes() {
        return memoryInMegabytes;
    }

    public void setMemoryInMegabytes(Integer memoryInMegabytes) {
        this.memoryInMegabytes = memoryInMegabytes;
    }

    public Integer getCpus() {
        return cpus;
    }

    public void setCpus(Integer cpus) {
        this.cpus = cpus;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public boolean isUpdateLibraries() {
        return updateLibraries;
    }

    public void setUpdateLibraries(boolean updateLibraries) {
        this.updateLibraries = updateLibraries;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public void setMysqlPassword(String mysqlPassword) {
        this.mysqlPassword = mysqlPassword;
    }

    public Set<View> getViews() {
        return views;
    }

    public void setViews(Set<View> views) {
        this.views = views;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }


    public String getBlueprintUrl() {
        return "http://" + hostname + ":8080/api/v1/blueprints/" + blueprintName;
    }

    public String getClusterUrl() {
        return "http://" + hostname + ":8080/api/v1/clusters/" + clusterName;
    }

    public String getHdpRepoUrl() {
        return "http://" + hostname + ":8080/api/v1/stacks/HDP/versions/2.2/operating_systems/redhat6/repositories/" + HDP_REPO_NAME;
    }

    public String getHdpUtilsRepoUrl() {
        return "http://" + hostname + ":8080/api/v1/stacks/HDP/versions/2.2/operating_systems/redhat6/repositories/" + HDP_UTILS_REPO_NAME;
    }

    public boolean containsHive() {
        return components != null && components.contains(Component.hive);
    }

    public boolean containsKnox() {
        return components != null && components.contains(Component.knox);
    }


    public void prettyPrint() {

    }
}
