package sla.api;

import java.text.DecimalFormat;
import java.util.LinkedList;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class ExemploCarrinhoColisores extends FX_CG_2D_API {

	// Controle das setas do teclado
	boolean up = false;
	boolean down = false;
	boolean left = false;
	boolean right = false;

	// Posi��o do carro, �ngulo de rota��o e acelera��o.
	float px = 0.0f;
	float py = 0.0f;
	float ang = 0.0f;
	float ac = 0.0f;

	// Controles para acelera��o
	boolean des = false;
	boolean dir = true;

	// "textura" do carro
	Image carro;

	public ExemploCarrinhoColisores(Stage stage) {
		super("Carrinho com Colisores", stage, 100, 1200, 800);
	}

	public static void main(String[] args) {
		App.iniciar(cena -> new ExemploCarrinhoColisores(cena), args);
	}

	@Override
	public void acaoAoSair() {
		// TODO Auto-generated method stub

	}

	@Override
	public void acaoAoIniciar() {
		carro = new Image(getClass().getResourceAsStream("/imagens/carro.png"));
	}

	@Override
	public void teclaPressionada(KeyEvent e) {
		if (e.getCode() == KeyCode.UP) {
			up = true;
		}

		if (e.getCode() == KeyCode.DOWN) {
			down = true;
		}

		if (e.getCode() == KeyCode.RIGHT) {
			right = true;

		}

		if (e.getCode() == KeyCode.LEFT) {
			left = true;
		}

	}

	@Override
	public void teclaLiberada(KeyEvent e) {
		if (e.getCode() == KeyCode.UP) {
			up = false;
			des = true;
		}

		if (e.getCode() == KeyCode.DOWN) {
			down = false;
			des = true;
		}

		if (e.getCode() == KeyCode.RIGHT) {
			right = false;
		}

		if (e.getCode() == KeyCode.LEFT) {
			left = false;
		}

	}

	@Override
	public void teclaDigitada(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cliqueDoMouse(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void movimentoDoMouse(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void movimentoDoMousePressionado(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressionado(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void desacelera() {
		if (dir) {
			if (ac > 0) {
				ac -= 1.0 / fps();
				if (ac < 0) {
					ac = 0.0f;
				}
			}
		} else {
			if (ac < 0) {
				ac += 1.0 / fps();
				if (ac > 0) {
					ac = 0.0f;
				}
			}
		}
	}

	@Override
	public void atualizar() {
		// Atualiza a posi��o do carro conforme acelera��o e dire��o do �ngulo de
		// rota��o.
		px += Math.cos(ang) * ac;
		py += Math.sin(ang) * ac;

		if (des) {
			desacelera();
		}

		if (up) {
			if (ac < 3.0) {
				des = false;
				ac += 1.0 / fps();
				dir = true;
			} else {
				ac = 3.0f;
			}
		}

		if (left) {
			ang -= 3.0 / fps();
		}

		if (right) {
			ang += 3.0 / fps();
		}

		if (down) {
			if (ac > -2.0) {
				des = false;
				ac -= 1.0 / fps();
				dir = false;
			} else {
				ac = -2.0f;
			}
		}

	}

	@Override
	public void desenhar() {
		// Primeiro desenhos o fundo com a cor branca
		preenchimento(Color.WHITE);
		retangulo(0, 0, larguraTela(), alturaTela(), Estilo.PREENCHIDO);

		// Camera seguidora do carro
		double camX = px;
		double camY = py;
		transladar(-camX, -camY);

		// Cen�rio
		contorno(2, Color.GREEN);
		retangulo(100, 100, 200, 200, Estilo.LINHAS);
		contorno(2, Color.RED);
		retangulo(300, 600, 350, 670, Estilo.LINHAS);
		contorno(2, Color.BLUE);
		retangulo(1000, 800, 1200, 1000, Estilo.LINHAS);

		// Empilha o estado atual para fazer as transforma��es do carro.
		empilhar();
		// Translada o centro do sistema de coordenadas para o centro da tela.
		transladar(larguraTela() / 2, alturaTela() / 2);

		/** TRATAMENTO DO CEN�RIO COM COLIS�O **/
		preenchimento(Color.BLACK);
		// Rectangle2D colisor1 = new Rectangle2D(150, -150, 50, 200);
		// retangulo(colisor1, Estilo.PREENCHIDO);
		// Rectangle2D colisor2 = new Rectangle2D(350, 100, 200, 50);
		// retangulo(colisor2, Estilo.PREENCHIDO);
		Polygon colisor1 = new Polygon(150, -150, 200, -150, 150, -350, 200, -350);
		poligono(colisor1, Estilo.PREENCHIDO);
		// retangulo(colisor1, Estilo.PREENCHIDO);
		// Rectangle2D colisor2 = new Rectangle2D(350, 100, 200, 50);
		// retangulo(colisor2, Estilo.PREENCHIDO);

		// Adicionando colisores.
		// LinkedList<Rectangle2D> colisores = new LinkedList<Rectangle2D>();
		// colisores.add(colisor1);
		// colisores.add(colisor2);
		LinkedList<Polygon> colisores = new LinkedList<Polygon>();
		colisores.add(colisor1);

		// Colisor do carro
		Polygon pc = new Polygon(px + carro.getWidth() / 2 - 10, py - carro.getHeight() / 2 + 10,
				px + carro.getWidth() / 2 - 10, py + carro.getHeight() / 2 - 10, px - carro.getWidth() / 2 + 10,
				py + carro.getHeight() / 2 - 10, px - carro.getWidth() / 2 + 10, py - carro.getHeight() / 2 + 10);

		// Leva o carro para a origem do sistema, aplica as transforma��es de
		// rota��o e leva de volta para seu local correto.
		empilhar();
		transladar(px, py);
		rotacionar(ang * 57.2957f); // 1 radiano = 57.2957 graus.
		transladar(-px, -py);
		// Desenha o carro.
		imagem(carro, px - carro.getWidth() / 2, py - carro.getHeight() / 2);
		desempilhar();

		pc = rotacionar(pc, ang * 57.2957f); // 1 radiano = 57.2957 graus.
		poligono(pc, Estilo.LINHAS);

		// Tratamento da colis�o
		if (colisao(colisor1, pc)) {
			ac = 0;
		}

		desempilhar();

		// Mostra a velocidade
		DecimalFormat df = new DecimalFormat("0");
		String velocidade = df.format(ac / 3 * 100);
		String msg = "Velocidade = " + velocidade + " km/h";
		contorno(1, Color.BLACK);
		preenchimento(Color.WHITE);
		texto(msg, px + larguraTela() / 2 - 120, py + 40, 24);

		// Camera seguidora do carro.
		transladar(camX, camY);

	}
}