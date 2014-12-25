package tubosi;

import static java.lang.Integer.parseInt;

import java.util.HashMap;
import java.util.Map;

/**
 * ポケモンのダメージ計算を行うクラス
 * @author kazuma
 *
 */
public class Calc {
	private Pokemondata a_p;	//攻撃ポケモンのデータを保持するインスタンス
	private Pokemondata d_p;	//防御ポケモンのデータを保持するインスタンス
	private Wazadata waza;		//放たれる技に関するデータを保持するインスタンス
	private Field field;		//戦闘が行われる「場」に関するデータを保持するインスタンス
	private double[][] k;
	private double[][] l;
	private int[] damages;
	
	Calc(Pokemondata a, Pokemondata d, Wazadata waza, Field field){
		this.a_p = a;
		this.d_p = d;
		this.waza = waza;
		this.field = field;
	}
	
	/**
	 * 計算を行い、Resultオブジェクトに結果を格納し、返すメソッド
	 * @return	ダメージの結果を格納したResultクラスのインスタンスを返す
	 * @throws Exception
	 */
	 Result getResult() throws Exception{
		 double[][] k2 = {
			{1,1,1,1,0.02},	//レベル定数、威力、攻撃側能力値、1/防御側能力値、1/50 !!+2するが切り上げ（ceil）はない！
			{1,1},			//フィールド、天候（+2があるため上と配列を分けている）
			{1,0.84,1},		//急所、乱数、一致
			{1,1,1}			//相性、やけど、{さきどり・木の実・色眼鏡・マルスケフィルタ・達帯いのたま・壁}
		 };
		 double[][] l2 = {
			{1,1,1,1,1,1,1},//計算値、威力、攻撃側特性、攻撃側アイテム、防御側特性、防御アイテム、泥水
			{1,1,1,1,1},	//計算値、能力値、ランク、攻撃特性、攻撃アイテム
			{1,1,1,1,1,1},	//計算値、能力値、ランク、防御特性、防御アイテム、砂嵐補正
			{1,1,1,1,1,1,1},//計算値,木の実、色眼鏡・おやこあい、マルスケ・フィルタ、達帯・いのたま、壁、さきどり	 
		 };
		 
		 this.k = k2;
		 this.l = l2;
		setValue();
		calculation();
		
		if(a_p.oyakoai){
			int[] oya = this.damages.clone();
			l[3][2] = 0.5;
			calculation();
			int[] ans = new int[256];
			int c = 0;
			for(int i = 0; i<16; i++){
				for(int j = 0; j<16; j++){
					ans[c++] = oya[i] + damages[i];
				}
			}
			this.damages = ans;
		}
		
		Map<Integer, Integer> damage = toHashMap(this.damages);
		Result ans = new Result(d_p.HP, damage);
		double A = 50 * l[2][0] *(100.0*d_p.HP/85.0 - 2.0);
		ans.pIndex_D = ((A/(50.0 * 5.0) + 2.0) * 0.85) / 12;
		return ans;
	 }
	 
	 /**
	 * 実際のダメージを計算するメソッド
	 * フィールドのdamagesに値をセットする
	 */
	 void calculation(){
			damages = new int[16];
			for(int i=0; i<l.length; i++){
				double hosei = 1;
				for(int j=2; j<l[i].length; j++){
					hosei *= l[i][j];
				}
				if(i==3){
					l[i][0] = l[i][1]*hosei;
				}else{
					l[i][0] = Math.ceil(l[i][1]*hosei-0.5);
				}
			}
			
			k[0][1] = l[0][0];
			k[0][2] = l[1][0];
			k[0][3] = 1/l[2][0];
			k[3][2] = l[3][0];
			
			for(int h = 0; h<16; h++){
				int subans = 1; 
				k[2][1] += 0.01;
				for(int i = 0; i<k.length; i++){
						for(int j = 0; j<k[i].length; j++){
							subans = (int)(subans*k[i][j]);
							if(j==4 && i==0)subans += 2;
						}
						subans = (int)Math.ceil(subans - 0.5);
				}
				damages[h] = subans;
			}
			k[2][1] = 0.84;
		}

	/**
	 * 計算用の二次元配列に値をセットするメソッド
	 */
	void setValue(){
		double rank_A = (double)(Math.max(a_p.rank,0)+2)/(Math.abs((Math.min(a_p.rank,0)))+2);
		double rank_B = (double)(Math.max(d_p.rank,0)+2)/(Math.abs((Math.min(d_p.rank,0)))+2);
		double constantOfLv = a_p.lv * 2/5 +2;
		
		l[0][1] = waza.power_price;
		l[1][1] = a_p.a;
		l[1][2] = rank_A;
		l[2][1] = d_p.b;
		l[2][2] = rank_B;
		k[0][0] = constantOfLv;
		
		setField();				//「場」をセット
		getTypeItti();			//タイプ一致かどうかの判定
		getAisyou();			//相性判定
		doHosei(a_p.property);	//攻撃側の特性補正
		doHosei(d_p.property);	//防御側の特性補正
		doHosei(a_p.item);		//攻撃側のアイテム補正
		doHosei(d_p.item);		//防御側のアイテム補正
	
	}
	
