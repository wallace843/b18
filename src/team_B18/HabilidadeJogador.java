package team_B18;

import java.math.MathContext;
import java.util.ArrayList;

import simple_soccer_lib.PlayerCommander;
import simple_soccer_lib.perception.FieldPerception;
import simple_soccer_lib.perception.MatchPerception;
import simple_soccer_lib.perception.PlayerPerception;
import simple_soccer_lib.utils.EFieldSide;
import simple_soccer_lib.utils.EMatchState;
import simple_soccer_lib.utils.Vector2D;
import team_B18.Calculo.Complex;

public class HabilidadeJogador {
	private double CONST_PASSE = 6;
	private double desaceleracaoBola = -0.0625;
	private Vector2D posicoes[];
	private PlayerCommander commander;
	private PlayerPerception playerPerception;
	private FieldPerception fieldPerception;
	private MatchPerception matchPerception;
	private Vector2D posBola;
	private Vector2D velBola;
	private Vector2D PFABola;
	private long time;
	
	public HabilidadeJogador(PlayerCommander commander) {
		this.posicoes = new Vector2D[77];
		int i = 0;
		for(int x = -50 ; x <= 50 ; x = x + 10) {
			for(int y = -30 ; y <= 30 ; y = y + 10) {
				posicoes[i] = new Vector2D(x, y);
				i++;
			}
		}
		this.commander = commander;
		this.posBola = new Vector2D(0,0);
		this.velBola = posBola;
		this.PFABola = posBola;
		this.time = System.currentTimeMillis();
	}
	
	public Vector2D getPosBola() {
		return posBola;
	}

	public Vector2D getVelBola() {
		return velBola;
	}

	public void atualizarPercepcoes() {
		playerPerception = commander.perceiveSelfBlocking();
		fieldPerception = commander.perceiveFieldBlocking();
		matchPerception = commander.perceiveMatchBlocking();
		Vector2D posBolaAtual = fieldPerception.getBall().getPosition();
		velBola = posBolaAtual.sub(posBola);
		posBola = posBolaAtual;
		time = System.currentTimeMillis() - time;
	}
	
	public Vector2D pegarPontoFuturo() {
		Vector2D pontoFuturo = null;
		Vector2D posicaoBola = posBola;
		if(velBola.magnitude() == 0)
			pontoFuturo = posicaoBola;
		else {
			double espacoPercorridoParada = (-Math.pow(velBola.magnitude(),2))/(2*desaceleracaoBola);
			//espacoPercorridoParada = espacoPercorridoParada*7/10;
			pontoFuturo = velBola.multiply(espacoPercorridoParada/(velBola.magnitude()));
			pontoFuturo = posicaoBola.sum(pontoFuturo);
		}
		//if (playerPerception.getSide().equals(EFieldSide.LEFT))
		//	System.out.println("2 "+pontoFuturo);
		return pontoFuturo;
	}
	

	public Vector2D pontoFuturo() {
		Vector2D pontoFuturo = null;
		Vector2D posicaoBola = posBola;
		if(velBola.magnitude() == 0)
			pontoFuturo = posicaoBola;
		else {
			
			double intervalo = intervaloInterceptacao();
			if(intervalo == Double.MAX_VALUE)
				pontoFuturo = PFABola;
			else {
				double espacoPercorrido = velBola.magnitude()*intervalo - 0.01*Math.pow(intervalo, 2)/2;
				//espacoPercorridoParada = espacoPercorridoParada*7/10;
				pontoFuturo = velBola.multiply(espacoPercorrido/(velBola.magnitude()));
				pontoFuturo = posicaoBola.sum(pontoFuturo);	
			}
		}
		if (playerPerception.getSide().equals(EFieldSide.LEFT)) 
			System.out.println(pontoFuturo);
		PFABola = pontoFuturo;
		return pontoFuturo;
	}
	
