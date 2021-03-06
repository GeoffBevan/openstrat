/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package rich
package geom
package pDisp

trait CanvasMulti extends CanvUser
{   
   var panels: Seq[Panel] = Seq()
   def addPanel(clipPoly: Vec2s, simple: Boolean = false, cover: Boolean = true): Panel =
   {
      val newPanel = Panel(this, clipPoly, simple, cover)
      panels :+= newPanel
      newPanel
   }     
   canv.mouseUp = (v, b) =>
      {         
         panels.find(_.clipPoly.ptInPolygon(v)).foreach(pan =>
            {               
               val objs: List[AnyRef] = pan.subjs.flatMap(_(v)).reverse
               pan.simple match
               {
                  case true => objs match 
                  {
                     case Seq(f: Function0[_]) => f()
                     case Seq(cmd: M3Cmd) => cmd(b)
                     case obj => println(obj.toString + " not recognised")
                  }
                  case _ => pan.mouseUp(v, b, objs)
               }
            })
      }
   canv.mouseDown = (v, b) =>
      {
         panels.find(_.clipPoly.ptInPolygon(v)).foreach(pan =>
            {               
               val objs: List[AnyRef] = pan.subjs.flatMap(_(v)).reverse
               pan.simple match
               {
                  case true => //objs match 
//                  {
//                     case Seq(f: Function0[_]) => f()
//                     case Seq(cmd: M3Cmd) => cmd(b)
//                     case obj => println(obj.toString + " not recognised")
//                  }
                  case _ => pan.mouseDown(v, b, objs)
               }
            })
      }
//   canv.mouseMoved = (v, b) =>
//      {         
//         panels.find(_.clipPoly.ptInPolygon(v)).foreach(pan =>
//            {               
//               val objs: Seq[Any] = pan.subjs.flatMap(_(v)).reverse
//               pan.simple match
//               {
//                  case true => objs match 
//                  {
//                     case Seq(f: Function0[_]) => f()
//                     case Seq(cmd: M3Cmd) => cmd(b)
//                     case obj => println(obj.toString + " not recognised")
//                  }
//                  case _ => pan.fMouseMoved(v, b, objs)
//               }
//            })
//      }
    canv.mouseDragged = (v, b) =>
      {         
         panels.find(_.clipPoly.ptInPolygon(v)).foreach(pan =>
            {               
               val objs: Seq[Any] = pan.subjs.flatMap(_(v)).reverse
               pan.simple match
               {
                  case true => objs match 
                  {
                     case Seq(f: Function0[_]) => f()
                     case Seq(cmd: M3Cmd) => cmd(b)
                     case obj => println(obj.toString + " not recognised")
                  }
                  case _ => pan.fMouseDragged(v, b, objs)
               }
            })
      }  
      
   def refresh(): Unit = panels.foreach(refreshPanel)   
   
   def refreshPanel(panel: Panel): Unit =
   {
      val clipPoly = panel.clipPoly
      panel.subjs = Nil
      canv.gcSave()
      canv.clip(clipPoly)
      canv.polyFill(clipPoly, panel.backColour)
      val movedObjs: Seq[CanvObj[_]] = panel.canvObjs.slate(panel.cen)
      paintObjs(movedObjs, panel)
      canv.gcRestore()
   }   
}