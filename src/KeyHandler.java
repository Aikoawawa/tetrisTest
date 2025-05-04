import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public static boolean upPressed, downPressed, leftPressed, rightPressed, pausePressed;
    public static boolean hardDropPressed = false;
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        // Pause handling
        if(code == KeyEvent.VK_ESCAPE) {
            pausePressed = true;
        } else {
			pausePressed = false;
		}
        
        // Game controls
        if(PlayManager.gameState == GameState.PLAYING) {
            if(code == KeyEvent.VK_W) upPressed = true;
            if(code == KeyEvent.VK_A) leftPressed = true;
            if(code == KeyEvent.VK_S) downPressed = true;
            if(code == KeyEvent.VK_D) rightPressed = true;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { // Spacebar for hard drop
            hardDropPressed = true;
            System.out.println("Hard drop executed");
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W) upPressed = false;
        if(code == KeyEvent.VK_A) leftPressed = false;
        if(code == KeyEvent.VK_S) downPressed = false;
        if(code == KeyEvent.VK_D) rightPressed = false;
        if(code == KeyEvent.VK_ESCAPE) pausePressed = false;
        if (code == KeyEvent.VK_SPACE) {
            hardDropPressed = false; // Reset the flag on key release
        }
    }
}

