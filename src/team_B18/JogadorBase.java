package team_B18;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

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
	
	public void estadoChutarBola() {
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D golAdversario = habilidade.localGol(EFieldSide.invert(habilidade.ladoCampo()));
		double distanciaGolAdversario = golAdversario.distanceTo(habilidade.getPosBola());
		Vector2D melhorLancamento = habilidade.melhorPontoLancarBola();
		if(habilidade.getPlayerPerception().getSide().equals(EFieldSide.RIGHT))
			System.out.println(distanciaGolAdversario);
		if(distanciaGolAdversario < 30) {
			habilidade.chutarNoGol();
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;
			return;
		}/*else if (distanciaGolAdversario < 35 && 
				habilidade.pegarJogadorPerto(jogador.getPosition(),
						EFieldSide.invert(jogador.getSide()), false)
				.getPosition().distanceTo(golAdversario) > distanciaGolAdversario) {
			habilidade.chutarNoGol();
			ESTADO = JogadorEstado.POSICIONAR_ATAQUE;
			return;
		}*/else if(melhorLancamento != null) {
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
		PlayerPerception jogador = habilidade.getPlayerPerception();
		PlayerPerception jogadorAdv = habilidade.getFieldPerception().
				getTeamPlayer(EFieldSide.invert(jogador.getSide()), jogador.getUniformNumber());
		Vector2D localBola = habilidade.getPosBola();
		if(habilidade.naZonaDeChute()) {
			ESTADO = JogadorEstado.CHUTAR_BOLA;
			return;
		}else if(habilidade.pegarJogadorPerto(localBola, jogador.getSide(), true).getUniformNumber() == 
				jogador.getUniformNumber() || jogador.getPosition().distanceTo(localBola) < 10) {
			ESTADO = JogadorEstado.PERSEGUIR_BOLA;
			return;
		}else {
			if(jogador.getPosition().distanceTo(jogadorAdv.getPosition()) > 5) {
				habilidade.correrParaPonto(jogadorAdv.getPosition());
			}
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
