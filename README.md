# HDP Vagrant Generator

## Overview

A `Vagrantfile` generator for Hortonworks Data Platform (HDP).  Built using Spring Boot, this application will generate a `Vagrantfile` file based on the supplied `application.properties`.  This makes it very easy to create purpose built, custom Virtual Box HDP instances that are properly configured for your use case and hardware.


## How to run

To run, simply download or build the latest `hdp-vagrant-generator-*.jar`.  If you would like to customize, place a copy of `application.properties` in the same directory as the `hdp-vagrant-generator-*.jar` and from a command line run `java -jar hdp-vagrant-generator-*.jar`.  A directory named `out` will be created with your new `Vagrantfile` inside.

To use the newly created `Vagrantfile` make sure you have Vagrant installed along with the following plugins: 

```sh
vagrant plugin install vagrant-multi-hostsupdater
vagrant plugin install vagrant-vbguest
```

Assuming, Vagrant and the required plugins are installed you can use you new image by running `vagrant up`.  The `Vagrantfile` will provision an HDP cluster based on your specifications.  The process usually takes about 20 minutes based on hardware and network connectivity.

## How to customize

You can change the configuration of the Virtual Box image or the HDP cluster by creating a file called `application.properties` and placing it in the same directory as `hdp-vagrant-generator-*.jar`.  The following values are used by default if no custom `application.properties` is provided.

```dosini
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

# Kerberos enabled?
hdp.kerberos.enabled=false

# if Kerberos is enabled, the realm (ex. "example.com")
hdp.kerberos.realm=

# HDP optional components installed
hdp.components=hive,spark

# MySQL Connector version
mysql.connector.version=5.1.39
```

## Example of generated Vagrantfile 

