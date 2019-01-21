package team_B18;

import java.util.ArrayList;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;
import java.awt.Rectangle;

public class JogadorBase {
	private InformacaoTime informacao;
	public HabilidadeJogador habilidade;
	public JogadorEstado ESTADO;
	
	public JogadorBase(PlayerCommander commander, InformacaoTime informacao) {
		this.informacao = informacao;
		this.habilidade = new HabilidadeJogador(commander);
	}
	

	public void acao() {
		habilidade.atualizarPercepcoes();			
		switch (habilidade.estadoPartida()) {
		case NULL:
			break;
		case BEFORE_KICK_OFF:
			beforeKickOffAcao();
			break;
		case TIME_OVER:
			break;
		case PLAY_ON:
			playOnAcao();
			break;
		case KICK_OFF_LEFT:
			kickOffLeftAcao();
			break;
		case KICK_OFF_RIGHT:
			kickOffRightAcao();
			break;
		case KICK_IN_LEFT:
			freeKickLeftAcao();
			break;
		case KICK_IN_RIGHT:
			freeKickRightAcao();
			break;
		case FREE_KICK_LEFT:
			freeKickLeftAcao();
			break;
		case FREE_KICK_RIGHT:
			freeKickRightAcao();
			break;
		case CORNER_KICK_LEFT:
			beforeKickOffAcao();
			break;
		case CORNER_KICK_RIGHT:
			beforeKickOffAcao();
			break;
		case GOAL_KICK_LEFT:
			goalKickLeftAcao();
			break;
		case GOAL_KICK_RIGHT:
			goalKickRightAcao();
			break;
		case AFTER_GOAL_LEFT:
			beforeKickOffAcao();
			break;
		case AFTER_GOAL_RIGHT:
			beforeKickOffAcao();
			break;
		case DROP_BALL:
			beforeKickOffAcao();
			break;
		case OFFSIDE_LEFT:
			beforeKickOffAcao();
			break;
		case OFFSIDE_RIGHT:
			beforeKickOffAcao();
			break;
		case MAX:
			beforeKickOffAcao();
			break;
		case BACK_PASS_LEFT:
			beforeKickOffAcao();
			break;
		case BACK_PASS_RIGHT:
			beforeKickOffAcao();
			break;
		case FREE_KICK_FAULT_LEFT:
			beforeKickOffAcao();
			break;
		case FREE_KICK_FAULT_RIGHT:
			beforeKickOffAcao();
			break;
		case INDIRECT_FREE_KICK_LEFT:
			beforeKickOffAcao();
			break;
		case INDIRECT_FREE_KICK_RIGHT:
			beforeKickOffAcao();
			break;
		default:
			break;
		}
	}
	
	public void beforeKickOffAcao() {}
	
	public void playOnAcao() {
		switch (ESTADO) {
		case CHUTAR_BOLA:
			estadoChutarBola();
			break;
		case POSICIONAR_ATAQUE:
			estadoPosicionarAtaque();
			break;
		case POSICIONAR_DEFESA:
			estadoPosicionarDefesa();
			break;
		case INTERCEPTAR_LANCAMENTO:
			break;
		case RECEBER_BOLA:
			estadoReceberBola();
			break;
		case PERSEGUIR_BOLA:
			estadoPerseguirBola();
			break;
		default:
			break;
		}
	}
	
	public void kickOffLeftAcao() {
		if(habilidade.ladoCampo() == EFieldSide.LEFT)
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;	
		else
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
	}
	
	public void kickOffRightAcao() {
		if(habilidade.ladoCampo() == EFieldSide.RIGHT)
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;	
		else
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
	}
	
	public void goalKickLeftAcao() {}
	
	public void goalKickRightAcao() {}

	public void freeKickLeftAcao() {
		
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D posBola = habilidade.getPosBola();
		
		if(jogador.getSide() == EFieldSide.LEFT)
		{
			if(habilidade.pegarJogadorPerto(posBola, jogador.getSide(), false).getUniformNumber() == jogador.getUniformNumber())
			{
				habilidade.correrParaPonto(posBola);
				ESTADO = JogadorEstado.CHUTAR_BOLA;
			}
		}
	}
	
