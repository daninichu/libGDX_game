<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="o" tilewidth="32" tileheight="32" spacing="1" tilecount="8" columns="4">
 <image source="objects.png" width="132" height="66"/>
 <tile id="0" type="Switch">
  <properties>
   <property name="Door" type="object" value="0"/>
  </properties>
  <objectgroup draworder="index" id="2">
   <object id="2" name="Interaction" x="-5" y="6" width="42" height="40"/>
   <object id="1" name="Collision" x="7" y="20" width="18" height="12"/>
  </objectgroup>
 </tile>
 <tile id="1" type="Sign">
  <properties>
   <property name="Text" value=""/>
  </properties>
  <objectgroup draworder="index" id="2">
   <object id="1" name="Collision" x="2" y="24" width="28" height="8"/>
   <object id="2" name="Interaction" x="2" y="32" width="28" height="24"/>
  </objectgroup>
 </tile>
 <tile id="4" type="Door">
  <properties>
   <property name="Exit Door" type="object" value="0"/>
   <property name="Locked" type="bool" value="false"/>
  </properties>
  <objectgroup draworder="index" id="2">
   <object id="3" name="Collision" x="4" y="20" width="24" height="12"/>
   <object id="4" name="Interaction" x="4" y="32" width="24" height="20"/>
  </objectgroup>
 </tile>
 <tile id="5" type="PressurePlate">
  <properties>
   <property name="Link0" type="object" value="0"/>
  </properties>
  <objectgroup draworder="index" id="2">
   <object id="1" name="Interaction" x="0" y="2" width="32" height="30"/>
  </objectgroup>
 </tile>
</tileset>
