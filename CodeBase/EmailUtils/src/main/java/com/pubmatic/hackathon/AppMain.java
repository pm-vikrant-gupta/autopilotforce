package com.pubmatic.hackathon;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppMain {
	@SuppressWarnings({ "resource", "unused" })
	public static void main(String args[]){
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("quartz-context.xml");
	}
}
