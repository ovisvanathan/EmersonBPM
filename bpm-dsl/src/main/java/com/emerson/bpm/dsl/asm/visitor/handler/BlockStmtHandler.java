package com.emerson.bpm.dsl.asm.visitor.handler;

import com.evatic.paddle.asm.inject.TreeNodeProcessor;
import com.picasso.paddle.tasks.util.ProxyFactoryBean;

public class BlockStmtHandler implements TreeNodeProcessor, ProxyFactoryBean {

	@Override
	public Object process(Object t) {

		System.out.println("in BlockStmtHandler process arg= " + t.getClass().getName());
		return null;
	}

	
}
