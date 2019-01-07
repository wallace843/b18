package team_B18_2;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.utils.Vector2D;

public class Bola {
	private Vector2D posBolaAnt;
	private double tempBolaAnt;
	
	public Bola(Vector2D posicao, double instante) {
		this.posBolaAnt = posicao;
		this.tempBolaAnt = instante;
	}
	
	public Vector2D pegarVelocidadeBola(PlayerCommander commander, Vector2D bolaPosicao, double instanteMedicao) {
		Vector2D posBolaAtual;
		double tempBolaAtual;
		if(instanteMedicao - tempBolaAnt > 1000) {
			posBolaAnt = bolaPosicao;
			tempBolaAnt = instanteMedicao;
			posBolaAtual = commander.perceiveFieldBlocking().getBall().getPosition();
			tempBolaAtual = System.currentTimeMillis();
		}else {
			posBolaAtual = bolaPosicao;
			tempBolaAtual = instanteMedicao;
		}
		
		Vector2D velocity = posBolaAtual.sub(posBolaAnt);
		velocity = velocity.multiply(1/(tempBolaAtual - tempBolaAnt));
		posBolaAnt = posBolaAtual;
		tempBolaAnt = tempBolaAtual;
		return velocity;
	}
}
