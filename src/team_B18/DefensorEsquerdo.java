package team_B18;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.Vector2D;

public class DefensorEsquerdo extends JogadorBase{

	public DefensorEsquerdo(PlayerCommander commander, InformacaoTime informacao) {
		super(commander, informacao);
	}
	
	public void beforeKickOffAcao() {
		habilidade.virarParaPonto(habilidade.getPosBola());
		habilidade.posicaoInicial(new Vector2D(-20,-5));
	}
	
	/*public void playOnAcao() {
		if(habilidade.getPlayerPerception().getSide().equals(EFieldSide.LEFT))
		habilidade.correrParaPonto(habilidade.pontoFuturo());
	}*/
}
