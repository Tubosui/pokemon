/**
 能力値をセットする関数を定義
 */
/**function setNoryokuti(v, syuzokuti){	//個体値・努力値・レベルと種族値（引数）を使って能力値を設置する
	var kotaiti = parseInt(v.find('.kotaiti').val());
	var doryokuti = parseInt(v.find('.doryokuti').val());
	var lv =parseInt(v.parent().find('.lv').val());
	if(v.attr('id')!="bpH"){
		var hosei = v.find('.seikaku option:selected').val();
		v.find('.nouryokuti').val(parseInt((Math.floor(((syuzokuti*2+kotaiti+(doryokuti/4))*lv/100)+5))*hosei));
	}else{
		v.find('.nouryokuti').val(parseInt((((syuzokuti*2+kotaiti+(doryokuti/4))*lv/100)+10+lv)));	
	}
}

function getSyuzokuti($vDerive, b, c, t){//ポケモン名から種族値を求める関数
		var d = 3-b.val()*2;//物理技か特殊技によって求める種族値を変えるための数
		var pn = $vDerive.val();
		var index = $.inArray(pn, pokemonname);
		var ans ={
				h:pokemondata[index][0],
				a:pokemondata[index][d],
				b:pokemondata[index][d+c],
				type1:pokemondata[index][5],
				type2:pokemondata[index][6],
				sinkamae:pokemondata[index][7]
		};
		setTokusei(index, $vDerive, t);
		return ans;
}

function setP_Type($v,i1,i2){//ポケモンのタイプを設定する
	$v.parent().find('.p_Type').val(i1+1).next().val(i2+1);//parent()を使ってるのは#apAなどを指定するときに同時にできるように（今は別）
}

function setTokusei(index, $v, t_data){
	var p_tokusei = tokusei[index];
	var tokuseiIndex = [-1,-1];
	for(var i = 0; i< p_tokusei.length; i++){
		for(var j = 0; j<t_data.length; j++){
			if(p_tokusei[i] == t_data[j][0]){
			     tokuseiIndex[i] = j ;
			     continue;
			}
		}
	}
	$v1 = $v.parent().find('.property option:first').next();
	if(tokuseiIndex[0]>-1){
		$v1.val(t_data[tokuseiIndex[0]][1]).text(p_tokusei[0]);
	}
	if(tokuseiIndex[1]>-1){
		$v1.next().next().val(t_data[tokuseiIndex[1]][1]).text(p_tokusei[1]);
	}
	if(p_tokusei.length == 3){
		$v1.parent().prop('selectedIndex', 1).change();
	}else{
		$v1.parent().prop('selectedIndex', 0);
	}
}**/

$(function(){
	$('input[name="ApName"]').change(function(e){//ポケモンの名前が変化したら種族値を求め、そこから能力値を計算・設定する
	var ans = getSyuzokuti($('input[name="ApName"]'), $('input[name="buturi"]:checked'), 1, tokuseiValueOfA);
	setNoryokuti($('#apA'), ans.a);
	setP_Type($('#apA'), ans.type1, ans.type2);
	
	if($('input[name="waza"]').val()=="イカサマ"){
		var ans2 = getSyuzokuti($('input[name="BpName"]'),$('input[name="buturi"]:checked'), 1, tokuseiValueOfB);
		setNoryokuti($('#apA'),ans2.a);
	}

	});
	$('input[name="BpName"]').change(function(e){
		var c=1;
		for(var i=0;i<CtoB.length;i++){
			if($('input[name="waza"]').val()==(CtoB[i])) c = -1;//サイコショック用
		}
		var ans = getSyuzokuti($('input[name="BpName"]'),$('input[name="buturi"]:checked'),c,tokuseiValueOfB);
		setNoryokuti($('#bpH'),ans.h);
		setNoryokuti($('#bpB'),ans.b);setP_Type($('#bpB'),ans.type1,ans.type2);
		$('#Bp input[name="Sinkamae"]').val(ans.sinkamae);
		if($('input[name="waza"]').val()=="イカサマ"){
			setNoryokuti($('#apA'),ans.a);
		}
	})
	$('.seikaku').change(function(e){
		$(e.target).parent().parent().find('.p_Name').change();
	})
});
$(function(){//技→タイプ、威力、特殊か物理か変える関数をリスナーとして登録
	$('input[name="waza"]').change(function(){
		var wn =$('input[name="waza"]').val();
		if(wn=='やまあらし' || wn=="こおりのいぶき"){
			$('input[name="kyusyo"]').attr('checked',true);
			}else{
				$('input[name="kyusyo"]').attr('checked',false);
			};
			for(var i=0; i<wazaname.length; i++){
				if(wn==wazaname[i]|| katakana(wn)==wazaname[i]){
					var wd = wazadata[i];
					$('#wazatype').val(wd[0]+1);
					$('#wazairyoku').val(wd[1]);
					$('#Ap input[name="Wazakouka"]').val(wd[3]);
					$('input[name="buturi"]').val([wd[2]]).change();
					break;
				}
			}
		})
	})