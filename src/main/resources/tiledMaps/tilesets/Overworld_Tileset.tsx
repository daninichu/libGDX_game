<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="Overworld_Tileset" tilewidth="16" tileheight="16" tilecount="234" columns="18">
 <image source="Overworld_Tileset.png" width="288" height="208"/>
 <tile id="61" type="Sign">
  <properties>
   <property name="Text" value=""/>
  </properties>
  <objectgroup draworder="index" id="2">
   <object id="1" name="Collision" x="2" y="4" width="12" height="9"/>
   <object id="2" name="Interaction" x="0" y="13" width="16" height="12"/>
  </objectgroup>
 </tile>
 <tile id="75">
  <objectgroup draworder="index" id="3">
   <object id="2" name="Collision" x="3" y="5" width="9" height="8"/>
  </objectgroup>
 </tile>
 <tile id="76">
  <objectgroup draworder="index" id="2">
   <object id="1" name="Collision" x="4" y="5" width="10" height="8"/>
  </objectgroup>
 </tile>
 <tile id="77" type="Bonfire">
  <properties>
   <property name="Map File" value=""/>
  </properties>
  <objectgroup draworder="index" id="2">
   <object id="1" name="Collision" x="2" y="4" width="12" height="7"/>
   <object id="2" name="Spawn" x="8" y="-8">
    <point/>
   </object>
   <object id="3" name="Interaction" x="-8" y="-8" width="32" height="32"/>
  </objectgroup>
 </tile>
 <wangsets>
  <wangset name="Grass" type="corner" tile="24">
   <wangcolor name="" color="#ff0000" tile="113" probability="1"/>
   <wangtile tileid="24" wangid="1,1,1,1,1,1,1,1"/>
   <wangtile tileid="108" wangid="0,1,0,0,0,1,0,1"/>
   <wangtile tileid="109" wangid="0,1,0,0,0,0,0,1"/>
   <wangtile tileid="110" wangid="0,1,0,1,0,0,0,1"/>
   <wangtile tileid="111" wangid="0,0,1,1,1,0,0,0"/>
   <wangtile tileid="112" wangid="0,0,1,1,1,1,1,0"/>
   <wangtile tileid="113" wangid="0,0,0,0,1,1,1,0"/>
   <wangtile tileid="126" wangid="0,0,0,0,0,1,0,1"/>
   <wangtile tileid="128" wangid="0,1,0,1,0,0,0,0"/>
   <wangtile tileid="129" wangid="1,1,1,1,1,0,0,0"/>
   <wangtile tileid="131" wangid="1,0,0,0,1,1,1,1"/>
   <wangtile tileid="144" wangid="0,0,0,1,0,1,0,1"/>
   <wangtile tileid="145" wangid="0,0,0,1,0,1,0,0"/>
   <wangtile tileid="146" wangid="0,1,0,1,0,1,0,0"/>
   <wangtile tileid="147" wangid="1,1,1,0,0,0,0,0"/>
   <wangtile tileid="148" wangid="1,1,1,0,0,0,1,1"/>
   <wangtile tileid="149" wangid="1,0,0,0,0,0,1,1"/>
  </wangset>
  <wangset name="Dirt" type="corner" tile="54">
   <wangcolor name="" color="#ff0000" tile="-1" probability="1"/>
   <wangtile tileid="54" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="109" wangid="0,0,0,1,0,1,0,0"/>
   <wangtile tileid="126" wangid="0,1,0,1,0,0,0,0"/>
   <wangtile tileid="128" wangid="0,0,0,0,0,1,0,1"/>
   <wangtile tileid="145" wangid="0,1,0,0,0,0,0,1"/>
  </wangset>
 </wangsets>
</tileset>
