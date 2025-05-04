import java.net.URL;
import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.Type;

public class Sound {
    private Clip musicClip;
    private URL[] url = new URL[10];
    private long clipPosition = 0;
    private boolean isPaused = false;

    public Sound() {
        try {
            // Update paths to match the correct folder structure
            url[0] = getClass().getResource("/audio/Original Tetris theme (Tetris Soundtrack).wav");
            url[1] = getClass().getResource("/audio/delete line.wav");
            url[2] = getClass().getResource("/audio/gameover.wav");
            url[3] = getClass().getResource("/audio/rotation.wav");
            url[4] = getClass().getResource("/audio/touch floor.wav");

            // Check if any URL is null (file not found)
            for (int i = 0; i < url.length; i++) {
                if (url[i] == null) {
                    System.err.println("Audio file not found for index: " + i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(int i, boolean isMusic) {
        try {
            if (url[i] == null) {
                System.err.println("Cannot play sound. File not found for index: " + i);
                return;
            }

            if (isMusic) {
                if (musicClip != null && musicClip.isOpen()) {
                    musicClip.close();
                }

                AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
                musicClip = AudioSystem.getClip();
                musicClip.open(ais);

                musicClip.addLineListener(event -> {
                    if (event.getType() == Type.STOP && !isPaused) {
                        musicClip.close();
                    }
                });

                ais.close();
                musicClip.start();
                isPaused = false;
            } else {
                // For sound effects
                AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);

                clip.addLineListener(event -> {
                    if (event.getType() == Type.STOP) {
                        clip.close();
                    }
                });

                clip.start();
                ais.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (musicClip != null && musicClip.isRunning()) {
            clipPosition = musicClip.getMicrosecondPosition();
            musicClip.stop();
            isPaused = true;
        }
    }

    public void resume() {
        if (musicClip != null && isPaused) {
            try {
                if (!musicClip.isOpen()) {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(url[0]);
                    musicClip.open(ais);
                    ais.close();
                }
                musicClip.setMicrosecondPosition(clipPosition);
                musicClip.start();
                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
                isPaused = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loop() {
        if (musicClip != null && musicClip.isOpen()) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            clipPosition = 0;
            isPaused = false;
        }
    }
}

