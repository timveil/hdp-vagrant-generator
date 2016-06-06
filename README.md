# HDP Vagrant Generator

Vagrantfile generator for Hortonworks Data Platform (HDP).  Built using SpringBoot, this application will generate a `Vagrant` file based on the supplied `application.properties`.  This makes it very easy to create purpose built, custom Virtual Box HDP instances that are properly configured for your use case and hardware.

To run, simply download or build the latest `hdp-vagrant-generator-*.jar`.  If you would like to customize, place a copy of `application.properties` in the same directory as the `hdp-vagrant-generator-*.jar` and from a command line run `java -jar hdp-vagrant-generator-*.jar`.  A directory named `out` will be created with your new `Vagrant` file inside.

To use the newly created `Vagrant` file make sure you have Vagrant installed along with the following plugins: 

```
vagrant plugin install vagrant-multi-hostsupdater
vagrant plugin install vagrant-vbguest
```

Assuming, Vagrant and the required plugins are installed you can use you new image by running `vagrant up`.  The Vagrant file will provision an HDP cluster based on your specifications.  The process usually takes about 20 minutes based on hardware and network connectivity.

