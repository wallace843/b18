package team_B18;

import java.util.Vector;

public class Calculo {
	
	public Complex[] equacaoQuarto(double a, double b, double c, double d, double e) {
		b /= a;	c /= a;	d /= a;	e /= a; a = 1;
		
		double f = c - 3*Math.pow(b,2)/8;
		double g = d + Math.pow(b, 3)/8 - b*c/2;
		double h = e - 3*Math.pow(b, 4)/256 + Math.pow(b, 2)*c/16 - b*d/4;
		
		double a3 = 1, b3 = f/2d, c3 = (Math.pow(f, 2) - 4*h)/16, d3 = -Math.pow(g, 2)/64;
		double f3 = (3*c3/a3 - Math.pow(b3, 2)/Math.pow(a3, 2))/3;
		double g3 = (2*Math.pow(b3, 3)/Math.pow(a3, 3) - 9*b3*c3/Math.pow(a3, 2) + 27*d3/a3)/27;
		double h3 = Math.pow(g3, 2)/4 + Math.pow(f3, 3)/27;
		
		Complex Y1, Y2, Y3;
		if(f3 == 0 && g3 == 0 && h3 == 0) {
			Y1 = Y2 = Y3 = new Complex(Math.cbrt(d3/a3)*(-1));
		}else if (h3 <= 0) {
			double i =  Math.pow(Math.pow(g3, 2)/4 - h3, 1d/2d);
			double j = Math.pow(i, 1d/3d);
			double k = Math.acos(-g3/(2*i));
			double l = -j;
			double m = Math.cos(k/3);
			double n = Math.sqrt(3)*Math.sin(k/3);
			double p = -b3/(3*a3);
			
			Y1 = new Complex(2*j * Math.cos(k/3) - b3/(3*a3));
			Y2 = new Complex(l * (m+n) + p);
			Y3 = new Complex(l * (m-n) + p);
		}else {
			double r = -g3/2 + Math.sqrt(h3);
			double s = Math.cbrt(r);
			double t = -g3/2 - Math.sqrt(h3);
			double u = Math.cbrt(t);
			
			Y1 = new Complex(s + u - b3/(3*a3));
			Y2 = new Complex(-(s + u)/2 - b3/(3*a3), (s-u)*Math.sqrt(3)/2);
			Y3 = new Complex(-(s + u)/2 - b3/(3*a3),-(s-u)*Math.sqrt(3)/2);
		}
		
		Complex P, Q, R, S;
		int i;
		
		if(Math.abs(Y2.getImaginario()) > 1.0E-10) {
			P = Y2.exp(1d/2d);
			Q = Y3.exp(1d/2d);
			i = 2;
		}else {
			if(Math.abs(Y1.getImaginario()) < 1.0E-10 && Math.abs(Y1.getReal()) < 1.0E-10) {
				P = Y2.exp(1d/2d);
				Q = Y3.exp(1d/2d);
			}else if(Math.abs(Y2.getImaginario()) < 1.0E-10 && Math.abs(Y2.getReal()) < 1.0E-10) {
				P = Y1.exp(1d/2d);
				Q = Y3.exp(1d/2d);
			}else {
				P = Y2.exp(1d/2d);
				Q = Y1.exp(1d/2d);
			}
			i = 4;
		}
		
		
		R = P.mul(Q).mul(8).exp(-1).mul(-g);
		S = new Complex(b/(4*a));		
		
		Complex[] X = new Complex[4];
		double resultado[] = new double[4];//i];
		
		X[0] = P.adi(Q).adi(R).sub(S);
		X[1] = P.sub(Q).sub(R).sub(S);
		X[2] = Q.sub(P).sub(R).sub(S);
		X[3] = R.sub(Q).sub(P).sub(S);
		
		//i = 0;
		for(Complex x : X) {
			System.out.println(x);
			//if(Math.abs(x.getImaginario()) < 1.0E-8) {
			//	resultado[i] = x.real;
			//	i++;
			//}
		}
		
		return X;
		
		/*double A, B, C;
		A = c/a - 3*Math.pow(b, 2)/(8*Math.pow(a, 2));
		B = d/a - b*c/(2*Math.pow(a, 2)) + Math.pow(b, 3)/(8*Math.pow(a, 3));
		C = e/a - b*d/(4*Math.pow(a, 2)) + Math.pow(b, 2)*c/(16*Math.pow(a, 3)) - 3d*Math.pow(b, 4)/(256d*Math.pow(a, 4));
		
		double a2, b2, c2, d2;
		a2 = 8;
		b2 = -4*A;
		c2 = -8*C;
		d2 = 4*A*C - Math.pow(B, 2);
		
		double p, q;
		p = c2/a2 - Math.pow(b2, 2)/(3*Math.pow(a2, 2));
		q = 2*Math.pow(b2, 3)/(27*Math.pow(a2, 3)) - b2*c2/(3*Math.pow(a2, 2)) + d2/a2;*/
		/*
		//System.out.println(p + " " + q);
		double s, v1, v2, f, g, h;
		
		f = ((3*c2/a2) - (Math.pow(b2, 2)/Math.pow(a2, 2)))/3;
		g = ((2*Math.pow(b2, 3)/Math.pow(a2, 3)) - (9*b2*c2/Math.pow(a2, 2)) + (27*d2/a2))/27;
		h = (Math.pow(g, 2)/4) + (Math.pow(f, 3)/27);
		
		if(f == 0 && g==0 && h == 0) {
			s = Math.cbrt(d2/a2)*(-1);
		}else if(h <= 0) {
			double  i, j, k, L, M, N, P;
			i = Math.sqrt((Math.pow(g, 2)/4) - h);
			j = Math.cbrt(i);
			k = Math.acos(Math.toRadians(-(g / 2*i)));
			L = j * -1;
			M = Math.cos(k/3);
			N = Math.sqrt(3)*Math.sin(k/3);
			P = (b2/3*a2) * -1;
			
			s =  L * (M + N) + P;
		}else {
			double R,S,T,U;
			R = -(g/2) + Math.sqrt(h);
			S = Math.sqrt(R);
			T = -(g/2) - Math.sqrt(h);
			U = Math.sqrt(T);
			
			s = (S + U) - (b2/3*a2);
		}
		
		
		//if (playerPerception.getSide().equals(EFieldSide.LEFT)) 
			//System.out.println(a2 + " "+b2+ " "+c2+ " "+d2+ " "+s);
		
		s= Math.cbrt(-q/2d + (1d/2d)*Math.sqrt(Math.pow(q, 2) + 4*Math.pow(p, 3)/27d)) + 
		Math.cbrt(-q/2d - (1d/2d)*Math.sqrt(Math.pow(q, 2) + 4*Math.pow(p, 3)/27d)) - b2/(3*a2);*/
		
		/*v1 = 2d*s - A;
		if(v1 < 0)
			return 0;
		else {
			v2 = -2d*s - A + (2d*B)/(Math.sqrt(2d*s - A));
			if(v2 < 0) {
				v2 = -2d*s - A - (2d*B)/(Math.sqrt(2d*s - A));
				if(v2 < 0)
					return 0;
				else {
					tmps[2] = (1d/2d)*Math.sqrt(v1) + (1d/2d)*Math.sqrt(v2) - b/(4d*a);
					tmps[3] = (1d/2d)*Math.sqrt(v1) - (1d/2d)*Math.sqrt(v2) - b/(4d*a);
				}
			}else {
				tmps[0] = (-1d/2d)*Math.sqrt(v1) + (1d/2d)*Math.sqrt(v2) - b/(4d*a);
				tmps[1] = (-1d/2d)*Math.sqrt(v1) - (1d/2d)*Math.sqrt(v2) - b/(4d*a);
				v2 = -2d*s - A - (2d*B)/(Math.sqrt(2d*s - A));
				if(v2 > 0)
					tmps[2] = (1d/2d)*Math.sqrt(v1) + (1d/2d)*Math.sqrt(v2) - b/(4d*a);
					tmps[3] = (1d/2d)*Math.sqrt(v1) - (1d/2d)*Math.sqrt(v2) - b/(4d*a);
			}*/
		
		/*Complex S;
		Complex A2 = new Complex(a2, 0d);
		Complex B2 = new Complex(b2, 0d);
		Complex Q = new Complex(q, 0d);
		Complex P = new Complex(p, 0d);
		Complex C_1 = new Complex(-1d, 0d);
		Complex C1 = new Complex(1d, 0d);
		Complex C2 = new Complex(2d, 0d);
		Complex C3 = new Complex(3d, 0d);
		Complex C4 = new Complex(4d, 0d);
		Complex C27 = new Complex(27d, 0d);
		Complex AA = new Complex(A, 0d);
		Complex BB = new Complex(B, 0d);
		
		S = P.exp(3d).mul(C4).div(C27).adi(Q.exp(2d)).exp(1d/2d).mul(C1.div(C2)).adi(C_1.mul(Q).div(C2)).exp(1d/3d).adi(
		P.exp(3d).mul(C4).div(C27).adi(Q.exp(2d)).exp(1d/2d).mul(C_1.div(C2)).adi(C_1.mul(Q).div(C2)).exp(1d/3d)).sub(
		B2.div(C3.mul(A2)));
		
		Complex X1, X2, X3, X4;
		A2 = new Complex(a, 0d);
		B2 = new Complex(b, 0d);
		
		X1 = C2.mul(S).sub(AA).exp(1d/2d).mul(C_1.div(C2)).
		adi(C1.div(C2).mul(C_1.mul(C2).mul(S).sub(AA).adi(C2.mul(BB).div(C2.mul(S).sub(AA).exp(1d/2d))).exp(1d/2d))).
		sub(B2.div(C4.mul(A2)));
		System.out.println(X1);
		
		X2 = C2.mul(S).sub(AA).exp(1d/2d).mul(C_1.div(C2)).
		sub(C1.div(C2).mul(C_1.mul(C2).mul(S).sub(AA).adi(C2.mul(BB).div(C2.mul(S).sub(AA).exp(1d/2d))).exp(1d/2d))).
		sub(B2.div(C4.mul(A2)));
		System.out.println(X2);
				
		X3 = C2.mul(S).sub(AA).exp(1d/2d).mul(C1.div(C2)).
		adi(C1.div(C2).mul(C_1.mul(C2).mul(S).sub(AA).sub(C2.mul(BB).div(C2.mul(S).sub(AA).exp(1d/2d))).exp(1d/2d))).
		sub(B2.div(C4.mul(A2)));
		System.out.println(X3);
						
		X4 = C2.mul(S).sub(AA).exp(1d/2d).mul(C1.div(C2)).
		sub(C1.div(C2).mul(C_1.mul(C2).mul(S).sub(AA).sub(C2.mul(BB).div(C2.mul(S).sub(AA).exp(1d/2d))).exp(1d/2d))).
		sub(B2.div(C4.mul(A2)));
		System.out.println(X4);*/
		
	}
	
