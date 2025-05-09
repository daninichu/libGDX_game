<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="desertSet" tilewidth="32" tileheight="32" spacing="1" margin="1" tilecount="48" columns="8">
 <image source="desertSet.png" width="265" height="199"/>
 <tile id="30">
  <objectgroup draworder="index" id="2">
   <object id="1" name="Collision" x="5" y="8" width="22" height="16"/>
  </objectgroup>
 </tile>
 <tile id="37">
  <properties>
   <property name="Not Collidable" type="bool" value="false"/>
  </properties>
 </tile>
 <tile id="38">
  <properties>
   <property name="Not Collidable" type="bool" value="false"/>
  </properties>
 </tile>
 <tile id="39">
  <properties>
   <property name="Not Collidable" type="bool" value="false"/>
  </properties>
 </tile>
 <tile id="45" type="Sign">
  <properties>
   <property name="Text" value=""/>
  </properties>
  <objectgroup draworder="index" id="2">
   <object id="1" name="Collision" x="4" y="18" width="22" height="12"/>
   <object id="2" name="Interaction" x="4" y="30" width="22" height="24"/>
  </objectgroup>
 </tile>
 <wangsets>
  <wangset name="Sand" type="corner" tile="29">
   <wangcolor name="" color="#ff0000" tile="-1" probability="1"/>
   <wangtile tileid="0" wangid="0,1,0,0,0,1,0,1"/>
   <wangtile tileid="1" wangid="0,1,0,0,0,0,0,1"/>
   <wangtile tileid="2" wangid="0,1,0,1,0,0,0,1"/>
   <wangtile tileid="3" wangid="0,0,0,1,0,0,0,0"/>
   <wangtile tileid="4" wangid="0,0,0,0,0,1,0,0"/>
   <wangtile tileid="5" wangid="0,1,0,0,0,1,0,1"/>
   <wangtile tileid="6" wangid="0,1,0,0,0,0,0,1"/>
   <wangtile tileid="7" wangid="0,1,0,1,0,0,0,1"/>
   <wangtile tileid="8" wangid="0,0,0,0,0,1,0,1"/>
   <wangtile tileid="10" wangid="0,1,0,1,0,0,0,0"/>
   <wangtile tileid="11" wangid="0,1,0,0,0,0,0,0"/>
   <wangtile tileid="12" wangid="0,0,0,0,0,0,0,1"/>
   <wangtile tileid="13" wangid="0,0,0,0,0,1,0,1"/>
   <wangtile tileid="15" wangid="0,1,0,1,0,0,0,0"/>
   <wangtile tileid="16" wangid="0,0,0,1,0,1,0,1"/>
   <wangtile tileid="17" wangid="0,0,0,1,0,1,0,0"/>
   <wangtile tileid="18" wangid="0,1,0,1,0,1,0,0"/>
   <wangtile tileid="19" wangid="0,0,0,1,0,0,0,0"/>
   <wangtile tileid="20" wangid="0,0,0,0,0,1,0,0"/>
   <wangtile tileid="21" wangid="0,0,0,1,0,1,0,1"/>
   <wangtile tileid="22" wangid="0,0,0,1,0,1,0,0"/>
   <wangtile tileid="23" wangid="0,1,0,1,0,1,0,0"/>
   <wangtile tileid="24" wangid="0,1,0,0,0,1,0,1"/>
   <wangtile tileid="25" wangid="0,1,0,0,0,0,0,1"/>
   <wangtile tileid="26" wangid="0,1,0,1,0,0,0,1"/>
   <wangtile tileid="27" wangid="0,1,0,0,0,0,0,0"/>
   <wangtile tileid="28" wangid="0,0,0,0,0,0,0,1"/>
   <wangtile tileid="29" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="32" wangid="0,0,0,0,0,1,0,1"/>
   <wangtile tileid="34" wangid="0,1,0,1,0,0,0,0"/>
   <wangtile tileid="35" wangid="0,0,0,1,0,0,0,0"/>
   <wangtile tileid="36" wangid="0,0,0,0,0,1,0,0"/>
   <wangtile tileid="40" wangid="0,0,0,1,0,1,0,1"/>
   <wangtile tileid="41" wangid="0,0,0,1,0,1,0,0"/>
   <wangtile tileid="42" wangid="0,1,0,1,0,1,0,0"/>
   <wangtile tileid="43" wangid="0,1,0,0,0,0,0,0"/>
   <wangtile tileid="44" wangid="0,0,0,0,0,0,0,1"/>
  </wangset>
  <wangset name="Brick" type="corner" tile="9">
   <wangcolor name="" color="#ff0000" tile="-1" probability="1"/>
   <wangtile tileid="0" wangid="0,0,0,1,0,0,0,0"/>
   <wangtile tileid="1" wangid="0,0,0,1,1,1,0,0"/>
   <wangtile tileid="2" wangid="0,0,0,0,0,1,0,0"/>
   <wangtile tileid="8" wangid="0,1,1,1,0,0,0,0"/>
   <wangtile tileid="9" wangid="1,1,1,1,1,1,1,1"/>
   <wangtile tileid="10" wangid="0,0,0,0,0,1,1,1"/>
   <wangtile tileid="16" wangid="0,1,0,0,0,0,0,0"/>
   <wangtile tileid="17" wangid="1,1,0,0,0,0,0,1"/>
   <wangtile tileid="18" wangid="0,0,0,0,0,0,0,1"/>
   <wangtile tileid="19" wangid="1,1,0,0,0,1,1,1"/>
   <wangtile tileid="20" wangid="1,1,1,1,0,0,0,1"/>
   <wangtile tileid="27" wangid="0,0,0,1,1,1,1,1"/>
   <wangtile tileid="28" wangid="0,1,1,1,1,1,0,0"/>
  </wangset>
 </wangsets>
</tileset>