	private double intervaloInterceptacao() {
		double tempo = Double.MAX_VALUE, vA, vB, csTeta, ac, dis;
		double x1 = playerPerception.getPosition().sub(posBola).getX();
		double y1 = playerPerception.getPosition().sub(posBola).getY();
		double x2 = velBola.getX();
		double y2 = velBola.getY();
		
		vA = 1d;
		vB = velBola.magnitude();
		csTeta = (x1*x2 + y1*y2)/(Math.sqrt(Math.pow(x2, 2)+Math.pow(y2, 2))*Math.sqrt(Math.pow(x1, 2)+Math.pow(y1, 2)));
		ac = -0.01;//desaceleracaoBola;//desaceleracaoBola;
		dis = playerPerception.getPosition().distanceTo(posBola);
		
		double a, b, c, d, e;
		
		a = Math.pow(ac, 2)/4d;
		b = vB*ac;
		c = -Math.pow(vA, 2d) + Math.pow(vB, 2d) - dis*ac*csTeta;
		d = -2d*dis*csTeta*vB;
		e = Math.pow(dis, 2d);
		
		Calculo calcular = new Calculo();
		
		System.out.println(csTeta + " " + Math.toDegrees(Math.cos(csTeta)));
		Complex[] tmps = calcular.equacaoQuarto(a, b, c, d, e);
		
		if(Math.abs(tmps[2].getImaginario()) > 1.0E-8)
			tempo = tmps[3].getReal();
		else {
			for(Complex cc : tmps) {
				if(cc.getReal() > 0 && cc.getReal() < tempo)
					tempo = cc.getReal();
			}
		}
		
		return tempo;
	}
	
	public void correrPontoFuturo() {
		Vector2D velocidade = velBola;
		Vector2D posicaoBola = posBola;
		double magniVelocidadeBola = velocidade.magnitude();
		if(magniVelocidadeBola > 0) {
			double espacoPercorridoParada = (-Math.pow(magniVelocidadeBola,2))/(2*desaceleracaoBola);
			//espacoPercorridoParada = espacoPercorridoParada*7/10;
			Vector2D pontoCorrida = velocidade.multiply(espacoPercorridoParada/(velocidade.magnitude()));
			pontoCorrida = posicaoBola.sum(pontoCorrida);
			if (!estaAlinhadoPonto(pontoCorrida, 15)) 
				virarParaPonto(pontoCorrida);
			if(playerPerception.getPosition().distanceTo(pontoCorrida) > 1) 
				commander.doDash(100);
		}
		else {
			if (!estaAlinhadoPonto(posicaoBola, 15)) 
				virarParaPonto(posicaoBola);
			if(playerPerception.getPosition().distanceTo(posicaoBola) > 1) 
				commander.doDash(100);
		}
	}
	
	public void posicaoInicial(Vector2D inicio) {
		commander.doMoveBlocking(inicio.getX(), inicio.getY());
	}
	
	public PlayerPerception jogadorBemPosicionado(Vector2D point){
		ArrayList<PlayerPerception> aux = fieldPerception.getTeamPlayers(ladoCampo());
		ArrayList<PlayerPerception> lp = new ArrayList<PlayerPerception>();
		for(PlayerPerception p : aux) {
			if(p.getUniformNumber() != 1 && p.getUniformNumber() != playerPerception.getUniformNumber() && 
			!(p.getPosition().getX() * EFieldSide.invert(ladoCampo()).value() < 
			penultimoJogadorAdversario().getPosition().getX() * EFieldSide.invert(ladoCampo()).value() && 
			p.getPosition().getX() * EFieldSide.invert(ladoCampo()).value() < 
			posBola.getX() * EFieldSide.invert(ladoCampo()).value())) {
				lp.add(p);
			}
		}
		
		PlayerPerception np = null ;
		if (lp != null && !lp.isEmpty()){
			double dist,temp;
			dist = lp.get(0).getPosition().distanceTo(point);
			np = lp.get(0);
			for (PlayerPerception p : lp) {
				if (p.getPosition() == null ) 
					break ;
				temp = p.getPosition().distanceTo(point);
				if (temp < dist){
					dist = temp;
					np = p;
				}
			}
		}
		return np;
	}
	
