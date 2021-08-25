package com.emerson.bpm.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.api.GameAI;


public class Utils<T extends AutonomousActor> {
	
	ByteBuffer buffer = ByteBuffer.allocate(256);
	private static final String POISON_PILL = "POISON_PILL";
	
	GameAI GAI;
	
	public Utils(GameAI GAI) {
		this.GAI = GAI;
	}
	
	void broadcast(SocketChannel client, String msg) {
		sendMessage(msg, client);
	}
	
	public String sendMessage(String msg, SocketChannel client) {

		ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
		String response = null;
		try {
				client.write(buffer);
				buffer.clear();
				client.read(buffer);
				response = new String(buffer.array()).trim();
				System.out.println("response=" + response);
				buffer.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public String[] mergeArrays(String[] players, String[] events) {

		
		String [] merged = new String[players.length+events.length]; 
		
		int i=0;
		for(;i<players.length;i++)
			merged[i] = players[i];
		
		for(int k=0;k<events.length;k++)
			merged[i] = events[k];
					
		return merged;
	}

	public int getRandom(int maxval) {				
		Random rand = new Random();
		int num = (int) rand.nextInt(maxval)+1;			
		return num;				
	}
	
	public String getRandom(int maxval, List list) {				
		Random rand = new Random();
		int num = (int) rand.nextInt(maxval)+1;				
		return (String) list.get(num);				
	}
		
	boolean doRead(SelectionKey key, ServerSocketChannel serverSocket) {
		Selector sel = key.selector();

		try {
			if (key.isAcceptable()) {
			        register(sel, serverSocket);
			    }

			    if (key.isReadable()) {
			        answerWithEcho(buffer, key);
			    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
            return true;
	}
	
	 private void answerWithEcho(ByteBuffer buffer, SelectionKey key)
		      throws IOException {
		 
		 		String idkey = (String) key.attachment();

		 		T entity = (T) GAI.getEntity(idkey);
		 		
		 		SocketChannel client = (SocketChannel) key.channel();
		        client.read(buffer);
		        if (new String(buffer.array()).trim().equals(POISON_PILL)) {
		            client.close();
		            System.out.println("Not accepting client messages anymore");
		            
		    //        entity.getBusmq().add(BusMessage.END);
		        }
		        else {
		            buffer.flip();
		            client.write(buffer);
		            buffer.clear();
		        }
		    }
	 private void register(Selector selector, ServerSocketChannel serverSocket)
		      throws IOException {
		 
		        SocketChannel client = serverSocket.accept();
		        client.configureBlocking(false);
		        client.register(selector, SelectionKey.OP_READ);
		    }

	 
	 
	 /*
	void launch(OTNCoverNode subject, OTNSubject ... nodeObservers) {
	
		for(Observer ob2 : nodeObservers)
			Observable.just(subject).subscribe(
						ob2);
				
		
		subject.launch();
		launchPad.add(subject);			
	
	}
	*/

//	public void addObserver(Observable o, @NonNull Observer e) {
//			o.subscribe(e);
//	}				

	static int seed = 23;
	static char [] hashstore;
	public static int hash(String queryName) {

			try {
				hashstore = new char[256];

				MessageDigest md = MessageDigest.getInstance("MD5");
				
				md.update(queryName.getBytes());
				
				byte [] bytes = md.digest();
							
				return bytes.hashCode();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

	 public static int hashmerge(int hash, Class input) {

		    int mhash = computeClassHash(input);
		 
		 	mhash = hash * 37 + mhash;
		    return mhash;
	}	 
	 
	 private static int computeClassHash(Class input) {
	
		 Field [] fs = input.getDeclaredFields();
		 
		 int nhash = 0;
		 for(Field fa : fs) {
			 
			 String fname = fa.getName();
			 Class ftype = fa.getType();

			  nhash = nhash * 37 + new String [] { fname, ftype.getName() }.hashCode();			 
		 }
		 
		 return nhash;
	}


	 private static boolean compareHash(int hash1, int hash2) {
		 
		 if(hash1 == hash2)
			 return true;
		 
		 return false;		 
	 }


	public static int calculateQueryHash(String qry, List<Class> ets) {

		int queryHash = hash(qry);

		for(Class kz : ets)
			queryHash = hashmerge(queryHash, kz);

		return queryHash;

	}


		
}


