/**
 * ボタンを押すと入力、加算する関数を定義。
 */
$(function(){
	$('input[name="in"]').click(function(e){//数値を代入するボタンをクッリックしたときのリスナー
		var f = $(e.target);
		var i = f.val();//ボタンの値を取得（この値を代入する）
		while(f.prev().attr('type')!='text'){
			f=f.prev();//代入するtextfieldが見つかるまでさかのぼり、見つかったらループを抜ける
		}
		f.prev().val(i).change();//代入 
		
	});
	$('input[name="plus"]').click(function(e){//数値を加算するボタンをクリックしたときのリスナー
		var f = $(e.target);
		var i = f.val();//加算する値を取得
		while(f.prev().attr('type')!='text'){
			f=f.prev();//同上
		}
		f.prev().val(parseInt(f.prev().val())+parseInt(i)).change();
	});
	
	
	$('.nouryokuti').change(function(e){
		if($(e.target).val()<1) $(e.target).val(1);
	})
	
	$('#tenkoreset').click(function(e){
		$('#tenkou').val(0);
	});
	$('#start_ajax').click(function(e){
			$('#tusin').text("通信中");
			$.ajax({
				url: 'Transmission',
				type:'POST',
				dataType:'html',
				data: $('form').serializeArray(),
				timeout:5000,
				success: function(data){
					$('#result').html(data);
					$('#tusin').empty();
					$('#result').prepend(createResult());
				},
				error:function(){
					$('#tusin').text("通信エラー");
				}
		})
	});
});