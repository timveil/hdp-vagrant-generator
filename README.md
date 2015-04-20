# HDP Vagrant Generator

Generator for Vagrantfile's for Hortonworks Data Platform (HDP)

```
usage: java -jar <HDP Vagrant Generator>
 -b <blueprint>              Name of blueprint used to build cluster
 -cpu <number of cpus>       # of cpus of generated image
 -d <number of disks>        # of disks in generated image
 -h <hostname>               Hostname of generated image.  Required!
 -ip <ip>                    IP address of generated image
 -min <min container size>   Minimum container size memory (in megabytes)
 -n <name of cluster>        Name of cluster.  Required!
 -ram <ram>                  RAM allocated to generated image (in megabytes)
 -rh <reserved memory>       Reserved HBase memory (in megabytes)
 -rs <reserved memory>       Reserved system memory (in megabytes)
 -update                     Update YUM libraries while building image
```
