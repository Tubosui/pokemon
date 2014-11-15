
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.lang.Integer.parseInt;

class Calculation{
	double[][] kk =new double[4][];//補正のための配列（攻撃、防御、技の威力、壁等の補正）
	double[][] l = new double[4][];//全体計算の時のための配列
	String[] pName,kabe,asobi;
	String wazaName,ApItem,BpItem,ApTokusei,BpTokusei,yakedo,sakidori,kyusyo,Field;
	int wazaType,WazaIryoku,buturi,Aplv,ApNouryokuti,ApRank,HP,BpNouryokuti_B,BpRank,tenko,sinkamae,wazakouka;
	int[] type_A,type_B;
	boolean oyakoai=false;
	double R= 0.84;
	public class ATW{//瀕死になる攻撃回数と確定か乱数かを保持するラッパークラス。
		public boolean kakutei=false;
		public int a_times;
	}
	int[][] aisyou={
				{10,10,10,10,10,10,5,10,10,10,10,10,0,10,10,5,10,10},//	ノーマルとのタイプ相性0
				{20,10,10,10,10,10,20,10,5,5,5,5,0,10,20,20,20,5},//	格闘1
				{10,10,5,5,20,10,5,10,10,10,20,10,10,5,20,20,10,10},//	炎2
				{10,10,20,5,5,10,20,20,10,10,10,10,10,5,10,10,10,10},//	水3
				{10,10,5,20,5,10,20,20,5,5,5,10,10,5,10,5,10,10},//		草4
				{10,10,10,20,5,5,10,0,20,10,10,10,10,5,10,10,10,10},//	電気5
				{10,5,20,10,10,10,10,5,20,10,20,10,10,10,20,5,10,10},//	岩6
				{10,10,20,10,5,20,20,10,0,20,5,10,10,10,10,20,10,10},//	地面7
				{10,20,10,10,20,5,5,10,10,10,20,10,10,10,10,5,10,10},//	飛行8
				{10,10,10,10,20,10,5,5,10,5,10,10,5,10,10,0,10,20},//	毒9
				{10,5,5,10,20,10,10,10,5,5,10,20,5,10,10,5,20,5},//		虫10
				{10,20,10,10,10,10,10,10,10,20,10,5,10,10,10,5,0,10},//	エスパー11
				{0,10,10,10,10,10,10,10,10,10,10,20,20,10,10,10,5,10},//ゴースト12
				{10,10,10,10,10,10,10,10,10,10,10,10,10,20,10,5,10,0},//ドラゴン13
				{10,10,5,5,20,10,10,20,20,10,10,10,10,20,5,5,10,10},//	氷14
				{10,10,5,5,10,5,20,10,10,10,10,10,10,10,20,5,10,20},//	鋼15
				{10,5,10,10,10,10,10,10,10,10,10,20,20,10,10,10,5,5},//	悪16
				{10,20,5,10,10,10,10,10,10,5,10,10,10,20,10,5,20,10}//	フェアリー17
			};
	Calculation(int[] data_i,String[] data_s){//各値を配列から取り出して代入する
		String[] p_Name ={data_s[0],data_s[1]};pName = p_Name;
		wazaName = data_s[2];
		ApItem = data_s[3];BpItem = data_s[4];
		ApTokusei = data_s[5];BpTokusei = data_s[6];
		yakedo = data_s[7];sakidori = data_s[8];kyusyo = data_s[9];
		Field = data_s[10];
		String[] Kabe = {data_s[11],data_s[12]};kabe = Kabe;
		String[] Asobi = {data_s[13],data_s[14]};asobi = Asobi;
		
		
		wazaType = data_i[0]-1;WazaIryoku = data_i[1];buturi = data_i[2];
		Aplv = data_i[3];ApNouryokuti = data_i[4];ApRank = data_i[5];int[] typeA = {data_i[6]-1,data_i[7]-1};type_A=typeA;
		HP = data_i[8];BpNouryokuti_B = data_i[9];BpRank = data_i[10];int[] typeB = {data_i[11]-1,data_i[12]-1};type_B=typeB;
		tenko = data_i[13];wazakouka=data_i[14];sinkamae=data_i[15];
	}
	Calculation(int[] data_i,String[] data_s,String s){//ピィ計算用
		String[] p_Name ={data_s[0],s};pName = p_Name;
		wazaName = data_s[2];
		ApItem = data_s[3];BpItem = "N";
		ApTokusei = data_s[5];BpTokusei = "N";
		yakedo = data_s[7];sakidori = data_s[8];kyusyo = data_s[9];
		Field = data_s[10];
		String[] Kabe = {data_s[11],data_s[12]};kabe = Kabe;
		String[] Asobi = {data_s[13],data_s[14]};asobi = Asobi;
		
		
		wazaType = data_i[0]-1;WazaIryoku = data_i[1];buturi = data_i[2];
		Aplv = data_i[3];ApNouryokuti = data_i[4];ApRank = data_i[5];int[] typeA = {data_i[6]-1,data_i[7]-1};type_A=typeA;
		HP = 12;BpNouryokuti_B = 5;BpRank = 0;int[] typeB = {-1,-1};type_B=typeB;
		tenko = data_i[13];
	}
     public String[] Result(){//これを呼び出せば答えが返る。
    	getKeisan();
    	int[] damageR = calculate();
    	if(oyakoai){//親子愛補正
    		int dpc[]=new int[256];
    		l[3][2]=0.5;
    		kk[2][1]=0.84;
    		int dc[]=calculate();
    		int c=0;
    		for(int i=0;i<16;i++){
    			for(int j=0;j<16;j++){
    				dpc[c++]=damageR[i]+dc[j];
    			}
    		}
    		damageR=dpc;
    	}
    	String[] ansR;
    	try{ansR = getResult(damageR);}catch(StackOverflowError e){String[] s={"HPを正しく入力してください"};ansR=s;};
    	return ansR;
     }