	public PlayerPerception pegarJogadorPerto(Vector2D point, EFieldSide side, boolean incluirGoleiro){
		ArrayList<PlayerPerception> aux = fieldPerception.getTeamPlayers(side);
		
		ArrayList<PlayerPerception> lp = null;
		if(incluirGoleiro) {
			lp = new ArrayList<PlayerPerception>();
			for(PlayerPerception p : aux) {
				if(p.getUniformNumber() != 1) {
					lp.add(p);
				}
					
			}
		}else
			lp = aux;
		
		PlayerPerception np = null ;
		if (lp != null && !lp.isEmpty()){
			double dist,temp;
			dist = lp.get(0).getPosition().distanceTo(point);
			np = lp.get(0);
			for (PlayerPerception p : lp) {
				if (p.getPosition() == null ) 
					break ;
				temp = p.getPosition().distanceTo(point);
				if (temp < dist){
					dist = temp;
					np = p;
				}
			}
		}
		return np;
	}
	
	public boolean emModoAtaque() {
		EFieldSide lado = playerPerception.getSide();
		Vector2D bolaPosi = fieldPerception.getBall().getPosition();
		PlayerPerception pl = pegarJogadorPerto(bolaPosi, lado, true);
		if(pl == null)
			return false;
		else {
			PlayerPerception plAd = pegarJogadorPerto(bolaPosi, EFieldSide.invert(lado), true);
			if(plAd == null)
				return true;
			else {
								
				if(pl.getPosition().distanceTo(bolaPosi) > plAd.getPosition().distanceTo(bolaPosi))
					return false;
				else
					return true;
			}
		}
	}
	
	public void passarBola(Vector2D ponto) {
		double relativeAngle = ponto.sub(playerPerception.getPosition()).angleFrom(playerPerception.getDirection());
		double deslocamento = playerPerception.getPosition().distanceTo(ponto);
		double intensity = (deslocamento*CONST_PASSE);
		if (intensity > 100)
			intensity = 100;
		//if(getPlayerPerception().getSide().equals(EFieldSide.LEFT))
		//	System.out.println(playerPerception.getUniformNumber() + " " + playerPerception.getPosition() + " " + ponto);
		commander.doKickBlocking(intensity, relativeAngle);
	}
	
	public EMatchState estadoPartida() {
		return matchPerception.getState();
	}
	
	public void rotacionarBola(Vector2D ponto) {
		commander.doKickBlocking(0, 0);
		double relativeAngle = ponto.sub(playerPerception.getPosition()).angleFrom(playerPerception.getDirection());
		double intensity = 15;
		commander.doKickBlocking(intensity, relativeAngle);
		virarParaPonto(ponto);
	}
	
	public void correrParaPonto(Vector2D ponto) {
		double distancia = playerPerception.getPosition().distanceTo(ponto);
		if(distancia > 0.7) {
			double intensidade = 100;
			if(distancia < 10)
				intensidade = intensidade - 40 + (40*distancia)/10;
			if (!estaAlinhadoPonto(ponto, 15)) { 
				virarParaPonto(ponto);
				commander.doDashBlocking(intensidade);
			}
			else
				commander.doDash(intensidade);
		}
		else
			commander.doKick(10, 0);
	}
	
	public void posicionarNoPonto(Vector2D ponto) {
		if (!estaAlinhadoPonto(ponto, 15)) 
			virarParaPonto(ponto);
		double intensidade = 40;
		double distancia = playerPerception.getPosition().distanceTo(ponto);
		if(distancia <= 1) 
			return;
		if(distancia < 20)
			intensidade = intensidade - 30 + (30*distancia)/20;
		commander.doDash(intensidade);
	}
	
	public EFieldSide ladoCampo() {
		return commander.getFieldSide();
	}
	
	public void virarParaPonto(Vector2D point){
		Vector2D newDirection = point.sub(playerPerception.getPosition());
		commander.doTurnToDirectionBlocking(newDirection);
	}
	
	public boolean estaAlinhadoPonto(Vector2D point, double margin){
		double angle = point.sub(playerPerception.getPosition()).angleFrom(playerPerception.getDirection());
		return angle < margin && angle > margin*(-1);
	}
	
