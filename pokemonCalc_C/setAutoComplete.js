/**
 * ポケモンのAutoCompleteの実装
 */
function setAC(youso, d){//オートコンプリートをyousoに実装 + yousoにフォーカスしてる時、変化したらchangeを発生させる（結果　種族値→能力値が入力）
	youso.autocomplete({
		source : function(request, response){
				 	var vl = youso.val();
				 	response( $.grep(d, function(value){
					return (value.indexOf(katakana(request.term))==0 || value.indexOf(request.term)==0);
				 }))},
		delay : 100,
		minLength : 1
	});
}
function katakana(sa){//かな・カナ変換
	var id=0;
	return sa.replace(/[ぁ-ん]/g,function(s){
		return String.fromCharCode(s.charCodeAt(0)+0x60)
	});
}
$(function(){
	setAC($('input[name="ApName"]'),pokemonname);
	setAC($('input[name="BpName"]'),pokemonname);
	setAC($('input[name="wazaName"]'),wazaname);
	
})