    String[] getResult(int[] damages) throws StackOverflowError{
    	//ダメージパターンの配列を受け取ってダメージ、確定数をStringの配列で返す。
    	//String[] ANS={防御側のHP,ダメージの最小値、ダメージの最大値、乱数か否か、攻撃回数、[乱数の場合は瀕死になる確率、そうでないときは""]};
    	Map<Integer,Integer> dd = new HashMap<Integer,Integer>();
		for(int i=0;i<damages.length;i++){
			dd.put(damages[i],0);
		}
		for(int i=0;i<damages.length;i++){
			dd.put(damages[i],dd.get(damages[i])+1);
		}
		ATW a = null;
		try{a = getAttackTimes(damages[damages.length-1],damages[0],1);}catch(Exception e){String[] ex = {e.getMessage(),""};return ex;};
		String[] ANS =new String[7];
		int c=0;
		ANS[c++] = Integer.toString(HP);//HPを記録
		ANS[c++]=(""+damages[0]);ANS[c++]=(""+damages[damages.length-1]);//最小ダメージ、最大ダメージ
		DecimalFormat df2 = new DecimalFormat("##.###%");
		Iterator<Integer> iter = dd.keySet().iterator();
		int[] damage_A = new int[dd.size()];
		int i=0;
		while(iter.hasNext()){
			damage_A[i++]=iter.next();
		}
		if(!a.kakutei){
			String pro="";
			int kasuu=16;
			int lim=8;
			if(oyakoai){kasuu=256;lim=4;};//親子愛のときは特殊
			if(a.a_times<lim)pro = "("+df2.format((getProbability(HP,dd,damage_A,a.a_times)/Math.pow(kasuu,a.a_times)))+")";
			ANS[c++]="乱数";ANS[c++]=Integer.toString(a.a_times);ANS[c++]=pro;
		}else{
			ANS[c++]="確定";ANS[c++]=Integer.toString(a.a_times);ANS[c++]="";
		}
		double pk = HP*l[2][0]/60;//記録したHPと防御能力値でピィ指数を出す。
		ANS[c++]=Integer.toString((int)(Math.floor(pk)));
    	return ANS;
    }


