package jp.tubosi;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Keisan
 */
public class Keisan extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String[] dataToInt ={"wazaType","WazaIryoku","buturi","ApLv","ApNouryokuti","ApRank","type1_A","type2_A","BpNouryokuti_h","BpNouryokuti_b","BpRank","type1_B","type2_B","tenkou","Wazakouka","Sinkamae"};
	private	static String[] dataToString={"ApName","BpName","waza","ApItem","BpItem","ApTokusei","BpTokusei","yakedo","sakidori","kyusyo","Field"};   
    private String[] data_s;
    private int[] data_i;
	/**
     * @see HttpServlet#HttpServlet()
     */
    public Keisan() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		data_s = new String[dataToString.length+4];
		data_i = new int[dataToInt.length];
		for(int i=0;i<data_s.length;i++){
			data_s[i]="N";
		}
		String s;
		for(int i=0;i<dataToString.length;i++){
			if((s=request.getParameter(dataToString[i]))!=null)data_s[i]=s;
		}
		try{
			String d_i;
			for(int i=0;i<data_i.length;i++){
				if((d_i=request.getParameter(dataToInt[i]))==null) throw new Exception("入力されていない値があります。");
				data_i[i]=Integer.parseInt(d_i);
			}
			setStringV(request.getParameterValues("kabe"),data_s.length-4);//壁のデータをセット
			setStringV(request.getParameterValues("asobi"),data_s.length-2);//泥遊び、水遊びのデータをセット
		}catch(NumberFormatException e){
			out.write("数値を正しく入力してください");
		}catch(Exception e){
			out.write(e.getMessage());
		}
		Calculation cal = new Calculation(data_i,data_s);
		//String[] ANS={防御側のHP,ダメージの最小値、ダメージの最大値、乱数か否か、攻撃回数、[乱数の場合は瀕死になる確率、そうでないときは""]};
		String[] rs=cal.Result();
		int[] dm = {Integer.parseInt(rs[0]),Integer.parseInt(rs[1]),Integer.parseInt(rs[2])};
		DecimalFormat df = new DecimalFormat("##.#%");
		String[] par ={df.format((double)dm[1]/dm[0]),df.format((double)dm[2]/dm[0])};
		String bpp =rs[6];
		StringBuilder sb = new StringBuilder();
		sb.append("<table>");
		sb.append("<tr><td>ダメージ："+rs[1]+" 〜 "+rs[2]+" ("+par[0]+" 〜 "+par[1]+")</td></tr>");
		sb.append("<tr><td>残りＨＰ："+(dm[0]-dm[2])+" 〜 "+(dm[0]-dm[1])+"</td></tr>");
		sb.append("<tr><td>確定数　："+rs[3]+rs[4]+"発"+rs[5]+"</td></tr>");
		cal = new Calculation(data_i,data_s,"ピィ");
		rs=cal.Result();
		int[] dm2 = {Integer.parseInt(rs[0]),Integer.parseInt(rs[1]),Integer.parseInt(rs[2])};
		double d0 = dm2[1]/dm2[0];
		double d1 = dm2[2]/dm2[0];
		sb.append("<tr><td>p指数(A)："+(int)d0+"p 〜 "+(int)d1+"p</td></tr>");
		sb.append("<tr><td>p指数(B)："+bpp+"p</td></tr>");
		sb.append("</table>");
		String result = sb.toString();
		out.write(result);
	}
	private void setStringV(String[] s,int start){
		if(s==null) return;
		for(int j=0;j<s.length;j++){
			data_s[start++]=s[j];
		}
		return;
	}
}


