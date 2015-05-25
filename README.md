# HDP Vagrant Generator

Vagrantfile generator for Hortonworks Data Platform (HDP)

```
usage: java -jar <HDP Vagrant Generator>
 -ambariRepoUrl <url>        Ambar Repo URL
 -b <blueprint>              Name of blueprint used to build cluster
 -baseHdpUrl <url>           Base HDP URL
 -baseHdpUtilsUrl <url>      Base HDP Utils URL
 -cpu <number of cpus>       # of cpus of generated image
 -d <number of disks>        # of disks in generated image
 -h <hostname>               Required! Hostname of generated image
 -ip <ip>                    IP address of generated image
 -min <min container size>   Minimum container size memory (in megabytes)
 -n <name of cluster>        Required! Name of cluster
 -ram <ram>                  RAM allocated to generated image (in megabytes)
 -rh <reserved memory>       Reserved HBase memory (in megabytes)
 -rs <reserved memory>       Reserved system memory (in megabytes)
 -update                     Update YUM libraries while building image
```
