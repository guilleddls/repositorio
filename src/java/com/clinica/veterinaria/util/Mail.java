package com.clinica.veterinaria.util;

import com.clinica.veterinaria.clientes.Cliente;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class Mail {
    
    public static void mailPassword(Cliente cliente){
        String mensaje ="Hola " + cliente.getNombre() +" \nLas credenciales de su cuenta son: \nUsuario: "+cliente.getUsuario() +"\nContraseña: " +cliente.getPassword();
        String titulo = "Envio de datos para App Móvil";
        String email = cliente.getEmail();
        sendEmail(email, titulo, mensaje);
        
    }
    /*
    private static void sendEmail(String mail, String titulo, String mensaje){
        
        String to= mail ;  
        final String from ="reparacionpc.utn@gmail.com";
        final String password ="reparacion1";
  
        //Get the session object  
        
//        Properties props = new Properties();  
//        props.put("mail.smtp.host", "smtp.gmail.com");  
//        props.put("mail.smtp.socketFactory.port", "465");  
//        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
//        props.put("mail.smtp.auth", "true");  
//        props.put("mail.smtp.port", "465");  
                
        Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props, 
                new javax.mail.Authenticator() { 
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {  
                return new PasswordAuthentication(from,password);//change accordingly 
            }  
        });  

        //compose message  
        try {            
            MimeMessage message = new MimeMessage(session); 
            message.setFrom(new InternetAddress("info-cuenta@veterinaria.com"));//change accordingly
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
            message.setSubject(titulo);  
            message.setText(mensaje);  

         //send message  
         Transport.send(message);  

         System.out.println("E-mail enviado.");  

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }  

    }  
    */
    
     public static void sendEmail(String emailId, String titulo, String mensaje) {
        String host = "smtp.live.com";
        int puerto = 587;
        final String from = "dummy_guille@live.com";
        final String pass = "soymejor1";
        
        try {
            Properties props  = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", puerto+"");
            props.put("mail.smtp.user", from);
            props.put("mail.smtp.pwd", pass);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            
            Session session = Session.getInstance(props, new javax.mail.Authenticator() { 
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {  
                    return new PasswordAuthentication(from,pass);//change accordingly 
                }
            });
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setSubject(titulo);
            msg.setText(mensaje);
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailId));

            Transport transport = session.getTransport("smtp");
            transport.connect(host, puerto, from, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            
            System.out.println("Mail enviado "+emailId);
            transport.close();
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }
}

 
 

    

    
    

