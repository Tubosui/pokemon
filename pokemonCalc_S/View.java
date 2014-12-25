package tubosi;
import java.text.DecimalFormat;


public class View {
	Result r ;
	Result r_p;
	
	View(Result r, Result r_p){
		this.r = r;
		this.r_p = r_p;
	}
	
	String getResultHTML(){
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.0%");
		int a_t = r.getAttackTime();
		String result = r.isSecure?"確定" + "<strong>" + a_t + "</strong>" +"発":"乱数" + "<strong>" + a_t + "</strong>" + "発" 
						+ (a_t>7? "" : r.coefficient==256 && a_t>4? "" : "(" + df.format(r.getProbability()) + ")") ;
		String pIndex_O = (int)((double)r_p.minDamage/(double)12) + "p 〜 " + (int)((double)r_p.maxDamage/(double)12) +"p" ;
		
		sb.append("<table>");
		sb.append("<tr><td>● ダメージ：" + "<strong>" + r.minDamage + "</strong>" + " 〜 " + "<strong>" + r.maxDamage + "</strong></td>");
		sb.append("<td>(" + df.format((double)r.minDamage/(double)r.HP) + " 〜 "+ df.format((double)r.maxDamage/(double)r.HP) + ")" + "</td></tr>");
		sb.append("<tr><td>● 確定数　：" + result + "</td>");
		sb.append("<td>● 残りＨＰ：" + Math.max(0, r.HP - r.maxDamage) + " 〜 " + Math.max(0, r.HP - r.minDamage) +"</td></tr>");
		sb.append("<tr><td>● p指数(A)：" + pIndex_O + "</td>");
		sb.append("<td>● p指数(B)：" + (int)r.pIndex_D + "p</td></tr>");
		sb.append("</table>");
		return sb.toString();
	}
}
