# HDP Vagrant Generator

Generator for Vagrantfile's for Hortonworks Data Platform (HDP)

```
usage: java -jar <HDP Vagrant Generator>
 -b <arg>     Name of blueprint used to build cluster
 -cpu <arg>   # of cpus of generated image
 -d <arg>     # of disks in generated image
 -h <arg>     Hostname of generated image.  Required!
 -ip <arg>    IP address of generated image
 -min <arg>   Minimum container size memory (in megabytes)
 -n <arg>     Name of cluster.  Required!
 -ram <arg>   RAM allocated to generated image (in megabytes)
 -rh <arg>    Reserved HBase memory (in megabytes)
 -rs <arg>    Reserved system memory (in megabytes)
 -update      Update YUM libraries while building image
```
