import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class RectangleSpawner extends Rectangle {
    private final Random random = new Random();
    private final ArrayList<Rectangle> rectangles = new ArrayList<>();
    private int rectSize;
    private int probability;
    private int speed;

    public RectangleSpawner(int rectSize) {
        // Recebe o tamanho do retângulo
        this.setRectSize(rectSize);
        this.setProbability(10);
        this.setSpeed(5);
    }

    public void update() {
        // Nesa condição define uma porcentágem para a criação de um retângulo
        // essa probabilidade está contida na variável probability
        if (random.nextInt(100) < this.getProbability()) {
            // Chama método para criação do retângulo
            this.addRectangle();
        }

        // Movimento dos retângulos para a direita, respeitando uma velocidade pré determinada
        for (int i = 0; i < this.rectangles.size(); i++) {
            Rectangle rect = this.rectangles.get(i);
            rect.x += getSpeed();

            // Remove retângulos que saem da tela
            if (rect.x > GameEngine.WIDTH) {
                this.rectangles.remove(i);
                GameEngine.hitPoints-=1;
                i--;
            }
            // Nesse bloco é avaliado se o houve um clique pelo mouse, e realizado avaliações para saber se o clique
            // foi na area de um dos retângulos existentes na tela, para então fazer a exclusão do mesmo.
            if (GameEngine.onClicked) {
                if (GameEngine.mousePositionX >= rect.x &&
                        GameEngine.mousePositionX < rect.x + rect.width) {

                    if (GameEngine.mousePositionY >= rect.y &&
                            GameEngine.mousePositionY < rect.y + rect.height) {
                        GameEngine.score++;
                        GameEngine.onClicked = false;
                        this.rectangles.remove(i);
                    }
                }
            }
        }
    }

    private void addRectangle() {
        // Variável que vai guardar a informação de uma posição y, é valida para ser usada
        // com isso que evitar que um retângulo seja criado em cima de outro
        boolean cleanNumber = true;

        // Pegar um valor de y aleatório, a lógica abaixo evita que se pegue um número menor que 40
        // evitando que o retângulo seja criado e passe por cima da barra de vida
        int y = 40 + random.nextInt((GameEngine.HEIGHT - this.getRectSize()) - 40);

        // Bloco for para saber se a posição escolhida de y pode ser usada
        for (Rectangle rect : this.getRectangles()) {
            // Verifica a diferença entre Y e X para saber se existe algum retângulo próximo da posição
            // que o proximo será criado
            if (Math.abs(rect.y - y) <= 42) {
                if (Math.abs(rect.x - 42) <= 42) {
                    cleanNumber = false;
                }
            }
        }
        // Avalia se a variável continua verdadeira, se sim cria o objeto
        if (cleanNumber) {
            this.addRectangles(new Rectangle(0, y, rectSize, rectSize));
        }
    }

    public ArrayList<Rectangle> getRectangles() {
        return this.rectangles;
    }

    public void addRectangles(Rectangle rectangle) {
        this.rectangles.add(rectangle);
    }

    public void clearRectangles() {
        this.rectangles.clear();
    }

    public int getRectSize() {
        return rectSize;
    }

    public void setRectSize(int rectSize) {
        this.rectSize = rectSize;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
