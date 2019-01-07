package team_B18_2;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.Vector2D;

public class Goleiro extends JogadorBase{
	
	public Goleiro(PlayerCommander commander, InformacaoTime informacao) {
		super(commander, informacao);
	}
	
	public void beforeKickOffAcao() {
		habilidade.virarParaPonto(habilidade.pegarPosicaoBola());
		habilidade.posicaoInicial(new Vector2D(-50,0));
	}
	
	public void estadoPosicionarDefesa() {
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D localBola = habilidade.pegarPosicaoBola();
		Vector2D localGol = habilidade.localGol(jogador.getSide());
		if(habilidade.naZonaDeChute()) {
			ESTADO = JogadorEstado.CHUTAR_BOLA;
			return;
		}else if(habilidade.pegarJogadorPerto(localBola, jogador.getSide(), true).getUniformNumber() == jogador.getUniformNumber()) {
			ESTADO = JogadorEstado.PERSEGUIR_BOLA;
			return;
		}else if(jogador.getPosition().distanceTo(localGol) > 5) {
			habilidade.correrParaPonto(localGol);
		}else
			habilidade.virarParaPonto(localBola);
		
		if(habilidade.emModoAtaque())
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;
	}
}