	/**
	 * 攻撃側のポケモンのデータを変更するメソッド
	 * @param a_p 変更する攻撃側のポケモンのデータ
	 */
	void changeOffenseTo(Pokemondata a_p){
		this.a_p = a_p;
	}
	
	/**
	 *防御側のポケモンのデータを変更するメソッド 
	 * @param d_p　変更する防御側のポケモンのデータ
	 */
	void changeDeffenseto(Pokemondata d_p){
		this.d_p = d_p;
	}
	
	/**
	 * 技のタイプとポケモンのタイプが一致したとき1.5倍の補正をかけるメソッド
	 */
	void getTypeItti(){
		if(a_p.type[0] == waza.type || a_p.type[1] == waza.type)k[2][2]=1.5;
	}
	
	/**
	 * 技のタイプとポケモンの相性を計算し、反映するメソッド
	 */
	void getAisyou(){
		if(waza.type<0 || d_p.type[0] == -1)return;
		boolean flag = waza.name.equals("フリーズドライ");
		if(flag)Aisyo.aisyou[14][3] = 20;
		double aisyou_d = (double)Aisyo.aisyou[waza.type][d_p.type[0]] * 0.1;
		if(d_p.type[1] != -1){
			aisyou_d *= ((double)Aisyo.aisyou[waza.type][d_p.type[1]] * 0.1);
		}
		k[3][0] = aisyou_d;
		if(flag) Aisyo.aisyou[14][3] = 5;
	}
	
	/**
	 * 特性とアイテムの補正値を反映するメソッド
	 * @param s	補正場所、発動条件、補正値をあらわす数列を文字列で表したもの
	 */
	void doHosei(String s){
		if(s.equals("N")) return;
		int result = getHosei(s.substring(2,s.length()));
		if(result == -5) return;
		l[parseInt(""+s.charAt(0))][parseInt(""+s.charAt(1))] = result * 0.05;
	}
	
	/**
	 * 実際の補正値を計算するメソッド
	 * @param s	特性やアイテムの「発動条件」と「補正値」を表した数列を文字列で表したもの
	 * @return	発動条件を満たしているときは補正値を返し、そうでないときは-5を返す
	 */
	int getHosei(String s){
		int c = 0;
		switch (s.charAt(c++)){
			case '0'://技のタイプによって発動する特性
				switch (s.charAt(c++)) {
					case '0'://激流系＋虫の知らせ＋耐熱＋厚い脂肪＋ノーマルジュエル
						int f = parseInt(""+s.charAt(c++));
						for(int i=c; i<f*2+c; i+=2){
							if(waza.type == parseInt(s.substring(i,i+2))) return parseInt(s.substring(c+f*2,c+f*2+2));
						}
						return 	-5;
						
					case '1'://スキン系の特性
						if(waza.type == 0) {
							waza.type = parseInt(s.substring(c,c+2));
							getAisyou();
							getTypeItti();
							return 26;
							}else{
								return 	-5;
							}
						
					case '2'://ノーマルスキン
						if(waza.type != 0){
							waza.type=0;
							getAisyou();
							getTypeItti();
						}
						return -5;
						
					case '3'://砂のちから
						if(field.weather == 6 && (waza.type == 6 || waza.type == 7 || waza.type == 15)) return 26;
							else return -5;
						
					case '4'://適応力
						if(k[2][2] == 1.5) k[2][2] = 2;
					    return -5;
				}
				
			case '1'://技が物理が特殊かで発動する特性
				if(waza.physical != (parseInt(""+s.charAt(c++))))return -5;
				switch(s.charAt(c++)){
					case '0'://ヨガパワー、力持ち、はりきり、暴走系、ふしぎなうろこ、ファーコート,チョッキ,こだわり系
						return parseInt(s.substring(c,c+2));
					
					case '1'://サンパワー、フラワーギフトなど
						if(field.weather == 2) return parseInt(s.substring(c,c+2));
						else return -5;
					
					case '2'://根性　この後にsetField()を呼び出すと結果が変わるので注意！
						k[3][1]=1; return 30;
					
					case '3'://草の毛皮
						if(field.ground.equals("0530")) return 30; 
					   else return -5;
				}
				
			case '2'://タイプ相性によって発動する特性、もちもの
				switch(s.charAt(c++)){
					case '0'://変幻自在
						k[2][2] = 1.5;
						return -5;
					
					case '1'://こっちで入れるかむこうでいれるか　いろめがね
						if(k[3][0] < 1) return 40;
						else return -5;
					
					case '2'://ハードロック・フィルター・達人の帯 半減実
						if(k[3][0] > 1) return parseInt(s.substring(c,c+2));
						else return -5;
					
					case '3'://きもったま
						if((waza.type == 0 || waza.type == 1) && (d_p.type[0] == 12 || d_p.type[1] == 12)){
							Aisyo.aisyou[waza.type][12] = 10;
							getAisyou();
							Aisyo.aisyou[waza.type][12] = 0;
							return -5;
						}
						return -5;
				}
				
			case '3'://アナライズ、闘争心、弱気、天然、マルスケ 各種強化アイテム,メトロノーム いのちのたま
				return parseInt(s.substring(c,c+2));
			
			case '4'://スナイパー
				if(field.critical) k[2][0] = 3;
				return -5;
			
			case '5'://テクニシャン
				if(l[0][1] < 61) return 30;
				else return -5;
			
			case '6'://ポケモンの種類によって発動するもちもの
				String pName = "";
				if(parseInt(""+s.charAt(c++)) ==0) pName = a_p.name;//攻撃側か守備側か
				else pName = d_p.name;
				switch(s.charAt(c++)){
					case '0'://でんきだま
						if("ピカチュウ".equals(pName)) return 40;
						else return -5;
					
					case '1'://太い骨、メタルパウダー、深海の牙、深海の鱗
						if(parseInt(""+s.charAt(c++)) != waza.physical) return -5;
						String[] pn ={"ガラガラ","メタモン","パールル"};
						if(pn[(parseInt(""+s.charAt(c++)))].equals(pName)) return 40;
						else return -5;
						
				    case '2'://こころのしずく
				    	if(waza.physical == 1) return -5;
				    	String[] rathi = {"ラティオス","ラティアス"};
				    	if(rathi[0].equals(pName) || rathi[1].equals(pName)) return 30;	
				    	else return -5;
					
				    case '3'://しらたま、こんごうだま、はっきんだま
				    	String[] sinnou ={"パルキア","ディアルガ","ギラティナ"};
				    	if(pName.indexOf(sinnou[parseInt(""+s.charAt(c++))]) == 0 && 
							(waza.type == a_p.type[0] || waza.type == a_p.type[1])) return 24;
				    	else return -5;
				}
				
			case '7'://技の種類によって発動する特性の補正
				int hosei = -5;
				String kouka = s.substring(c,c+2);
				c+=2;
				if(waza.additional_effect % parseInt(kouka) == 0) hosei = parseInt(s.substring(c,c+2));
				return hosei;
			
			case '8'://進化の輝石
				int kiseki = -5;
				if(d_p.isDeveloping == 1) kiseki = 30;
				return kiseki;
				
			case '9'://親子愛
				a_p.oyakoai=true;
				return -5;
				
			default:return -5;
		}
	}
	
