package tubosi;

/**
 * ポケモンの場の状態のデータを保持するクラス
 * @author kazuma
 *
 */
public class Field {
	String[] wall = new String[2];			//壁
	String[] playWithWorM = new String[2];	//泥遊び、水遊び
	String ground;			//フィールド系の技（クラスの名前と被るのでgroundという名で定義）
	boolean takeInAdvance;	//先取り
	boolean critical;		//急所に当たったか否か
	int weather;			//天候
	
	Field(String[] wall, String[] playWithWN, String ground, boolean takeInAdvance, boolean critical, int weather){
		this.wall[0] = wall[0];
		this.wall[1] = wall[1];
		this.playWithWorM[0] = playWithWN[0];
		this.playWithWorM[1] = playWithWN[1];
		this.ground = ground;
		this.takeInAdvance = takeInAdvance;
		this.critical = critical;
		this.weather = weather;
	}
}

