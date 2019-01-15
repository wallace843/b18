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
				jogador.getUniformNumber() || jogador.getPosition().distanceTo(posBola) < 10) {
			ESTADO = JogadorEstado.PERSEGUIR_BOLA;
			return;
		}
		else{	 // se é o jogador do seu time que está MAIS PROXIMO da bola
			if(jogador.getPosition().distanceTo(posBola) <= 14)
			{
				habilidade.correrPontoFuturo();
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
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D posBola = habilidade.getPosBola();
		
		EFieldSide side = jogador.getSide();
		Vector2D gol = side == EFieldSide.LEFT? new Vector2D(-50d,0d):new Vector2D(50d,0d);
		
		
			if(jogador.getPosition().distanceTo(posBola) <= 1)
				ESTADO = JogadorEstado.CHUTAR_BOLA;
			else{
				if(jogador.getPosition().distanceTo(posBola) <= 14) {
					habilidade.correrPontoFuturo();
					if(habilidade.naZonaDeChute())
						ESTADO = JogadorEstado.CHUTAR_BOLA;
				}else if(jogador.getPosition().distanceTo(gol) > 8 && jogador.getPosition().distanceTo(posBola) >= 10) 						
					habilidade.correrParaPonto(gol);
				else
					habilidade.virarParaPonto(posBola);
				}
	}
	
	public void goalKickLeftAcao() {
	
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D posBola = habilidade.getPosBola();
		
		if(jogador.getSide() == EFieldSide.LEFT)
		{
			habilidade.correrParaPonto(posBola);
			if(habilidade.naZonaDeChute())
				ESTADO = JogadorEstado.CHUTAR_BOLA;
		}
		
	}
	
	public void goalKickRightAcao() {
		
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D posBola = habilidade.getPosBola();
		
		if(jogador.getSide() == EFieldSide.RIGHT)
		{
			habilidade.correrParaPonto(posBola);
			if(habilidade.naZonaDeChute())
				ESTADO = JogadorEstado.CHUTAR_BOLA;
		}
		
	}
	
}

	
	
	/*
	    public void estadoPosicionarDefesa() {
		PlayerPerception jogador = habilidade.getPlayerPerception();
		Vector2D localBola = habilidade.getPosBola();
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
	*/
	
