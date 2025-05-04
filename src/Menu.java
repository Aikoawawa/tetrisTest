

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.GradientPaint;

public class Menu {
    private final Rectangle playButton = new Rectangle(450, 300, 200, 50);
    private final Rectangle quitButton = new Rectangle(450, 400, 200, 50);
    private final Rectangle titleBox = new Rectangle(350, 80, 400, 120);

    public void draw(Graphics2D g2) {
        // Semi-transparent overlay with gradient
        GradientPaint bgGradient = new GradientPaint(
            0, 0, new Color(0, 0, 0, 255),
            0, GamePanel.HEIGHT, new Color(50, 50, 100, 180)
        );
        g2.setPaint(bgGradient);
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        
        // Title Box Background
        g2.setColor(new Color(20, 20, 20, 200));
        g2.fill(titleBox);
        g2.setColor(new Color(255, 255, 255, 100));
        g2.draw(titleBox);
        
        // Title with Shadow Effect
        int titleX = 430;
        int titleY = 150;

        // Shadow
        g2.setColor(new Color(50, 50, 50, 150));
        g2.setFont(new Font("Impact", Font.BOLD, 80));
        g2.drawString("TETRIS", titleX+5, titleY+5);

        // Main text with gradient
        GradientPaint titleGradient = new GradientPaint(
            titleX, titleY-20, new Color(255, 50, 50),
            titleX, titleY+60, new Color(255, 215, 0)
        );
        g2.setPaint(titleGradient);
        g2.setFont(new Font("Impact", Font.BOLD, 80));
        g2.drawString("TETRIS", titleX, titleY);
        g2.setPaint(null); // Reset gradient

        // Subtitle
        g2.setColor(new Color(200, 200, 255));
        g2.setFont(new Font("Arial", Font.ITALIC, 24));
        g2.drawString("The Classic Block Game", titleX-15, titleY+40);

        // Buttons with improved styling
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        
        // Play Button
        GradientPaint playGradient = new GradientPaint(
            playButton.x, playButton.y, new Color(100, 255, 100),
            playButton.x, playButton.y+50, new Color(50, 200, 50)
        );
        g2.setPaint(playGradient);
        g2.fill(playButton);
        g2.setColor(Color.WHITE);
        g2.drawString("PLAY", playButton.x + 53, playButton.y + 35);
        g2.setColor(new Color(255, 255, 255, 80));
        g2.draw(playButton);
        
        // Quit Button
        GradientPaint quitGradient = new GradientPaint(
            quitButton.x, quitButton.y, new Color(255, 100, 100),
            quitButton.x, quitButton.y+50, new Color(200, 50, 50)
        );
        g2.setPaint(quitGradient);
        g2.fill(quitButton);
        g2.setColor(Color.WHITE);
        g2.drawString("QUIT", quitButton.x + 53, quitButton.y + 35);
        g2.setColor(new Color(255, 255, 255, 80));
        g2.draw(quitButton);
        
        // Footer text
        g2.setColor(new Color(200, 200, 200, 150));
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("© 2025 Tetris Clone B Group 4", 450, 550);
    }
    
    public boolean handleClick(int x, int y) {
        if (playButton.contains(x, y)) {
            return true; // Play clicked
        } else if (quitButton.contains(x, y)) {
            System.exit(0);
        }
        return false;
    }
}

