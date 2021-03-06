/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */package rich
package geom

class Dist2(val xMetres: Double, val yMetres: Double) extends ProdD2
{
   override def canEqual(other: Any): Boolean = other.isInstanceOf[Dist2]
   def x: Dist = Dist(xMetres)
   val y: Dist = Dist(yMetres)
   override def _1: Double = xMetres
   override def _2: Double = yMetres
   def + (op: Dist2): Dist2 = Dist2(x + op.x, y + op.y)
   def - (op: Dist2): Dist2 = Dist2(x - op.x, y - op.y)
   def * (operator: Double): Dist2 = Dist2(x * operator, y * operator)
   def / (operator: Double): Dist2 = Dist2(x / operator, y / operator)
   def magnitude: Dist = Dist(math.sqrt(xMetres.squared + yMetres.squared))
   def rotate(a: Angle): Dist2 =  Dist2.metres(x.metres * a.cos - y.metres * a.sin, x.metres * a.sin + y.metres * a.cos)
   /** Currently not working for angles greater than Pi / 2 */
   def toLatLong: LatLong = LatLong(math.asin(y / EarthPolarRadius), math.asin(x / EarthEquatorialRadius))
   
   def xPos: Boolean = x.pos
   def xNeg: Boolean = x.neg
   def yPos: Boolean = y.pos
   def yNeg: Boolean = y.neg
}


object Dist2
{
   def metres(xMetres: Double, yMetres: Double): Dist2 = new Dist2(xMetres, yMetres)
   def apply(x: Dist, y: Dist): Dist2 = new Dist2(x.metres, y.metres)
   implicit class Dist2Implicit(thisDist2: Dist2)
   {
      def / (operator: Dist): Vec2 = Vec2(thisDist2.x/ operator, thisDist2.y / operator)
   }
}

class Dist2s(val arr: Array[Double]) extends AnyVal with DoubleProduct2s[Dist2]//(length)
{
   override def newElem(d1: Double, d2: Double): Dist2 = new Dist2(d1, d2)
}

object Dist2s extends Double2sMaker[Dist2, Dist2s]
{
   implicit val factory: Int => Dist2s = i => new Dist2s(new Array[Double](i * 2))   
}


