<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.4/themes/redmond/jquery-ui.min.css">
	<link rel="stylesheet" href="po.css">
	<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
	<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.min.js"></script>
	<script  charset="UTF-8" type="text/javascript" src="p_data.js"></script>
	<script charset="UTF-8" type="text/javascript" src="PokemonPanel.js"></script>
	<script charset="UTF-8" type = "text/javascript" src = "setWaza.js"></script>
	<script charset="UTF-8" type="text/javascript" src="setActionButton.js"></script>
	<script charset="UTF-8" type="text/javascript" src="setAutoComplete.js"></script>

	<title>Pokemon keisan</title>
	<%!
 	String[] type ={"なし","ノーマル","かくとう","ほのお","みず","くさ","でんき","いわ","じめん","ひこう","どく","むし","エスパー","ゴースト","ドラゴン","こおり","はがね","あく","フェアリー"};
 	String getType(){
		 String str = "";
		 for(int i=0;i<type.length;i++){
			 str+="<option value="+i+">"+type[i]+"</option>";
		 }
		 return str;
	 };
	%>
	</head>
	<body>
	<h1>ポケモンダメージ計算機</h1><hr>
		<form method="POST" action="Transmission" id="data">
			<div id="dez">
			<div id="Ap">
				<p>攻撃側のポケモン</p>
				ポケモン：<input type="text" name="ApName" size=14 class="p_Name">
				タイプ：<select class="p_Type" name="type1_A"><%=getType()%></select><select name="type2_A"><%=getType() %></select><br>
				Lv:<input type="text" value="50" class="lv" name="ApLv" size=4>
				<input type="button" value="50" name="in">
				<input type="button" value="100" name="in"><br>
				
				技名：<input type="text" name="wazaName" size=14> 技タイプ：<select id="wazatype" name="wazaType"><%=getType() %></select><br>
				<input type="radio" name="buturi" checked="checked" value="1" id="buturii"><label for="buturii">物理</label>
				<input type="radio" name="buturi" value="0" id="tokusyu"><label for="tokusyu">特殊</label>
				<input type="hidden" name="Wazakouka" value="1">
				威力：<input type="text" value="0" id="wazairyoku" name="WazaIryoku" size=4><br>
				
				<div id = 'apA'  class="ability">
					<label for="buturi">攻撃</label>努力値：<input type="text" name="ApDoryokuti" class='doryokuti' value="252" size=4>
					<input type="button" value="252" name="in"><input type="button" value="0" name="in">
					<input type="button" value="+1" name="plus"> <input type="button" value="-1" name="plus">
					<select class='seikaku'>
						<option value='1.0'>性格補正なし</option>
						<option value='1.1'>＋補正(1.1倍)</option>
						<option value='0.9'>ー補正(0.9倍)</option>
					</select><br>
					<label for="buturi">攻撃</label>実数値：<input type="text" value="1" name="ApNouryokuti" class="nouryokuti" size=4>
					<label for="buturi">攻撃</label>個体値：<input type="text" size=4 value="31" name="ApKotaiti" class='kotaiti'>
					<input type="button" value="31" name="in"><input type="button" value="+1" name="plus"><input type="button" value="-1" name="plus" ><br>
				</div><!-- /apA -->
				
				ランク；<input type="text" value="0" name="ApRank" class="rank" size=2><input type="button" value="+2" name="plus">
				<input type="button" value="+1" name="plus"><input type="button" value="0" name="in"><input type="button" value="-1" name="plus">
				<input type="button" value="-2" name="plus"><br>
				もちもの:<select name = "ApItem">
				<% 
					String[][] motimonoa={{"なし/その他","N"},{"各種強化アイテム（もくたん等）","03324"},{"いのちのたま","34326"},{"こだわりハチマキ","1411030"},{"こだわりメガネ","1410030"},{"こころのしずく","14602"},{"しんかいのキバ","1460102"},{"たつじんのおび","342224"},{"ちからのハチマキ","0311022"},{"でんきだま","03600"},{"ノーマルジュエル","030010026"},{"はっきんだま","03603"},{"ふといほね","1460110"},{"ものしりメガネ","0310022"},
						{"メトロノーム(1.2)","03324"},{"メトロノーム(1.4)","03328"},{"メトロノーム(1.6)","03332"},{"メトロノーム(1.8)","03336"},{"メトロノーム(2.0)","03340"}};
					for(int i=0;i<motimonoa.length;i++){
						out.print("<option name=\""+ motimonoa[i][0]+"\" value=\""+motimonoa[i][1]+"\">"+motimonoa[i][0]+"</option>" );
					}
				%>
				</select><br>
				特性：<select id='Aptokusei' name="ApTokusei" class="property">
					<option value="N">未発動</option>
					<option value="N"></option>
					<option value="N"></option>
					<% 
						String[][] tokusei={{"","N"},{"アナライズ","02326"},{"あめふらし","N"},{"いろめがね","3321"},{"おやこあい","009"},{"おやこあい（子のみ）","33310"},{"かたいツメ","0270226"},{"がんじょうあご","0272930"},{"きもったま","0023"},{"げきりゅう","020010330"},{"こんじょう","13112"},{"サンパワー","1310130"},{"しんりょく","020010430"},{"スカイスキン","020108"},{"すてみ","0271324"},{"すなおこし","N"},{"すなのちから","0203"},
							{"ちからづく","0271126"},{"ちからもち","1311040"},{"てきおうりょく","0004"},{"テクニシャン","025"},{"てつのこぶし","0271324"},{"てんねん","22320"},{"とうそうしんー","02315"},{"とうそうしん＋","02325"},{"どくぼうそう","0311030"},{"ねつぼうそう","0310030"},{"ノーマルスキン","0002"},
							{"はりきり","1311030"},{"ひでり","N"},{"へんげんじざい","0020"},{"フェアリースキン","020117"},{"フラワーギフト","1311130"},{"フリーズスキン","020114"},{"プラス・マイナス","0210030"},{"メガランチャー","0273130"},{"むしのしらせ","020011030"},{"もうか","020010230"},{"もらいび","020010230"},
							{"ヨガパワー","1311040"},{"よわき","13310"}};
						for(int i=0;i<tokusei.length;i++){
							out.print("<option name=\""+ tokusei[i][0]+"\" value=\""+tokusei[i][1]+"\">"+tokusei[i][0]+"</option>" );
						}%>
					</select>
			</div><!-- /Ap -->

			<div id="Bp">
				<p>防御側のポケモン</p>
				ポケモン：<input type="text" name="BpName" size=14 class="p_Name"> <input type="hidden" value="0" name="Sinkamae">
				タイプ：<select class="p_Type" name="type1_B"><%=getType() %></select><select name="type2_B"><%=getType() %></select><br>
				<div  id='bpH'>
					Lv:<input type="text" value="50" class="lv" name="BpLv" size=4>
					<input type="button" value="50" name="in"><input type="button" value="100" name="in"><br>
					<input type="hidden" value="-1">
					HP努力値：<input type="text" name="BpDoryokuti_h" value="252" size=4 class='doryokuti'><input type="button" value="252" name="in">
					<input type="button" value="0" name="in">
					<input type="button" value="+1" name="plus"> <input type="button" value="-1" name="plus"><br>
					HP実数値：<input type="text" value="1" name="BpNouryokuti_h" class="nouryokuti" size=4>
					HP個体値：<input type="text" size=1 value="31" name="BpKotaiti_h" class='kotaiti'><input type="button" value="31" name="in">
					<input type="button" value="+1" name="plus"><input type="button" value="-1" name="plus"><br>
				</div><!-- /bpH -->
				
				<div id='bpB' class="ability">
					<input type="hidden" value="-1">
					<label for="buturi">防御</label>努力値：<input type="text" name="BpDoryokuti_b" class='doryokuti' value="252" size=4 >
					<input type="button" value="252"  name="in"><input type="button" value="0" name="in">
					<input type="button" value="+1" name="plus"> <input type="button" value="-1" name="plus">
					<select class='seikaku'>
						<option value='1.0'>性格補正なし</option>
						<option value='1.1'>＋補正(1.1倍)</option>
						<option value='0.9'>ー補正(0.9倍)</option>
					</select><br>
					<label for="buturi">防御</label>実数値：<input type="text" value="1" name="BpNouryokuti_b" class="nouryokuti" size=4>
					<label for="buturi">防御</label>個体値：<input type="text" size=4 value="31" name="BpKotaiti_b" class='kotaiti'>
					<input type="button" value="31" name="in"><input type="button" value="+1" name="plus"><input type="button" value="-1" name="plus"><br>
				</div><!-- /bpB -->
				
				ランク；<input type="text" value="0" name="BpRank" class="rank" size=2><input type="button" value="+2" name="plus">
				<input type="button" value="+1" name="plus"><input type="button" value="0" name="in"><input type="button" value="-1" name="plus">
				<input type="button" value="-2" name="plus"><br>
				もちもの:<select name="BpItem">
					<% 
						String[][] motimonob={{"なし/その他","N"},{"こころのしずく","24612"},{"しんかいのウロコ","2461102"},{"しんかのきせき","248"},{"とつげきチョッキ","2410030"},{"メタルパウダー","246111"},{"半減実","312210"}};
						for(int i=0;i<motimonob.length;i++){
							out.print("<option name=\""+ motimonob[i][0]+"\" value=\""+motimonob[i][1]+"\">"+motimonob[i][0]+"</option>" );
						}
					%>
				</select><br>
				特性：<select id='tokusei_B' name="BpTokusei" class ="property">
				<%
					String[][] tokuseib={{"未発動","N"},{"","N"},{"","N"},{"","N"},{"あついしぼう","05002021410"},{"あめふらし","N"},{"かんそうはだ","050010225"},{"くさのけがわ","23113"},{"すなおこし","N"},{"たいねつ","050010210"},{"てんねん","13320"},{"ハードロック","342215"},
						{"ひでり","N"},{"ファーコート","2311040"},{"フィルター","342215"},{"フラワーギフト","2310130"},{"ふしぎなうろこ","2311030"},{"マルチスケイル","34310"}};
					for(int i=0;i<tokuseib.length;i++){
						out.print("<option name=\""+ tokuseib[i][0]+"\" value=\""+tokuseib[i][1]+"\">"+tokuseib[i][0]+"</option>" );
					}
				%>
				</select>
			</div><!-- /Bp -->
			
			
			<div id="exe"><input type="button" id="start_ajax" value=" 計算　"></div>
			</div>

			<div id="pn1">
				<div id="ba">
					天候：<select id="tenkou" name="tenkou">
						<option value="0">通常</option>
						<option value="2">はれ</option>
						<option value="3">あめ</option>
						<option value="6">すなあらし</option>
						<option value="14">あられ</option>
					</select>
					<input type="button" value="reset" id="tenkoreset"><br>
					
					フィールド：<select id="field" name="Field">
						<option value="N">通常</option>
						<option value="0530">エレキフィールド</option>
						<option value="0430">グラスフィールド</option>
						<option value="1310">ミストフィールド</option>
					</select>
					<table>
						<tr><td><input type="checkbox" name="kabe" value="1">リフレクター</td><td><input type="checkbox" name="kabe" value="0">ひかりのかべ</td></tr>
						<tr><td><input type="checkbox" name="yakedo" value="1">やけど（攻撃*0.5）</td><td><input type="checkbox" name="kyusyo" value="1">きゅうしょ</td></tr>
						<tr><td><input type="checkbox" name="asobi" value="2">みずあそび（炎*1/3）</td><td><input type="checkbox" name="asobi" value="5">どろあそび（電*1/3）</td></tr>
						<tr><td><input type="checkbox" name="sakidori" value="1">さきどり（*1.5）</td><!-- <td><input type="checkbox" name="nerainomato" value="?????">みやぶる（霊に技があたる！！）</td></tr> -->
						<!--  <tr><td><input type="checkbox" name="mirakuruai" value="?????">ミラクルアイ（悪に技が(ry）</td><td><input type="checkbox" name="juuden" value="5">じゅうでん（電*2）</td></tr> -->
					</table>
				</div>
				<div id="result">
					<table>
						<tr><td>ダメージ：</td></tr>
						<tr><td>残りＨＰ：</td></tr>
						<tr><td>確定数　：</td></tr>
						<tr><td>p指数(A)：</td></tr>
						<tr><td>p指数(B)：</td></tr>
					</table>
				</div>
				<div id="tusin"></div>
			</div>
		</form>
		<div id="pn2">
		<hr>
		<p>※p指数とは</p>
		<p>p指数(A):その攻撃でLv.1の性格のうてんきのピィ(H=12,B=D=5)が何体瀕死状態になるかという指数です。(タイプ相性は常に等倍で計算されます)</p>
		<p>p指数(B):防御側のポケモンの防御指数（HP×BまたはHP×D）をLv.1のピィの防御指数(60)で割ったものです。
		<p>　→(B)のほうはただ単に数値を割っているだけなので、概算になってしまいますが、だいたいそのp指数の攻撃でひんしになる、ということです。</p>
		</div>
		
	</body>
</html>