```rb
# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile generated by https://github.com/timveil/hdp-vagrant-veil.hdp.generator on 6/6/16 1:45 PM

require 'ffi'

Vagrant.configure("2") do |config|
    config.vm.box = "timveil/centos7-hdp-base"

    config.vm.box_check_update = true

    print "\n\n==> Host OS is " + FFI::Platform::OS + "\n\n"

    if FFI::Platform::IS_MAC

        config.vbguest.auto_update = true

        config.vbguest.no_remote = true

        config.vbguest.no_install = false

        config.multihostsupdater.aliases = {'192.168.7.101' => ['test.hdp.local']}

    end

    config.vm.provision "hosts", type: "shell", inline: $hostsFile

    config.vm.provision "logCleanup", type: "shell", inline: $logCleanup, run: "always"

    config.vm.provision "build", type: "shell", inline: $build

    config.vm.provision "checkStatus", type: "shell", path: "vagrant-checkstatus.sh"

    config.vm.provision "postInstall", type: "shell", inline: $postInstall

    config.vm.provision "complete", type: "shell", inline: $complete

    config.vm.hostname = 'test.hdp.local'

    config.vm.network "private_network", ip: '192.168.7.101'

    config.vm.provider "virtualbox" do |v|
        v.memory = 8192
        v.cpus = 4
        v.name = 'test.hdp.local'
    end
end

$hostsFile = <<SCRIPT

cat > /etc/hosts <<EOF
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

192.168.7.101   test.hdp.local
EOF

SCRIPT

$logCleanup = <<SCRIPT

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Cleaning Logs"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

rm -rf /var/log/ambari-metrics-collector/*
rm -rf /var/log/ambari-metrics-monitor/*
rm -rf /var/log/falcon/*
rm -rf /var/log/flume/*
rm -rf /var/log/hadoop/hdfs/*
rm -rf /var/log/hadoop/mapreduce/*
rm -rf /var/log/hadoop/yarn/*
rm -rf /var/log/hadoop-mapreduce/mapred/*
rm -rf /var/log/hadoop-yarn/yarn/*
rm -rf /var/log/hadoop-yarn/nodemanager/*
rm -rf /var/log/hadoop-httpfs/*
rm -rf /var/log/hbase/*
rm -rf /var/log/hive/*
rm -rf /var/log/kafka/*
rm -rf /var/log/knox/*
rm -rf /var/log/oozie/*
rm -rf /var/log/ranger/admin/*
rm -rf /var/log/ranger/usersync/*
rm -rf /var/log/sqoop/*
rm -rf /var/log/storm/*
rm -rf /var/log/webhcat/*
rm -rf /var/log/zookeeper/*

rm -rf /tmp/hive/*
rm -rf /tmp/hcat/*
rm -rf /tmp/ambari-qa/*
rm -rf /tmp/root/*

SCRIPT

$build = <<SCRIPT

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Disable firewalld"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

systemctl stop firewalld
systemctl disable firewalld

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Updating YUM Cache"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

yum makecache fast

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Cleaning YUM"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

yum clean all

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Updating YUM"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

yum update -y -q

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Deleting YUM History"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

yum history new

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Getting Ambari YUM Repo from http://public-repo-1.hortonworks.com/ambari/centos7/2.x/updates/2.2.2.0/ambari.repo"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

wget -q http://public-repo-1.hortonworks.com/ambari/centos7/2.x/updates/2.2.2.0/ambari.repo -O /etc/yum.repos.d/ambari.repo

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Installing Ambari Server and Agent"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

yum install ambari-server ambari-agent -y

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Updating Ambari Agent Hostname"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

sed -i "s/^hostname=localhost/hostname=test.hdp.local/g" /etc/ambari-agent/conf/ambari-agent.ini

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Downloading New MySQL Connector Jar"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

wget -q http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.39.zip -O /usr/share/java/mysql-connector-java-5.1.39.zip
cd /usr/share/java
unzip -q mysql-connector-java-5.1.39.zip

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Running Ambari Server setup"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

ambari-server setup -s -j $JAVA_HOME

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Updating MySql Connector Jar"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

ambari-server setup --jdbc-driver=/usr/share/java/mysql-connector-java-5.1.39/mysql-connector-java-5.1.39-bin.jar --jdbc-db=mysql

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Cloning Zepplin and updating stack definition"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

git clone https://github.com/hortonworks-gallery/ambari-zeppelin-service.git /var/lib/ambari-server/resources/stacks/HDP/2.4/services/ZEPPELIN
sed -i.bak '/dependencies for all/a \    "ZEPPELIN_MASTER-START": ["NAMENODE-START", "DATANODE-START"],' /var/lib/ambari-server/resources/stacks/HDP/2.4/role_command_order.json

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Starting Ambari Server"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

ambari-server start

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Starting Ambari Agent"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

ambari-agent start

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Sleep..."
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

sleep 30


echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Executing POST to Create Blueprint"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

curl -s --show-error -H "X-Requested-By: ambari" -X POST -d '{"configurations": [{"hdfs-site": {"properties": {"dfs.replication": "1"}}},{"core-site": {"properties": {"hadoop.proxyuser.root.groups": "*","hadoop.proxyuser.root.hosts": "*","hadoop.proxyuser.hcat.groups": "*","hadoop.proxyuser.hcat.hosts": "*"}}},{"webhcat-site": {"properties": {"webhcat.proxyuser.root.groups": "*","webhcat.proxyuser.root.hosts": "*"}}},{"mapred-site": {"properties": {"mapreduce.map.java.opts": "-Xmx614m","mapreduce.map.memory.mb": "768","mapreduce.reduce.java.opts": "-Xmx1228m","mapreduce.reduce.memory.mb": "1536","yarn.app.mapreduce.am.command-opts": "-Xmx1228m -Dhdp.version=${hdp.version}","yarn.app.mapreduce.am.resource.mb": "1536"}}},{"yarn-site": {"properties": {"yarn.nodemanager.resource.memory-mb": "6144","yarn.scheduler.maximum-allocation-mb": "6144","yarn.scheduler.minimum-allocation-mb": "768"}}}],"host_groups": [{"name": "host_group_1","configurations": [],"cardinality": "1","components": [{"name": "AMBARI_SERVER"},{"name": "APP_TIMELINE_SERVER"},{"name": "DATANODE"},{"name": "HCAT"},{"name": "HDFS_CLIENT"},{"name": "HISTORYSERVER"},{"name": "SPARK_JOBHISTORYSERVER"},{"name": "SPARK_CLIENT"},{"name": "ZEPPELIN_MASTER"},{"name": "HIVE_CLIENT"},{"name": "HIVE_METASTORE"},{"name": "HIVE_SERVER"},{"name": "MAPREDUCE2_CLIENT"},{"name": "METRICS_COLLECTOR"},{"name": "METRICS_MONITOR"},{"name": "MYSQL_SERVER"},{"name": "NAMENODE"},{"name": "NODEMANAGER"},{"name": "PIG"},{"name": "RESOURCEMANAGER"},{"name": "SECONDARY_NAMENODE"},{"name": "TEZ_CLIENT"},{"name": "WEBHCAT_SERVER"},{"name": "YARN_CLIENT"},{"name": "ZOOKEEPER_CLIENT"},{"name": "ZOOKEEPER_SERVER"}]}],"Blueprints": {"blueprint_name": "test","stack_name": "HDP","stack_version": "2.4"}}' -u admin:admin http://test.hdp.local:8080/api/v1/blueprints/test

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Executing POST to Create Files View"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

curl -s --show-error -H "X-Requested-By: ambari" -X POST -d '{"ViewInstanceInfo":{"instance_name":"FILES_VIEW_INSTANCE","label":"Files View","visible":true,"icon_path":"","icon64_path":"","properties":{"webhdfs.url":"webhdfs://test.hdp.local:50070","webhdfs.username":null,"webhdfs.auth":"auth=SIMPLE"},"description":"Files View installed by https://github.com/timveil/hdp-vagrant-veil.hdp.generator"}}' -u admin:admin http://test.hdp.local:8080/api/v1/views/FILES/versions/1.0.0/instances/Files

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Executing POST to Install Cluster"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

curl -s --show-error -H "X-Requested-By: ambari" -X POST -d '{"blueprint" : "test","default_password" : "password","host_groups" :[{"name" : "host_group_1","hosts" : [{"fqdn" : "test.hdp.local"}]}]}' -u admin:admin http://test.hdp.local:8080/api/v1/clusters/test


SCRIPT

$postInstall = <<SCRIPT

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Adding Admin User"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

useradd -G hdfs admin
usermod -a -G users admin
usermod -a -G hadoop admin
usermod -a -G hive admin

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Updating Vagrant User"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "

usermod -a -G users vagrant
usermod -a -G hdfs vagrant
usermod -a -G hadoop vagrant
usermod -a -G hive vagrant

SCRIPT

$complete = <<SCRIPT

echo " "
echo "---------------------------------------------------------------------------------------------------------------"
echo "----- Cluster Install Complete!  http://test.hdp.local:8080"
echo "---------------------------------------------------------------------------------------------------------------"
echo " "


SCRIPT
```