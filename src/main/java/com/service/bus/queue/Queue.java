package com.service.bus.queue;

import com.microsoft.windowsazure.services.servicebus.*;
import com.microsoft.windowsazure.services.servicebus.models.*;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;

public class Queue {
	
	private static ServiceBusContract service = null;
	
	

	
	public void createQueue() {			
		Configuration config =
		    ServiceBusConfiguration.configureWithSASAuthentication(
		            "rjqueue",
		            "RootManageSharedAccessKey",
		            "/TRkmFsNpakQOvewQHxjiy04ncPd+zYzeotZvBNaYAY=",
		            ".servicebus.windows.net"
		            );

		service = ServiceBusService.create(config);
	
		
		try{
			if(null!= service.getQueue("rjTestQueue")) {
				GetQueueResult result = service.getQueue("rjTestQueue");
			}else {
				QueueInfo queueInfo = new QueueInfo("rjTestQueue");
				CreateQueueResult result = service.createQueue(queueInfo);
			}
		    
		}
		catch (ServiceException e){
		    System.out.print("ServiceException encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}	
		
	}


	public void sendMessage() {
		try
		{
		    
		    for (int i=0; i<5; i++)
		    {
		         // Create message, passing a string message for the body.
		    	 BrokeredMessage message = new BrokeredMessage("Test message " + i);
		         // Set an additional app-specific property.
		         message.setProperty("MyProperty", i);
		         // Send message to the queue
		         service.sendQueueMessage("rjTestQueue", message);
		    }
		}
		catch (ServiceException e)
		{
		    System.out.print("ServiceException encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
	}
	
	
	public void reciveMessage() {
		try
		{
		    ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
		    opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

		    while(true)  {
		         ReceiveQueueMessageResult resultQM =
		                 service.receiveQueueMessage("rjTestQueue", opts);
		        BrokeredMessage message = resultQM.getValue();
		        if (message != null && message.getMessageId() != null)
		        {
		            System.out.println("MessageID: " + message.getMessageId());
		            // Display the queue message.
		            System.out.print("From queue: ");
		            byte[] b = new byte[200];
		            String s = null;
		            int numRead = message.getBody().read(b);
		            while (-1 != numRead)
		            {
		                s = new String(b);
		                s = s.trim();
		                System.out.print(s);
		                numRead = message.getBody().read(b);
		            }
		            System.out.println();
		            System.out.println("Custom Property: " +
		                message.getProperty("MyProperty"));
		            // Remove message from queue.
		            System.out.println("Deleting this message.");
		            //service.deleteMessage(message);
		        }  
		        else  
		        {
		            System.out.println("Finishing up - no more messages.");
		            break;
		            // Added to handle no more messages.
		            // Could instead wait for more messages to be added.
		        }
		    }
		}
		catch (ServiceException e) {
		    System.out.print("ServiceException encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
		catch (Exception e) {
		    System.out.print("Generic exception encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
	}
}
