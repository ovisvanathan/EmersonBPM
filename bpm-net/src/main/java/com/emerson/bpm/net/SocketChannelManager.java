package com.emerson.bpm.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.emerson.bpm.api.ChannelManager;
import com.emerson.bpm.api.Registrant;
import com.emerson.bpm.event.SelectionHandler;

public class SocketChannelManager<T> extends SelectionBroker implements ChannelManager {
				
			
			SelectionBroker selectionBroker;
			
			ServerSocketChannel [] socketChannels = new ServerSocketChannel[128];
			
			int [] socketChannelAvailability = new int[742];
			
			int MAX_NUM_SOCKETS = 742;
			
			AtomicInteger numSockets;
			
			Map<String, Integer> socketIndexMap = new HashMap();

			Selector selector;

			List<SelectionKey> socketKeys = new ArrayList();

			Map<Integer, String> socketKeyMap = new HashMap();


			String[] pubevents;
			
			public SocketChannelManager(Registrant registrant, String [] pubevents) {
				super(registrant, pubevents);
				
				try {
					selector = Selector.open();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
			public List<SelectionKey> getSocketKeys() {
				return socketKeys;
			}

			public ServerSocketChannel 
					createSocketChannel(String idkey, int c_id) throws Exception {

			//	super(pub, idkey, c_id);
				
				return getSocketChannel(idkey, c_id);				
			}

			ServerSocketChannel getSocketChannel(String idkey, int c_id) throws Exception {
				
				if(numSockets.intValue() >= MAX_NUM_SOCKETS)
					throw new Exception("no socket channels available");
			
				int i=0;
				for(;i<MAX_NUM_SOCKETS;i++)
					if(socketChannelAvailability[i] == 0)
						break;
			
				ServerSocketChannel userSocket = ServerSocketChannel.open();
				userSocket.bind(new InetSocketAddress("localhost", 5454));
				userSocket.configureBlocking(false);
		
				SelectionKey ownerKey = userSocket.register(selector, SelectionKey.OP_READ);
				ownerKey.attach(idkey);
		
				socketIndexMap.put(idkey, i);
				
				socketKeyMap.put(c_id, idkey);
				
				numSockets.incrementAndGet();
								
				socketKeys.add(ownerKey);
				
				socketChannelAvailability[i] = 1;
				return userSocket;
			}

			public SelectionHandler keyToIdHandler(SelectionKey channelkey) {
				int c_id = (int) channelkey.attachment();	
				
				return super.keyToIdHandler(c_id);				
			}

			/* Clients willing to use SocketChannelManager should call
			 * init first to register the events they are interested
			 * in Listening to
			 */
			public Selector getSelector() {
				return this.selector;
			}

			public T getEventObject(String c_id) {

			//	String idkey = socketKeyMap.get(c_id);				
				return (T) this.registrar.getEntity(c_id);
							
			}
						
		}