	protected void getKeisan(){　//フィールドの配列に補正値を入力するメソッド
		double rank_A = (double)(Math.max(ApRank,0)+2)/(Math.abs((Math.min(ApRank,0)))+2);
		double rank_B = (double)(Math.max(BpRank,0)+2)/(Math.abs((Math.min(BpRank,0)))+2);
		
		double[] l0={1,WazaIryoku,1,1,1,1,1};l[0]=l0;//計算値、威力、攻撃側特性、攻撃側アイテム、防御側特性、防御アイテム、泥水
		double[] l1={1,ApNouryokuti,rank_A,1,1};l[1]=l1;//計算値、能力値、ランク、攻撃特性、攻撃アイテム
		double[] l2={1,BpNouryokuti_B,rank_B,1,1,1};l[2]=l2;//計算値、能力値、ランク、防御特性、防御アイテム、砂嵐補正
		double[] l3={1,1,1,1,1,1,1};l[3]=l3;//計算値,木の実、色眼鏡・おやこあい、マルスケ・フィルタ、達帯・いのたま、壁、さきどり

		double k00=Aplv*2/5+2;
		double[] k0={k00,1,1,1,0.02};kk[0]=k0;//レベル定数、威力、攻撃側能力値、防御側能力値、1/50 !!+2するが切り上げ（ceil）はない！
		double[] k1={1,1};kk[1]=k1;//フィールド、天候（+2があるため上と配列を分けている）
		double[] k2={1,R,1};kk[2]=k2;//急所、乱数、一致
		double[] k3={1,1,1};kk[3]=k3;//相性、やけど、{さきどり・木の実・色眼鏡・マルスケフィルタ・達帯いのたま・壁}
		
		
		
	//どろあそび、みずあそびの判定● あそびのvalueはtypeのみ
		for(int i=0;i<2;i++){
			if(Integer.toString(wazaType).equals(asobi[i])){
				l[0][6]=0.33333;
				break;
			}
		}
		//フィールド判定● フィールドのvalueはtypeと補正＊20
		if(!Field.equals("N")){
			String s = Field.substring(0,2);
			if(wazaType==(parseInt(s))+1){
				l[1][0] = parseInt(Field.substring(2,4));
			};
		}
		
		//かべ補正●
		for(int i=0;i<2;i++){
			if(Integer.toString(buturi).equals(kabe[i])){
				l[3][5]=0.5;
				break;
			}
		}
		
		if(tenko>0 && tenko<10){ //天候補正●
			if(tenko<5){
				if(wazaType==tenko){
					kk[1][1]=1.5;
				}else if(tenko+wazaType==5){
					kk[1][1]=0.5;
				}
			}else if((buturi!=1) && (tenko==type_B[0] || tenko==type_B[1])){
				l[2][5] = 1.5;//砂嵐補正
			}
		}
		if(kyusyo.equals("1")){ //急所判定●
			kk[2][0]=1.5;
			l[3][5]=1.0;
			l[1][2]=Math.max(rank_A, 1);
			l[2][2]=Math.min(rank_B, 1);
		}
		
		if(buturi==1&&yakedo.equals("1")){//やけど判定●
			kk[3][1]=0.5;
		}
		
		if(sakidori.equals("1")){//先取り判定●
			l[3][6]=1.5;
		}
		getTypeItti();//タイプ一致かどうかの判定●
		getAisyou();//相性判定●
		doHosei(ApTokusei);doHosei(BpTokusei);//特性補正
		doHosei(ApItem);doHosei(BpItem);//アイテム補正
	}

