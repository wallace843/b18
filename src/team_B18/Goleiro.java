package team_B18;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class Goleiro extends JogadorBase{
	
	public Goleiro(PlayerCommander commander, InformacaoTime informacao) {
		super(commander, informacao);
	}
	
	public void beforeKickOffAcao() {
		habilidade.virarParaPonto(habilidade.getPosBola());
		habilidade.posicaoInicial(new Vector2D(-50,0));
	}
	
	
	
	public void estadoPosicionarDefesa() {
		Vector2D golAdv = habilidade.localGol(EFieldSide.invert(habilidade.ladoCampo()));
		Vector2D posBola = habilidade.getPosBola();
		PlayerPerception jogador = habilidade.getPlayerPerception();
		EFieldSide side = jogador.getSide();
		Vector2D gol = side == EFieldSide.LEFT ? new Vector2D(-50d,0d):new Vector2D(50d,0d);
				
		if(habilidade.naZonaDeChute()) {
			habilidade.passarBola(golAdv, false);
			return;
			// se o jogador for o mais próximo da bola
		}else if(habilidade.pegarJogadorPerto(posBola, jogador.getSide(), true).getUniformNumber() ==  
				jogador.getUniformNumber()) {
			ESTADO = JogadorEstado.PERSEGUIR_BOLA;
			return;
		}
		else{	 // se é o jogador do seu time que está MAIS PROXIMO da bola
			if(jogador.getPosition().distanceTo(posBola) <= 14)
			{
				//habilidade.correrPontoFuturo();
				habilidade.correrPontoDeAgarre();
				if(habilidade.naZonaDeChute())
					ESTADO = JogadorEstado.CHUTAR_BOLA;
			}
			else if(jogador.getPosition().distanceTo(gol) > 8 && jogador.getPosition().distanceTo(posBola) >= 10)
			{
				habilidade.correrParaPonto(gol);
			}
			else
			{
				habilidade.virarParaPonto(habilidade.getPosBola());
			}
			
		}	
	}

	public void estadoPosicionarAtaque() {
		Vector2D golAdv = habilidade.localGol(EFieldSide.invert(habilidade.ladoCampo()));
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D posBola = habilidade.getPosBola();
		
		EFieldSide side = jogador.getSide();
		Vector2D gol = side == EFieldSide.LEFT? new Vector2D(-54d,0d):new Vector2D(54d,0d);
		
		
			if(jogador.getPosition().distanceTo(posBola) <= 1)
				//ESTADO = JogadorEstado.CHUTAR_BOLA;
				habilidade.passarBola(golAdv, false);
			else{
				if(jogador.getPosition().distanceTo(posBola) <= 14 && 
						habilidade.pegarJogadorPerto(posBola, habilidade.ladoCampo(), false).getUniformNumber() == jogador.getUniformNumber()){
					habilidade.correrParaPonto(posBola);
					if(habilidade.naZonaDeChute())
						habilidade.passarBola(golAdv, false);
				}else if(jogador.getPosition().distanceTo(gol) > 8 && jogador.getPosition().distanceTo(posBola) >= 10) 						
					habilidade.correrParaPonto(gol);
				else
					habilidade.virarParaPonto(posBola);
				}
	}
	
	public void estadoPerseguirBola() {
		Vector2D golAdv = habilidade.localGol(EFieldSide.invert(habilidade.ladoCampo()));
		Vector2D myGol= habilidade.localGol((habilidade.ladoCampo()));
		
		if(habilidade.getPosBola().distanceTo(myGol) < 16)
		{
			habilidade.correrParaPonto(habilidade.getPosBola());
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
		}
		else
		if(!(habilidade.pegarJogadorPerto(habilidade.getPosBola(), habilidade.ladoCampo(),true)
										 .getUniformNumber() 
										 	== habilidade.getPlayerPerception()	
										 	             .getUniformNumber())) {
				ESTADO = JogadorEstado.POSICIONAR_DEFESA;
		}
		if(habilidade.naZonaDeChute()) {
			habilidade.passarBola(golAdv, false);
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
		}
	}
	
	public void estadoChutarBola() {
		Vector2D golAdversario = habilidade.localGol(EFieldSide.invert(habilidade.ladoCampo()));
		if(habilidade.pegarJogadorPerto(habilidade.getPosBola(), EFieldSide.invert(habilidade.ladoCampo()), true)
				.getPosition().distanceTo(habilidade.getPosBola())>10) {
			habilidade.passarBola(golAdversario,false);
			ESTADO = JogadorEstado.POSICIONAR_DEFESA;
			return;
		}else {
			Vector2D melhorPosicao = habilidade.melhorPontoPassarBola();
			PlayerPerception jogadorMelhorPosicionado = habilidade.jogadorBemPosicionado(melhorPosicao);
			if(jogadorMelhorPosicionado == null) {
				habilidade.passarBola(habilidade.pegarJogadorPerto(habilidade.getPosBola(), habilidade.ladoCampo(), false)
						.getPosition(), false);
			}else {
				if(habilidade.getPosBola().distanceTo(habilidade.localGol(habilidade.ladoCampo())) > 30) {
					habilidade.passarBola(jogadorMelhorPosicionado.getPosition(), false);
				}else {
					habilidade.passarBola(golAdversario, false);
				}
			}
		}

	}
	
	public void goalKickLeftAcao() {
	
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D posBola = habilidade.getPosBola();
		
		if(jogador.getSide() == EFieldSide.LEFT)
		{
			habilidade.correrParaPonto(posBola);
			ESTADO = JogadorEstado.CHUTAR_BOLA;
		}
		
	}
	
	public void goalKickRightAcao() {
		
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D posBola = habilidade.getPosBola();
		
		if(jogador.getSide() == EFieldSide.RIGHT)
		{
			habilidade.correrParaPonto(posBola);
			ESTADO = JogadorEstado.CHUTAR_BOLA;
		}
		
	}
	
}

	