	public Vector2D melhorPontoPassarBola() {
		double pesoPontoGol = 7;
		double pesoPontoBola = 2;
		double pesoPontoJogador = 3;
		double pesoPontoJogadorOutros = 25;
		double somaPesos = pesoPontoGol + pesoPontoBola + pesoPontoJogador + pesoPontoJogadorOutros;
		EFieldSide lado = commander.getFieldSide();
		Vector2D golAdv = (new Vector2D(50,0)).multiply(lado.value());
		Vector2D ballPos = fieldPerception.getBall().getPosition();
		Vector2D posicao = null;
		double melhorPontuacao = -1;
		for(Vector2D p: posicoes) {
			double distanciaBola = ballPos.distanceTo(p);
			if(distanciaBola < 40 && distanciaBola > 20) {
				double tempPontos = 0;
				tempPontos += pesoPontoGol * (120 - p.distanceTo(golAdv));
				tempPontos += pesoPontoBola * (120 - distanciaBola);
				tempPontos += pesoPontoJogador * (120 - distaniciaMediaJogadoresPonto(p, 1));
				tempPontos += pesoPontoJogadorOutros * distaniciaMediaJogadoresPonto(p,2);
				tempPontos /= somaPesos;
				//if(getPlayerPerception().getSide().equals(EFieldSide.LEFT) && getPlayerPerception().getUniformNumber() == 5)
				//	System.out.println(p + " " + pesoPontoGol *  (120 - p.distanceTo(golAdv) )+ " " + pesoPontoBola *(120 - distanciaBola) + " " + pesoPontoJogador * (120 - distaniciaMediaJogadoresPonto(p, 1)) + " " +pesoPontoJogadorOutros *  distaniciaMediaJogadoresPonto(p,2));
				if(tempPontos > melhorPontuacao) {
					melhorPontuacao = tempPontos;
					posicao = p;
				}
			}
		}
		//if(getPlayerPerception().getSide().equals(EFieldSide.LEFT) && getPlayerPerception().getUniformNumber() == 5)
		//	System.out.println("\n" + posicao + "\n");
		//if(getPlayerPerception().getSide().equals(EFieldSide.LEFT) && getPlayerPerception().getUniformNumber() == 5)
		//	System.out.println(posicao);
		
		return posicao;
	}
	
	public Vector2D localGol(EFieldSide lado) {
		if(lado.equals(EFieldSide.LEFT))
			return new Vector2D(-54,0);
		else
			return new Vector2D(54,0);
			
	}
	
	public void chutarNoGol() {
		Vector2D golAdv = localGol(EFieldSide.invert(ladoCampo()));
		if(estaAlinhadoPonto(golAdv, 20)) {
			virarParaPonto(golAdv);
			commander.doKickBlocking(100, 0);
		}else
			commander.doKickBlocking(100,  golAdv.sub(playerPerception.getPosition()).angleFrom(playerPerception.getDirection()));
	}
	
	public void conduzirBola() {
		double intensidade = 10;
		Vector2D golAdv = localGol(EFieldSide.invert(ladoCampo()));
		PlayerPerception adversario = pegarJogadorPerto(posBola, EFieldSide.invert(ladoCampo()), true);
		double relativeAngle = golAdv.sub(playerPerception.getPosition()).angleFrom(playerPerception.getDirection());
		if(adversario.getPosition().distanceTo(posBola) > 30) {
			if(adversario.getPosition().distanceTo(golAdv) > posBola.distanceTo(golAdv)) {
				commander.doKickBlocking(intensidade + 20, relativeAngle);
			}else {
				commander.doKickBlocking(intensidade + 10, relativeAngle);
			}
		}
		else {
			if(adversario.getPosition().distanceTo(golAdv) > posBola.distanceTo(golAdv)) {
				commander.doKickBlocking(intensidade + 10, relativeAngle);
			}else {
				commander.doKickBlocking(intensidade, relativeAngle);
			}
		}
	}
	
	public FieldPerception getFieldPerception() {
		return this.fieldPerception;
	}
	
	public boolean naZonaDeChute() {
		return playerPerception.getPosition().distanceTo(posBola) < 0.7;
	}
	
