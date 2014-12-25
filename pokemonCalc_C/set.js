/**
 * 技に関する関数を定義
 */
function checkWaza(wazaname, w_data){
	for(var i = 0; i<w_data.length; i++){
		if(wazaname == w_data[i]) return true;
	}
	return false;
}

$(function(){
	Ap = new PokemonPanel($('#Ap'), tokuseiValueOfA, indexO, lb_O, charcterO);
	setActionListener(Ap, $('#apA'));
	Bp = new PokemonPanel($('#Bp'), tokuseiValueOfB, indexD, lb_D, charcterD);
	setActionListener(Bp, $('#bpH'));
	setActionListener(Bp, $('#bpB'));
	Ap.$name.change(function(e){
		if($('input[name="wazaName"]').val() == 'イカサマ'){
			window.setTimeout(function(){
				Ap.iOP = Bp.iOP;
				Ap.changeN($('#apA'));
			}, 100);
		}
	});
	Bp.$name.change(function(e){
		if($('input[name="wazaName"]').val() == 'イカサマ'){
			window.setTimeout(function(){
				Ap.iOP = Bp.iOP;
				Ap.changeN($('#apA'));
			}, 100);
		}
	});
	$('input[name="wazaName"]').focus(function(e){
		setItv($(e.target));
	}).change(function(e){
		var name = $(e.target).val();
		
		if(checkWaza(name, chWaza))$('input[name="kyusyo"]').prop("checked", true);
		else $('input[name="kyusyo"]').prop("checked", false);
		
		if(name == "イカサマ"){
			Ap.iOP = Bp.iOP;
		}else{
			Ap.iOP = Math.max(0, $.inArray(Ap.$name.val(), pokemonname));
		}
		
		var wazaI = $.inArray(name, wazaname);
		if(wazaI>0){
			var waza = wazadata[wazaI];
			$('#wazatype').val(waza[0]+1);
			$('#wazairyoku').val(waza[1]);
			$('#Ap input[name="Wazakouka"]').val(waza[3]);
			$('input[name="buturi"]').val([waza[2]]).change();
		}
	
	});
	$('input[name="buturi"]').change(function(e){
		var buturi = $(e.target).val();
		var waza = $('input[name="wazaName"]').val();
		var flag = true;
		for(var i = 0; i<cToB.length; i++){
			if(cToB[i] == waza){
				Ap.changeButuri(Ap.$AorB, 0);
				Bp.changeButuri(Bp.$AorB, 1);
				flag = false;
			}
		}
		if(flag){
			Ap.changeButuri(Ap.$AorB);
			Bp.changeButuri(Bp.$AorB);
		}
	});
})
