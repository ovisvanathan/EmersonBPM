package com.emerson.bpm.dsl.asm.tree;

import com.evatic.paddle.asm.inject.TreeNodeProcessor;
import com.picasso.paddle.tasks.util.ProxyFactoryBean;

public interface NodeFactory extends ProxyFactoryBean {

	@Override
	public TreeNodeProcessor get(Object node);

}
