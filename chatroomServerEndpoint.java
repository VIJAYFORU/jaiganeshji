package io.websocket;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.http.Cookie;
import javax.websocket.CloseReason;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.MessageHandler.Partial;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chatroomServerEndpoint")
public class ChatroomServerEndpoint {
	String username;
	private Session userSession;
	  private static HashMap<String, String> users = new HashMap<>();
	private static Set<Session> chatroomusers=Collections.synchronizedSet(new HashSet<Session>());
	@OnOpen
	public void handleOpen(Session userSession)
	{/*
		this.userSession=userSession;
		
		String s1=userSession.toString();
		System.out.println("usersession"+s1);	
String hj=userSession.getId();
Cookie ck=new Cookie(hj,username);


System.out.println("usersession with id"+hj);
System.out.println("add"+userSession);*/
		chatroomusers.add(userSession);

		
	}
	@OnMessage
	public void handleMessage(String message ,Session userSession) throws IOException
	{
		  username=(String)userSession.getUserProperties().get("username");
		
		if(username==null) {
			
			userSession.getUserProperties().put("username", message);
			System.out.println("text message"+message);
			userSession.getBasicRemote().sendText(buildJsonData("System", "you are now coonected as"+message));
		}
		
		else
		{
			Iterator<Session> iterator=chatroomusers.iterator();
	while(iterator.hasNext()) iterator.next().getBasicRemote().sendText(buildJsonData(username,message));
		System.out.println("test message"+message);
		
		
		
		}
	}
	@OnClose
	public void handleClose(Session userSession)
	{
		
		chatroomusers.remove(userSession);
		
		System.out.println("reomved ssession"+userSession);
	}
	
	
	private String buildJsonData(String username,String message) {
		JsonObject jsonObject=Json.createObjectBuilder().add("message",username+": "+message).build();
		StringWriter stringwriter=new StringWriter(); 
		
		try(JsonWriter jsonwriter=Json.createWriter(stringwriter))
					
		{jsonwriter.write(jsonObject);}
			return stringwriter.toString();
		
		
		
		
 	}
	
	
	
}
