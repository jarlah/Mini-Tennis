package game

import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.ActionEvent
import game.sprites.Ball
import game.sprites.Racquet
import game.sprites.Racquet
import javax.swing.AbstractAction
import javax.swing.JPanel
import javax.swing.KeyStroke._
import java.awt.event.KeyEvent._
import java.awt.event.InputEvent
import javax.swing.JOptionPane
import game.sprites.Racquet
import game.sprites.Racquet
import javax.swing.JFrame
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import java.awt.Color
import java.awt.EventQueue
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine
import game.effects.Sounds

class Game extends JPanel {
  private var motion: Boolean = true
  private var run: Boolean = true
  private val ABORT = -1
  private val racquet: Racquet = new Racquet(this)
  private val ball: Ball = new Ball(
    () => this.getWidth,
    () => this.getHeight,
    () => this.gameOver(),
    () => racquet.y,
    () => racquet.getBounds)

  private val inputs = this.getInputMap
  inputs.put(getKeyStroke(VK_SPACE, 0, true), "pauseGame")
  inputs.put(getKeyStroke(VK_LEFT, 0, true), "stopRacquet")
  inputs.put(getKeyStroke(VK_LEFT, 0, false), "moveLeft")
  inputs.put(getKeyStroke(VK_RIGHT, 0, true), "stopRacquet")
  inputs.put(getKeyStroke(VK_RIGHT, 0, false), "moveRight")

  private val actions = this.getActionMap
  actions.put("pauseGame", new AbstractAction() {
    def actionPerformed(e: ActionEvent) {
      if (run) {
        motion = if (motion) {
          Sounds.stopBackgroundMusic
          false
        } else {
          Sounds.startBackgroundMusic
          true
        }
      }
    }
  })
  actions.put("stopRacquet", new AbstractAction() {
    def actionPerformed(e: ActionEvent) {
      if (motion) racquet.stop
    }
  })
  actions.put("moveLeft", new AbstractAction() {
    def actionPerformed(e: ActionEvent) {
      if (motion) racquet.moveLeft
    }
  })
  actions.put("moveRight", new AbstractAction() {
    def actionPerformed(e: ActionEvent) {
      if (motion) racquet.moveRight
    }
  })

  def move = {
    ball.move
    racquet.move
  }

  lazy val image = new ImageIcon(ImageIO.read(this.getClass.getResourceAsStream("back.jpg"))).getImage()

  override def paint(g: Graphics): Unit = {
    super.paint(g)
    val g2d = g.asInstanceOf[Graphics2D]
    g2d.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON)
    ball.paint(g2d)
    racquet.paint(g2d);
  }

  def gameOver() = {
    motion = false
    run = false
  }
}

object Game extends App {
  val game = new Game

  val frame = new JFrame("Mini Tennis")
  frame.add(game)
  frame.setSize(300, 365)
  frame.setVisible(true)
  frame.setResizable(false)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  Sounds.startBackgroundMusic

  var runLoop = true
  while (runLoop) {
    if (game.motion) {
      game.move
      game.repaint()
    } else if (!game.run) {
      runLoop = false
    }
    Thread.sleep(10)
  }

  Sounds.stopBackgroundMusic
  Sounds.gameover.play
}