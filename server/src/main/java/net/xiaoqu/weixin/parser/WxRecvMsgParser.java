package net.xiaoqu.weixin.parser;

import org.jdom.Document;
import org.jdom.JDOMException;

import net.xiaoqu.weixin.vo.recv.WxRecvMsg;

public interface WxRecvMsgParser {
	WxRecvMsg parser(Document doc) throws JDOMException;
}
