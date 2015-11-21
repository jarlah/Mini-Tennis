package game.sprites

import java.awt.Graphics2D
import game.Game
import java.awt.Rectangle
import game.effects.Sounds
import java.awt.Color
import game.Game

class Ball(
    val width: () => Int,
    val height: () => Int,
    val gameOver: () => Unit,
    val racquetY: () => Int,
    val racquetRectangle: () => Rectangle,
    var x: Int = 0,
    var y: Int = 0,
    var xa: Int = 1,
    var ya: Int = 1,
    val diameter: Int = 30) {

  def move = {
    if (x + xa < 0) {
      xa = 1
      Sounds.ball.play
    } else if (x + xa > width() - diameter) {
      xa = -1
      Sounds.ball.play
    }

    x = x + xa

    if (y + ya < 0) {
      ya = 1
      Sounds.ball.play
    } else if (y + ya > height() - diameter) {
      gameOver()
    } else if (collision) {
      ya = -1;
      y = racquetY() - diameter;
      Sounds.ball.play
    }

    y = y + ya
  }
  def collision: Boolean = racquetRectangle().getBounds.intersects(getBounds)
  def paint(g: Graphics2D) = {
    g.setColor(Color.LIGHT_GRAY)
    g.fillOval(x, y, diameter, diameter)
  }
  def getBounds: Rectangle = new Rectangle(x, y, diameter, diameter)
}