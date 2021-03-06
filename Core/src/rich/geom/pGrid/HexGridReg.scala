/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package rich
package geom
package pGrid

class HexGridReg[TileT <: Tile](xTileMin: Int, xTileMax: Int, yTileMin: Int, yTileMax: Int)(implicit evTile: IsType[TileT]) extends
   HexGrid[TileT](xTileMin, xTileMax, yTileMin, yTileMax)
{  
   val row2Start = xTileMin.incrementTill(_ % 4 == 2)
   val row4Start = xTileMin.incrementTill(_ % 4 == 0)
   val row2End = xTileMax.decrementTill(_ % 4 == 2)
   val row4End = xTileMax.decrementTill(_ % 4 == 0)
   val sideRow2Start = row2Start + 2
   val sideRow4Start = row4Start + 2
   val sideRow2End = row2End - 2
   val sideRow4End = row4End - 2
   val sideRowOddStart = (row2Start + row4Start) / 2
   val sideRowOddEnd = (row2End + row4End) / 2
   
   /** rows 2, 6,10 ... -2, -6, -10 ... */
   def row2sForeach(f: Int => Unit): Unit =
      for { y <- yTileMin.incrementTill(_ % 4 == 2) to yTileMax.decrementTill(_ % 4 == 2) by 4 } yield f(y)
      
   /** rows 4, 8 12 ... 0, -4, -8 ... */
   def row4sForeach(f: Int => Unit): Unit =
      for { y <- yTileMin.incrementTill(_ % 4 == 0) to yTileMax.decrementTill(_ % 4 == 0) by 4 } yield f(y)
      
   override def tileXYForeach(f: (Int, Int) => Unit): Unit = 
   { row2sForeach(y => for { x <- row2Start to row2End by 4} yield f(x, y))
     row4sForeach(y => for { x <- row4Start to row4End by 4} yield f(x, y))
   }
     
   def sideXYForeach(f: (Int, Int) => Unit): Unit =
   { row2sForeach(y => for { x <- sideRow2Start to sideRow2End by 4} yield f(x, y))
     row4sForeach(y => for { x <- sideRow4Start to sideRow4End by 4} yield f(x, y))
     for {y <- (yTileMin + 1) to (yTileMax - 1) by 2
        x <- sideRowOddStart to sideRowOddEnd by 2
     } yield f(x, y)     
   }    
}
