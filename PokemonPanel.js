/**
 * ＃Apと＃Bpを制御するクラスを定義
 */

/**
 * ポケモンの能力値をセットする関数
 * @param $ability	ポケモンの個体値、努力値、性格をもつdiv要素
 * @param index1	ポケモンのインデックス
 * @param index2	種族値を求める特に必要なインデックス（物理か特殊か。）
 */
function setNoryokuti($ability, index1, index2){
	if($ability.find('.seikaku').val() == undefined)index2 = 0;
	var seikaku = index2==0? 0 : $ability.find('.seikaku').val();
	
	var syuzokuti = pokemondata[index1][index2];
	var kotaiti = parseInt($ability.find('.kotaiti').val());
	var doryokuti = parseInt($ability.find('.doryokuti').val());
	var level = parseInt($ability.parent().find('.lv').val());
	$ability.find('.nouryokuti').val(calcNoryokuti(kotaiti, doryokuti, level, syuzokuti, seikaku));
};

/**
 * ポケモンの能力値を計算する関数
 * @param k	個体値
 * @param d	努力値
 * @param l	レベル
 * @param s	種族値
 * @param seikaku　性格補正
 * @returns	ポケモンの能力の実数値
 */
function calcNoryokuti(k, d, l, s, seikaku){
	if(seikaku == 0) return parseInt(Math.floor((s*2+k+(d/4))*l/100)+10+l);
	return parseInt((Math.floor(((s*2+k+(d/4))*l/100)+5))*seikaku);
};

/**
 * 結果表示ようのHTMLを生成する関数
 * @returns {String}	攻撃側のポケモンと防御側の情報を含んだHTMLテキスト
 */
function createResult(){
	var a = Ap.$pnl.find('.rank').val();
	var b = Bp.$pnl.find('.rank').val();
	var wP = Ap.$pnl.parent().parent().find('select[name="tenkou"]');
	var wV = wP.val() == '0'? '' : '《' + wP.find('option:selected').text() + '》';
	var r_a = a==0? "± " + 0: a>0? "+ " + a: a;
	var r_b = b==0? "± " + 0: b>0? "+ " + b: b;
	var r1 = "<tr><td><strong>"+ Ap.$name.val() +"</strong>("+ Ap.$pnl.find('input[name="wazaName"]').val() +")</td><td><strong>→</strong></td><td><strong>"+Bp.$name.val() + '</strong>' + ' ' + wV + "</td></tr>";
	var r2 = "<tr><td>"+ Ap.getView(Ap.$AorB)+"："+ r_a +"</td><td></td><td>"+ Bp.getView(Bp.$AorB) +' '+ Bp.getView($('#bpH'))+ "：" + r_b +"</td></tr>";
	var r3 = "<tr><td>＠"+ Ap.$pnl.find('select[name="ApItem"] option:selected').text() + "</td><td> </td><td>＠"+ Bp.$pnl.find('select[name="BpItem"] option:selected').text() +"</td></tr>";
	var r4 = "<tr><td>"+ Ap.$pnl.find('.property option:selected').text() + "</td><td> </td><td>"+ Bp.$pnl.find('.property option:selected').text() +"</td></tr>";
	return "<table>" + r1 + r2 + r3 + r4 +"</table>";
}

/**
 * ＃Apと＃Bpを制御するクラスを定義
 * @param　$v	＃Apか＃Bp
 * @param　t_D	特性のデータを保持する配列
 * @param　index	種族値を取得する時のindex(攻撃側：[3,1] 防御側：[4,2])
 * @prop　 $pnl	ポケモンの努力値、個体値、性格を保持するdiv要素
 * @prop　 $name	ポケモンの名前を入力するtextfield
 * @prop　 tokuseiData ポケモンの特性のデータを保持する配列
 * @prop 　iOP	ポケモンのindex
 * @prop 　indexOfSyuzokuti 種族値を取得する時のindex(攻撃側：[3,1] 防御側：[4,2])
 * @prop 　buturi	 技が物理か特殊か
 */
var PokemonPanel =function($v, t_D, index, lb, cD){
	this.$pnl = $v;					
	this.$name = $v.find('.p_Name');
	this.$AorB = $v.find('.ability');
	this.tokuseiData = t_D;			
	this.iOP = 0;					
	this.indexOfSyuzokuti = index;//配列 [3,1] か　[4,2]
	this.buturi = parseInt($('input[name="buturi"]:checked').val());
	this.label = lb;
	this.cd = cD;
};


