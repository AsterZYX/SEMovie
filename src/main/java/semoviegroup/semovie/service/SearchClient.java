package semoviegroup.semovie.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

import org.json.JSONObject;

import semoviegroup.semovie.model.Cinema;
import semoviegroup.semovie.model.Movie;
import semoviegroup.semovie.vo.ResultVO;

public class SearchClient {

	public String search(String state, String type, String region, String time, String sort, Integer size,
			Integer currentPage) throws Exception {
		String URL = "https://maoyan.com/films?showType=" + state + "&catId=" + type + "&sourceId=" + region
				+ "&yearId=" + time + "&sortId=" + sort + "&offset=" + (currentPage - 1) * size;

		String host = "localhost"; // 要连接的服务端IP地址
		int port = 8001; // 要连接的服务端对应的监听端口
		// 与服务端建立连接
		Socket client = new Socket(host, port);
		// 建立连接后就可以往服务端写数据了

		HashMap<String, String> sendData = new HashMap<String, String>();
		sendData.put("url", URL);

		DataOutputStream outputStream = null;
		outputStream = new DataOutputStream(client.getOutputStream());

		JSONObject json = new JSONObject(sendData);
		String jsonString = json.toString();
		byte[] jsonByte = jsonString.getBytes();

		System.out.println("发的数据长度为:" + jsonByte.length);
		outputStream.write(jsonByte);
		outputStream.flush();
		System.out.println("传输数据完毕");
		client.shutdownOutput();

		InputStream is = client.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String info = null;
		String s = "";
		while ((info = in.readLine()) != null) {
			s = info;
			System.out.println("我是客户端，Python服务器说：" + info);
		}
		is.close();
		in.close();
		client.close();

		return s.substring(16);

	}

	public String searchByName(String movieid, String name) throws Exception {
		String URL = "https://maoyan.com/films/" + movieid;

		String host = "localhost"; // 要连接的服务端IP地址
		int port = 8002; // 要连接的服务端对应的监听端口
		// 与服务端建立连接
		Socket client = new Socket(host, port);
		// 建立连接后就可以往服务端写数据了

		HashMap<String, String> sendData = new HashMap<String, String>();
		sendData.put("url", URL);

		DataOutputStream outputStream = null;
		outputStream = new DataOutputStream(client.getOutputStream());

		JSONObject json = new JSONObject(sendData);
		String jsonString = json.toString();
		byte[] jsonByte = jsonString.getBytes();

		System.out.println("发的数据长度为:" + jsonByte.length);
		outputStream.write(jsonByte);
		outputStream.flush();
		System.out.println("传输数据完毕");
		client.shutdownOutput();

		InputStream is = client.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String info = null;
		String s = "";
		while ((info = in.readLine()) != null) {
			s = info;
			System.out.println("我是客户端，Python服务器说：" + info);
		}
		is.close();
		in.close();
		client.close();

		return s.substring(16);

	}

	public String searchDoubanRatingByName(String name) throws Exception {
		String URL = name;

		String host = "localhost"; // 要连接的服务端IP地址
		int port = 8003; // 要连接的服务端对应的监听端口
		// 与服务端建立连接
		Socket client = new Socket(host, port);
		// 建立连接后就可以往服务端写数据了

		HashMap<String, String> sendData = new HashMap<String, String>();
		sendData.put("url", URL);

		DataOutputStream outputStream = null;
		outputStream = new DataOutputStream(client.getOutputStream());

		JSONObject json = new JSONObject(sendData);
		String jsonString = json.toString();
		byte[] jsonByte = jsonString.getBytes();

		System.out.println("发的数据长度为:" + jsonByte.length);
		outputStream.write(jsonByte);
		outputStream.flush();
		System.out.println("传输数据完毕");
		client.shutdownOutput();

		InputStream is = client.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String info = null;
		String s = "";
		while ((info = in.readLine()) != null) {
			s = info;
			System.out.println("我是客户端，Python服务器说：" + info);
		}
		is.close();
		in.close();
		client.close();

		return s;
	}

	public String getMaoYanTicketByMovieId(String movieid, String showDate, String offset) throws Exception {
		String URL = "https://maoyan.com/cinemas?movieId=" + movieid + "&showDate=" + showDate + "&offset=" + offset;

		String host = "localhost"; // 要连接的服务端IP地址
		int port = 8004; // 要连接的服务端对应的监听端口
		// 与服务端建立连接
		Socket client = new Socket(host, port);
		// 建立连接后就可以往服务端写数据了

		HashMap<String, String> sendData = new HashMap<String, String>();
		sendData.put("url", URL);

		DataOutputStream outputStream = null;
		outputStream = new DataOutputStream(client.getOutputStream());

		JSONObject json = new JSONObject(sendData);
		String jsonString = json.toString();
		byte[] jsonByte = jsonString.getBytes();

		System.out.println("发的数据长度为:" + jsonByte.length);
		outputStream.write(jsonByte);
		outputStream.flush();
		System.out.println("传输数据完毕");
		client.shutdownOutput();

		InputStream is = client.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String info = null;
		String s = "";
		while ((info = in.readLine()) != null) {
			s = info;
			System.out.println("我是客户端，Python服务器说：" + info);
		}
		is.close();
		in.close();
		client.close();

		return s;
	}

}