	public void freeKickRightAcao() {
		
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D posBola = habilidade.getPosBola();
		
		if(jogador.getSide() == EFieldSide.RIGHT)
		{
			if(habilidade.pegarJogadorPerto(posBola, jogador.getSide(), false).getUniformNumber() == jogador.getUniformNumber())
			{
				habilidade.correrParaPonto(posBola);
				ESTADO = JogadorEstado.CHUTAR_BOLA;
			}
		}
	}
	
	public void estadoChutarBola() {
		Vector2D golAdversario = habilidade.localGol(EFieldSide.invert(habilidade.ladoCampo()));
		double distanciaGolAdversario = golAdversario.distanceTo(habilidade.getPosBola());
		Vector2D melhorLancamento = habilidade.melhorPontoLancarBola();
		//if(habilidade.getPlayerPerception().getSide().equals(EFieldSide.RIGHT)){}
		//	System.out.println(distanciaGolAdversario);
		if(distanciaGolAdversario < 30) {
			habilidade.chutarNoGol(golAdversario);
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;
			return;
		}else if(melhorLancamento != null) {
			informacao.setPosicaoLancamento(melhorLancamento);
			habilidade.passarBola(melhorLancamento,true);
			habilidade.correrParaPonto(habilidade.getPosBola());
			habilidade.passarBola(melhorLancamento, false);
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;
			return;
		}else if(habilidade.pegarJogadorPerto(
				habilidade.getPosBola(), EFieldSide.invert(habilidade.ladoCampo()), true)
				.getPosition().distanceTo(habilidade.getPosBola())>10) {
			habilidade.conduzirBola();
			ESTADO = JogadorEstado.PERSEGUIR_BOLA;
			return;
		}else {
			Vector2D melhorPosicao = habilidade.melhorPontoPassarBola();
			PlayerPerception jogadorMelhorPosicionado = habilidade.jogadorBemPosicionado(melhorPosicao);
			if(jogadorMelhorPosicionado == null) {
				habilidade.passarBola(golAdversario,true);
				habilidade.correrParaPonto(habilidade.getPosBola());
				habilidade.passarBola(golAdversario, false);
			}else {
				if(habilidade.getPosBola().distanceTo(habilidade.localGol(habilidade.ladoCampo())) > 30) {
					habilidade.passarBola(jogadorMelhorPosicionado.getPosition(),true);
					habilidade.correrParaPonto(habilidade.getPosBola());
					habilidade.passarBola(jogadorMelhorPosicionado.getPosition(), false);
				}else {
					habilidade.passarBola(golAdversario,true);
					habilidade.correrParaPonto(habilidade.getPosBola());
					habilidade.passarBola(golAdversario, false);
				}
			}
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;
		}
	}
	
	public void estadoPosicionarAtaque() {
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D posBola = habilidade.getPosBola();
		EFieldSide ladoJogador = habilidade.ladoCampo();
		Vector2D velocidadeBola = habilidade.getVelBola();
		Vector2D pontoParada = habilidade.pegarPontoFuturo();
		Vector2D melhorPosicao = habilidade.melhorPontoReceberBola();
		EFieldSide ladoAdversario = EFieldSide.invert(habilidade.ladoCampo());
		double linhaImpedimento = habilidade.penultimoJogadorAdversario().getPosition().getX();
		
		if(habilidade.naZonaDeChute()) {
			ESTADO = JogadorEstado.CHUTAR_BOLA;
			return;
		}else if(informacao.getPosicaoLancamento() != null) {
			informacao.setPosicaoLancamento(null);
			ESTADO = JogadorEstado.RECEBER_BOLA;
		}
		else if (velocidadeBola.magnitude() == 0 && habilidade.pegarJogadorPerto(posBola, ladoJogador, true).getUniformNumber() == jogador.getUniformNumber()) {
			ESTADO = JogadorEstado.PERSEGUIR_BOLA;
			return;
		}else if (velocidadeBola.magnitude() != 0 && habilidade.pegarJogadorPerto(pontoParada, ladoJogador, true).getUniformNumber() == jogador.getUniformNumber()) {
			habilidade.correrParaPonto(pontoParada);
		}else if(jogador.getPosition().getX() * ladoAdversario.value() < linhaImpedimento * ladoAdversario.value() + 5 && 
				jogador.getPosition().getX() * ladoAdversario.value() < posBola.getX() * ladoAdversario.value()) {
			habilidade.posicionarNoPonto(new Vector2D(jogador.getPosition().getX() + 10*ladoAdversario.value(), jogador.getPosition().getY()));
		}else if (jogador.getPosition().distanceTo(melhorPosicao) > 5) {
			habilidade.posicionarNoPonto(melhorPosicao);
		}else {
			habilidade.virarParaPonto(posBola);
		}
		if(!habilidade.emModoAtaque())
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
	}
	
