package game.sprites

import java.awt.Graphics2D
import java.awt.event.KeyEvent
import game.Game
import java.awt.Rectangle
import java.awt.Color

class Racquet(val game: Game, var x: Int = 0, var xa: Int = 0, val y: Int = 330, val width: Int = 60, val height: Int = 10) {
  def move = if (x + xa > 0 && x + xa < game.getWidth() - width) x = x + xa
  def paint(g: Graphics2D) = {
    g.setColor(Color.YELLOW)
    g.fillRect(x, y, width, height)
  }
  def stop = xa = 0
  def moveLeft = xa = -1
  def moveRight = xa = 1
  def getBounds: Rectangle = new Rectangle(x, y, width, height)
  def topY = y
}