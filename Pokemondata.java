package tubosi;

/**
 * ポケモンのデータを保持するクラス
 * @author kazuma
 *
 */
public class Pokemondata {
	String name;			//名前
	String property;		//特性
	String item;			//もちもの
	int lv;					//レベル
	int HP;					//体力
	int a;					//攻撃力
	int b;					//防御力
	int isDeveloping;		//最終進化形態であるか
	int[] type = {-1,-1};	//タイプ
	int rank;				//攻撃力や防御力の能力上昇が起きているかどうか
	boolean oyakoai = false;//特性が親子愛であるか否か
	boolean isScaled;		//火傷状態かどうか
	
	/**
	 * 攻撃ポケモン用のコンストラクタ
	 * @param s_data	{名前, 特性, 持ち物}
	 * @param i_data	{タイプ1, タイプ2, 攻撃力, ランク, レベル}
	 * @param yakedo	やけど状態ならtrue
	 */
	Pokemondata(String[] s_data, int[] i_data, boolean yakedo){
		this.name = s_data[0];
		this.property = s_data[1];
		this.item = s_data[2];
		this.type[0] = i_data[0] - 1;
		this.type[1] = i_data[1] - 1;
		this.a = i_data[2];
		this.rank = i_data[3];
		this.lv = i_data[4];
		this.isScaled = yakedo;
	}
	
	/**
	 * 防御側のポケモン用のコンストラクタ
	 * @param s_data	{名前, 特性, 持ち物}
	 * @param i_data	{タイプ1, タイプ2, 体力, 防御力, ランク, 進化前かどうか（進化前なら1）}
	 */
	Pokemondata(String[] s_data, int[] i_data){
		this.name = s_data[0];
		this.property = s_data[1];
		this.item = s_data[2];
		this.type[0] = i_data[0] - 1;
		this.type[1] = i_data[1] - 1;
		this.HP = i_data[2];
		this.b = i_data[3];
		this.rank = i_data[4];
		this.isDeveloping = i_data[5];
	}
	
	Pokemondata(){
		this.name = "ピィ";
		this.property = "N";
		this.item = "N";
		this.type[0] = -1;
		this.type[1] = -1;
		this.HP = 12;
		this.b = 5;
		this.rank = 0;
		this.isDeveloping = 0;
	}
}


