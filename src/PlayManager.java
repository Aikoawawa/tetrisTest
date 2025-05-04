//meow
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;



public class PlayManager {
    // Game State
    public static GameState gameState = GameState.MENU;
    
    // Play area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;
    
    // Mino
    public Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    public Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();
    
    //How fast the mino drops
    public static int dropInterval = 60;
    
    //Game Over
    boolean gameOver;
    
    //Effects
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();
    
    //Score
    int level = 1;
    int lines;
    int score;
    
    //7 bag shuffle
    private ArrayList<Mino> bag = new ArrayList<>();
    private int currentIndex = 0;


    public PlayManager() {
        // Play area frame
        left_x = (GamePanel.WIDTH/2) - (WIDTH/2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;
        
        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;
        
        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;
        
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }
    
    public Mino pickMino() {
        // Check if the bag is empty or all Minos have been used
        if (bag.isEmpty() || currentIndex >= bag.size()) {
            // Create a new array of Minos
            Mino[] minoIndex = {
                new Mino_L1(), new Mino_L2(), new Mino_Square(),
                new Mino_Bar(), new Mino_T(), new Mino_Z1(), new Mino_Z2()
            };

            // Clear the bag and refill it with new Minos
            bag.clear();
            Collections.addAll(bag, minoIndex);

            // Shuffle the bag to randomize the order
            Collections.shuffle(bag);

            // Reset the current index
            currentIndex = 0;
        }

        // Pick the next Mino from the shuffled bag
        Mino nextMino = bag.get(currentIndex);
        currentIndex++;
        return nextMino;
    }
    
        /*switch(i) {
            case 0: mino = new Mino_L1(); break;
            case 1: mino = new Mino_L2(); break;
            case 2: mino = new Mino_Square(); break;
            case 3: mino = new Mino_Bar(); break;
            case 4: mino = new Mino_T(); break;
            case 5: mino = new Mino_Z1(); break;
            case 6: mino = new Mino_Z2(); break;
        }
        return mino;*/
    
    public void update() {
        if (GameState.PLAYING != GameState.PLAYING) return; // Ensure the game is active

        // Check for hard drop
        if (KeyHandler.hardDropPressed) {
            hardDrop();
            KeyHandler.hardDropPressed = false; // Reset the flag
            return; // Skip the rest of the update logic for this frame
        }

        // Regular update logic for the current Mino
        if (!currentMino.active) {
            handleMinoDeactivation();
        } else {
            currentMino.update();
        }
    }
    
    private void handleMinoDeactivation() {
        staticBlocks.add(currentMino.b[0]);
        staticBlocks.add(currentMino.b[1]);
        staticBlocks.add(currentMino.b[2]);
        staticBlocks.add(currentMino.b[3]);

        // Check if the game is over
        if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
            gameOver = true;
            GamePanel.music.stop();
            GamePanel.se.play(2, false);
        }

        currentMino.deactivating = false;
        currentMino = nextMino;
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

        checkDelete();
    }
    
    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;
        
        while(x < right_x && y < bottom_y) {
            for(int i = 0; i < staticBlocks.size(); i++) {
                if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    blockCount++;
                }
            }
            
            x += Block.SIZE;
            
            if(x == right_x) {
                if(blockCount == 12) {
                    effectCounterOn = true;
                    effectY.add(y);
                    
                    for(int i = staticBlocks.size()-1; i > -1; i--) {
                        if(staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }
                    
                    //Increases line count when a line is cleared 
                    lineCount++;
                    lines++;
                    
                    //Drop speed increases when a certain score is reached
                    //1 is the fastest
                    if(lines % 10 == 0 && dropInterval > 1) {
                        level++;
                        if(dropInterval > 10) {
                            dropInterval -= 10;
                        }
                        else {
                            dropInterval -= 1;
                        }
                    }
                  
                    //If a line has been deleted so need to slide down the blocks that are above it
                    for(int i = 0; i < staticBlocks.size(); i++) {
                        //If a block is above the current y, move it down by the block size
                        if(staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }
                
                blockCount = 0;
                x = left_x;
                y += Block.SIZE;  // Changed from y = Block.SIZE to y += Block.SIZE
            }
        }
        
        //Add score
        if(lineCount > 0) {
        	GamePanel.se.play(1, false);
            int singleLineScore = 50 * level;
            score += singleLineScore * lineCount;
        }
    }
    
    public void draw(Graphics2D g2) {
        // Draw play area frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);
        
        // Draw next mino frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.setColor(Color.white);
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+60, y+60);
        
        //Draw score frame
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 50;
        g2.drawString("LEVEL: " + level, x, y); y+=70;
        g2.drawString("LINES: " + lines, x, y); y+=70;
        g2.drawString("SCORE: " + score, x, y); y+=70;
        
        if(gameState != GameState.MENU) {
            // Draw current mino
            if(currentMino != null) {
                currentMino.draw(g2);
            }
            
            // Draw next mino
            nextMino.draw(g2);
            
            // Draw static blocks
            for(Block block : staticBlocks) {
                block.draw(g2);
            }
            
            //Draw Effects
            if (effectCounterOn) {
                effectCounter++;
                
                // Alternate colors for flashing effect
                Color flashColor = (effectCounter % 4 < 2) ? Color.red : Color.white;
                g2.setColor(flashColor);
                
                // Draw the lines with a slight transparency
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
                for (int i = 0; i < effectY.size(); i++) {
                    g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
                }
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                
                // End after 8 frames (2 full flashes)
                if (effectCounter >= 8) {
                    effectCounterOn = false;
                    effectCounter = 0;
                    effectY.clear();
                }
            }
            
            //Draw Game Over
            if(gameOver) {
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect(left_x, top_y, WIDTH, HEIGHT);
                g2.setColor(Color.RED);
                g2.setFont(new Font("Arial", Font.BOLD, 50));
                g2.drawString("GAME OVER", left_x + 20, top_y + 320);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.PLAIN, 24));
            }
            // Draw Pause
            else if(gameState == GameState.PAUSED) {
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect(left_x, top_y, WIDTH, HEIGHT);
                g2.setColor(Color.YELLOW);
                g2.setFont(new Font("Arial", Font.BOLD, 50));
                g2.drawString("PAUSED", left_x + 70, top_y + 320);
            }
        }
        
        // Draw game title with shadow effect
        int titleX = left_x - 300;
        int titleY = top_y + 350;

        // Shadow
        g2.setColor(new Color(50, 50, 50, 150));
        g2.setFont(new Font("Impact", Font.BOLD, 80));
        g2.drawString("TETRIS", titleX+5, titleY+5);

        // Main text
        g2.setColor(new Color(255, 50, 50));  // Red color
        g2.setFont(new Font("Impact", Font.BOLD, 80));
        g2.drawString("TETRIS", titleX, titleY);

        // Optional subtitle
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("The Classic Block Game", titleX+8, titleY+30);
    }
    
    public void hardDrop() {
        // Keep moving the current Mino down until it collides
        while (!currentMino.bottomCollision) {
            currentMino.update(); // Move the Mino down
            currentMino.checkMovementCollision(); // Check for collisions
        }

        // Deactivate the Mino immediately
        currentMino.active = false;

        // Play a sound effect for the hard drop (optional)
        GamePanel.se.play(5, false); // Assuming sound effect index 5 is for hard drop
    }
}