	int[] calculate(){//フィールドの配列を掛け合わせてダメージを計算するメソッド。
		int ans[] = new int[16];
		for(int i=0;i<l.length;i++){
			double hosei =1;
			for(int j=2;j<l[i].length;j++){
				hosei*=l[i][j];
			}
			if(i==3){
				l[i][0] = l[i][1]*hosei;
			}else{
				l[i][0]=Math.ceil(l[i][1]*hosei-0.5);//配列の使い方について再考の余地あり！！！！！！！！！！！！！！！！！！！
			}
		}
		kk[0][1]=l[0][0];kk[0][2]=l[1][0];kk[0][3]=1/l[2][0];kk[3][2]=l[3][0];//威力、攻撃力、防御力、壁補正などを全体計算の配列に代入。
		for(int h=0;h<ans.length;h++){
			int subans=1; 
			kk[2][1]+=0.01;
			for(int i=0;i<kk.length;i++){
					for(int j=0;j<kk[i].length;j++){
						subans=(int)(subans*kk[i][j]);
						if(j==4&&i==0){subans+=2;}
					}
					subans = (int)Math.ceil(subans-0.5);
			}
			ans[h]=subans;
		}
		return ans;
	}
	
	void doHosei(String s){//補正配列l（エル）に補正値を代入するメソッド（文字列は数列：02010826など）
		if(s.equals("N"))return;
		int result =getHosei(s.substring(2,s.length()));//補正値を取得
		if(result==-5) return;
		l[parseInt(""+s.charAt(0))][parseInt(""+s.charAt(1))]=result*0.05;
	}
	
	void getTypeItti(){//タイプ一致かどうか調べるメソッド●
		for(int i=0; i<type_A.length;i++){
			if(wazaType==type_A[i]&&type_A[i]>=0){
				kk[2][2]=1.5;
			}
		}
	}
	
	void getAisyou(){//タイプ相性を調べるメソッド
		if(wazaType<0 || type_B[0]==-1) {kk[3][0]=1;return;}
		double aisyou_d =aisyou[wazaType][type_B[0]]*0.1;
		if(type_B[1]!=-1){
			aisyou_d*=(aisyou[wazaType][type_B[1]]*0.1);
		}
		kk[3][0]=aisyou_d;
	}
	