	public void estadoPosicionarDefesa() {
		Vector2D localBola = habilidade.getPosBola();
		PlayerPerception jogador = habilidade.getPlayerPerception();
		PlayerPerception jogadorAdv = habilidade.getFieldPerception().
												 getTeamPlayer(EFieldSide.invert(jogador.getSide()), 
														 habilidade.pegarJogadorPerto(jogador.getPosition(), EFieldSide.invert(jogador.getSide()), false).getUniformNumber());
				//habilidade.getFieldPerception().getTeamPlayer(EFieldSide.invert(jogador.getSide()), jogador.getUniformNumber());
				
		if(habilidade.naZonaDeChute()) {
			ESTADO = JogadorEstado.CHUTAR_BOLA;
			return;
			// se o jogador for o mais próximo da bola
		}else if(habilidade.pegarJogadorPerto(localBola, jogador.getSide(), true).getUniformNumber() ==  
				jogador.getUniformNumber() || jogador.getPosition().distanceTo(localBola) < 5) {
			ESTADO = JogadorEstado.PERSEGUIR_BOLA;
			return;
		}else {
			
			//if(jogador.getPosition().distanceTo(jogadorAdv.getPosition()) > 5) 
			   // && habilidade.pegarJogadorPerto(jogadorAdv.getPosition(), jogador.getSide(), false).getSide() != jogador.getSide()) 
			//{
				ArrayList<PlayerPerception> aux = habilidade.getFieldPerception().getTeamPlayers(jogadorAdv.getSide());
				for(PlayerPerception p : aux)
				{
					if(p.getUniformNumber() != 1) {
						
						if(habilidade.pegarJogadorPerto(p.getPosition(), jogador.getSide(), false).getUniformNumber() != jogador.getUniformNumber()
								&& habilidade.pegarJogadorPerto(p.getPosition(), jogador.getSide(), false).getPosition().distanceTo(p.getPosition()) > 4)
						{
							habilidade.correrParaPonto(p.getPosition());
							break;
						}	
					}
				}
				
			//}
		}
		
		if(habilidade.emModoAtaque())
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;
	}
		
	public void estadoReceberBola() {
		if(habilidade.naZonaDeChute())
			ESTADO = JogadorEstado.CHUTAR_BOLA;
		
		habilidade.correrPontoFuturo();
		
		if(!(habilidade.pegarJogadorPerto(habilidade.getPosBola(), 
				habilidade.ladoCampo(),true).getUniformNumber() == habilidade.getPlayerPerception().getUniformNumber())) {
			if(habilidade.emModoAtaque())
				ESTADO = JogadorEstado.POSICIONAR_ATAQUE;
			else
				ESTADO = JogadorEstado.POSICIONAR_DEFESA;
		}
	}
	
	public void estadoPerseguirBola() {
		if(habilidade.naZonaDeChute())
			ESTADO = JogadorEstado.CHUTAR_BOLA;
		
		habilidade.correrParaPonto(habilidade.getPosBola());
		
		if(!(habilidade.pegarJogadorPerto(habilidade.getPosBola(), 
				habilidade.ladoCampo(),true).getUniformNumber() == habilidade.getPlayerPerception().getUniformNumber())) {
			if(habilidade.emModoAtaque())
				ESTADO = JogadorEstado.POSICIONAR_ATAQUE;
			else
				ESTADO = JogadorEstado.POSICIONAR_DEFESA;
		}	
	}
}
