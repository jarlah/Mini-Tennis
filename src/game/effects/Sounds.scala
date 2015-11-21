package game.effects

import java.applet.Applet
import java.applet.AudioClip
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import java.awt.EventQueue

object Sounds {
  private var backgroundPosition = 0l
  
  private val backgroundLoop: Clip = {
    val clip = AudioSystem.getClip();
    val ais = AudioSystem.getAudioInputStream(Sounds.getClass.getResource("back.wav"))
    clip.open(ais)
    clip
  }
  
  def stopBackgroundMusic = EventQueue.invokeLater(new Runnable() {
    def run = {
      backgroundPosition = Sounds.backgroundLoop.getLongFramePosition
      Sounds.backgroundLoop.stop
    }
  })

  def startBackgroundMusic = EventQueue.invokeLater(new Runnable() {
    def run = {
      Sounds.backgroundLoop.setFramePosition(backgroundPosition.toInt)
      Sounds.backgroundLoop.loop(Clip.LOOP_CONTINUOUSLY)
    }
  })

  val gameover: AudioClip = Applet.newAudioClip(Sounds.getClass.getResource("gameover.wav"));

  val ball: AudioClip = Applet.newAudioClip(Sounds.getClass.getResource("ball.wav"));
}