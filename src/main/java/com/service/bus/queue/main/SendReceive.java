package com.service.bus.queue.main;

import com.service.bus.queue.Queue;

public class SendReceive {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Queue queue = new Queue();
		queue.createQueue();
		queue.sendMessage();
		queue.reciveMessage();

	}

}
