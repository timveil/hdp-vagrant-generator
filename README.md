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
# ####################################### Logging Configurations

logging.level.root=WARN
logging.level.veil=INFO

logging.file=hdp-vagrant-generator.log

# ####################################### Virtual Box Configurations

# fully qualified domain name (FQDN) of virtual box image
vm.fqdn=default.hdp.local

# dns hostname of virtual box image
vm.hostname=default

# ip address of virtual box image
vm.ip=192.168.7.101

# ram allocated to virtual box image in MB
vm.memory=8192

# number of cores allocated to virtual box image
vm.cores=4

# number of disks allocated virtual box image
vm.disks=1

# do you want yum to run update command during vagrant provisioning
vm.update.yum=true


# ####################################### HDP Configurations

# HDP stack version
hdp.stack.version=2.6

# HDP Ambari version
hdp.ambari.version=2.5.1.0

# Kerberos enabled (defaults to false)?
hdp.kerberos.enabled=

# if Kerberos is enabled, the realm (ex. "example.com")
hdp.kerberos.realm=

# HDP optional components installed
hdp.components=hive,spark


# ####################################### Custom Ambari Repository Configurations

# is custom repo enabled (defaults to false)?
custom.repo.enabled=

# the fqdn of the custom repo (used in VM hosts file)
custom.repo.fqdn=

# the ip of the custom repo (used in VM hosts file)
custom.repo.ip=

# the url of the ambari.repo file in the custom repository (ex. "http://repo.hdp.local/repos/centos7/ambari/2.4.2.0/ambari.repo")
custom.repo.ambari.url=

# Custom Base URL for HDP Repo (ex. "http://repo.hdp.local/hdp/centos7/HDP-2.5.3.0")
custom.repo.hdp.url=

# Custom Base URL for HDP Utils Repo (ex. "http://repo.hdp.local/hdp/centos7/HDP-UTILS-1.1.0.21")
custom.repo.hdp-utils.url=
```