	/**
	 *戦闘の「場」のデータをFieldオブジェクトから取り出し、
	 *計算用の2次元配列にセットするメソッド 
	 */
	void setField(){
		//どろあそび、みずあそびの判定● あそびのvalueはtypeのみ
		for(int i=0; i<2; i++){
			if(Integer.toString(waza.type).equals(field.playWithWorM[i])){
				l[0][6] = 1/3;
				break;
			}
		}
		
	//フィールド判定● フィールドのvalueはtypeと補正＊20
		if(!field.ground.equals("N")){
			String s = field.ground.substring(0,2);
			if(waza.type == (parseInt(s)) + 1){
				l[1][0] = parseInt(field.ground.substring(2,4));
			}
		}
		
	//かべ補正●
		for(int i = 0; i<2; i++){
			if(Integer.toString(waza.physical).equals(field.wall[i])){
				l[3][5] = 0.5;
				break;
			}
		}
		
	//天候補正●
		if(field.weather>0 && field.weather<10){ 
			if(field.weather<5){
				if(waza.type == field.weather){
					k[1][1] = 1.5;
				}else if(field.weather + waza.type == 5){
					k[1][1] = 0.5;
				}
			}else if((waza.physical != 1) && (field.weather == d_p.type[0] || field.weather == d_p.type[1])){
				l[2][5] = 1.5;//砂嵐補正
			}
		}
		
	//急所判定●
		if(field.critical){ 
			k[2][0] = 1.5;
			l[3][5] = 1.0;
			l[1][2] = Math.max(l[1][2], 1);
			l[2][2] = Math.min(l[2][2], 1);
		}
		
	//やけど判定●
		if(waza.physical == 1 && a_p.isScaled){
			k[3][1]=0.5;
		}
		
	//先取り判定●
		if(field.takeInAdvance){
			l[3][6]=1.5;
		}
	}
	
	/**
	 * 受け取った配列を連想配列にして返すメソッド
	 * @param damages ダメージのパターン
	 * @return	ダメージのパターンと場数を格納した連想配列　Map
	 */
	Map<Integer, Integer> toHashMap(int[] damages){
		Map<Integer, Integer> ans = new HashMap<Integer, Integer>();
		for(int i = 0; i<damages.length; i++){
			if(ans.get(damages[i]) == null){
				ans.put(damages[i], 1);
			}else{
				ans.put(damages[i], ans.get(damages[i])+1);
			}
		}
		return ans;
	}
}