	int getHosei(String s){//補正値を取得するメソッド（基本的に数列を分解しながら分岐することで補正値を返している）
		int c=0;
		switch (s.charAt(c++)){
			case '0':
				switch (s.charAt(c++)) {
					case '0':int f =parseInt(""+s.charAt(c++));//激流系＋虫の知らせ＋耐熱＋厚い脂肪＋ノーマルジュエル
						for(int i=c;i<f*2+c;i+=2){
							if(wazaType==parseInt(s.substring(i,i+2)))return parseInt(s.substring(c+f*2,c+f*2+2));;
						}
						return 	-5;
						
					case '1':if(wazaType==0) {//スキン系の特性
							//c++;
							wazaType=parseInt(s.substring(c,c+2));getAisyou();getTypeItti();
							return 26;
							}else{
								return 	-5;
							}
					case '2':if(wazaType!=0){wazaType=0;getAisyou();getTypeItti();}return -5;//ノーマルスキン
					case '3':if(tenko==6 && (wazaType==6 || wazaType==7 || wazaType==15)) return 26;//砂のちから
							else return -5;
					case '4':if(kk[2][2]==1.5)kk[2][2]=2;//適応力
					     	return -5;
				}
			case '1':if(buturi!=(parseInt(""+s.charAt(c++))))return -5;
				switch(s.charAt(c++)){
				case '0':return parseInt(s.substring(c,c+2));//ヨガパワー、力持ち、はりきり、暴走系、ふしぎなうろこ、ファーコート,チョッキ,こだわり系
				case '1':if(tenko==2) return parseInt(s.substring(c,c+2));//サンパワー、フラワーギフトなど
					   else return -5;
				case '2':kk[3][1]=1; return 30;//parseInt(s.substring(c,c+2));//根性
				case '3':if(Field.equals("0530")) return 30;
					   else return -5;
				}
			case '2':
				switch(s.charAt(c++)){
				case '0':kk[2][2]=1.5; return -5;//変幻自在
				case '1':if(kk[3][0]<1) return 20;//こっちで入れるかむこうでいれるか　いろめがね
					   else return -5;
				case '2':if(kk[3][0]>1) return parseInt(s.substring(c,c+2));//ハードロック・フィルター・達人の帯 半減実
					   else return -5;
				case '3':if((wazaType==0||wazaType==1)&&(type_B[0]==12||type_B[1]==12)){//きもったま
						aisyou[wazaType][12]=1;
						getAisyou();
						aisyou[wazaType][12]=0;return -5;
					}
				}
			case '3':return parseInt(s.substring(c,c+2));//アナライズ、闘争心、弱気、天然、マルスケ 各種強化アイテム,メトロノーム いのちのたま
			case '4':if(kk[2][0]==1.5){kk[2][0] = 3;}return -5;//スナイパー
			case '5':if(l[0][1]<61) return 30;//テクニシャン
				   else return -5;
			case '6':String pNumber = pName[parseInt(""+s.charAt(c++))];//攻撃側か守備側か
				switch(s.charAt(c++)){
				case '0':if("ピカチュウ".equals(pNumber)) return 40;//でんきだま
				else return -5;
				case '1':if(parseInt(""+s.charAt(c++))!=buturi)return -5;
					String[] pn ={"ガラガラ","メタモン","パールル"};
					if(pn[(parseInt(""+s.charAt(c++)))].equals(pNumber)) return 40;
					else return -5;
				case '2':if(buturi==1)return -5;//こころのしずく
					String[] rathi = {"ラティオス","ラティアス"};
					if(rathi[0].equals(pNumber)||rathi[1].equals(pNumber)) return 30;	
					else return -5;
				case '3':String[] sinnou ={"パルキア","ディアルガ","ギラティナ"};
					if(pNumber.indexOf(sinnou[parseInt(""+s.charAt(c++))])==0&&(wazaType==type_A[0]||wazaType==type_A[1])) return 24;
					else return -5;
				}
			case '7':int hosei = -5;
				String kouka = s.substring(c,c+2);c+=2;
				if(wazakouka%parseInt(kouka)==0) hosei=parseInt(s.substring(c,c+2));
				return hosei;
			case '8': int kiseki = -5;
				if(sinkamae==1)kiseki=30;
				return kiseki;
			case '9':oyakoai=true;
			return -5;
			default:return -5;
			}
		
		}
	public int getProbability(int HP,Map<Integer,Integer> m,int[] damagea,int t){
　　　　　　　　　//乱数の場合に瀕死になる確率(分子のみ)を求めるメソッドで、実際シミュレーションした結果を返す。
　　　　　　　　　//mにはそのダメージになる確率(分子のみ)が格納されており、それを掛けることで実際の数値を出す。
		if(HP<=0) return 1;
		if(t<1) return 0;
		int ans=0;
		for(int i=0;i<damagea.length;i++){
			ans+= m.get(damagea[i])*getProbability(HP-damagea[i],m,damagea,t-1);
		}
		return ans;
	}
	public ATW getAttackTimes(int max,int min,int t) throws StackOverflowError{//瀕死になるための攻撃回数と、確定か乱数かを求める。
		if(HP-max*t<=0){
			ATW ans = new ATW();//ラッパークラスを返す。
			ans.a_times=t;//瀕死になる為に必要な攻撃回数
			if(HP-min*t<=0)ans.kakutei=true;//(ダメージの最小値)×(攻撃回数)でも体力が0になった場合、確定で瀕死になることになる
			return ans;
		}
		return getAttackTimes(max,min,t+1);
	}
}
