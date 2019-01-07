package team_B18_2;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.utils.Vector2D;

public class DefensorEsquerdo extends JogadorBase{

	public DefensorEsquerdo(PlayerCommander commander, InformacaoTime informacao) {
		super(commander, informacao);
	}
	
	public void beforeKickOffAcao() {
		habilidade.virarParaPonto(habilidade.pegarPosicaoBola());
		habilidade.posicaoInicial(new Vector2D(-20,-5));
	}
	
	/*public void playOnAcao() {
		habilidade.correrParaPonto(habilidade.pegarPontoFuturo(habilidade.velocidadeBola()));
	}*/
}
