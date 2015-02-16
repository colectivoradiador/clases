Manifiesto{

	var <>ruido,<wav,<glichy,<bongo,<aguita,<guita,<jar,<arpbu,<llavero,armonico,b,a;
	var <fmResS1,<fmResS2,<fmResS3,<fmResS4,<fmResS5;

	*new{
		^super.new.init;
	}




	init{

		wav = Wavesets.from(Platform.resourceDir +/+ "sounds/pulseglitches1Mono.wav");
		aguita = Wavesets.from(Platform.resourceDir +/+ "sounds/agua-pvc-tubo_sel.wav");
		glichy = Wavesets.from(Platform.resourceDir +/+ "sounds/Glitchoso1_Mono.wav");
		bongo = Wavesets.from(Platform.resourceDir +/+ "sounds/bongos1_Mono.wav");
		guita = Wavesets.from(Platform.resourceDir +/+ "sounds/rasgueoApagadoselMono.wav");
		jar = Wavesets.from(Platform.resourceDir +/+ "sounds/jaranaMono.wav");
		arpbu = Wavesets.from(Platform.resourceDir +/+ "sounds/arpegioBuffMono.wav");
		llavero = Wavesets.from(Platform.resourceDir +/+ "sounds/llaveroMono.wav");
		Wavesets.prepareSynthDefs;


		b = Buffer.alloc(Server.default,1024,1);
		a = Harmonics.new(16);

			SynthDef(\armonic,{ arg out=0,gate = 1,tonica = 240, bufnum=0;
				var env;
				env = EnvGen.kr(Env.perc(0.2,10,0.3),gate,doneAction:2);
				Out.ar(out,Pan2.ar(
					Osc.ar(bufnum, tonica, 0, 0.1)*env,0)
				)
			}).add;



		SynthDef(\fmRes, {|amp = 0.35, gate = 1, dur = 0.75, freq = 440|

			var env, port1,port2, mod1, indx1, indx2, out, lfo1, lfo2;

			env = EnvGen.kr(Env.perc(0.5, 0.5, 1), gate, timeScale: dur,  doneAction: 2);
			//env = EnvGen.kr(Env.asr(0.2, 1, 1), gate, doneAction: 2);
			indx1 = Line.ar(1*freq, 2*freq, dur);
			indx2 = Line.ar(2*freq, 0.25*freq, dur);
			lfo1 = Line.ar(freq, 1.25*freq, dur);
			lfo2 = LFNoise2.ar((0.25*freq)/dur,1).range(0.4975, 0.5125);

			//mod = SinOsc.ar((freq*MouseX.kr(0.1,2).poll), 0, indx);
			mod1 = SinOsc.ar([(freq*1.23), (freq*lfo2)], 0, [indx1, indx2]);
			port1 = SinOsc.ar([(freq + mod1), (0.25*freq + mod1)], 0, 0.25);
			port2 = SinOsc.ar([(0.5*freq + mod1), (2*freq + mod1)], 0, 0.125);

			out = LPF.ar(port1+port2, lfo1);

			Out.ar([0,1], Limiter.ar((out*env*amp), 0.7));

		}).add;

	}


	el{arg a;

        {a = Array.fill(2,{|r,g| r = rrand(100,450); g = r*2 });
			Pan2.ar(SinOsc.ar(a,0,0.3)*EnvGen.kr(Env.perc(1,5,0.35),doneAction:2),
				Dust.kr(1).range(-1,1))}.play


	}

	live{arg a;

        {a = Array.fill(2,{|r,g| r = rrand(100,450); g = r*2 });
			Pan2.ar(SinOsc.ar(a,0,0.3)*Pulse.kr([4,6])*EnvGen.kr(Env.perc(1,5,0.35),doneAction:2),
				Dust.kr(1).range(-1,1))}.play


	}

	coding{

		{CombN.ar(SinOsc.ar([220,110],0,0.3)*Pulse.kr([4,6]),
			0.3,LFNoise0.kr(3).range(0.2,0.3))*EnvGen.kr(Env.perc(1,5,0.35),doneAction:2)}.play


	}

	no{arg orden;

		orden = wav.lengths.order;

		Task({

			orden.do { arg start;
				var ev = wav.eventFor(start, numWs: 12, repeats: 1);

				ev.putPairs([\amp, 0.5]);
				ev.play;
				(ev.sustain).wait;
			};


			(300, 350 .. 2320).do { arg start;
				var ev = wav.eventFor(start, numWs: 200, repeats: 1);

				ev.putPairs([\amp, 0.5]);
				ev.play;
				(ev.sustain).wait;
			};



		}).play

	}


	tiene{arg start =300, numW = 300,rate = 1;

		bongo.eventFor(startWs: start, numWs: numW, repeats: 2,playRate: rate).play;

	}

	es{arg start =300, numW = 300,rate = 1;

		glichy.eventFor(startWs: start, numWs: numW, repeats: 2,playRate: rate).play;

	}

	una{arg start =300, numW = 300;


		Task({

			var orden;
			//orden = ~aguita.xings;
			//orden = ~aguita.lengths.order;

			(50,55..3020).do { arg start;

				var ev, numW;
				//numW = [40,4].choose;
				numW = [42,10,2].choose;
				//numW = 120;

				ev = bongo.eventFor(start, numWs: numW, repeats: 1,playRate: 1);

				ev.putPairs([\amp, 0.8]);
				ev.play;
				//1.wait;
				(ev.sustain).wait;

			};

		}).play;


	}

	tecnica{arg start =300, numW = 300,rate = 1;

		glichy.eventFor(startWs: start, numWs: numW, repeats: 2,playRate: rate).play;

	}

	teoria{arg start =300, numW = 300,rate = 1;

		wav.eventFor(startWs: start, numWs: numW, repeats: 2,playRate: rate).play;

	}

	fin{arg start =300, numW = 300,rate = 1;

		bongo.eventFor(startWs: start, numWs: numW, repeats: 2,playRate: rate).play;

	}

	proceso{

		Task({


			var orden;
			//orden = ~aguita.xings;
			//orden = llavero.lengths.order;
//
			(10,40..3820).do { arg start;

				var ev, numW,rep;
				//numW = [40,4].choose;
				numW = [5,50,10].choose;
				rep = [1,5,3,10].choose;
				//numW = 120;

				ev = llavero.eventFor(start, numWs: numW, repeats: rep,playRate: 0.3);

				ev.putPairs([\amp, 0.7]);
				ev.play;
				//1.wait;
				(ev.sustain).wait;

			};

		}).play;
}
		nunca{

				Task({


			var orden;
			//orden = ~aguita.xings;
			//orden = llavero.lengths.order;
//
			(1000,1012..2822).do { arg start;

				var ev, numW,rep;
				//numW = [40,4].choose;
				numW = [5,100,1].choose;
				rep = [1,5,3,10].choose;
				//numW = 120;

				ev = llavero.eventFor(start, numWs: numW, repeats: rep,playRate: 0.7);

				ev.putPairs([\amp, 0.7]);
				ev.play;
				//1.wait;
				(ev.sustain).wait;

			};

		}).play;




		}

	solo{arg start =300, numW = 300,rate = 1;

		llavero.eventFor(startWs: start, numWs: numW, repeats: 5,playRate: rate).play;

		}

	concierto{


		Task({


			var orden;
			//orden = ~aguita.xings;
			//orden = ~aguita.lengths.order;

			(50,55..7020).do { arg start;

				var ev, numW;
				//numW = [40,4].choose;
				numW = [420,10,2].choose;
				//numW = 120;

				ev = glichy.eventFor(start, numWs: numW, repeats: 1,playRate: 1);

				ev.putPairs([\amp, 0.6]);
				ev.play;
				//1.wait;
				(ev.sustain).wait;

			};

		}).play;

	}

	forma{


		Task({


			var orden;
			//orden = ~aguita.xings;
			//orden = ~aguita.lengths.order;

			(50,150..10020).do { arg start;

				var ev, numW;
				//numW = [40,4].choose;
				numW = [42,10,200].choose;
				//numW = 120;

				ev = aguita.eventFor(start, numWs: numW, repeats: 1,playRate: 1);

				ev.putPairs([\amp, 0.7]);
				ev.play;
				//1.wait;
				(ev.sustain).wait;

			};

		}).play;

	}

	practica{

		arg start =30, numW = 1100,rate = 1;

		guita.eventFor(startWs: start, numWs: numW, repeats: 4,playRate: rate).play;

	}

	reto{

		arg start =30, numW = 1100,rate = 1;

		aguita.eventFor(startWs: start, numWs: numW, repeats: 4,playRate: rate).play;

	}

	interaccion{



		Task({


			var orden;
			orden = guita.xings.scramble;
			//orden = ~guita.lengths.order.pyramid;

			orden.do { arg start;

				var ev, numW;
				//numW = [40,4].choose;
				numW = [4,100,2,1].choose;
				//numW = 120;

				ev = guita.eventFor(start, numWs: numW, repeats: 2,playRate: 0.75);

				ev.putPairs([\amp, 0.6]);
				ev.play;
				//1.wait;
				(ev.sustain).wait;

			};

		}).play;

	}

	instrumento{


		Task({
			// segments of 10 wavesets, step by 1 => 10x longer
			(190, 191 .. 2350).do { arg start;
				var ev, ev2;
				ev = jar.eventFor(start, numWs: 60, repeats: 1);


				ev.putPairs([\amp, 0.4]);
				ev.play;
				(ev.sustain).wait;

			};
			1.wait;

			// segments of 10 wavesets, step by 1 => 10x longer
			(10, 13 .. 1350).do { arg start;
				var ev = jar.eventFor(start, numWs: 50, repeats: 1);

				ev.putPairs([\amp, 0.4]);
				ev.play;
				(ev.sustain).wait;
			};
			1.wait;

			// segments of 10 wavesets, step by 1 => 10x longer
			(300, 350 .. 2320).do { arg start;
				var ev = jar.eventFor(start, numWs: 200, repeats: 1);

				ev.putPairs([\amp, 0.4]);
				ev.play;
				(ev.sustain).wait;
			};
			1.wait;


		}).play;



	}


	texto{
		arg start =30, numW = 11000,rate = 1;

		arpbu.eventFor(startWs: start, numWs: numW, repeats: 4,playRate: rate).play;

	}



	discurso { { Mix.new(Pan2.ar(
		BPF.ar(WhiteNoise.ar(1/3),
			Array.fill(12,{|f|f = rrand(80,1200)}),
			Array.fill(12,{|r| r = rrand(0.001,0.013)})
		)*EnvGen.kr(Env.perc(5,50),doneAction:2)
		,0))}.play
	}


	sentido{arg geom = 0.001, cut = 1;

		b.sine2(a.geom(geom), a.cutoff(cut), true, true, true);
		armonico = Synth(\armonic,[\out, 0, \bufnum, b]);

		}


	con{arg nota = 71;
		fmResS1 = Synth(\fmRes, [\freq, nota.midicps,\dur , 10, \amp, 0.25]);


		}

	sin{arg nota = 64;

		fmResS2 = Synth(\fmRes, [\freq, nota.midicps, \dur , 60, \amp, 0.5]);

	}

	durante{arg nota = 69;

		fmResS3 = Synth(\fmRes, [\freq, nota.midicps, \dur , 30, \amp, 0.75]);

	}

	hacia{arg nota = 36;

		fmResS4 = Synth(\fmRes, [\freq, nota.midicps, \dur , 60, \amp, 0.75]);

	}

	para{arg nota = 48;

		fmResS5 = Synth(\fmRes, [\freq, nota.midicps, \dur , 60, \amp, 0.75]);

	}

	improvisacion{

		Task({
			30.do{"silencio por favor".postln;1.wait}}).play

		}

	dialogo{

		Task({
			1.do{  this.discurso;
				1.wait;
			       this.hacia;
				1.wait;
				   this.interaccion}



		}).play


		}



	publico{

		Task({
			1.do{  this.para;
				1.wait;
			       this.reto;
				1.wait;
				   this.proceso}



		}).play


		}

	silencio{|time = 2|

		ruido.set(\gate,0);
	}

}