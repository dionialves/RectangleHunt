import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import static java.lang.Thread.sleep;

public class GameEngine extends Canvas implements Runnable, MouseListener, KeyListener {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static int score = 0;
    public static int mousePositionX;
    public static int mousePositionY;
    public static boolean onClicked = false;
    public static int hitPoints = 10;
    public static boolean gameOver = false;

    private final RectangleSpawner spawner;

    public GameEngine() {
        // Seta os valores de altura e largura dentro do objeto Dimension que é atribuído
        // nas propriedades do Canvas via setPreferredSize
        this.setPreferredSize(new Dimension(GameEngine.WIDTH, GameEngine.HEIGHT));
        // Adiciona a captura dos cliques do mouse
        this.addMouseListener(this);
        // Adiciona a captura do teclado
        this.addKeyListener(this);
        // Define o tamanho dos Retângulos em 40 píxels
        this.spawner = new RectangleSpawner(40);
    }

    public void update() {
        if (!GameEngine.gameOver) {
            this.spawner.update();
            if (GameEngine.hitPoints < 0) {
                GameEngine.hitPoints = 10;
                GameEngine.gameOver = true;
            }
        }
    }

    public void render() {
        // Melhora a renderização gráfica com uma estratégia de buffer usando o BufferStrategy
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        // Seta a cor de fundo como preto
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameEngine.WIDTH, GameEngine.HEIGHT);

        if (!GameEngine.gameOver) {
            g.setColor(Color.GREEN);
            g.fillRect(GameEngine.WIDTH / 2 - 140 , 20, GameEngine.hitPoints*30, 20);

            g.setColor(Color.WHITE);
            g.drawRect(GameEngine.WIDTH / 2 - 140 , 20, 300, 20);

            g.setColor(Color.RED);
            for (Rectangle rect : this.spawner.getRectangles()) {
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
            }

        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 30));

            g.drawString("Game Over ", GameEngine.WIDTH/2 - 45, GameEngine.HEIGHT/2 - 60);
            g.drawString("Your Final Score Was: " + GameEngine.score, GameEngine.WIDTH/2 - 125, GameEngine.HEIGHT/2 - 20);
            g.drawString(">> Press Enter to Play Again! <<", GameEngine.WIDTH/2 - 185, GameEngine.HEIGHT/2 + 40);
        }
        bs.show();
    }

    @Override
    public void run() {
        final int ticksPerSecond = 25;
        final int skipTicks = 1000 / ticksPerSecond;
        final int maxFrameSkip = 5;
        int loops;

        double next_game_tick = System.currentTimeMillis();

        while (true) {
            loops = 0;
            while (System.currentTimeMillis() > next_game_tick && loops < maxFrameSkip) {

                this.update();
                next_game_tick += skipTicks;
                loops++;
            }
            this.render();
        }
    }

    public static void main(String[] args) {
        GameEngine game = new GameEngine();

        // Bloco de código responsável por construir a janela
        JFrame frame = new JFrame("## Rectangle Hunt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new Thread(game).start();
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        GameEngine.onClicked = true;
        GameEngine.mousePositionX = e.getX();
        GameEngine.mousePositionY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (GameEngine.gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                GameEngine.gameOver = false;
                GameEngine.score = 0;
                GameEngine.hitPoints = 10;
                this.spawner.clearRectangles();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}