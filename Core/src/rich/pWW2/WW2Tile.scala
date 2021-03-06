/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package rich
package pWW2
import geom._
import pEarth._


class W2Tile(val x: Int, val y: Int, val terr: Terrain) extends ETile
{
   var lunits: List[Army] = Nil

}

object W2Tile
{
   def apply(x: Int, y: Int, terr: Terrain) = new W2Tile(x, y, terr)
   implicit object W2TileIsType extends IsType[W2Tile]
   {
      override def isType(obj: AnyRef): Boolean = obj.isInstanceOf[W2Tile]
      override def asType(obj: AnyRef): W2Tile = obj.asInstanceOf[W2Tile]
   }
   implicit object W2TilePersist extends Persist3[Int, Int, Terrain, W2Tile]('W2Tile, obj => (obj.x , obj.y, obj.terr), apply)
}

