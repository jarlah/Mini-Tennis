package game

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent._
import game.effects.Sounds
import game.sprites.Ball
import game.sprites.Racquet
import game.sprites.Racquet
import game.sprites.Racquet
import game.sprites.Racquet
import javax.imageio.ImageIO
import javax.swing.AbstractAction
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.KeyStroke._
import java.text.DateFormat

case class Height(value: Int)
case class Width(value: Int)

class Game extends JPanel {
  private lazy val image = new ImageIcon(ImageIO.read(this.getClass.getResourceAsStream("back.jpg"))).getImage()
  private var motion: Boolean = true
  private var run: Boolean = true
  private val racquet: Racquet = new Racquet(this)
  private val ball: Ball = new Ball(
    () => Width(this.getWidth),
    () => Height(this.getHeight),
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
  private val game = new Game

  private val frame = new JFrame("Mini Tennis")
  frame.setLocationRelativeTo(null)
  frame.setLayout(new BorderLayout());
  frame.add(game, BorderLayout.CENTER)
  frame.setSize(400, 385)
  frame.setResizable(true)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  val statusBar = new JPanel();
  val msg = new JLabel(" Good Day!");
  msg.setForeground(Color.WHITE);
  msg.setToolTipText("Tool Tip Here");
  val welcomedate = new JLabel();
  val now = new java.util.Date();
  val ss = DateFormat.getDateTimeInstance().format(now);
  welcomedate.setText(ss)
  welcomedate.setOpaque(true); //to set the color for jlabel
  welcomedate.setBackground(Color.black);
  welcomedate.setForeground(Color.WHITE);
  statusBar.setLayout(new BorderLayout());
  statusBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
  statusBar.setBackground(Color.DARK_GRAY);
  statusBar.add(msg, BorderLayout.WEST);
  statusBar.add(welcomedate, BorderLayout.EAST);
  frame.add("South", statusBar);
  frame.setVisible(true)

  Sounds.startBackgroundMusic
  private var runLoop = true
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