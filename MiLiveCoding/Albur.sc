
Albur{

 var <>ritmo,<>bajos,<bombito,<hola,<>rudo,<>tambores,<>pianito,<>bomoseq,<>otrobajo;


 *new{
  ^super.new.init;
 }


init{


SynthDef(\bombo, {arg freq=100, gate=1,amp=1,out=0;
 var sig, kick, env;
 kick=LPF.ar(EnvGen.ar(Env.perc(0,0.45)),70,0.45);
 sig=Demand.ar(Impulse.ar(freq),0,Dseq(Array.rand(20,-1.0,1.0),100000));
 sig=RLPF.ar(sig,XLine.kr([180,170],30,0.1),0.5,0.15329)+SinOsc.ar(XLine.kr([180,170],30,0.1),pi/2);
 sig=Mix(sig+kick);
 env=EnvGen.kr(Env.perc(0,0.54365),gate,doneAction:2);
 Out.ar(out,sig*env*amp);
}).add;


SynthDef(\hola,{|gate,dur =1,amp1 =0.5,amp2=0.5,amp3=0.5,amp4=0.5,amp5=0.5,freq=60,volhola=0.5|
  var sig1,sig2,sig3,sig4,sig5,sig,env,env2;
  sig1=SinOsc.ar(freq)*amp1*2;
  sig2=Saw.ar(freq)*amp2;
  sig3=Pulse.ar(freq)*amp3;
  sig4=Blip.ar(freq,4)*amp4;
  sig5=Formant.ar(freq,67)*amp5;
  sig=(Mix([sig1,sig2,sig3,sig4,sig5])*volhola)!2;
  sig=Limiter.ar(sig,1,0.01);
  env=EnvGen.ar(Env.adsr(0.01,dur,0.01,0.01,1,-5),Line.kr(1,0,dur), doneAction:2);
      env2=EnvGen.ar(Env.asr(0,1,0.01), gate, doneAction:2);
  Out.ar(0,sig*env*env2*0.4);
  }).add;

SynthDef(\rudo, {|a = 2, gate=0 amp= 0.09, duracion = 1|
			var e= EnvGen.ar(Env.linen(7,duracion,10),gate, doneAction:2);
			var i= MoogFF.ar(Pulse.ar([18, 36, 72, 9]*a), FSinOsc.kr(LFNoise0.kr([10.2, 0.34]!2)).range(20, 50)*a, 0.3).clump(2);
			var z = Limiter.ar(GVerb.ar(i,80,4.85,0.41,0.19,15,-3,-11,-9)*e,0.8,0.01);

Out.ar(0, z*amp);
}).add;

SynthDef(\tambor,{|gate=1, rel1=0.4, rel2=0.283, amp=1.2,out|
var filtWhite = LPF.ar(WhiteNoise.ar(1), 7040, 1),sen;
sen=((SinOsc.ar(330,0,0.25) * EnvGen.ar(Env.perc(0.0005,0.055), gate))
			+(SinOsc.ar(185,0,0.25) * EnvGen.ar(Env.perc(0.0005,0.075), gate))
			+(filtWhite * EnvGen.ar(Env.perc(0.0005,rel1), gate) * 0.2)
			+(HPF.ar(filtWhite, 523, 1) * EnvGen.ar(Env.perc(0.0005,rel2), gate) * 0.2)
			) * amp;
			DetectSilence.ar(sen,doneAction:2);
			sen=Pan2.ar(sen,0);
			Out.ar(out,sen)
			}).add;


SynthDef(\bombito,

	{ arg out = 0, freq = 50, mod_freq = 5, mod_index = 5,
	decay = 0.4, amp = 0.5,
	beater_noise_level = 0.025;
	var pitch_contour, drum_osc, drum_lpf, drum_env;
	var beater_source, beater_hpf, beater_lpf, lpf_cutoff_contour, beater_env;
	var kick_mix;
	pitch_contour = Line.kr(freq*2, freq, 0.02);
	drum_osc = PMOsc.ar(	pitch_contour,
				mod_freq,
				mod_index/1.3,
				mul: 1,
				add: 0);
	drum_lpf = LPF.ar(in: drum_osc, freq: 1000, mul: 1, add: 0);
	drum_env = drum_lpf * EnvGen.ar(Env.perc(0.005, decay), 1.0, doneAction: 2);
	beater_source = WhiteNoise.ar(beater_noise_level);
	beater_hpf = HPF.ar(in: beater_source, freq: 500, mul: 1, add: 0);
	lpf_cutoff_contour = Line.kr(6000, 500, 0.03);
	beater_lpf = LPF.ar(in: beater_hpf, freq: lpf_cutoff_contour, mul: 1, add: 0);
	beater_env = beater_lpf * EnvGen.ar(Env.perc, 1.0, doneAction: 2);
	kick_mix = Mix.new([drum_env, beater_env]);
	Out.ar(out, kick_mix*amp)}
	).add;

SynthDef(\bajoseq1,{|gate=1,amp=1,frec=20,cutoff=100,ancho=0.1,rq=0.5,out=0|
 var sen,env;
 sen=RLPF.ar(Saw.ar(frec*[1,1.01],amp),cutoff,rq);
 env=EnvGen.kr(Env.perc(0.01,1),gate,doneAction:2);
 Out.ar(out,(sen*env).clip2(0.5)*amp)}
).add;

SynthDef(\mdapiano, { |out=0, freq=440, gate=1, amp=0.4|
	var son = MdaPiano.ar(freq, gate, release: 0.9, stereo: 0.3, sustain: 0);
	DetectSilence.ar(son, 0.01, doneAction:2);
	Out.ar(out, son * amp,0.1)}).add

	}

		alma_Madero_Benitez{arg frec=10, vol = 0.6, dur=2;
		rudo = Synth(\rudo,[\gate, 1,\a,frec,\duracion,dur]);
		}


		pajaro{
		ritmo = Tdef(\bombo,{
		30.do{Synth(\bombo,[\amp,rand(0.3,0.5)*2,\freq,rand(10,500).cpsmidi,\out,rrand(0,4)]);[0.25/2,0.25,0.5].choose.wait}}).play;
		}

		mea_Orcas{arg frec=20, vol=0.4;
		pianito = Tdef(\pianito,{
			15.do{Synth(\mdapiano,[\amp,vol,\freq,Prand(Array.fib(7,2,frec),inf).asStream.next]);
			[1,0.25,0.5,1.25].choose.wait}}).play
		}

		mea_Placas{arg cutof=100;
		otrobajo=Tdef(\bajo_seq,{
			var notas;
			notas = Prand([60,45,34,56,7,8,9],inf).asStream;
			20.do{
              Synth(\bajoseq1,[\frec,notas.next,\cutoff, cutof]);
                             [1,0.25,0.5].choose.wait}}).play
		}

		chico{
		bajos=Tdef(\bajo,{
		7.do{Synth(\hola,[\gate,1,\dur,0.5*2, \amp1,rrand(0.9,1.8),\amp2,rrand(1.4,2),\amp3,rrand(0.9,1.8),\amp4,rrand(0.9,1.8),\amp5,rrand(0.9,1.8),\freq,Array.fib(5,100,2).choose.cpsmidi, \volhola,rrand(0.5,1)]);[1,0.5,2].wchoose([0.2,0.5,0.9].normalizeSum).wait}}).play;
	}

		medallas{
		bajos.play;
		}

		pelon{arg rela1=0.4,rela2=0.283;
		tambores = Tdef(\tamborseq,{
		20.do{Synth(\tambor,[\amp,exprand(0.125,2.5),\out,exprand(0,1),\rel1,exprand(0.1,rela1),\rel2,rrand(0.0, rela2)]);[0.25/2,0.25/4,0.25,0.5,1].wchoose([0.5,0.25,0.5,0.5,0.9].normalizeSum).wait}
}).play
	}

		con_Suleas{
		tambores.play;
		}

		chile{
		bomoseq=Tdef(\bombitoseq,{arg mod_frec=10.0, frec=500;
			20.do{Synth(\bombito,[\mod_freq,rand(2.0,mod_frec),\amp,rand(0.08,1),\freq,rand(100,frec).cpsmidi,\out,rrand(0,1)]);[0.25/2,0.25,0.5].choose.wait}
}).play
		}

		en_cajones{
		bomoseq.play;
		}

}