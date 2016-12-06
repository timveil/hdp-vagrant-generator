# HDP Vagrant Generator

## Overview

A `Vagrantfile` generator for Hortonworks Data Platform (HDP).  Built using Spring Boot, this application will generate a `Vagrantfile` file and supporting files based on the supplied `application.properties`.  This makes it very easy to create purpose built, custom Virtual Box HDP instances that are properly configured for your use case and hardware.


## How to run

To run, simply download or build the latest `hdp-vagrant-generator-*.jar`.  If you would like to customize, place a copy of `application.properties` in the same directory as the `hdp-vagrant-generator-*.jar` and from a command line run `java -jar hdp-vagrant-generator-*.jar`.  A directory named `out` will be created with your new `Vagrantfile`, plus supporting files inside.

To use the newly created `Vagrantfile` make sure you have Vagrant installed along with the following plugins: 

```sh
vagrant plugin install vagrant-hostsupdater
vagrant plugin install vagrant-vbguest
```

Assuming, Vagrant and the required plugins are installed you can use you new image by running `vagrant up`.  The `Vagrantfile` will provision an HDP cluster based on your specifications.  The process usually takes about 20 minutes based on hardware and network connectivity.

## How to customize

You can change the configuration of the Virtual Box image or the HDP cluster by creating a file called `application.properties` and placing it in the same directory as `hdp-vagrant-generator-*.jar`.  The following values are used by default if no custom `application.properties` is provided.

See [HDP Vagrant Local Repo](https://github.com/timveil/hdp-vagrant-local-repo) for a quick and easy way to spin up a local yum repository using Vagrant.

```dosini
## ####################################### Logging Configurations

logging.level.root=WARN
logging.level.veil=INFO

logging.file=hdp-vagrant-generator.log

# ####################################### Required Configurations

# fully qualified domain name (FQDN) of virtual box image
vm.fqdn=default.hdp.local

# dns hostname of virtual box image
vm.hostname=default

# name of HDP cluster
hdp.cluster.name=default


# ####################################### Virtual Box Configurations

# ip address of virtual box image
vm.ip=192.168.7.101

# ram allocated to virtual box image in MB
vm.ram=8192

# number of cores allocated to virtual box image
vm.cores=4

# number of disks allocated virtual box image
vm.disks=1

# do you want yum to run update command during vagrant provisioning
vm.update.yum=true


# ####################################### HDP Configurations

# amount of memory reserved for system in MB
hdp.memory.reserved.system=2048

# amount of memory reserved for HBase in MB
hdp.memory.reserved.hbase=0

# minimum container size in MB
hdp.min.container.size=512

# Ambari blueprint name
hdp.blueprint.name=custom-generated-blueprint

# HDP stack version
hdp.stack.version=2.5

# Kerberos enabled?
hdp.kerberos.enabled=false

# if Kerberos is enabled, the realm (ex. "example.com")
hdp.kerberos.realm=

# HDP optional components installed
hdp.components=hive,spark

# URL of Ambari yum repo file (ex. "http://public-repo-1.hortonworks.com/ambari/centos7/2.x/updates/2.4.1.0/ambari.repo")
hdp.repo.ambari.file=http://public-repo-1.hortonworks.com/ambari/centos7/2.x/updates/2.4.1.0/ambari.repo

hdp.ambari.api.blueprints.url=http://${vm.fqdn}:8080/api/v1/blueprints/${hdp.blueprint.name}
hdp.ambari.api.clusters.url=http://${vm.fqdn}:8080/api/v1/clusters/${hdp.cluster.name}
hdp.ambari.api.clusters.requests.url=${hdp.ambari.api.clusters.url}/requests/1

# Custom Base URL for HDP Repo (ex. "http://repo.hdp.local/hdp/centos7/HDP-2.5.0.0")
hdp.repo.base=

# Custom Base URL for HDP Utils Repo (ex. "http://repo.hdp.local/hdp/centos7/HDP-UTILS-1.1.0.21")
hdp.repo.utils.base=

# API URL to create custom HDP Repo (ex. "http://${vm.fqdn}:8080/api/v1/stacks/HDP/versions/2.5/operating_systems/redhat7/repositories/HDP-2.5")
hdp.ambari.api.repositories.hdp.url=

# API URL to create custom HDP Utils Repo (ex. "http://${vm.fqdn}:8080/api/v1/stacks/HDP/versions/2.5/operating_systems/redhat7/repositories/HDP-UTILS-1.1.0.21")
hdp.ambari.api.repositories.hdputils.url=
```
