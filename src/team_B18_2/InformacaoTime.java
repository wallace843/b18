package team_B18_2;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.utils.Vector2D;

public class InformacaoTime {
	private PlayerCommander jogadorComBola;
	private Vector2D posicaoLancamento;
	
	public Vector2D getPosicaoLancamento() {
		return posicaoLancamento;
	}
	public void setPosicaoLancamento(Vector2D posicaoLancamento) {
		this.posicaoLancamento = posicaoLancamento;
	}
	public PlayerCommander getJogadorComBola() {
		return jogadorComBola;
	}
	public void setJogadorComBola(PlayerCommander jogadorComBola) {
		this.jogadorComBola = jogadorComBola;
	}
}
