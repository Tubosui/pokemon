/**
 * 天候、フィールドを設定する関数を定義
 */
function setTenko(i){
	if(typeof i !== 'undefined')$('#tenkou').val(i);
	else return $('#tenkou').val();
}
function setField(i){
	if(typeof i !== 'undefined')$('#field').val(i);
	else return 0;//$('#field').val();
}

function setBa(hosei, a, b, set){
	a.change(function(e){
	var tenko = set();
	for(i=0;i<hosei.length;i++){
		if(a.find('option:selected').text()==hosei[i][0]){
			tenko = hosei[i][1];
			break;
		}
	}
	if(tenko == 0){
		for(i=0;i<hosei.length;i++){
			if((b.find('option:selected').text())==hosei[i][0]){
				tenko=hosei[i][1];
				break;
			}
		}
	}
	set(tenko);
	});
}

$(function(){
		var tenkohosei=[['ひでり', 2], ['サンパワー' ,2], ['フラワーギフト', 2], ['あめふらし', 3], ['すなおこし',6], ['すなのちから', 6]];
		setBa(tenkohosei, $('#Aptokusei'), $('#Bptokusei'), setTenko);
		setBa(tenkohosei, $('#Bptokusei'), $('#Aptokusei'), setTenko);
		var fieldhosei=[['くさのけがわ',4]];
		setBa(fieldhosei, $('#Bptokusei'), $('#Aptokusei'), setField);
		}
)