	public static class Complex {
		private double real;
		private double imaginario;
		private double norma;
		private double teta;
		
		public Complex(double r, double i) {
			this.real = r;
			this.imaginario = i;
			this.norma = Math.sqrt(r*r + i*i);
			this.teta = Math.atan2(i,r);
		}
		
		public Complex(double r) {
			this.real = r;
			this.imaginario = 0;
			this.norma = r;
			this.teta = Math.atan2(0,r);
		}
		
		public double getReal() {
			return real;
		}

		
		public double getImaginario() {
			return imaginario;
		}
		
		public double getNorma() {
			return norma;
		}

		public double getTeta() {
			return teta;
		}

		public Complex adi(Complex c) {
			return new Complex(real + c.getReal(), imaginario + c.getImaginario());
		}
		
		public Complex adi(double r) {
			Complex c = new Complex(r, 0);
			return adi(c);
		}
		
		public Complex sub(Complex c) {
			return new Complex(real - c.getReal(), imaginario - c.getImaginario());
		}
		
		public Complex mul(Complex c) {
			return new Complex(norma*c.getNorma()*Math.cos(teta+c.getTeta()), norma*c.getNorma()*Math.sin(teta+c.getTeta()));
		}
		
		public Complex mul(double r) {
			Complex c = new Complex(r, 0);
			return mul(c);
		}
		
		public Complex div(Complex c) {
			return new Complex(norma/c.getNorma()*Math.cos(teta-c.getTeta()), norma/c.getNorma()*Math.sin(teta-c.getTeta()));
		}
		
		public Complex exp(double e) {
			return new Complex(Math.pow(norma,e)*Math.cos(teta*e), Math.pow(norma,e)*Math.sin(teta*e));
		}
		
		public String toString() {
			return real + " + " + imaginario + "i";
		}
	}
}
