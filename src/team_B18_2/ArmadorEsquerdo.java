package team_B18_2;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.utils.Vector2D;

public class ArmadorEsquerdo extends JogadorBase{

	public ArmadorEsquerdo(PlayerCommander commander, InformacaoTime informacao) {
		super(commander, informacao);
	}
	
	public void beforeKickOffAcao() {
		habilidade.virarParaPonto(habilidade.pegarPosicaoBola());
		habilidade.posicaoInicial(new Vector2D(-12,-7));
	}
}
