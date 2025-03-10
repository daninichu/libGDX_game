<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="door" tilewidth="32" tileheight="32" tilecount="1" columns="1">
 <image source="door.png" width="32" height="32"/>
 <tile id="0" type="Door">
  <properties>
   <property name="Exit Door" type="object" value="0"/>
   <property name="New x" type="float" value="0"/>
   <property name="New y" type="float" value="0"/>
   <property name="Next Map" value=""/>
  </properties>
  <objectgroup draworder="index" id="2">
   <object id="1" x="5" y="20" width="22" height="12">
    <properties>
     <property name="Exit Door" type="object" value="0"/>
     <property name="New x" type="float" value="0"/>
     <property name="New y" type="float" value="0"/>
     <property name="Next Map" value=""/>
    </properties>
   </object>
  </objectgroup>
 </tile>
</tileset>
