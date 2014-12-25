package tubosi;

import java.util.Map;
import java.util.Iterator;

/**
 * ポケモンのダメージ計算の結果と瀕死率を計算するのに必要なデータとメソッドを持つクラス。
 * @author kazuma
 *
 */
public class Result {
	int HP;		 		//防御ポケモンのHP
	Map<Integer,Integer> damages;//ダメージのパターンとその数
	
	int[] damagePattern;//damagesのkeyを配列にしたもの。
	int coefficient = 0;//分母の仮数（16か256;　一回の攻撃で生まれるダメージの幅
	int maxDamage = -1;	//最大ダメージ
	int minDamage = Integer.MAX_VALUE;//最小ダメージ
	int attackTime;		//ポケモンのHPが0以下になるのに必要な最小の攻撃回数
	boolean isSecure;	//確定ならtrue,　乱数ならfalse
	double probability;	//乱数の場合、ポケモンが瀕死になる確率。
	double pIndex_D = 0;
	
	/**
	 * Resultクラスのコンストラクタ。
	 * @param HP		防御ポケモンのHPを表す数値
	 * @param damages	ダメージのパターンとその数を保持した連想配列
	 */
	Result(int HP, Map<Integer, Integer> damages){
		this.HP = HP;
		this.damages = damages;
		setMaxAndMin();
	}
	
	/**
	 * ダメージのパターンが保持されている連想配列から
	 * 最大ダメージと最小ダメージ、ダメージのパターンの配列
	 * また分母の仮数を求めてセットするメソッド
	 */
	void setMaxAndMin(){
		if(this.damages == null) return;//例外を考える
		Iterator<Integer> iter = damages.keySet().iterator();
		this.damagePattern = new int[this.damages.size()];
		int c = 0;
		while(iter.hasNext()){
			int i = iter.next();
			this.damagePattern[c++] = i;
			this.coefficient += damages.get(i);
			maxDamage = Math.max(maxDamage, i);
			minDamage = Math.min(minDamage, i);
		}
	}
	
	/**
	 * 攻撃回数をフィールドへセットし、それを返す。
	 * @return フィールドの攻撃回数を返す
	 */
	int getAttackTime(){
		if(maxDamage <= 0) return -1;
		setAttackTimes(1);
		return this.attackTime;
	}
	
	/**　★ダメージの最大値が0の時は例外を投げるようにする！！！！！
	 * ポケモンが瀕死になるのに必要な最小の攻撃回数
	 * @param max	ダメージの最大値
	 * @param min	ダメージの最小値
	 * @param t		攻撃回数
	 * @throws StackOverflowError
	 */
	public void setAttackTimes(int t){
		if(HP - maxDamage*t <= 0){
			attackTime = t;
			if(HP - minDamage*t <= 0) isSecure = true;
			return;
		}
		setAttackTimes(t+1);
	}
	
	/**
	 * ポケモンがｔ回の攻撃で瀕死になる確率を求めてセット、そしてそれを返すメソッド
	 * @return	確定なら1、攻撃回数が7回より多かったら-1（攻撃回数が多いと確率はあまり役に立たないので計算しない）
	 * 			その他なら計算してフィールドにセット、フィールドの値を返す
	 */
	double getProbability(){
		if(isSecure) return 1;
		if(attackTime > 7) return  -1;
		long denominator = (long)Math.pow(coefficient, attackTime);//分母
		int ans = setProbability(HP, attackTime);
		this.probability = (double)ans/(double)denominator;
		return this.probability;
	}
	
	/**
	 * ポケモンがｔ回の攻撃で瀕死になる確率をもとめるメソッド
	 * @param HP		防御側のポケモンの体力
	 * @param m			ダメージのパターンとその数
	 * @param damages	ダメージのパターン
	 * @param t			残りの攻撃回数
	 * @return			ポケモンの瀕死になるパターン数
	 */
	
	int setProbability(int HPleft, int t){
		if(HPleft <= 0) return 1;
		if(t<1) return 0;
		int ans = 0;
		for(int i = 0; i<damagePattern.length; i++){
			ans += damages.get(damagePattern[i]) * setProbability(HPleft - damagePattern[i], t-1);
		}
		return ans;
	}
}

