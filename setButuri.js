/**
 * 特殊・物理によって表示を変える
 */
$(function(){
	var buturiw=[["特攻","特防"],["攻撃","防御"],["特攻","防御"]];
	$('input[name="buturi"]').change(function(e){
		var buturi=$('input[name="buturi"]:checked').val();//物理なら1、特殊なら0
		for(var i=0; i<CtoB.length; i++){
			if($('input[name="waza"]').val()==(CtoB[i])) buturi=2;//サイコショック系用
		}
		$('#Ap label[for="buturi"]').text(buturiw[buturi][0]);
		$('#Bp label[for="buturi"]').text(buturiw[buturi][1]);
		$('input[name="ApName"]').change();
		$('input[name="BpName"]').change();
	});
})