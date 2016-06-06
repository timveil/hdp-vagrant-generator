# HDP Vagrant Generator

## Overview

`Vagrantfile` generator for Hortonworks Data Platform (HDP).  Built using SpringBoot, this application will generate a `Vagrantfile` file based on the supplied `application.properties`.  This makes it very easy to create purpose built, custom Virtual Box HDP instances that are properly configured for your use case and hardware.


## How to run

To run, simply download or build the latest `hdp-vagrant-generator-*.jar`.  If you would like to customize, place a copy of `application.properties` in the same directory as the `hdp-vagrant-generator-*.jar` and from a command line run `java -jar hdp-vagrant-generator-*.jar`.  A directory named `out` will be created with your new `Vagrantfile` inside.

To use the newly created `Vagrantfile` make sure you have Vagrant installed along with the following plugins: 

```
vagrant plugin install vagrant-multi-hostsupdater
vagrant plugin install vagrant-vbguest
```

Assuming, Vagrant and the required plugins are installed you can use you new image by running `vagrant up`.  The `Vagrantfile` will provision an HDP cluster based on your specifications.  The process usually takes about 20 minutes based on hardware and network connectivity.

## How to customize

You can change the configuration of the Virtual Box image or the HDP cluster by creating a file called `application.properties` and placing it in the same directory as `hdp-vagrant-generator-*.jar`.  The following values are used by default if no custom `application.properties` is provided.

```
# ####################################### Required Configurations

# dns hostname of virtual box image
vm.hostname=default.hdp.local

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

# Ambari repo url
hdp.ambari.repo=http://public-repo-1.hortonworks.com/ambari/centos7/2.x/updates/2.2.2.0/ambari.repo

# HDP stack version
hdp.stack.version=2.4

# HDP optional components installed
hdp.components=hive,spark

# HDP optional views installed
hdp.views=files

# MySQL Connector version
mysql.connector.version=5.1.39
```