package net.xiaoqu.weixin.parser;

import org.jdom.Element;
import org.jdom.JDOMException;

import net.xiaoqu.weixin.vo.recv.WxRecvMsg;
import net.xiaoqu.weixin.vo.recv.WxRecvPicMsg;

public class WxRecvPicMsgParser extends WxRecvMsgBaseParser {

	@Override
	protected WxRecvPicMsg parser(Element root, WxRecvMsg msg) throws JDOMException {
		return new WxRecvPicMsg(msg, getElementText(root, "PicUrl"));
	}

}