PokemonPanel.prototype = {
	/**
	 * ポケモンのタイプをセットする関数
	 */
	setType : function(){
		var p_type = [pokemondata[this.iOP][5], pokemondata[this.iOP][6]];
		this.$pnl.find('.p_Type').val(p_type[0]+1).next().val(p_type[1]+1);
	},
	
	/**
	 * ポケモンの特性をセットする関数
	 */
	setTokusei : function(){
		var p_tokusei = tokusei[this.iOP];
		var $v = this.$pnl.find('.property option:first').next();
		var flag = false;
		for(var i = 0; i< p_tokusei.length; i++){
			for(var j = 0; j<this.tokuseiData.length; j++){
				if(p_tokusei[i] == this.tokuseiData[j][0]){
				     $v.val(this.tokuseiData[j][1]).text(this.tokuseiData[j][0]);
				     if(this.tokuseiData[j][0].length>2) flag = true; 
				     $v = $v.next()
				     continue;
				}
			}
		}
		if(p_tokusei.length==3 && flag){
			$v.parent().prop('selectedIndex', 1).change();
		}else{
			$v.parent().prop('selectedIndex', 0);
		}
	},
	
	/**
	 * ポケモンの能力値をsetし直す関数
	 * @param $ability	ポケモンの個体値、努力値、性格を保持するdiv要素
	 */
	changeN : function($ability){
		setNoryokuti($ability, this.iOP, this.indexOfSyuzokuti[this.buturi]);
	},
	
	/**
	 * 技の物理、特殊が変化した時に
	 * @param buturi	物理なら1、特殊なら0を指定：指定しなかった場合は自動的に取得
	 */
	changeButuri : function($ability, buturi){
		if(buturi == undefined) buturi = $('input[name="buturi"]:checked').val();
		this.buturi = parseInt(buturi);
		$ability.find('label[for="buturi"]').text(this.label[this.buturi]);
		setNoryokuti($ability, this.iOP, this.indexOfSyuzokuti[this.buturi]);
	},
	
	/**
	 * ポケモンの能力を表すテキストを作成する関数
	 * @param $ability	ポケモンの個体値、努力値、性格を保持するdiv要素
	 * @returns	
	 */
	getView : function($ability){
		var doryokuti = $ability.find('.doryokuti').val();
		var seikakuS = $ability.find('.seikaku');
		var seikaku = seikakuS.length == 0? '' : seikakuS.val() == '1.1'? '補正あり' : seikakuS.val() == 1.0? '補正なし' : '逆補正';
		var noryoku = seikaku == ''? 'H ' : this.cd[this.buturi] + ' ';
		return seikaku + noryoku + doryokuti;		
	}
};
/**
 * ポケモンの最大値と最小値を設定する関数
 * @param $v	設定する対象のtextfiled
 * @param min	最小値
 * @param max	最大値
 */
function setMinMax($v, min, max){
	var value = $v.val();
	if(value<min) $v.val(max);
	if(value>max) $v.val(min);
}

/**
 * フォーカスがある時に値をチェックし、変化したらchangeを発生させる関数
 * @param $o	対象となるjQueryオブジェクト
 */
function setItv($o){
	var vl = $o.val();
	window.setInterval(function(){
		var bf = $o.val();
		if(vl != bf && bf !=""){
			vl = $o.val();
			$o.change();
		}
	}, 100);
}

/**
 * アクションリスナーをセットするメソッド
 * @param pokemonPnl	ポケモンパネルクラスのオブジェクト
 * @param $ability		努力値、個体値、性格を保持するdiv要素
 */
function setActionListener(pokemonPnl, $ability){
	pokemonPnl.$name.focus(function(e){
		var pn = $(e.target).val();
		window.setInterval(function(){
			var bf = $(e.target).val();
			if(pn != bf && bf !=""){
					pn = bf;
					$(e.target).change();
				}
			}, 100);
	}).change(function(e){
		pokemonPnl.iOP = Math.max(0, $.inArray(pokemonPnl.$name.val(), pokemonname));
		if(pokemonPnl.iOP > 0){
			pokemonPnl.changeN($ability);
			pokemonPnl.setTokusei();
			pokemonPnl.setType();
			pokemonPnl.$pnl.find('input[name="Sinkamae"]').val(pokemondata[pokemonPnl.iOP][7]);
		}
	});
	
	$ability.parent().find('.rank').change(function(e){
		setMinMax($(e.target), -6, 6);
	}).focus(function(e){
		setItv($(e.target));
	});
	
	$ability.parent().find('.lv').change(function(e){
		setMinMax($(e.target), 0, 100);
		pokemonPnl.changeN($ability);
	}).focus(function(e){
		setItv($(e.target));
	});
	
	$ability.find('.kotaiti').change(function(e){
		setMinMax($(e.target), 0, 31);
		pokemonPnl.changeN($ability);
	}).focus(function(e){
		setItv($(e.target));
	});
	
	$ability.find('.doryokuti').change(function(e){
		setMinMax($(e.target), 0, 255);
		pokemonPnl.changeN($ability);
	}).focus(function(e){
		setItv($(e.target));
	});
	
	$ability.find('.seikaku').change(function(e){
		pokemonPnl.changeN($ability);
	});
	
	$ability.parent().find('.property').change(function(e){
		var tokusei = $(e.target).find('option:selected').text();
		for(var i = 0; i<tenkouhosei.length; i++){
			if(tokusei == tenkouhosei[i][0]){
				pokemonPnl.$pnl.parent().parent().find('select[name="tenkou"]').val(tenkouhosei[i][1]);
				break;
			}
		}
		for(var i = 0; i<fieldhosei.length; i++){
			if(tokusei == fieldhosei[i][0]){
				pokemonPnl.$pnl.parent().parent().find('select[name="Field"]').val(fieldhosei[i][1]);
				break;
			}
		}
	});
}

