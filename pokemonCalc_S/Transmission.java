package tubosi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Transmission
 */
@WebServlet("/Transmission")
public class Transmission extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private	static String[] dataOfOffense_String = {"ApName", "ApTokusei", "ApItem"};
	private	static String[] dataOfDeffense_String = {"BpName", "BpTokusei", "BpItem"};
	private static String[] dataOfOffense_int  = {"type1_A", "type2_A", "ApNouryokuti", "ApRank", "ApLv"};
	private static String[] dataOfDeffense_int  = {"type1_B", "type2_B", "BpNouryokuti_h", "BpNouryokuti_b", "BpRank", "Sinkamae"};
	private static String[] dataOfWaza  = {"wazaType", "WazaIryoku", "buturi", "Wazakouka"};
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Transmission() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		/*
		 *<1> ~ <4> で　Calcインスタンスを作成するのに必要なデータを取得する 
		 *<1>: 攻撃側のポケモンのデータを保持するPokemondataオブジェクトを作成
		 *<2>: 防御側のポケモンのデータを保持するPokemondataオブジェクトを作成
		 *<3>: 攻撃に使われる技のデータを保持するWazadataオブジェクトを作成
		 *<4>: 戦闘の『場』のデータを保持するFieldオブジェクトを作成
		 */
		
		//<1> 攻撃側のポケモンのデータを取得し、Pokemondataオブジェクトを作成。
		String[] offense_string = {"N", "N", "N"};
		for(int i = 0; i<3; i++){
			offense_string[i] = request.getParameter(dataOfOffense_String[i]);
		}
		
		int[] offence_int = new int[5];
		for(int i = 0; i<5; i++){
			offence_int[i] = Integer.parseInt(request.getParameter(dataOfOffense_int[i]));
		}
		
		Pokemondata a_p = new Pokemondata(offense_string, offence_int, request.getParameter("yakedo") != null);
		
		
		//<2> 防御側のポケモンのデータを取得し、Pokemondataオブジェクトを作成。
		String[] deffense_string = {"N", "N", "N"};
		for(int i = 0; i<3; i++){
			deffense_string[i] = request.getParameter(dataOfDeffense_String[i]);
		}
		
		int[] deffense_int = new int[6];
		for(int i = 0; i<6; i++){
			deffense_int[i] = Integer.parseInt(request.getParameter(dataOfDeffense_int[i]));
		}
		
		Pokemondata d_p = new Pokemondata(deffense_string, deffense_int);
		
		
		//<3> 技のデータを取得し、Wazadataのオブジェクトを作成。
		int[] waza = new int[4];
		for(int i = 0; i<4; i++){
			waza[i] = Integer.parseInt(request.getParameter(dataOfWaza[i]));
		}
		
		Wazadata w = new Wazadata(request.getParameter("wazaName"), waza);
		
		
		//<4> フィールドのデータを取得し、Fieldオブジェクトを作成。
		String[] wall = {"N", "N"};
		String[] play = {"N", "N"};
		Field f = new Field(setStringV(request.getParameterValues("kabe"), wall), setStringV(request.getParameterValues("asobi"), play),
				request.getParameter("Field"), request.getParameter("sakidori") != null,
				request.getParameter("kyusyo") != null, Integer.parseInt(request.getParameter("tenkou")));
		
		
		//Clacオブジェクトを作成・計算し、その結果を保持したResultオブジェクトを取得、さらにそれをViewオブジェクトに渡して結果をクライアントに送信。
		Calc c = new Calc(a_p, d_p, w, f);
		Result r = null;
		Result r_p = null;
		try{
			r = c.getResult();
			c.changeDeffenseto(new Pokemondata());
			r_p = c.getResult();
			View v = new View(r, r_p);
			out.print(v.getResultHTML());
		}catch(Exception e){
			e.printStackTrace();
			out.write("<p>予期せぬエラーが発生しました</p>");
		};
		
	}
	
	String[] setStringV(String[] s1, String[] s2){
		if(s1 == null) return s2;
		for(int j=0; j<s1.length; j++){
			s2[j] = s1[j]; 
		}
		return s2;
	}

}
