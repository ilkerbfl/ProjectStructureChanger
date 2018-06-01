# ProjectStructureChanger
This repository bundles entity related class to same folder

Repository,Service,Controller,DTO,Entity,Mapper etc. have seperate folders. This project first scans Entity annotation then searchs for related classes. If it finds , bundles them in same folder.To illusturate : if we have an entity which called Address.java. It scans the packages and it finds the Address entity. Then if there exists entity related classes like AddressDTO,AddressRepository ,AddressMapper etc. automatically will copy them to folder which name is Address
