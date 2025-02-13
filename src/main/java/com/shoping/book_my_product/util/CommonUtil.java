package com.shoping.book_my_product.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.shoping.book_my_product.entity.ProductOrder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {
	@Autowired
	private JavaMailSender mailSender;
	
	public Boolean sendMail(String url,String recipentEmail) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		helper.setFrom("abhiayyappa9@gmail.com", "BookMyProduct");
		helper.setTo(recipentEmail);
		
		String content="<p>Hello,</p>"+"<p>You have requested to reset your password.</p>"+
		"<p>Click the link below to change your password : </p>"+
		"<p><a href=\""+url+"\">change my passsword</a></p>";
		
		helper.setSubject("Password Reset");
		helper.setText(content,true);
		mailSender.send(message);
		return true;
	}

	public static String generateUrl(HttpServletRequest request) {
		 String siteUrl = request.getRequestURL().toString();
		 return siteUrl.replaceAll(request.getServletPath(), "");
	}
	
	String contentMsg=null;
	public Boolean sendEmailForProductOrder(ProductOrder productOrder,String status) throws Exception {
		 contentMsg="<p>[[name]] [[lname]]</p><br/><p>Thank You Order <b>[[orderStatus]]</b></p>"
				+"<p><b>Product Details : </b></p>"
				+"<p>Product Name : [[productName]]</p>"
				+"<p>Product Category : [[category]]</p>"
				+"<p>Product Quantity : [[quantity]]</p>"
				+"<p>Product Price : [[price]]</p>"
				+"<p>Payment Type : [[paymentType]]</p>";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		helper.setFrom("abhiayyappa9@gmail.com", "BookMyProduct");
		helper.setTo(productOrder.getOrderAddress().getEmail());
		OrderStatus[] orderStatus = OrderStatus.values();
		
		contentMsg=contentMsg.replace("[[name]]",productOrder.getOrderAddress().getFirstName());
		contentMsg=contentMsg.replace("[[lname]]",productOrder.getOrderAddress().getLastName());
		contentMsg=contentMsg.replace("[[orderStatus]]",status);
		contentMsg=contentMsg.replace("[[productName]]", productOrder.getProduct().getPName());
		contentMsg=contentMsg.replace("[[category]]", productOrder.getProduct().getCategory());
		contentMsg=contentMsg.replace("[[quantity]]", productOrder.getQuantity().toString());
		contentMsg=contentMsg.replace("[[price]]", productOrder.getPrice().toString());
		contentMsg=contentMsg.replace("[[paymentType]]", productOrder.getPaymentType());
		for (OrderStatus statusData : orderStatus) {
			if (statusData.getId().equals(status)) {
				contentMsg=contentMsg.replace("[[orderStatus]]", status);
			}
		}
		helper.setSubject("Product Order Success");
		helper.setText(contentMsg,true);
		mailSender.send(message);
		return true;
	}
}