	public PlayerPerception getPlayerPerception() {
		return playerPerception;
	}
	
	
	public Vector2D melhorPontoReceberBola() {
		double teste = 12;
		double pesoPontoGol = teste;
		double pesoPontoJogador = teste + 10;
		double pesoPontoJogadorMeu = teste + 20;
		double pesoPontoBola = teste + 25;
		double pesoPontoJogadorOutros = teste + 25;
		double somaPesos = pesoPontoGol + pesoPontoBola + pesoPontoJogador + pesoPontoJogadorOutros + pesoPontoJogadorMeu;
		EFieldSide lado = ladoCampo();
		Vector2D golAdv = (new Vector2D(50,0)).multiply(lado.value());
		Vector2D ballPos = posBola;
		Vector2D posicao = null;
		double melhorPontuacao = -1;
		for(Vector2D p: posicoes) {
			double distanciaBola = ballPos.distanceTo(p);
			//if(distanciaBola < 50) {
				double tempPontos = 0;
				tempPontos += pesoPontoGol * (120 - p.distanceTo(golAdv));
				tempPontos += pesoPontoBola * (120 - distanciaBola);
				tempPontos += pesoPontoJogador * (120 - playerPerception.getPosition().distanceTo(p));
				tempPontos += pesoPontoJogadorOutros * distaniciaMediaJogadoresPonto(p,2);
				tempPontos += pesoPontoJogadorMeu * distaniciaMediaJogadoresPonto(p, 1);
				tempPontos /= somaPesos;
				if(tempPontos > melhorPontuacao) {
					melhorPontuacao = tempPontos;
					posicao = p;
				}
			//}
		}
		//if(getPlayerPerception().getTeam().equals("B18"))
		//	System.out.println(playerPerception.getUniformNumber()+" "+posicao);
		//System.out.println(posicao);
		return posicao;
	}
		 
	public double distaniciaMediaJogadoresPonto(Vector2D point, int tipo){
		ArrayList<PlayerPerception> lp;
		if(tipo == 1)
			lp = fieldPerception.getTeamPlayers(ladoCampo());
		else if (tipo == 2) 
			lp = fieldPerception.getTeamPlayers(EFieldSide.invert(ladoCampo()));
		else
			lp = fieldPerception.getAllPlayers();
		int uniformeJogador = playerPerception.getUniformNumber();
		double valor = 0;
		for (PlayerPerception p : lp){
			if (((tipo == 1 || tipo == 3) && p.getUniformNumber() != uniformeJogador) || tipo == 2)
				valor += p.getPosition().distanceTo(point);
		}
		if(tipo == 1)
			valor /= 6;
		else if (tipo == 2) 
			valor /= 7;
		else
			valor /= 13;
		return valor;
	}
	
	public PlayerPerception penultimoJogadorAdversario() {
		EFieldSide ladoAdversario = EFieldSide.invert(ladoCampo());
		ArrayList<PlayerPerception> jogadoresAdversarios = fieldPerception.getTeamPlayers(ladoAdversario);
		PlayerPerception ultimoJogador;
		PlayerPerception penutimoJogador;
		if(ladoAdversario.value()*jogadoresAdversarios.get(0).getPosition().getX() > ladoAdversario.value()*jogadoresAdversarios.get(1).getPosition().getX()) {
			penutimoJogador = jogadoresAdversarios.get(0);
			ultimoJogador = jogadoresAdversarios.get(1);
		}else {
			penutimoJogador = jogadoresAdversarios.get(1);
			ultimoJogador = jogadoresAdversarios.get(0);
		}
		for(int i = 2; i < jogadoresAdversarios.size(); i++) {
			if(ladoAdversario.value()*jogadoresAdversarios.get(i).getPosition().getX() < ladoAdversario.value()*penutimoJogador.getPosition().getX()) {
				if(ladoAdversario.value()*jogadoresAdversarios.get(i).getPosition().getX() < ladoAdversario.value()*ultimoJogador.getPosition().getX()) {
					penutimoJogador = ultimoJogador;
					ultimoJogador = jogadoresAdversarios.get(i);
				}else {
					penutimoJogador = jogadoresAdversarios.get(i);
				}
			}
		}
		return penutimoJogador;
	}
}