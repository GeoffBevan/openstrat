/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package rich
package pSpace
import geom._
import pDisp._
import Colour._

/** Currently extending EuclidGui, I'm not sure if this is helpful, as the user can not move about in the map except change focus. */
case class Planets(val canv: CanvasLike) extends EuclidGui
{
   val maxOrbit: Dist = 3700.millionMiles
   var years: Double = 0
   var paused: Boolean = false
   def pausedStr = paused.fold("Restart", "Pause")
  // override var mapLeft, mapBottom = -maxOrbit   
  // override var mapRight, mapTop = maxOrbit
   override var scale = 0.5.millionMiles   
   override val scaleMax: Dist = 10.millionMiles
   override val scaleMin: Dist = 100000.miles
   val earthDist = 93.millionMiles
   /** Years per second */
   
   mapPanel.backColour = Black

   mapPanel.mouseUp = (a, b, s) => println(s)
   case class Planet(dist: Dist, colour: Colour, name: String)
   {
      var posn: Dist2 = Dist2(dist, 0.metre)
      //Gets the angle and the multiplies by the scala. (* dist) at end
      def move(elapsed: Double): Unit =
      {
         val auRatio = dist / earthDist        
         posn = Vec2.circlePtClockwise(elapsed * 0.001 / math.sqrt(auRatio.cubed)) *  dist
         }
      def size = 10
      def paint = Circle(size).fillFixed(this, colour).slate(toCanv(posn))
      override def toString = name
   }
   val mercury = Planet(36.millionMiles, Colour.Gray, "Mercury")
   val venus = Planet(67.2.millionMiles , White, "Venus" )      
   val earth = Planet(earthDist, Blue, "Earth")
   val mars = Planet(141.6.millionMiles , Red, "Mars")
   val jupiter = Planet(483.6.millionMiles , Orange, "Jupiter")
   val saturn = Planet(886.7.millionMiles , Gold, "Saturn")
   val uranus = Planet(1784.0.millionMiles , Colour.BrightSkyBlue, "Uranus")
   val neptune = Planet(2794.4.millionMiles , LightGreen, "Neptune")
   val pluto = Planet(3674.5.millionMiles, Colour.SandyBrown, "Pluto")
   object Sun extends Planet(0.millionMiles, Yellow, "Sun")
   {
      override def move(elapsed: Double): Unit = {}
      override val size = 14
   }
   val pls: Seq[Planet] = Seq(mercury, venus, earth, mars, jupiter, saturn, uranus, neptune, pluto, Sun)
   var planetFocus: Planet = earth
   override def eTop(): Unit = ???
   def fBut(planet: Planet) = buttonStd(planet.name, () => {planetFocus = planet; repaintMap}, planet.colour)
   def pause = buttonStd(pausedStr, () => { println(pausedStr); paused = !paused; reTop(cmds)})
   
   def cmds: Seq[ShapeSubj] = zoomable.:+(pause) ++ pls.map(fBut)   
   reTop(cmds)
   canv.frameZero((el, st) => out(el, st))
   def mapObjs = pls.map(_.paint)
   def out(elapsed: Double, startTime: Double): Unit =
   {
      pls.foreach(_.move(elapsed))
      mapFocus = planetFocus.posn
      repaintMap()
      canv.frame(out, startTime)           
   }      
}
