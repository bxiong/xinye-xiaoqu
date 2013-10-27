package net.xiaoqu.weixin.parser;

import org.jdom.Element;
import org.jdom.JDOMException;

import net.xiaoqu.weixin.vo.recv.WxRecvMsg;
import net.xiaoqu.weixin.vo.recv.WxRecvTextMsg;

public class WxRecvTextMsgParser extends WxRecvMsgBaseParser{

	@Override
	protected WxRecvTextMsg parser(Element root, WxRecvMsg msg) throws JDOMException {
		return new WxRecvTextMsg(msg, getElementText(root, "Content"));
	}

	
}
